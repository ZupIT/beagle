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
        let items = 30

        // When
        triggerNewRecord(manyTimes: items)

        // Then
        XCTAssertEqual(receivedRecords().count, items)
    }

    func testChangingConfig() {
        // Given is working normally
        testNormalOperation()
        provider.records = []

        // When change config to
        disabledGlobalConfig()
        // And
        triggerNewRecord(manyTimes: 5)
        triggerAction()

        // Then should not receive more records
        XCTAssertEqual(receivedRecords().count, 0)

        // When change config again to
        enabledGlobalConfig()
        // And
        triggerNewRecord(manyTimes: 30)
        triggerAction()

        // Then
        XCTAssertEqual(receivedRecords().count, 30 + 1)
        assertActionAttributes(equalTo: """
        {
          "attributes" : {
            "path" : "PATH"
          }
        }
        """)
    }

    func testConfigWithDifferentActions() {
        // Given
        let caseSensitive = "beagle:FoRmReMoTeAcTion"
        // And
        globalConfig(.init(actions: [
            caseSensitive: [],
            "beagle:SENDREQUEST": [],
            .beagleActionName(SetContext.self): []
        ]))

        // When
        triggerAction(FormRemoteAction(path: "path", method: .get))
        triggerAction(SendRequest(url: .value("url"), method: .value(.delete)))
        triggerAction(SetContext(contextId: "context", value: true))

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

    private lazy var provider = AnalyticsProviderStub()

    private func receivedRecords() -> [AnalyticsRecord] {
        return provider.records
    }

    private func triggerNewRecord(manyTimes: Int = 1) {
        for _ in 1...manyTimes {
            sut.createRecord(screen: .remote(.init(url: "REMOTE")))
        }
    }

    private func triggerAction(_ action: Action? = nil) {
        sut.createRecord(action: .init(
            action: action ?? FormRemoteAction(path: "PATH", method: .delete),
            event: nil,
            origin: ViewDummy(),
            controller: BeagleScreenViewController(ComponentDummy())
        ))
    }

    func assertActionAttributes(
        equalTo string: String,
        record: Bool = false,
        line: UInt = #line
    ) {
        let action = receivedRecords().last?.onlyAttributesAndAdditional()
        _assertInlineSnapshot(matching: action, as: .json, record: record, with: string, line: line)
    }

    private func enabledGlobalConfig() {
        provider.config = .init(actions: [
            "beagle:formremoteaction": ["path"]
        ])
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

    var config = AnalyticsConfig()
    
    func createRecord(_ record: AnalyticsRecord) {
        records.append(record)
    }

    func getConfig() -> AnalyticsConfig {
        return config
    }
}
