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

private var analyticsPath = Path(nodes: [.key("analytics")])

extension Action {

    func getSomeAttributes(_ attributes: ActionAttributes, contextProvider: UIView) -> [String: DynamicObject] {
        if case .some(let array) = attributes, array.isEmpty { return [:] }

        let dynamicObject = asDynamicObject()
            .evaluate(with: contextProvider)
            // don't use analytics attribute
            .set(.empty, with: analyticsPath)

        switch attributes {
        case .some(let some):
            return getSomeAttributes(some, of: dynamicObject, contextProvider: contextProvider)

        case .all:
            guard case .dictionary(let dict) = dynamicObject else {
                assertionFailure("this should not happen since an Action will always be mapped to DynamicObject.dictionary"); return .init()
            }
            return dict
        }
    }

    func getSomeAttributes(_ attributes: [String], of object: DynamicObject, contextProvider: UIView) -> [String: DynamicObject] {
        var values = [String: DynamicObject]()
        attributes.forEach { attribute in
            guard let path = pathForAttribute(attribute) else { return }

            let value = object[path].evaluate(with: contextProvider)
            guard value != .empty else { return }
            values[attribute] = value
        }

        return values
    }

    private func pathForAttribute(_ attribute: String) -> Path? {
        guard let path = Path(rawValue: attribute) else { return nil }

        for node in path.nodes {
            if case .index = node { return nil }
        }
        return path
    }

    // MARK: - JSON Transformation

    private func asDynamicObject() -> DynamicObject {
        let json = validJsonFromObject(self)
        let data = try! JSONSerialization.data(withJSONObject: json)
        return try! JSONDecoder().decode(DynamicObject.self, from: data)
    }

    private func validJsonFromObject(_ object: Any) -> Any {
        switch handleJsonForSpecifcTypes(object) {
        case .alreadyTransformed(let json):
            return json

        case .shouldUseChildren(let children):
            guard !children.isEmpty else { return object }
            return dictFromChildren(children)
        }
    }

    private func handleJsonForSpecifcTypes(_ object: Any) -> SpecificTypeResult {
        let result: Any?

        switch object {
        case let expression as ExpressionRawValue:
            result = validJsonFromObject(expression.rawValue)
        case let dynamicObject as DynamicObject:
            result = dynamicObject.asAny() as Any
        case let array as [Any]:
            result = array.compactMap(validJsonFromObject)
        case let dict as [String: Any]:
            result = dict
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

    private func dictFromChildren(_ children: Mirror.Children) -> [String: Any] { // swiftlint:disable syntactic_sugar
        let allAttributes: [(String, Any)] = children.compactMap {
            guard
                let label = $0.label,
                case Optional<Any>.some(let value) = $0.value
            else { return nil }

            return (label, validJsonFromObject(value))
        }

        return [String: Any](uniqueKeysWithValues: allAttributes)
    }
}

internal enum ActionAttributes {
    case all
    case some([String])
}

private enum SpecificTypeResult {
    case alreadyTransformed(Any)
    case shouldUseChildren(Mirror.Children)
}

private enum Error: Swift.Error {
    case indexNotPermittedInAttribute
    case keyNotFound(String)
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
