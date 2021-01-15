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

class AnalyticsServiceTests: XCTestCase {
    
    private var timeout: TimeInterval { 1 }
    private var remoteScreen: ScreenType { .remote(.init(url: "REMOTE")) }
    private var declarativeScreen: ScreenType { .declarative(Screen(identifier: "DECLARATIVE", child: ComponentDummy())) }
    
    // MARK: Configuration tests
    
    func testWaitStartSessionAndGetConfigToCreateRecord() {
        // Given
        let getConfig = expectation(description: "getConfig")
        let startSession = expectation(description: "startSession")
        
        let provider = AnalyticsProviderStub()
        provider.getConfig = { completion in
            completion(.success(AnalyticsConfig(enableScreenAnalytics: true)))
            getConfig.fulfill()
        }
        provider.startSession = { completion in
            completion(.success(()))
            startSession.fulfill()
        }
        let sut = AnalyticsService(provider: provider)
        
        // When create session without config available
        sut.createRecord(screen: remoteScreen)
        wait(for: [startSession], timeout: timeout)
        
        // Then should not create records
        XCTAssertEqual(provider.records.count, 0)
        
        // When did start session and config and available
        wait(for: [getConfig], timeout: timeout)
        waitCreateRecords(sut)
        
        // Then should create records
        XCTAssertEqual(provider.records.count, 1)
    }
    
    func testMaximumItemsInQueueConfig() {
        // Given
        let maximumItemsInQueue = 20
        let getConfig = expectation(description: "getConfig")
        let startSession = expectation(description: "startSession")
        var configCompletion: ((Result<AnalyticsConfig, Error>) -> Void)?
        let provider = AnalyticsProviderStub()
        provider.maximumItemsInQueue = maximumItemsInQueue
        provider.getConfig = {
            configCompletion = $0
            getConfig.fulfill()
        }
        provider.startSession = {
            $0(.success(()))
            startSession.fulfill()
        }
        
        let sut = AnalyticsService(provider: provider)
        
        // When
        (0..<(maximumItemsInQueue + 10)).forEach { _ in
            sut.createRecord(screen: remoteScreen)
        }
        wait(for: [getConfig, startSession], timeout: timeout)
        configCompletion?(.success(.init()))
        waitCreateRecords(sut)
        
        // Then
        XCTAssertEqual(provider.records.count, maximumItemsInQueue)
    }
    
    func testEnableScreenAnalyticsConfig() {
        testEnableScreenAnalytics(false)
        testEnableScreenAnalytics(true)
    }
    
    private func testEnableScreenAnalytics(_ enabled: Bool) {
        // Given
        let (sut, provider) = analyticsServiceAndProviderStub(
            config: .success(.init(enableScreenAnalytics: enabled)),
            session: .success(())
        )
        // When
        sut.createRecord(screen: remoteScreen)
        waitCreateRecords(sut)
        // Then
        XCTAssertEqual(provider.records.count, enabled ? 1 : 0)
    }
    
    func testDefaultActionConfig() {
        // Given
        let action = AnalyticsTestAction(
            _beagleAction_: "enabled",
            values: ["itemA": "Value A", "itemB": "Value B", "itemC": "Value C"],
            analytics: .enabled(.init(
                additionalEntries: ["case": "use default config"]
            ))
        )
        let (sut, provider) = analyticsServiceAndProviderStub(
            config: .success(.init(actions: [action._beagleAction_: ["values.itemC", "values.itemA"]])),
            session: .success(())
        )
        
        // When
        sut.createRecord(action: action, origin: UIView(), event: "testCase", controller: BeagleControllerStub(remoteScreen))
        waitCreateRecords(sut)
        
        // Then
        assertSnapshot(matching: provider.records, as: .json)
    }
    
    // MARK: Screen tests
    
    func testScreenRemote() {
        // Given
        let (sut, provider) = analyticsServiceAndProviderStub(
            config: .success(.init()),
            session: .success(())
        )
        
        // When
        sut.createRecord(screen: remoteScreen)
        waitCreateRecords(sut)
        
        // Then
        assertSnapshot(matching: provider.records, as: .json)
    }
    
    func testScreenDeclarative() {
        // Given
        let (sut, provider) = analyticsServiceAndProviderStub(
            config: .success(.init()),
            session: .success(())
        )
        
        // When
        sut.createRecord(screen: declarativeScreen)
        waitCreateRecords(sut)
        
        // Then
        assertSnapshot(matching: provider.records, as: .json)
    }
    
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

// MARK: - Helpers

extension AnalyticsServiceTests {
    
    private func waitCreateRecords(_ service: AnalyticsService?) {
        let createRecords = expectation(description: "AnalyticsService create queued records")
        let consumeMainQueue = expectation(description: "return to main queue")
        service?.queue.async {
            createRecords.fulfill()
            DispatchQueue.main.async { consumeMainQueue.fulfill() }
        }
        wait(for: [createRecords, consumeMainQueue], timeout: timeout)
    }
    
    private func analyticsServiceAndProviderStub(
        config: Result<AnalyticsConfig, Error>,
        session: Result<Void, Error>
    ) -> (AnalyticsService, AnalyticsProviderStub) {
        let getConfig = expectation(description: "getConfig")
        let startSession = expectation(description: "startSession")
        let provider = AnalyticsProviderStub()
        provider.getConfig = { completion in
            completion(config)
            getConfig.fulfill()
        }
        provider.startSession = { completion in
            completion(session)
            startSession.fulfill()
        }
        let service = AnalyticsService(provider: provider)
        wait(for: [startSession, getConfig], timeout: timeout)
        return (service, provider)
    }
    
}

// MARK: - AnalyticsProviderStub

private class AnalyticsProviderStub: AnalyticsProvider {
    
    private (set) var records = [AnalyticsRecord]()
    
    var maximumItemsInQueue: Int?
    
    var getConfig: (@escaping (Result<AnalyticsConfig, Error>) -> Void) -> Void = { _ in
        // Intentionally unimplemented...
    }
    
    var startSession: (@escaping (Result<Void, Error>) -> Void) -> Void = { _ in
        // Intentionally unimplemented...
    }
    
    func createRecord(_ record: AnalyticsRecord) {
        records.append(record)
    }
    
}

private struct AnalyticsTestAction: Action {
    var _beagleAction_: String
    var values = [String: String]()
    var analytics: ActionAnalyticsConfig?
    
    func execute(controller: BeagleController, origin: UIView) {
        // Intentionally unimplemented...
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

extension AnalyticsRecord: Encodable {
    
    public func encode(to encoder: Encoder) throws {
        var encodable = values.reduce(into: [:]) { $0[$1.key] = Wrapper($1.value) }
        encodable["type"] = Wrapper(type.rawValue)
        encodable["platform"] = Wrapper(platform)
        try encodable.encode(to: encoder)
    }
    
    private struct Wrapper: Encodable {
        let value: Any
        
        init(_ value: Any) {
            self.value = value
        }
        
        func encode(to encoder: Encoder) throws {
            if let encodable = value as? Encodable {
                try encodable.encode(to: encoder)
                return
            }
            var container = encoder.singleValueContainer()
            switch value {
            case Optional<Any>.none:
                try container.encodeNil()
            case let array as [Any]:
                let encodable = array.map { Wrapper($0) }
                try container.encode(encodable)
            case let dictionary as [String: Any]:
                let encodable = dictionary.reduce(into: [:]) { $0[$1.key] = Wrapper($1.value) }
                try container.encode(encodable)
            default:
                try container.encode(String(reflecting: value))
            }
        }
    }
}
