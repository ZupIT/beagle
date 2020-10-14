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

/// It's a `String` that will be treated internally as an `Expression<String>` if passed a value like "@{someExression}". Otherwise, it will be just a normal `String`.
public typealias StringOrExpression = String

public enum Expression<T: Decodable> {
    case value(T)
    case expression(ContextExpression)
}

public enum ContextExpression: Equatable {
    case single(SingleExpression)
    case multiple(MultipleExpression)
}

public enum SingleExpression {
    case value(Value)
    case operation(Operation)
}

public struct MultipleExpression {
    public let nodes: [Node]

    public enum Node: Equatable {
        case string(String)
        case expression(SingleExpression)
    }
    
    public init(nodes: [Node]) {
        self.nodes = nodes
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

// MARK: - RepresentableByParsableString

/// Types that uses `RawRepresentable` to facilitate usage with strings that could be parsed by `Parser` logic.
/// By using `rawValue`, the compiler can automatically synthesize conformances to `Decodable` and `Equatable`.
///
/// - Note:
/// Here is an example that uses a string instead of working with enum cases:
///
/// `SingleExpression("@{context.name}")`.
///
public protocol RepresentableByParsableString: RawRepresentable, Decodable, Equatable {
    static var parser: Parser<Self> { get }
}

public extension RepresentableByParsableString where RawValue == String {
    init?(rawValue: RawValue) {
        let result = Self.parser.run(rawValue)
        guard let expression = result.match, result.rest.isEmpty else { return nil }
        self = expression
    }
}

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
