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

public enum DynamicObject: Equatable {
    case empty
    case bool(Bool)
    case int(Int)
    case double(Double)
    case string(String)
    case array([DynamicObject])
    case dictionary([String: DynamicObject])
    case expression(ContextExpression)
}
    
extension DynamicObject {
        
    public func asAny() -> Any? {
        switch self {
        case .empty:
            return nil
        case let .bool(bool):
            return bool
        case let .int(int):
            return int
        case let .double(double):
            return double
        case let .string(string):
            return string
        case let .array(array):
            return array.map { $0.asAny() }
        case let .dictionary(dictionary):
            return dictionary.mapValues { $0.asAny() }
        case let .expression(expression):
            switch expression {
            case let .single(expression):
                return expression.rawValue
            case let .multiple(expression):
                return expression.rawValue
            }
        }
    }
    
    public func toString() -> String {
        switch self {
        case .empty:
            return ""
        case let .bool(bool):
            return "\(bool)"
        case let .int(int):
            return "\(int)"
        case let .double(double):
            return "\(double)"
        case let .string(string):
            return string
        case let .array(array):
            return "\(array)"
        case let .dictionary(dictionary):
            return "\(dictionary)"
        case let .expression(.multiple(multipleExpression)):
            return multipleExpression.rawValue
        case let .expression(.single(singleExpression)):
            return singleExpression.rawValue
        }
    }
    
    public func isEqualIgnoringAssociatedValues(_ anotherObject: DynamicObject) -> Bool {
        switch (self, anotherObject) {
        case (.empty, .empty):
            return true
        case (.bool, .bool):
            return true
        case (.int, .int):
            return true
        case (.double, .double):
            return true
        case (.string, .string):
            return true
        case (.array, .array):
            return true
        case (.dictionary, .dictionary):
            return true
        case (.expression, .expression):
            return true
        default:
            return false
        }
    }
}

// MARK: Codable

extension DynamicObject: Decodable {
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()
        if container.decodeNil() {
            self = .empty
        } else if let bool = try? container.decode(Bool.self) {
            self = .bool(bool)
        } else if let int = try? container.decode(Int.self) {
            self = .int(int)
        } else if let double = try? container.decode(Double.self) {
            self = .double(double)
        } else if let expression = try? container.decode(ContextExpression.self) {
            self = .expression(expression)
        } else if let string = try? container.decode(String.self) {
            self = .string(string.escapeExpressions())
        } else if let array = try? container.decode([DynamicObject].self) {
            self = .array(array)
        } else if let dictionary = try? container.decode([String: DynamicObject].self) {
            self = .dictionary(dictionary)
        } else {
            throw DecodingError.dataCorruptedError(in: container, debugDescription: "DynamicObject value cannot be decoded")
        }
    }
}

extension DynamicObject: Encodable {
    
    public func encode(to encoder: Encoder) throws {
        var container = encoder.singleValueContainer()

        switch self {
        case .empty:
            try container.encodeNil()
        case let .bool(bool):
            try container.encode(bool)
        case let .int(int):
            try container.encode(int)
        case let .double(double):
            try container.encode(double)
        case let .string(string):
            try container.encode(string)
        case let .array(array):
            try container.encode(array)
        case let .dictionary(dictionary):
            try container.encode(dictionary)
        case let .expression(expression):
            switch expression {
            case let .single(expression):
                try container.encode(expression.rawValue)
            case let .multiple(expression):
                try container.encode(expression.rawValue)
            }
        }
    }
}
