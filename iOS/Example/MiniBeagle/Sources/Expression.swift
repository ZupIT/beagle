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

enum ValueExpression<T: Decodable> {
    case value(T)
    case expression(Expression)
}

struct Expression: Decodable {
    let nodes: [Node]

    enum Node: Equatable {
        case property(String)
        case arrayItem(Int)
    }

    func evaluate(model: Any) -> Any? {
        var nodes = self.nodes[...]
        return Expression.evaluate(&nodes, model)
    }
    
    private static func evaluate(_ expression: inout ArraySlice<Node>, _ model: Any) -> Any? {
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

extension Expression: RawRepresentable {
    static let expression = #"^\$\{(\w+(?:\[\d+\])*(?:\.\w+(?:\[\d+\])*)*)\}$"#
    static let token = #"\w+"# // properties + arrayItems
    static let property = #"[a-zA-Z_]\w*"#
    static let arrayItem = #"\d+"#

    init?(rawValue: String) {
        guard let expression = rawValue.match(pattern: Expression.expression) else {
            return nil
        }

        let tokens = expression.matches(pattern: Expression.token)
        self.nodes = tokens.compactMap {
            if let property = $0.match(pattern: Expression.property) {
                return Expression.Node.property(property)
            } else if let index = $0.match(pattern: Expression.arrayItem) {
                return Expression.Node.arrayItem(Int(index) ?? 0)
            } else {
                return nil
            }
        }
    }

    var rawValue: String {
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

// MARK: BeagleUtils

extension NSRegularExpression {
    convenience init(_ pattern: String) {
        do {
            try self.init(pattern: pattern)
        } catch {
            preconditionFailure("Illegal regular expression: \(pattern).")
        }
    }
}
