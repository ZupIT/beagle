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

public struct SetContext: Action {
    let context: String
    let path: String?
    let value: Expression<AnyDecodable>
    
    public init(
        context: String,
        path: String? = nil,
        value: Any
    ) {
        self.context = context
        self.path = path
        if let string = value as? String, let expression = SingleExpression(rawValue: string) {
            self.value = Expression.expression(expression)
        } else {
            self.value = Expression.value(AnyDecodable(value))
        }
    }
}

// TODO: Generate this with Sourcery
extension SetContext: Decodable {
    enum CodingKeys: String, CodingKey {
        case context
        case path
        case value
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        context = try container.decode(String.self, forKey: .context)
        path = try container.decodeIfPresent(String.self, forKey: .path)
        value = try container.decode(Expression<AnyDecodable>.self, forKey: .value)
    }
}
