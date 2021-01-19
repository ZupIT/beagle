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

struct AnalyticsGenerator {

    let info: AnalyticsService.ActionInfo

    // MARK: - Action

    func createRecord() -> AnalyticsRecord? {
        let reflectionName = Mirror(reflecting: info.action).descendant("_beagleAction_") as? String
        guard
            let name = reflectionName ?? info.controller.dependencies.decoder.nameForAction(ofType: type(of: info.action))
        else { return nil }

        var values = [String: Any]()
        setValues(in: &values)

        if let screen = screenURL() {
            values["screen"] = screen
        }

        values["beagleAction"] = name
        if let event = info.event {
            values["event"] = event
        }

        var componentInfo = (values["component"] as? [String: Any]) ?? [:]
        if let type = info.origin.componentType,
           let name = info.controller.dependencies.decoder.nameForComponent(ofType: type) {
            componentInfo["type"] = name
        }
        if let identifier = info.origin.accessibilityIdentifier {
            componentInfo["id"] = identifier
        }
        let position = info.origin.convert(CGPoint.zero, to: nil)
        componentInfo["position"] = [
            "x": Double(position.x),
            "y": Double(position.y)
        ]
        values["component"] = componentInfo

        return AnalyticsRecord(type: .action, values: values)
    }

    private func setValues(in values: inout [String: Any]) {
        var attributes = [String]()
        var additionalEntries = [String: DynamicObject]()
        if case .enabled(let d) = info.action.analytics {
            attributes = d?.attributes ?? []
            additionalEntries = d?.additionalEntries ?? [:]
        }

        for attribute in attributes {
            if let path = Path(rawValue: attribute), !path.nodes.isEmpty,
               let attributeValue = try? value(at: path) {
                values = object(values, setting: attributeValue, at: path)
            }
        }

        let reduced = additionalEntries.reduce(into: [String: Any]()) { result, entry in
            if let value = entry.value.asAny() {
                result[entry.key] = value
            }
        }
        values.merge(reduced) { _, new in new }
    }

    func screenURL() -> String? {
        switch info.controller.screenType {
        case .remote(let remote):
            return remote.url
        case .declarative(let screen):
            return screen.identifier
        case .declarativeText:
            return nil
        }
    }

    private func value(at path: Path) throws -> Any? {
        var value: Any = info.action
        for node in path.nodes {
            switch node {
            case .index(let index):
                value = try valueAt(index, in: value)
            case .key(let name):
                value = try valueAt(name, in: value)
            }

            if let evaluable = value as? ContextEvaluable {
                value = evaluable.evaluateWith(contextProvider: info.origin)
            }
            if let dynamicObject = value as? DynamicObject {
                guard let any = dynamicObject.asAny() else { return nil }
                value = any
            }
        }
        return value
    }

    private func valueAt(_ index: Int, in value: Any) throws -> Any {
        let children = Mirror(reflecting: value).children
        let (start, end) = (children.startIndex, children.endIndex)
        guard let childIndex = children.index(start, offsetBy: index, limitedBy: end),
              start..<end ~= childIndex  else {
            throw EvaluationError.indexOutOfBounds(index)
        }
        return unwrap(children[childIndex].value)
    }

    private func valueAt(_ name: String, in value: Any) throws -> Any {
        if let dictionary = value as? [String: Any] {
            guard let nodeValue = dictionary[name] else {
                throw EvaluationError.keyNotFound(name)
            }
            return nodeValue
        }
        guard let descendant = Mirror(reflecting: value).descendant(name) else {
            throw EvaluationError.keyNotFound(name)
        }
        return unwrap(descendant)
    }

    private func object(_ original: [String: Any], setting value: Any, at path: Path) -> [String: Any] {
        func object(_ original: Any, setting value: Any, nodes: inout ArraySlice<Path.Node>) -> Any {
            guard let node = nodes.popFirst() else { return value }
            switch node {
            case .key(let key):
                var result = original as? [String: Any] ?? [:]
                result[key] = unwrap(object(result[key, default: [:]], setting: value, nodes: &nodes))
                return result
            case .index(let index):
                var result = original as? [Any] ?? []
                let nilValue = Optional<Any>.none as Any
                let valueAtIndex = index < result.count ? result[index] : nilValue
                while result.count <= index {
                    result.append(nilValue)
                }
                result[index] = unwrap(object(valueAtIndex, setting: value, nodes: &nodes))
                return result
            }
        }
        var nodes = path.nodes[...]
        return object(original, setting: value, nodes: &nodes) as? [String: Any] ?? original
    }

    private func unwrap(_ object: Any) -> Any {
        // swiftlint:disable syntactic_sugar
        if case Optional<Any>.some(let unwraped) = object {
            return unwraped
        }
        // swiftlint:enable syntactic_sugar
        return object
    }

    private enum EvaluationError: Error {
        case indexOutOfBounds(Int)
        case keyNotFound(String)
    }
}

// MARK: - Helper Extensions

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

