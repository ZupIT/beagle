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

import UIKit

public struct Context: Decodable {
    let id: String
    let value: DynamicObject
    
    public init(id: String, value: DynamicObject) {
        self.id = id
        self.value = value
    }
}

public enum DynamicObject {
    case empty
    case bool(Bool)
    case int(Int)
    case double(Double)
    case string(String)
    case array([DynamicObject])
    case dictionary([String: DynamicObject])
    case expression(SingleExpression)
}

extension DynamicObject {
// TODO: usar mutating?
    func get(with view: UIView) -> DynamicObject {
        switch self {
        case .empty:
            return .empty
        case let .bool(bool):
            return .bool(bool)
        case let .int(int):
            return .int(int)
        case let .double(double):
            return .double(double)
        case let .string(string):
            return .string(string)
        case let .array(array):
            return .array(array.map { $0.get(with: view) })
        case let .dictionary(dictionary):
            return .dictionary(dictionary.mapValues { $0.get(with: view) })
        case let .expression(expression):
            return DynamicObject(from: view.evaluate(for: expression))
        }
    }
    
    init(from any: Any?) {
        if let bool = any as? Bool {
            self = .bool(bool)
        } else if let int = any as? Int {
            self = .int(int)
        } else if let double = any as? Double {
            self = .double(double)
        } else if let string = any as? String {
            self = .string(string)
        } else if let array = any as? [Any?] {
            self = .array(array.map { DynamicObject(from: $0) })
        } else if let dictionary = any as? [String: Any?] {
            self = .dictionary(dictionary.mapValues { DynamicObject(from: $0) })
        } else {
            self = .empty
        }
    }
    
    func asAny() -> Any? {
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
            return expression.rawValue
        }
    }
}

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
        } else if let expression = try? container.decode(SingleExpression.self) {
            self = .expression(expression)
        } else if let string = try? container.decode(String.self) {
            self = .string(string)
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
            try container.encode(expression.rawValue)
        }
    }
}

extension DynamicObject {

    mutating func set(_ value: Any, forPath path: String) {
        do {
            let path = try parsePath(path)
            let object = try compilePath(value, path)
            self = object
        } catch {
            print("error: \(error)")
        }
    }
    
    func merge(_ other: DynamicObject) -> DynamicObject {
        return _mergeDynamicObjects(self, other)
    }
}

extension DynamicObject: ExpressibleByNilLiteral {
    public init(nilLiteral: ()) {
        self = .empty
    }
}

extension DynamicObject: ExpressibleByBooleanLiteral {
    public init(booleanLiteral value: Bool) {
        self = .bool(value)
    }
}

extension DynamicObject: ExpressibleByIntegerLiteral {
    public init(integerLiteral value: Int) {
        self = .int(value)
    }
}

extension DynamicObject: ExpressibleByFloatLiteral {
    public init(floatLiteral value: Double) {
        self = .double(value)
    }
}

extension DynamicObject: ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        if let expression = SingleExpression(rawValue: value) {
            self = .expression(expression)
        } else {
            self = .string(value)
        }
    }
}

extension DynamicObject: ExpressibleByArrayLiteral {
    public init(arrayLiteral elements: DynamicObject...) {
        self = .array(elements)
    }
}

extension DynamicObject: ExpressibleByDictionaryLiteral {
    public init(dictionaryLiteral elements: (String, DynamicObject)...) {
        var dictionary: [String: DynamicObject] = [:]
        elements.forEach { dictionary[$0.0] = $0.1 }
        self = .dictionary(dictionary)
    }
}

fileprivate func _mergeDynamicObjects(_ d1: DynamicObject, _ d2: DynamicObject) -> DynamicObject {
    
    guard case .dictionary(let dict1) = d1, case .dictionary(let dict2) = d2 else {
        return d2
    }
    
    var dObject: [String: DynamicObject] = [:]
    
    let d1Keys = dict1.keys
    let d2Keys = dict2.keys
    
    for k in d1Keys where !d2Keys.contains(k) {
        dObject[k] = dict1[k]
    }

    for k in d2Keys where !d1Keys.contains(k) {
        dObject[k] = dict2[k]
    }

    let commonKeys = d1Keys.filter({
        d2Keys.contains($0)
    })

    for k in commonKeys {

        guard let d1Obj = dict1[k], let d2Obj = dict2[k] else {
            continue
        }
        
        if case .dictionary = d1Obj, case .dictionary = d2Obj {
            dObject[k] = _mergeDynamicObjects(d1Obj, d2Obj)
            continue
        }

        dObject[k] = d2Obj
    }
    
    return .dictionary(dObject)
}
