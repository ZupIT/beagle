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

public struct AnyDecodable {
    public let value: Any
    
    public init<T>(_ value: T) {
        self.value = value
    }
}

extension AnyDecodable: Decodable {

    public init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()
        if container.decodeNil() {
            self.init(()) // Void
        } else if let bool = try? container.decode(Bool.self) {
            self.init(bool)
        } else if let int = try? container.decode(Int.self) {
            self.init(int)
        } else if let double = try? container.decode(Double.self) {
            self.init(double)
        } else if let string = try? container.decode(String.self) {
            self.init(string)
        } else if let array = try? container.decode([AnyDecodable].self) {
            self.init(array.map { $0.value })
        } else if let dictionary = try? container.decode([String: AnyDecodable].self) {
            self.init(dictionary.mapValues { $0.value })
        } else {
            throw DecodingError.dataCorruptedError(in: container, debugDescription: "AnyDecodable value cannot be decoded")
        }
    }
}

public struct AnyDecodableContainer: Decodable {
    public let value: Decodable?

    static let types: [String: Decodable.Type] = [
        "component.container": Container.self,
        "component.text": Text.self,
        "component.button": Button.self
    ]
    
    enum Key: CodingKey {
        case type
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        let type = try container.decode(String.self, forKey: Key.type)
        if let component = AnyDecodableContainer.types[type] {
            self.value = try component.init(from: decoder)
        } else {
            self.value = nil
        }
    }
}

extension Container: Decodable {
    enum Key: String, CodingKey {
        case children
        case context
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        let children = try container.decode([AnyDecodableContainer].self, forKey: .children)
        self.children = children.map { $0.value as? Component ?? UnknownComponent() }
        self.context = try container.decode(Context.self, forKey: .context)
    }
}

extension Text: Decodable {
    enum Key: CodingKey {
        case text
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        self.text = try container.decode(ValueExpression<String?>.self, forKey: Key.text)
    }
}

extension Button: Decodable {
    enum Key: CodingKey {
        case text
        case action
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        self.text = try container.decodeIfPresent(String.self, forKey: Key.text)
        let action = try container.decodeIfPresent(AnyDecodableContainer.self, forKey: Key.action)
        self.action = action as? Action
    }
}

extension Context: Decodable {
    enum Key: String, CodingKey {
        case id
        case value
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: Key.self)
        self.id = try container.decode(String.self, forKey: .id)
        self.value = try container.decode(AnyDecodable.self, forKey: .value).value
    }
}

extension ValueExpression: Decodable {
    public init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()
        if let expression = try? container.decode(Expression.self) {
            self = .expression(expression)
        } else if let value = try? container.decode(T.self) {
            self = .value(value)
        } else {
            throw DecodingError.dataCorruptedError(in: container, debugDescription: "ValueExpression cannot be decoded")
        }
    }
}
