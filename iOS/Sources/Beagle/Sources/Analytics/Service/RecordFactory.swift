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

// MARK: Screen

func makeScreenRecord(
    screen: ScreenType,
    isScreenEnabled: Bool
) -> AnalyticsRecord? {
    guard isScreenEnabled else { return nil }

    return AnalyticsRecord(type: .screen, screen: screenInfo(screen), timestamp: timestamp())
}

// MARK: Action

struct ActionRecordFactory {

    let info: AnalyticsService.ActionInfo
    let globalConfig: AnalyticsConfig.AttributesByActionName

    func makeRecord() -> AnalyticsRecord? {
        guard let name = getActionName() else { return nil }
        guard let values = enabledValuesForAction(named: name) else { return nil }

        let attributes = info.action.getAttributes(values.attributes, contextProvider: info.origin)

        let action = AnalyticsRecord.Action(
            beagleAction: name,
            event: info.event,
            component: componentInfo(),
            attributes: attributes,
            additionalEntries: values.additional
        )

        return AnalyticsRecord(
            type: .action(action),
            screen: screenInfo(info.controller.screenType),
            timestamp: timestamp()
        )
    }
}

// MARK: - Private

private func timestamp() -> Double {
    return (Date().timeIntervalSince1970 * 1000).rounded()
}

private func screenInfo(_ screenType: ScreenType) -> String? {
    switch screenType {
    case .remote(let remote):
        return remote.url
    case .declarative(let declarative):
        return declarative.identifier
    case .declarativeText:
        return ""
    }
}

private extension ActionRecordFactory {

    func getActionName() -> String? {
        Mirror(reflecting: info.action).descendant("_beagleAction_") as? String
            ?? info.controller.dependencies.decoder.nameForAction(ofType: type(of: info.action))
    }

    struct EnabledValues {
        let attributes: ActionAttributes
        var additional = DynamicDictionary()
    }

    func enabledValuesForAction(named name: String) -> EnabledValues? {
        switch (info.action as? AnalyticsAction)?.analytics {
        case .disabled:
            return nil

        case .enabled(let analytics?):
            return .init(
                attributes: .some(analytics.attributes ?? []),
                additional: analytics.additionalEntries ?? [:]
            )

        case nil:
            return globalAttributes(action: name)

        case .enabled(nil):
            return globalAttributes(action: name)
                ?? .init(attributes: .some([]))
        }
    }

    private func globalAttributes(action: String) -> EnabledValues? {
        return globalConfig[action].ifSome {
            .init(attributes: .some($0))
        }
    }

    func componentInfo() -> AnalyticsRecord.Action.Component {
        let name = info.origin.componentType
            .ifSome(
                info.controller.dependencies.decoder.nameForComponent(ofType:)
            )

        let id = info.origin.accessibilityIdentifier

        let point = info.origin.convert(CGPoint.zero, to: nil)
        let position = (
            x: Double(point.x),
            y: Double(point.y)
        )
        return .init(id: id, type: name, position: .init(x: position.x, y: position.y))
    }
}
