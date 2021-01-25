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
        guard let dict = recursivelyIterateAttributesAndTransformToJson(action) else { return [:] }

        let data = try! JSONSerialization.data(withJSONObject: dict)
        let json = try! JSONDecoder().decode(DynamicObject.self, from: data)
//        let evaluated = json.evaluate(with: contextProvider)

        var values = [String: Any]()
        attributes.forEach { attribute in
            guard let path = Path(rawValue: attribute) else { return }
            let value = json[path].evaluate(with: contextProvider)
            guard value != .empty else { return }
            values[attribute] = value
        }

        return values
    }

    private func recursivelyIterateAttributesAndTransformToJson(_ object: Any) -> Any? {
        if let expression = object as? ExpressionRawValue {
            let value = expression.rawValue
            return recursivelyIterateAttributesAndTransformToJson(value) ?? expression.rawValue
        }
        if let dynamicObject = object as? DynamicObject {
            return dynamicObject.asAny()
        }
        if let array = object as? [Any] {
            return array.compactMap(recursivelyIterateAttributesAndTransformToJson)
        }
        if let dict = object as? [String: Any] {
            return dict
        }

        let mirror = Mirror(reflecting: object)

        let isEnum = mirror.displayStyle == .enum
        if isEnum {
            return String(describing: object)
        }

        let children = mirror.children
        guard !children.isEmpty else { return nil }

        return transformChildrenToDict(mirror.children)
    }

    private func transformChildrenToDict(_ children: Mirror.Children) -> [String: Any] {
        let allAttributes: [(String, Any)] = children.compactMap {
            guard
                let label = $0.label,
                case Optional<Any>.some(var value) = $0.value
            else { return nil }

            value = recursivelyIterateAttributesAndTransformToJson(value) ?? value

            return (label, value)
        }

        return [String: Any](uniqueKeysWithValues: allAttributes)
    }

    enum Error: Swift.Error {
        case indexNotPermittedInAttribute
        case keyNotFound(String)
    }
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
