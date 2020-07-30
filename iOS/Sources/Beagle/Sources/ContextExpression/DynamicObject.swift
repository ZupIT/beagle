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
    @available(*, deprecated, message: "use evaluate(with view: UIView) instead")
    public func get(with view: UIView) -> DynamicObject {
        return evaluate(with: view)
    }
    
    public func evaluate(with view: UIView) -> DynamicObject {
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
            return .array(array.map { $0.evaluate(with: view) })
        case let .dictionary(dictionary):
            return .dictionary(dictionary.mapValues { $0.evaluate(with: view) })
        case let .expression(expression):
            let dynamicObject: DynamicObject? = view.evaluate(for: expression)
            return dynamicObject ?? .empty
        }
    }
}

// MARK: ExpressibleByLiteral

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

// MARK: Set value with Path

extension DynamicObject {

    mutating func set(_ value: DynamicObject, forPath path: Path) {
        let object = _compilePath(value, path)
        self = self.merge(object)
    }
    
    func merge(_ other: DynamicObject) -> DynamicObject {
        return _mergeDynamicObjects(self, other)
    }
}

private func _compilePath(_ value: DynamicObject, _ path: Path) -> DynamicObject {
    
    guard let pathElement = path.nodes.first else {
        return value
    }
    
    let remainingPath = Path(nodes: Array(path.nodes[1..<path.nodes.count]))
    
    if remainingPath.nodes.isEmpty {
        
        if case .key(let name) = pathElement {
            return .dictionary([name: value])
        }
        
        if case .index(let idx) = pathElement {
            
            let size = idx + 1
            var array: [DynamicObject] = Array(
                repeating: .empty,
                count: size
            )
            
            array[idx] = value
            
            return .array(array)
        }
    }
    
    var object: DynamicObject = .empty
    
    if case .key(let name) = pathElement {
        object = .dictionary([name: _compilePath(value, remainingPath)])
    }
    
    if case .index(let idx) = pathElement {
        
        let size = idx + 1
        var array: [DynamicObject] = Array(
            repeating: .empty,
            count: size
        )
        
        array[idx] = _compilePath(value, remainingPath)
        
        object = .array(array)
    }
    
    return object
}

private func _mergeDynamicObjects(_ d1: DynamicObject, _ d2: DynamicObject) -> DynamicObject {
    
    if case .array(let array1) = d1, case .array(let array2) = d2 {
        
        func _select(_ e1: DynamicObject?, _ e2: DynamicObject?) -> DynamicObject? {
            if e2 == nil { return e1 }
            if case .empty = e2 { return e1 }
            return e2
        }
        
        let size = max(array1.count, array2.count)
        var array = [DynamicObject](repeating: .empty, count: size)
        for i in 0..<size {
            if let element = _select(array1[safe: i], array2[safe: i]) {
                array[i] = element
            }
        }
        
        return .array(array)
    }
    
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

    let commonKeys = d1Keys.filter {
        d2Keys.contains($0)
    }

    for k in commonKeys {

        guard let d1Obj = dict1[k], let d2Obj = dict2[k] else {
            continue
        }
        
        if case .dictionary = d1Obj, case .dictionary = d2Obj {
            dObject[k] = _mergeDynamicObjects(d1Obj, d2Obj)
            continue
        }
        
        if case .array = d1Obj, case .array = d2Obj {
            dObject[k] = _mergeDynamicObjects(d1Obj, d2Obj)
            continue
        }

        dObject[k] = d2Obj
    }
    
    return .dictionary(dObject)
}
