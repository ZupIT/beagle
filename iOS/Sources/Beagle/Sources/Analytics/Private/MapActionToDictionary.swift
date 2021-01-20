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
import UIKit

// swiftlint:disable syntactic_sugar

struct MapActionToDictionary {

    let action: Action
    let contextProvider: UIView

    func getAttributes(_ attributes: [String]) -> [String: Any] {
        var values = [String: Any]()
        attributes.forEach { attribute in
            guard
                let path = Path(rawValue: attribute),
                let value = try? value(at: path)
            else { return }

            values[attribute] = value
        }

        return values
    }

    private func value(at path: Path) throws -> Any? {
        var current: Any = action
        for node in path.nodes {
            guard case .key(let key) = node else { throw Error.indexNotPermittedInAttribute }

            current = try valueForKey(key, in: current)

            if let evaluable = current as? ContextEvaluable {
                current = evaluable.evaluateWith(contextProvider: contextProvider)
            }
            if let dynamicObject = current as? DynamicObject {
                guard let any = dynamicObject.asAny() else { return nil }
                current = any
            }
        }

        transformCustomTypeToDict(current).map {
            current = $0
        }

        (current as? [Any]).map(transformArrayWithCustomTypes).map {
            current = $0 as Any
        }

        return current
    }

    private func transformCustomTypeToDict(_ object: Any) -> [String: Any]? {
        if let dict = object as? [String: Any] { return dict }
        if object is [Any] { return nil }

        let children = Mirror(reflecting: object).children

        guard !children.isEmpty else { return nil }

        return [String: Any].init(uniqueKeysWithValues:
            children.compactMap {
                guard
                    let label = $0.label,
                    case Optional<Any>.some(var value) = $0.value
                else { return nil }

                if let evaluable = value as? ContextEvaluable {
                    value = evaluable.evaluateWith(contextProvider: contextProvider)
                }
                if let dynamicObject = value as? DynamicObject {
                    guard let any = dynamicObject.asAny() else { return nil }
                    value = any
                }

                transformCustomTypeToDict(value).map {
                    value = $0
                }

                return (label, value)
            }
        )
    }

    private func transformArrayWithCustomTypes(_ array: [Any]) -> [Any]? {
        array.compactMap {
            if let otherArray = $0 as? [Any] {
                return transformArrayWithCustomTypes(otherArray)
            } else {
                return transformCustomTypeToDict($0)
            }
        }
    }

    private func valueForKey(_ key: String, in container: Any) throws -> Any {
        if let dictionary = container as? [String: Any] {
            guard let nodeValue = dictionary[key] else {
                throw Error.keyNotFound(key)
            }
            return unwrap(nodeValue)
        }
        guard let descendant = Mirror(reflecting: container).descendant(key) else {
            throw Error.keyNotFound(key)
        }
        return unwrap(descendant)
    }

//    private func valueForIndex(_ index: Int, in value: Any) throws -> Any {
//        let children = Mirror(reflecting: value).children
//        let (start, end) = (children.startIndex, children.endIndex)
//        guard let childIndex = children.index(start, offsetBy: index, limitedBy: end),
//              start..<end ~= childIndex  else {
//            throw Error.indexOutOfBounds(index)
//        }
//        return unwrap(children[childIndex].value)
//    }
//
//    private func object(_ original: [String: Any], setting value: Any, at path: Path) -> [String: Any] {
//        func object(_ original: Any, setting value: Any, nodes: inout ArraySlice<Path.Node>) -> Any {
//            guard let node = nodes.popFirst() else { return value }
//            switch node {
//            case .key(let key):
//                var result = original as? [String: Any] ?? [:]
//                result[key] = unwrap(object(result[key, default: [:]], setting: value, nodes: &nodes))
//                return result
//            case .index(let index):
//                var result = original as? [Any] ?? []
//                let nilValue = Optional<Any>.none as Any
//                let valueAtIndex = index < result.count ? result[index] : nilValue
//                while result.count <= index {
//                    result.append(nilValue)
//                }
//                result[index] = unwrap(object(valueAtIndex, setting: value, nodes: &nodes))
//                return result
//            }
//        }
//        var nodes = path.nodes[...]
//        return object(original, setting: value, nodes: &nodes) as? [String: Any] ?? original
//    }

    enum Error: Swift.Error {
        case indexNotPermittedInAttribute
        case keyNotFound(String)
    }

    fileprivate func unwrap(_ object: Any) -> Any {
        if case Optional<Any>.some(let unwraped) = object {
            return unwraped
        }
        return object
    }
}

private protocol ContextEvaluable {
    func evaluateWith(contextProvider view: UIView) -> Any
}

extension Expression: ContextEvaluable {
    fileprivate func evaluateWith(contextProvider view: UIView) -> Any {
        switch self {
        case let .expression(expression):
            // As the expression evaluation is done using the view
            // hierarchy, it must be done within the main thread.
//            var result: DynamicObject = .empty
//            let workItem = DispatchWorkItem {
//                result = view.evaluate(for: expression)
//            }
//            DispatchQueue.main.async(execute: workItem)
//            workItem.wait()
            return view.evaluate(for: expression)
        case let .value(value):
            return value
        }
    }
}
