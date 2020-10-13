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

import BeagleSchema
import Foundation
import UIKit

extension DynamicObject {
    public func evaluate(with view: UIView) -> DynamicObject {
        switch self {
        case .empty, .bool, .int, .double, .string:
            return self
            
        case let .array(array):
            return .array(array.map { $0.evaluate(with: view) })
        case let .dictionary(dictionary):
            return .dictionary(dictionary.mapValues { $0.evaluate(with: view) })
        case let .expression(expression):
            let dynamicObject: DynamicObject? = view.evaluate(for: expression)
            return dynamicObject ?? .empty
        }
    }
    
    @available(*, deprecated, message: "use evaluate(with view: UIView) instead")
    public func get(with view: UIView) -> DynamicObject {
        return evaluate(with: view)
    }
}

// MARK: ExpressibleByLiteral

extension DynamicObject: ExpressibleByNilLiteral {
    public init(nilLiteral: Void) {
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
            self = .expression(.single(expression))
        } else if let expression = MultipleExpression(rawValue: value) {
            self = .expression(.multiple(expression))
        } else {
            self = .string(value.escapeExpressions())
        }
    }
}

extension DynamicObject: ExpressibleByStringInterpolation {}

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

// MARK: Evaluate Path

extension DynamicObject {
    
    subscript(path: Path) -> DynamicObject {
        var nodes = path.nodes[...]
        return Self.evaluate(&nodes, self)
    }
    
    private static func evaluate(_ expression: inout ArraySlice<Path.Node>, _ model: DynamicObject) -> DynamicObject {
        guard let first = expression.first else {
            return model
        }
        switch first {
        case let .key(key):
            guard case let .dictionary(dictionary) = model, let value = dictionary[key] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)

        case let .index(index):
            guard case let .array(array) = model, let value = array[safe: index] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)
        }
    }
}

// MARK: Set value with Path

extension DynamicObject {
    func set(_ value: DynamicObject, with path: Path) -> DynamicObject {
        return set(value, with: path.nodes[...])
    }
    
    private func set(_ value: DynamicObject, with path: ArraySlice<Path.Node>) -> DynamicObject {
        guard let node = path.first else { return value }
        let newPath = path.dropFirst()
        
        switch node {
        case let .key(key):
            if case var .dictionary(dictionary) = self {
                if let old = dictionary[key] {
                    dictionary[key] = old.set(value, with: newPath)
                    return .dictionary(dictionary)
                }
                dictionary[key] = DynamicObject.empty.set(value, with: newPath)
                return .dictionary(dictionary)
            }
            return .dictionary([key: DynamicObject.empty.set(value, with: newPath)])
        case let .index(index):
            if case var .array(array) = self {
                if let old = array[safe: index] {
                    array[index] = old.set(value, with: newPath)
                    return .array(array)
                }
                var newArray = dynamicArray(count: index + 1, with: array)
                newArray[index] = DynamicObject.empty.set(value, with: newPath)
                return .array(newArray)
            }
            var newArray = dynamicArray(count: index + 1)
            newArray[index] = DynamicObject.empty.set(value, with: newPath)
            return .array(newArray)
        }
    }
    
    private func dynamicArray(count: Int, with array: [DynamicObject]? = nil) -> [DynamicObject] {
        var result: [DynamicObject] = .init(repeating: .empty, count: count)
        guard let array = array, array.count < count else { return result }
        for (index, element) in array.enumerated() {
            result[index] = element
        }
        return result
    }
}
