//
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

import Foundation

public enum Expression<T: Decodable> {
    case value(T)
    case expression(SingleExpression)
}

extension Expression: Decodable {
    public init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()
        if let expression = try? container.decode(SingleExpression.self) {
            self = .expression(expression)
        } else if let value = try? container.decode(T.self) {
            self = .value(value)
        } else {
            throw DecodingError.dataCorruptedError(in: container, debugDescription: "ValueExpression cannot be decoded")
        }
    }
}

public struct SingleExpression: Decodable {
    let nodes: [Node]

    enum Node: Equatable {
        case property(String)
        case arrayItem(Int)
    }

    func evaluate(model: DynamicObject) -> Any? {
        let model = model.asAny()
        var nodes = self.nodes[...]
        return SingleExpression.evaluate(&nodes, model)
    }
    
    private static func evaluate(_ expression: inout ArraySlice<Node>, _ model: Any?) -> Any? {
        guard let first = expression.first else {
            return model
        }
        switch first {
        case let .property(key):
            guard let dictionary = model as? [String: Any], let value = dictionary[key] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)

        case let .arrayItem(index):
            guard let array = model as? [Any], let value = array[safe: index] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)
        }
    }

    // verify context?
    func context() -> String? {
        if let node = nodes.first {
            switch node {
            case let .property(context):
                return context
            default:
                return nil
            }
        }
        return  nil
    }
}

extension SingleExpression: RawRepresentable {
    static let expression = #"^\$\{(\w+(?:\[\d+\])*(?:\.\w+(?:\[\d+\])*)*)\}$"#
    static let token = #"\w+"#
    static let property = #"[a-zA-Z_]\w*"#
    static let arrayItem = #"\d+"#

    public init?(rawValue: String) {
        guard let expression = rawValue.match(pattern: SingleExpression.expression) else {
            return nil
        }

        let tokens = expression.matches(pattern: SingleExpression.token)
        self.nodes = tokens.compactMap {
            if let property = $0.match(pattern: SingleExpression.property) {
                return SingleExpression.Node.property(property)
            } else if let index = $0.match(pattern: SingleExpression.arrayItem) {
                return SingleExpression.Node.arrayItem(Int(index) ?? 0)
            } else {
                return nil
            }
        }
    }

    public var rawValue: String {
        var expression = "${"
        for node in self.nodes {
            switch node {
            case .property(let string):
                if node != nodes.first { expression += "." }
                expression += string
            case .arrayItem(let index):
                expression += "[\(index)]"
            }
        }
        expression += "}"
        return expression
    }
}

// TODO: revisar tratativa de erro (criar Expression do tipo unknown???)
extension Expression: ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        if let expression = SingleExpression(rawValue: value) {
            self = .expression(expression)
        } else if let value = value as? T {
            self = .value(value)
        } else {
            preconditionFailure("Expected to set \(T.self) but found String instead")
        }
    }
}

extension Expression: ExpressibleByIntegerLiteral {
    public init(integerLiteral value: Int) {
        if let value = value as? T {
            self = .value(value)
        } else {
            preconditionFailure("Expected to set \(T.self) but found Int instead")
        }
    }
}

extension Expression: ExpressibleByFloatLiteral {
    public init(floatLiteral value: Float) {
        if let value = value as? T {
            self = .value(value)
        } else {
            preconditionFailure("Expected to set \(T.self) but found Float instead")
        }
    }
}
