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

internal enum ActionAttributes: Equatable {
    case all
    case some([String])
}

extension Action {

    func getAttributes(_ attributes: ActionAttributes, contextProvider: UIView) -> DynamicDictionary {
        if case .some(let array) = attributes, array.isEmpty { return [:] }

        let dynamicObject = transformToDynamicObject(self)
            .evaluate(with: contextProvider)
            // don't use analytics attribute
            .set(.empty, with: analyticsPath)

        let dict = dynamicObject.asDictionary()

        switch attributes {
        case .some(let some):
            return dict.getSomeAttributes(some, contextProvider: contextProvider)

        case .all:
            return dict
        }
    }
}

extension DynamicDictionary {

    func getSomeAttributes(_ attributes: [String], contextProvider: UIView?) -> DynamicDictionary {
        let object = DynamicObject.dictionary(self)

        var values = DynamicDictionary()
        attributes.forEach { attribute in
            guard let path = pathForAttribute(attribute) else { return }

            var value = object[path]
            contextProvider.ifSome {
                value = value.evaluate(with: $0)
            }

            guard value != .empty else { return }
            values[attribute] = value
        }

        return values
    }
}

private func pathForAttribute(_ attribute: String) -> Path? {
    guard let path = Path(rawValue: attribute) else { return nil }

    for node in path.nodes {
        if case .index = node { return nil }
    }
    return path
}

// MARK: - JSON Transformation

func transformToDynamicObject(_ any: Any) -> DynamicObject {
    guard let json = validJsonFromObject(any) else { return .empty }

    do {
        let data = try JSONSerialization.data(withJSONObject: json)
        return try JSONDecoder().decode(DynamicObject.self, from: data)
    } catch {
        return .empty
    }
}

// MARK: - Private

private var analyticsPath = Path(nodes: [.key("analytics")])

private func validJsonFromObject(_ object: Any) -> Any? {
    switch handleJsonForSpecifcTypes(object) {
    case .alreadyTransformed(let json):
        return json

    case .shouldUseChildren(let children):
        guard !children.isEmpty else { return object }
        return dictFromChildren(children)

    case .isAnEmptyCollection:
        return nil
    }
}

private func handleJsonForSpecifcTypes(_ object: Any) -> SpecificTypeResult {
    let result: Any?

    switch object {
    case let expression as ExpressionRawValue:
        result = validJsonFromObject(expression.rawValue)

    case let dynamicObject as DynamicObject:
        result = dynamicObject.asAny() as Any
        if isEmptyCollection(result) { return .isAnEmptyCollection }

    case let array as [Any]:
        if isEmptyCollection(array) { return .isAnEmptyCollection }
        result = array.map(validJsonFromObject)

    case let dict as [String: Any]:
        if isEmptyCollection(dict) { return .isAnEmptyCollection }
        result = dict.mapValues(validJsonFromObject)

    default:
        result = nil
    }

    if let result = result {
        return .alreadyTransformed(result)
    }

    let mirror = Mirror(reflecting: object)
    let isEnum = mirror.displayStyle == .enum

    if isEnum {
        let string = String(describing: object).uppercased()
        return .alreadyTransformed(string)
    } else {
        return .shouldUseChildren(mirror.children)
    }
}

private func isEmptyCollection(_ object: Any?) -> Bool {
    guard let object = object else { return true }

    switch object {
    case let array as [Any]:
        return array.isEmpty
    case let dict as [String: Any]:
        return dict.isEmpty
    default:
        return false
    }
}

private func dictFromChildren(_ children: Mirror.Children) -> [String: Any] { // swiftlint:disable syntactic_sugar
    let allAttributes: [(String, Any)] = children.compactMap {
        guard
            let label = $0.label,
            case Optional<Any>.some(let value) = $0.value
        else { return nil }

        guard let newValue = validJsonFromObject(value) else { return nil }
        return (label, newValue)
    }

    return [String: Any](uniqueKeysWithValues: allAttributes)
}

private enum SpecificTypeResult {
    case alreadyTransformed(Any)
    case shouldUseChildren(Mirror.Children)
    case isAnEmptyCollection
}

private protocol ExpressionRawValue {
    var rawValue: Any { get }
}

extension Expression: ExpressionRawValue {
    var rawValue: Any {
        switch self {
        case .expression(let expression): return expression.rawValue
        case .value(let value): return value
        }
    }
}
