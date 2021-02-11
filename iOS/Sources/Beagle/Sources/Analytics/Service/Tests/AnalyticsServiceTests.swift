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

// swiftlint:disable force_unwrapping

class AnalyticsServiceTests: XCTestCase {

    lazy var sut = AnalyticsService(provider: provider, logger: LoggerMocked())

    func testNormalOperation() {
        // Given
        enabledGlobalConfig()
        let items = 4 * maxItems

        // When
        triggerNewRecord(manyTimes: items)

        // Then
        XCTAssertEqual(receivedRecords().count, items)
    }

    func testDefaultValueOfMaxItems() {
        // Given
        provider.maximumItemsInQueue = nil

        // Then
        XCTAssertEqual(sut.maxItemsInQueue(), 100)
    }

    func testMaximumItemsInQueue() {
        // Given
        noGlobalConfig()
        // When
        sendNewRecordsUntilQueueIsFull()
        // Then
        XCTAssertEqual(receivedRecords(), [])

        // When changing to
        enabledGlobalConfig()
        // And
        triggerNewRecord()

        // Then should receive all items that were queued
        XCTAssertEqual(receivedRecords().count, maxItems + 1)
    }

    func testChangingConfig() {
        // Given is working normally
        testNormalOperation()
        provider.records = []

        // When change config to
        noGlobalConfig()
        // And
        sendNewRecordsUntilQueueIsFull()
        // Then should not receive more records
        XCTAssertEqual(receivedRecords().count, 0)

        // When change config again to
        enabledGlobalConfig()
        // And
        triggerNewRecord(manyTimes: 30)

        // Then
        let totalItems = 30 + maxItems
        XCTAssertEqual(receivedRecords().count, totalItems)
    }

    func testDisabledConfigShouldDisableItemsInQueue() {
        // Given
        noGlobalConfig()
        // And
        sendNewRecordsUntilQueueIsFull()

        // When change to
        disabledGlobalConfig()
        // And
        triggerNewRecord()

        // Then
        XCTAssertEqual(receivedRecords().count, 0)
    }

    func testFirstWithoutConfigAndThenWithAttributes() {
        // Given
        let action = FormRemoteAction(path: "PATH", method: .delete)
        // And
        noGlobalConfig()

        // When
        triggerActionRecord(action)
        // *
        globalConfig(.init(
            enableScreenAnalytics: false,
            actions: ["beagle:formremoteaction": ["path"]]
        ))
        // *
        triggerNewRecord()

        // Then
        // should only have Action record
        XCTAssertEqual(receivedRecords().count, 1)
        // with  "path" attribute
        _assertInlineSnapshot(matching: recordedAttributes(), as: .json, with: """
        {
          "attributes" : {
            "path" : "PATH"
          }
        }
        """)
    }

    func testGlobalConfigWithDifferentActions() {
        // Given
        let caseSensitive = "beagle:FoRmReMoTeAcTion"
        // And
        globalConfig(.init(actions: [
            caseSensitive: [],
            "beagle:SENDREQUEST": [],
            .beagleActionName(SetContext.self): []
        ]))

        // When
        sut.createRecord(action: actionInfo(FormRemoteAction(path: "path", method: .get)))
        sut.createRecord(action: actionInfo(SendRequest(url: .value("url"), method: .value(.delete))))
        sut.createRecord(action: actionInfo(SetContext(contextId: "context", value: true)))

        // Then
        XCTAssertEqual(receivedRecords().count, 3)
    }

    func testGlobalConfigJson() throws {
        // Given
        let json = """
        {
          "actions": {
            "key1": ["attribute"],
            "KEY2": ["attribute"],
            "kEy3": ["attribute"]
          }
        }
        """.data(using: .utf8)!

        // When
        let config = try JSONDecoder().decode(AnalyticsConfig.self, from: json)

        // Then
        _assertInlineSnapshot(matching: config, as: .json, with: """
        {
          "actions" : {
            "key1" : [
              "attribute"
            ],
            "key2" : [
              "attribute"
            ],
            "key3" : [
              "attribute"
            ]
          },
          "enableScreenAnalytics" : true
        }
        """)
    }

    // MARK: - Aux

    private let maxItems = 5

    private lazy var provider: AnalyticsProviderStub = {
        let it = AnalyticsProviderStub()
        it.maximumItemsInQueue = maxItems
        return it
    }()

    private func receivedRecords() -> [AnalyticsRecord] {
        let expec = expectation(description: "wait records")
        sut.serialThread.async {
            expec.fulfill()
        }
        wait(for: [expec], timeout: 1)

        let records = provider.records
        return records
    }

    private func triggerNewRecord(manyTimes: Int = 1) {
        for _ in 1...manyTimes {
            sut.createRecord(screen: .remote(.init(url: "REMOTE")))
        }
    }

    private func triggerActionRecord(_ action: Action) {
        sut.createRecord(action: actionInfo(action))
    }

    func actionInfo(_ action: Action) -> AnalyticsService.ActionInfo {
        AnalyticsService.ActionInfo(
            action: action,
            event: nil,
            origin: ViewDummy(),
            controller: BeagleScreenViewController(ComponentDummy())
        )
    }

    private func sendNewRecordsUntilQueueIsFull() {
        let extra = [0, 1, 2].randomElement() ?? 0
        triggerNewRecord(manyTimes: maxItems + extra)
    }

    private func recordedAttributes() -> DynamicDictionary? {
        receivedRecords().first?.onlyAttributesAndAdditional()
    }

    private func noGlobalConfig() {
        provider.config = nil
    }

    private func enabledGlobalConfig() {
        provider.config = .init()
    }

    private func disabledGlobalConfig() {
        provider.config = .init(enableScreenAnalytics: false)
    }

    private func globalConfig(_ config: AnalyticsConfig) {
        provider.config = config
    }
}

// MARK: - AnalyticsProviderStub

class AnalyticsProviderStub: AnalyticsProvider {
    
    var records = [AnalyticsRecord]()
    
    var maximumItemsInQueue: Int?

    var config: AnalyticsConfig? = .init()
    
    func createRecord(_ record: AnalyticsRecord) {
        records.append(record)
    }

    func getConfig() -> AnalyticsConfig? {
        return config
    }
}
