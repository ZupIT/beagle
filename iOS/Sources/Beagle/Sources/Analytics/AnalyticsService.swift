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

class AnalyticsService {
    
    static var shared: AnalyticsService?
    
    private let provider: AnalyticsProvider
    private var startSessionResult: Result<Void, Error>?
    private var configResult: Result<AnalyticsConfig, Error>?
    
    private let itemsLock = DispatchSemaphore(value: 1)
    private (set) var itemsInQueue = 0
    private (set) lazy var queue = DispatchQueue(
        label: "BeagleAnalyticsService",
        qos: .utility,
        attributes: .initiallyInactive,
        autoreleaseFrequency: .inherit,
        target: .global(qos: .utility)
    )
    
    init(provider: AnalyticsProvider) {
        self.provider = provider
        self.provider.startSession { result in
            self.startSessionResult = result
            self.activateQueueIfNeeded()
        }
        self.provider.getConfig { result in
            self.configResult = result
            self.activateQueueIfNeeded()
        }
    }
    
    private func activateQueueIfNeeded() {
        guard startSessionResult != nil && configResult != nil else { return }
        queue.activate()
    }
    
    // MARK: - Create Events
    
    func createRecord(screen: ScreenType) {
        createRecord { self.sendScreenRecord(screen) }
    }
    
    func createRecord(action: Action, origin: UIView, event: String?, controller: BeagleControllerProtocol) {
        createRecord { self.sendActionRecord(action, event: event, origin: origin, controller: controller) }
    }
    
    private func createRecord(_ work: @escaping () -> Void) {
        guard getQueueSlot() else { return }
        queue.async {
            work()
            self.releaseQueueSlot()
        }
    }
    
    private func getQueueSlot() -> Bool {
        itemsLock.wait()
        defer { itemsLock.signal() }
        guard itemsInQueue < provider.maximumItemsInQueue ?? 100 else {
            return false
        }
        itemsInQueue += 1
        return true
    }
    
    private func releaseQueueSlot() {
        itemsLock.wait()
        defer { itemsLock.signal() }
        itemsInQueue -= 1
    }
    
    // MARK: - Screen
    
    private func sendScreenRecord(_ screen: ScreenType) {
        guard case .success(let config) = configResult, config.enableScreenAnalytics ?? true else { return }
        let record = AnalyticsRecord(type: .screen, values: valuesFor(screen: screen))
        DispatchQueue.main.async {
            self.provider.createRecord(record)
        }
    }
    
    private func valuesFor(screen: ScreenType) -> [String: Any] {
        var values = [String: Any]()
        switch screen {
        case .remote(let remote):
            values["url"] = remote.url
        case .declarative(let screen):
            values["screenId"] = screen.identifier
        case .declarativeText: ()
        }
        return values
    }
    
    // MARK: - Action
    
    private func sendActionRecord(_ action: Action, event: String?, origin: UIView, controller: BeagleControllerProtocol) {
        guard case .success(let config) = configResult else { return }
        
        let reflectionName = Mirror(reflecting: action).descendant("_beagleAction_") as? String
        guard let name = reflectionName ?? controller.dependencies.decoder.nameForAction(ofType: type(of: action)),
              shouldGenerateAnalytics(action: action, name: name, config: config) else { return }
        
        var values = [String: Any]()
        setValues(of: action, named: name, config: config, origin: origin, in: &values)
        
        if let screen = screenURL(from: controller) {
            values["screen"] = screen
        }
        
        values["beagleAction"] = name
        if let event = event {
            values["event"] = event
        }
        
        DispatchQueue.main.async {
            var componentInfo = (values["component"] as? [String: Any]) ?? [:]
            if let type = origin.componentType,
               let name = controller.dependencies.decoder.nameForComponent(ofType: type) {
                componentInfo["type"] = name
            }
            if let identifier = origin.accessibilityIdentifier {
                componentInfo["id"] = identifier
            }
            let position = origin.convert(CGPoint.zero, to: nil)
            componentInfo["position"] = [
                "x": Double(position.x),
                "y": Double(position.y)
            ]
            values["component"] = componentInfo
            
            let record = AnalyticsRecord(type: .action, values: values)
            self.provider.createRecord(record)
        }
    }
    
    private func setValues(of action: Action, named name: String, config: AnalyticsConfig, origin: UIView, in values: inout [String: Any]) {
        guard case .enabled(let data) = action.analytics else { return }

        let attributes = data?.attributes ?? config.actions[name]
        for attribute in attributes ?? [] {
            if let path = Path(rawValue: attribute), !path.nodes.isEmpty,
               let attributeValue = try? value(from: action, at: path, origin: origin) {
                values = object(values, setting: attributeValue, at: path)
            }
        }
        
        let additionalEntries = (data?.additionalEntries ?? [:])
            .reduce(into: [String: Any]()) { result, entry in
                if let value = entry.value.asAny() {
                    result[entry.key] = value
                }
            }
        values.merge(additionalEntries) { _, new in new }
    }
    
    private func shouldGenerateAnalytics(action: Action, name: String, config: AnalyticsConfig) -> Bool {
        guard let analytics = action.analytics else {
            return config.actions[name] != nil
        }

        return analytics != .disabled
    }
    
    func screenURL(from controller: BeagleControllerProtocol) -> String? {
        switch controller.screenType {
        case .remote(let remote):
            let urlBuilder = controller.dependencies.urlBuilder
            if let baseUrl = urlBuilder.baseUrl, let url = urlBuilder.build(path: remote.url) {
                let urlString = url.absoluteString
                let baseString = baseUrl.absoluteString
                if urlString.hasPrefix(baseString) {
                    let offset = baseString.count - (baseString.last == "/" ? 1 : 0)
                    let start = urlString.index(urlString.startIndex, offsetBy: offset)
                    return String(urlString[start...])
                }
            }
            return remote.url
        case .declarative(let screen):
            return screen.identifier
        case .declarativeText:
            return nil
        }
    }
    
    private func value(from action: Action, at path: Path, origin: UIView) throws -> Any? {
        var value: Any = action
        for node in path.nodes {
            switch node {
            case .index(let index):
                value = try valueAt(index, in: value)
            case .key(let name):
                value = try valueAt(name, in: value)
            }
            
            if let evaluable = value as? ContextEvaluable {
                value = evaluable.evaluateWith(contextProvider: origin)
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
            var result: DynamicObject = .empty
            let workItem = DispatchWorkItem {
                result = view.evaluate(for: expression)
            }
            DispatchQueue.main.async(execute: workItem)
            workItem.wait()
            return result
        case let .value(value):
            return value
        }
    }
}
