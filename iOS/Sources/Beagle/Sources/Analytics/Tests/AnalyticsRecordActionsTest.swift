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

import XCTest
import SnapshotTesting
@testable import Beagle

class AnalyticsRecordActionsTest: AnalyticsTestHelpers {

    func testRecordingMultipleActions() throws {
        let records = [
            try doRecord(AddChildren.self, fromJson: "AddChildren-1"),
            try doRecord(Alert.self, fromJson: "Alert-1"),
            try doRecord(Condition.self, fromJson: "Condition-1"),
            try doRecord(Confirm.self, fromJson: "Confirm-1"),
            try doRecord(FormLocalAction.self, fromJson: "FormLocalAction-1"),
            try doRecord(FormValidation.self, fromJson: "FormValidation-1"),
            try doRecord(Navigate.self, fromJson: "OpenNativeRoute-1"),
            try doRecord(Navigate.self, fromJson: "PopView-1"),
            try doRecord(Navigate.self, fromJson: "PushStack-1"),
            try doRecord(Navigate.self, fromJson: "PushStack-2"),
            try doRecord(SendRequest.self, fromJson: "SendRequest-1")
        ]

        records.forEach {
            assertSnapshot(matching: $0.0, as: .json, named: $0.file, testName: "recorded")
        }
    }

    // MARK: - Aux

    // TODO: full end to end scenario
    // I still need to test a full end to end scenario, in which I could use the screen with context below to actually
    // record a full action (with default values).

    private func doRecord<A: Action>(_: A.Type, fromJson: String) throws -> ([String: DynamicObject], file: String) {
        let action: A = try actionFromJsonFile(fileName: fromJson)
        
        let child = AnalyticsTestComponent()
        let context = Context(
            id: "context",
            value: [
                "query": "beagle",
                "remoteRoute": "some other value -> @{context.headers.pi}",
                "method": "GET",
                "headers": [
                    "platform": "beagle-ios",
                    "pi": "3.14159265359"
                ]
            ]
        )
        let screen = Screen(identifier: "analytics-actions", child: child, context: context)
        let dependencies = BeagleDependencies()
        dependencies.decoder.register(component: AnalyticsTestComponent.self)

        let controller = BeagleScreenViewController(
            viewModel: .init(screenType: .declarative(screen), dependencies: dependencies)
        )
        _ = BeagleNavigationController(rootViewController: controller)
        let origin = try XCTUnwrap(controller.view.viewWithTag(type(of: child).tag))

        var attributes = [String]()
        if case .enabled(let config?) = action.analytics {
            attributes = config.attributes ?? []
        }

        let result = action.getSomeAttributes(.some(attributes), contextProvider: origin)
        return (result, fromJson)
    }
}

private struct AnalyticsTestComponent: ServerDrivenComponent, IdentifiableComponent {

    static var tag: Int { 682297 }

    var id: String? { "test-component-id" }

    func toView(renderer: BeagleRenderer) -> UIView {
        let view = UIView()
        view.tag = Self.tag
        return view
    }
}
