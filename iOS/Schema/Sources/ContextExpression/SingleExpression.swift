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

public struct SingleExpression: Decodable {
    public let nodes: [Node]

    public enum Node: Equatable {
        case property(String)
        case arrayItem(Int)
    }
    
    public init(nodes: [Node]) {
        self.nodes = nodes
    }
}

extension SingleExpression: RawRepresentable {
    static let expression = #"^\@\{(\w+(?:\[\d+\])*(?:\.\w+(?:\[\d+\])*)*)\}$"#
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
        var expression = "@{"
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
