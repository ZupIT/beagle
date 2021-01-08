/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import UIKit

/// It's a `String` that will be treated internally as an `Expression<String>` if passed a value like "@{someExression}". Otherwise, it will be just a normal `String`.
public typealias StringOrExpression = String

public enum Expression<T: Decodable> {
    case value(T)
    case expression(ContextExpression)
}

public enum ContextExpression: Equatable, Hashable {
    case single(SingleExpression)
    case multiple(MultipleExpression)
}

public enum SingleExpression: Hashable {
    case value(Value)
    case operation(Operation)
}

public struct MultipleExpression: Hashable {
    public let nodes: [Node]

    public enum Node: Equatable {
        case string(String)
        case expression(SingleExpression)
    }
    
    public init(nodes: [Node]) {
        self.nodes = nodes
    }
}

public extension Expression {
    func observe(
        view: UIView,
        controller: BeagleControllerProtocol,
        updateFunction: @escaping (T?) -> Void
    ) {
        switch self {
        case let .expression(expression):
            controller.addBinding(expression: expression, in: view, update: updateFunction)
        case let .value(value):
            updateFunction(value)
        }
    }

    func evaluate(with view: UIView?) -> T? {
        switch self {
        case let .expression(expression):
            return view?.evaluate(for: expression).transform()
        case let .value(value):
            return value
        }
    }
}

// MARK: - RepresentableByParsableString

extension SingleExpression: RepresentableByParsableString {
    public static let parser = singleExpression
    
    public var rawValue: String {
        var result = "@{"
        switch self {
        case let .value(value):
            result += value.rawValue
        case let .operation(operation):
            result += operation.rawValue
        }
        
        result += "}"
        return result
    }
}

extension MultipleExpression: RepresentableByParsableString {
    public static let parser = multipleExpression

    public var rawValue: String {
        var result = ""
        for node in nodes {
            switch node {
            case let .string(string):
                result += string
            case let .expression(expression):
                result += expression.rawValue
            }
        }
        return result
    }
}

// MARK: EscapeExpressions

extension String {
    public func escapeExpressions() -> String {
        let result = self.replacingOccurrences(of: "\\\\", with: "\\")
        return result.replacingOccurrences(of: "\\@{", with: "@{")
    }
}

// MARK: ExpressibleByLiteral

extension Expression: ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        let escaped = value.escapeExpressions()
        if let expression = SingleExpression(rawValue: value) {
            self = .expression(.single(expression))
        } else if let multiple = MultipleExpression(rawValue: value) {
            self = .expression(.multiple(multiple))
        } else if let value = escaped as? T {
            self = .value(value)
        } else {
            assertionFailure("Error: invalid Expression syntax \(value)")
            self = .expression(.multiple(MultipleExpression(nodes: [])))
        }
    }
}

extension Expression: ExpressibleByStringInterpolation {}

extension Expression: ExpressibleByIntegerLiteral where T == Int {
    public init(integerLiteral value: Int) {
        self = .value(value)
    }
}

extension Expression: ExpressibleByFloatLiteral where T == Float {
    public init(floatLiteral value: Float) {
        self = .value(value)
    }
}

extension Expression: ExpressibleByBooleanLiteral where T == Bool {
    public init(booleanLiteral value: BooleanLiteralType) {
        self = .value(value)
    }
}

// MARK: - Equatable

extension Expression: Equatable where T: Equatable {
    public static func == (lhs: Expression<T>, rhs: Expression<T>) -> Bool {
        switch (lhs, rhs) {
        case let (.value(lhsValue), .value(rhsValue)):
            return lhsValue == rhsValue
        case let (.expression(lhsValue), .expression(rhsValue)):
            return lhsValue == rhsValue
        case (.value, .expression),
             (.expression, .value):
            return false
        }
    }
}

// MARK: - Decodable

extension Expression: Decodable {
    public init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()
        if let expression = try? container.decode(ContextExpression.self) {
            self = .expression(expression)
        } else if let value = try? container.decode(T.self) {
            if let string = value as? String {
                // swiftlint:disable force_cast
                self = .value(string.escapeExpressions() as! T)
                // swiftlint:enable force_cast
            } else {
                self = .value(value)
            }
        } else {
            throw DecodingError.dataCorruptedError(in: container, debugDescription: "Expression cannot be decoded")
        }
    }
}

extension ContextExpression: Decodable {
    public init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()
        if let expression = try? container.decode(SingleExpression.self) {
            self = .single(expression)
        } else if let expression = try? container.decode(MultipleExpression.self) {
            self = .multiple(expression)
        } else {
            throw DecodingError.dataCorruptedError(in: container, debugDescription: "ContextExpression cannot be decoded")
        }
    }
}
