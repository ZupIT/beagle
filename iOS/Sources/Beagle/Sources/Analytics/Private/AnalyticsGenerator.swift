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

    // MARK: - Action

    func createRecord() -> AnalyticsRecord? {
        guard let name = getActionName() else { return assertNeverGetsHere(or: nil) }

        guard let config = configForAction(named: name) else { return nil }

        addAttributesAndAdditionalEntries(config: config)

        addScreenInfo()

        values["beagleAction"] = name
        if let event = info.event {
            values["event"] = event
        }

        addComponentInfo()

        return AnalyticsRecord(type: .action, values: values)
    }

    private func getActionName() -> String? {
        Mirror(reflecting: info.action).descendant("_beagleAction_") as? String
            ?? info.controller.dependencies.decoder.nameForAction(ofType: type(of: info.action))
    }

    private func configForAction(named: String) -> ActionConfig? {
        guard let global = globalConfig else {
            // we need to store all attributes until globalConfig gets set
            return .init(attributes: .all)
        }

        let attributes = global[named] ?? []

        switch info.action.analytics {
        case .disabled:
            return nil
        case .enabled(nil), nil:
            return .init(attributes: .some(attributes))
        case .enabled(let analytics?):
            return .init(
                attributes: .some(analytics.attributes ?? [] + attributes),
                additional: analytics.additionalEntries ?? [:]
            )
        }
    }

    private struct ActionConfig {
        let attributes: ActionAttributes
        var additional = [String: DynamicObject]()
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

    private func addAttributesAndAdditionalEntries(config: ActionConfig) {
        [
            info.action.getSomeAttributes(config.attributes, contextProvider: info.origin),
            makeAdditionalEntries(config.additional)
        ].forEach {
            values.merge($0) { _, new in new }
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
