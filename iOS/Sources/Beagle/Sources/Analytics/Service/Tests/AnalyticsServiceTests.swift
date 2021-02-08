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

    private lazy var sut = AnalyticsService(provider: provider)
    
    // MARK: Configuration tests

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
        XCTAssertEqual(receivedRecords().count, maxItems)
    }

    func testChangingConfig() {
        // Given is working normally
        testNormalOperation()

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
        let items = 30 + maxItems - 1 // -1 due to losing item to full queue
        XCTAssertEqual(receivedRecords().count, items)
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
        let action = FormRemoteAction(
            path: "PATH",
            method: .delete
        )

        provider.config = nil

        sut.createRecord(action: .init(action: action, event: nil, origin: ViewDummy(), controller: BeagleScreenViewController(ComponentDummy())) )

        provider.config = .init(actions: ["beagle:formremoteaction": ["path"]])

        triggerNewRecord()

        _assertInlineSnapshot(matching: recordedAttributes(), as: .json, with: """
        {
          "attributes" : {
            "path" : "PATH"
          }
        }
        """)
    }
    
    // MARK: Screen tests

    func testScreenRecord() {
        testScreenRecordWithConfig(enabled: false)
        testScreenRecordWithConfig(enabled: true)
    }

    private func testScreenRecordWithConfig(enabled: Bool) {
        // Given
        provider.config = .init(enableScreenAnalytics: enabled)

        // When
        triggerNewRecord()

        // Then
        XCTAssertEqual(receivedRecords().count, enabled ? 1 : 0)
    }
    
    func testScreenRemote() {
        // When
        triggerNewRecord()
        
        // Then
        assertSnapshot(matching: provider.records, as: .json)
    }
    
    func testScreenDeclarative() {
        // Given
        let declarative = ScreenType.declarative(Screen(
            identifier: "DECLARATIVE",
            child: ComponentDummy()
        ))

        // When
        sut.createRecord(screen: declarative)
        
        // Then
        assertSnapshot(matching: provider.records, as: .json)
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
        sut.serialDispatch.async {
            expec.fulfill()
        }
        wait(for: [expec], timeout: 1)

        let records = provider.records
        provider.records = []
        return records
    }

    private func triggerNewRecord(manyTimes: Int = 1) {
        for _ in 1...manyTimes {
            sut.createRecord(screen: remoteScreen)
        }
    }

    private func sendNewRecordsUntilQueueIsFull() {
        triggerNewRecord(manyTimes: maxItems)
    }

    private var remoteScreen: ScreenType { .remote(.init(url: "REMOTE")) }

    private func recordedAttributes() -> [String: DynamicObject]? {
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
