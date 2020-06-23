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

public enum Expression<T: Decodable> {
    case value(T)
    case expression(ContextExpression)
}

public enum ContextExpression {
    case single(SingleExpression)
    case multiple(MultipleExpression)
}

public struct SingleExpression: Decodable {
    public let context: String
    public let path: Path
    
    public init(context: String, path: Path) {
        self.context = context
        self.path = path
    }
}

extension SingleExpression: RawRepresentable {
    public init?(rawValue: String) {
        let result = singleExpression.run(rawValue)
        guard let expression = result.match, result.rest.isEmpty else { return nil }
        self.context = expression.context
        self.path = expression.path
    }

    public var rawValue: String {
        var result = "@{\(context)"
        if !path.nodes.isEmpty {
            result += ".\(path.rawValue)"
        }
        result += "}"
        return result
    }
}

public struct MultipleExpression: Decodable {
    public let nodes: [Node]

    public enum Node {
        case string(String)
        case expression(SingleExpression)
    }
    
    public init(nodes: [Node]) {
        self.nodes = nodes
    }
}

extension MultipleExpression: RawRepresentable {
    public init?(rawValue: String) {
        let result = multipleExpression.run(rawValue)
        guard let multipleExpression = result.match, result.rest.isEmpty else { return nil }
        self.nodes = multipleExpression.nodes
    }

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

// MARK: Decodable

extension Expression: Decodable {
    public init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()
        if let expression = try? container.decode(ContextExpression.self) {
            self = .expression(expression)
        } else if let value = try? container.decode(T.self) {
            self = .value(value)
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
