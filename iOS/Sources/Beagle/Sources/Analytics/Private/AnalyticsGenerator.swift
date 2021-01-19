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

    private let mapToDictionary: MapActionToDictionary

    init(info: AnalyticsService.ActionInfo) {
        self.info = info
        self.mapToDictionary = MapActionToDictionary(action: info.action, contextProvider: info.origin)
    }

    // MARK: - Action

    func createRecord() -> AnalyticsRecord? {
        let reflectionName = Mirror(reflecting: info.action).descendant("_beagleAction_") as? String
        guard
            let name = reflectionName ?? info.controller.dependencies.decoder.nameForAction(ofType: type(of: info.action))
        else { return nil }

//        var values = [String: Any].init(uniqueKeysWithValues:
//            Mirror(reflecting: info.action).children.compactMap {
//                if let label = $0.label {
//                    return (label, $0.value)
//                } else {
//                    return nil
//                }
//            }
//        )
//        values.removeValue(forKey: "analytics")

        var values = getAttributesAndAdditionalEntries()

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

    private func getAttributesAndAdditionalEntries() -> [String: Any] {
        guard case .enabled(let analytics) = info.action.analytics else { return [:] }

        var values = [String: Any]()
        [
            mapToDictionary.getAttributes(analytics?.attributes ?? []),
            makeAdditionalEntries(analytics?.additionalEntries ?? [:])
        ].forEach {
            values.merge($0) { _, new in new }
        }
        return values
    }

    private func makeAdditionalEntries(_ entries: [String: DynamicObject]) -> [String: Any] {
        entries.reduce(into: [String: Any]()) { result, entry in
            if let value = entry.value.asAny() {
                result[entry.key] = value
            }
        }
    }

    private func screenURL() -> String? {
        switch info.controller.screenType {
        case .remote(let remote):
            return remote.url
        case .declarative(let screen):
            return screen.identifier
        case .declarativeText:
            return nil
        }
    }
}
