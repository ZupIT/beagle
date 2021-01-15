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

    private var remoteScreen: ScreenType { .remote(.init(url: "REMOTE")) }
    private var declarativeScreen: ScreenType { .declarative(Screen(identifier: "DECLARATIVE", child: ComponentDummy())) }

    // MARK: - Action tests

    func testActionAddChildren() throws {
        // Given, When
        let records = try recordsForAction(AddChildren.self, fileName: "testActionAddChildren.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionAlert() throws {
        // Given, When
        let records = try recordsForAction(Alert.self, fileName: "testActionAlert.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionCondition() throws {
        // Given, When
        let records = try recordsForAction(Condition.self, fileName: "testActionCondition.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionConfirm() throws {
        // Given, When
        let records = try recordsForAction(Confirm.self, fileName: "testActionConfirm.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionFormLocalAction() throws {
        // Given, When
        let records = try recordsForAction(FormLocalAction.self, fileName: "testActionFormLocalAction.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionFormRemoteAction() throws {
        // Given, When
        let records = try recordsForAction(FormRemoteAction.self, fileName: "testActionFormRemoteAction.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionFormValidation() throws {
        // Given, When
        let records = try recordsForAction(FormValidation.self, fileName: "testActionFormValidation.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionOpenExternalURL() throws {
        // Given, When
        let records = try recordsForAction(Navigate.self, fileName: "testActionOpenExternalURL.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionOpenNativeRoute() throws {
        // Given, When
        let records = try recordsForAction(Navigate.self, fileName: "testActionOpenNativeRoute.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionPopStack() throws {
        // Given, When
        let records = try recordsForAction(Navigate.self, fileName: "testActionPopStack.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionPopToView() throws {
        // Given, When
        let records = try recordsForAction(Navigate.self, fileName: "testActionPopToView.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionPopView() throws {
        // Given, When
        let records = try recordsForAction(Navigate.self, fileName: "testActionPopView.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionPushStack() throws {
        // Given, When
        let remote = try recordsForAction(Navigate.self, fileName: "testActionPushStack.1")
        let local = try recordsForAction(Navigate.self, fileName: "testActionPushStack.2")
        // Then
        assertSnapshot(matching: remote, as: .json)
        assertSnapshot(matching: local, as: .json)
    }

    func testActionPushView() throws {
        // Given, When
        let remote = try recordsForAction(Navigate.self, fileName: "testActionPushView.1")
        let local = try recordsForAction(Navigate.self, fileName: "testActionPushView.2")
        // Then
        assertSnapshot(matching: remote, as: .json)
        assertSnapshot(matching: local, as: .json)
    }

    func testActionResetApplication() throws {
        // Given, When
        let remote = try recordsForAction(Navigate.self, fileName: "testActionResetApplication.1")
        let local = try recordsForAction(Navigate.self, fileName: "testActionResetApplication.2")
        // Then
        assertSnapshot(matching: remote, as: .json)
        assertSnapshot(matching: local, as: .json)
    }

    func testActionResetStack() throws {
        // Given, When
        let remote = try recordsForAction(Navigate.self, fileName: "testActionResetStack.1")
        let local = try recordsForAction(Navigate.self, fileName: "testActionResetStack.2")
        // Then
        assertSnapshot(matching: remote, as: .json)
        assertSnapshot(matching: local, as: .json)
    }

    func testActionSendRequest() throws {
        // Given, When
        let records = try recordsForAction(SendRequest.self, fileName: "testActionSendRequest.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionSetContext() throws {
        // Given, When
        let records = try recordsForAction(SetContext.self, fileName: "testActionSetContext.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    func testActionSubmitForm() throws {
        // Given, When
        let records = try recordsForAction(SubmitForm.self, fileName: "testActionSubmitForm.1")
        // Then
        assertSnapshot(matching: records, as: .json)
    }

    // MARK: - Aux

    private func recordsForAction<A: Action>(_: A.Type, fileName: String) throws -> [AnalyticsRecord] {
        let action: A = try actionFromJsonFile(fileName: fileName)
        let (service, provider) = analyticsServiceAndProviderStub(
            config: .success(.init()),
            session: .success(())
        )
        let child = AnalyticsTestComponent()
        let context = Context(
            id: "context",
            value: [
                "query": "beagle",
                "remoteRoute": "result of @{context.remoteRoute}",
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
        // swiftlint:disable force_unwrapping
        let origin = controller.view.viewWithTag(type(of: child).tag)!
        // swiftlint:enable force_unwrapping

        service.createRecord(action: action, origin: origin, event: nil, controller: controller)
        waitCreateRecords(service)
        return provider.records
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
