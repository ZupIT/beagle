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

class AnalyticsGenerator {

    let info: AnalyticsService.ActionInfo
    let globalConfig: AnalyticsConfig.AttributesByActionName?

    init(
        info: AnalyticsService.ActionInfo,
        globalConfig: AnalyticsConfig.AttributesByActionName?
    ) {
        self.info = info
        self.globalConfig = globalConfig
    }

    private var values = [String: Any]()
    private var name = ""

    // MARK: - Action

    func createRecord() -> AnalyticsRecord? {
        let reflectionName = Mirror(reflecting: info.action).descendant("_beagleAction_") as? String
        guard
            let name = reflectionName ?? info.controller.dependencies.decoder.nameForAction(ofType: type(of: info.action))
        else { return nil }
        self.name = name

        addAttributesAndAdditionalEntries()

        addScreenInfo()

        values["beagleAction"] = name
        if let event = info.event {
            values["event"] = event
        }

        addComponentInfo()

        return AnalyticsRecord(type: .action, values: values)
    }

    private func addScreenInfo() {
        let screen: String?
        switch info.controller.screenType {
        case .remote(let remote):
            screen = remote.url
        case .declarative(let declerative):
            screen = declerative.identifier
        case .declarativeText:
            screen = nil
        }

        screen.map { values["screen"] = $0 }
    }

    private func addAttributesAndAdditionalEntries() {
        let attributes = decideWhichAttributesShouldBeRecorded()

        var additional = [String: DynamicObject]()
        if case .enabled(let analytics?) = info.action.analytics {
            additional = analytics.additionalEntries ?? [:]
        }

        [
            info.action.getSomeAttributes(attributes, contextProvider: info.origin),
            makeAdditionalEntries(additional)
        ].forEach {
            values.merge($0) { _, new in new }
        }
    }

    private func decideWhichAttributesShouldBeRecorded() -> ActionAttributes {
        guard let global = globalConfig else {
            // we need to store all attributes for when globalConfig gets set
            return .all
        }

        let attributes = global[name] ?? []
        switch info.action.analytics {
        case .disabled:
            return .some([])
        case .enabled(nil), nil:
            return .some(attributes)
        case .enabled(let analytics?):
            return .some(analytics.attributes ?? [] + attributes)
        }
    }

    private func makeAdditionalEntries(_ entries: [String: DynamicObject]) -> [String: Any] {
        entries.reduce(into: [String: Any]()) { result, entry in
            if let value = entry.value.asAny() {
                result[entry.key] = value
            }
        }
    }

    private func addComponentInfo() {
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
    }
}
