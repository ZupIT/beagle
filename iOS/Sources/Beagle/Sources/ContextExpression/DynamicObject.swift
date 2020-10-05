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

    mutating func set(_ value: DynamicObject, forPath path: Path) {
        let object = compilePath(path, in: value)
        self = mergeDynamicObjects(self, object)
    }
}

private func compilePath(_ path: Path, in value: DynamicObject) -> DynamicObject {
    guard let current = path.nodes.first else {
        return value
    }
    
    let remainingPath = Path(nodes: Array(path.nodes[1..<path.nodes.count]))
    
    switch current {
    case .key(let name):
        return .dictionary([name: compilePath(remainingPath, in: value)])
    
    case .index(let index):
        var array = [DynamicObject].init(
            repeating: .empty,
            count: index + 1
        )
        
        array[index] = compilePath(remainingPath, in: value)
        
        return .array(array)
    }
}

private func mergeDynamicObjects(_ object1: DynamicObject, _ object2: DynamicObject) -> DynamicObject {
    switch(object1, object2) {
        
    case let (.array(array1), .array(array2)):
        return mergeArrays(array1, array2)
    
    case let (.dictionary(dict1), .dictionary(dict2)):
        return mergeDictionaries(dict1, dict2)
        
    default:
        return object2
    }
}

private func mergeArrays(_ arrray1: [DynamicObject], _ array2: [DynamicObject]) -> DynamicObject {
    func _select(_ e1: DynamicObject?, _ e2: DynamicObject?) -> DynamicObject? {
        switch e2 {
        case nil, .empty: return e1
        default: return e2
        }
    }
    
    let size = max(arrray1.count, array2.count)
    var array = [DynamicObject](repeating: .empty, count: size)
    for i in 0..<size {
        if let element = _select(arrray1[safe: i], array2[safe: i]) {
            array[i] = element
        }
    }
    
    return .array(array)
}

private func mergeDictionaries(
    _ dict1: [String: DynamicObject],
    _ dict2: [String: DynamicObject]
) -> DynamicObject {
    var dict = dict1
    dict.merge(dict2, uniquingKeysWith: { obj1, obj2 in
        mergeDynamicObjects(obj1, obj2)
    })
    
    return .dictionary(dict)
}
