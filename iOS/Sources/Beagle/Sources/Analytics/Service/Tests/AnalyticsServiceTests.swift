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

    private var remoteScreen: ScreenType { .remote(.init(url: "REMOTE")) }
    private var declarativeScreen: ScreenType { .declarative(Screen(identifier: "DECLARATIVE", child: ComponentDummy())) }
    
    // MARK: Configuration tests

    func testMaximumItemsInQueue() {
        provider.config = nil

        for _ in 1...maxItems {
            sut.createRecord(screen: remoteScreen)
        }
        waitRecords()
        XCTAssertEqual(provider.records, [])

        provider.config = .init()

        sut.createRecord(screen: remoteScreen)

        waitRecords()
        XCTAssertEqual(provider.records.count, maxItems)
    }

    func testWithConfig() {
        provider.config = .init()
        let overMax = 4 * maxItems

        for _ in 1...overMax {
            sut.createRecord(screen: remoteScreen)
        }

        waitRecords()
        XCTAssertEqual(provider.records.count, overMax)
    }

    func testChangingConfig() {
        provider.config = .init()

        for _ in 1...3 {
            sut.createRecord(screen: remoteScreen)
        }

        waitRecords()
        XCTAssertEqual(provider.records.count, 3)

        provider.config = nil

        for _ in 1...5 {
            sut.createRecord(screen: remoteScreen)
        }

        waitRecords()
        XCTAssertEqual(provider.records.count, 3)

        provider.config = .init()

        for _ in 1...5 {
            sut.createRecord(screen: remoteScreen)
        }

        waitRecords()
        XCTAssertEqual(provider.records.count, 12)
    }
    
    func testScreenRecord() {
        testScreenRecordWithConfig(enabled: false)
        testScreenRecordWithConfig(enabled: true)
    }

    func testFirstWithoutConfigAndThenWithDisabledConfig() {
        provider.config = nil

        sut.createRecord(screen: remoteScreen)

        provider.config = .init(enableScreenAnalytics: false)

        sut.createRecord(screen: remoteScreen)

        waitRecords()
        XCTAssertEqual(provider.records.count, 0)
    }

    func testFirstWithoutConfigAndThenWithEnabledConfig() {
        provider.config = nil

        sut.createRecord(screen: remoteScreen)

        provider.config = .init(enableScreenAnalytics: true)

        sut.createRecord(screen: remoteScreen)

        waitRecords()
        XCTAssertEqual(provider.records.count, 2)
    }

    func testFirstWithoutConfigAndThenWithAttributes() {
        let action = FormRemoteAction(
            path: "PATH",
            method: .delete
        )

        provider.config = nil

        sut.createRecord(action: .init(action: action, event: nil, origin: ViewDummy(), controller: BeagleScreenViewController(ComponentDummy())) )

        provider.config = .init(actions: ["beagle:formremoteaction": ["path"]])

        sut.createRecord(screen: remoteScreen)

        waitRecords()
        _assertInlineSnapshot(matching: resultWithAttributes(), as: .json, with: """
        {
          "attributes" : {
            "path" : "PATH"
          }
        }
        """)
    }

    private func resultWithAttributes() -> [String: DynamicObject]? {
        provider.records.first?.onlyAttributesAndAdditional()
    }
    
    private func testScreenRecordWithConfig(enabled: Bool) {
        // Given
        provider.config = .init(enableScreenAnalytics: enabled)

        // When
        sut.createRecord(screen: remoteScreen)
        waitRecords()

        // Then
        XCTAssertEqual(provider.records.count, enabled ? 1 : 0)
    }
    
    // MARK: Screen tests
    
    func testScreenRemote() {
        // When
        sut.createRecord(screen: remoteScreen)
        
        // Then
        assertSnapshot(matching: provider.records, as: .json)
    }
    
    func testScreenDeclarative() {
        // When
        sut.createRecord(screen: declarativeScreen)
        
        // Then
        assertSnapshot(matching: provider.records, as: .json)
    }

    private let maxItems = 5

    private lazy var provider: AnalyticsProviderStub = {
        let it = AnalyticsProviderStub()
        it.maximumItemsInQueue = maxItems
        return it
    }()

    private func waitRecords() {
        let expec = expectation(description: "wait records")
        sut.serialDispatch.async {
            expec.fulfill()
        }
        wait(for: [expec], timeout: 1)
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
