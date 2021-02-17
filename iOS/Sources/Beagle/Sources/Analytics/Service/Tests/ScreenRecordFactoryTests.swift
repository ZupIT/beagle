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

class ScreenRecordFactoryTests: XCTestCase {

    func testScreenRemote() {
        // Given
        screen = remoteScreen

        // Then
        recordShouldBeEqualTo("""
        {
          "platform" : "ios",
          "screen" : "REMOTE",
          "type" : "screen"
        }
        """)
    }

    func testScreenDeclarative() {
        // Given
        screen = .declarative(Screen(
            identifier: "DECLARATIVE",
            child: ComponentDummy()
        ))

        // Then
        recordShouldBeEqualTo("""
        {
          "platform" : "ios",
          "screen" : "DECLARATIVE",
          "type" : "screen"
        }
        """)
    }

    func testConfigDisabled() {
        // Given
        disabledGlobalConfig()

        // Then
        recordShouldBeEqualTo("""
        null
        """)
    }

    // MARK: - Aux

    lazy var screen = remoteScreen

    var remoteScreen: ScreenType { .remote(.init(url: "REMOTE")) }

    var _globalConfig: Bool = true

    func disabledGlobalConfig() {
        _globalConfig = false
    }

    func recordShouldBeEqualTo(
        _ string: String,
        override: Bool = false,
        testName: String = #function,
        line: UInt = #line
    ) {
        // When
        let result = makeScreenRecord(screen: screen, isScreenEnabled: _globalConfig)
            .ifSome(removeTimestamp)

        // Then
        _assertInlineSnapshot(matching: result, as: .json, record: override, with: string, testName: testName, line: line)
    }

    func removeTimestamp(_ record: AnalyticsRecord) -> DynamicDictionary? {
        var dict = record.toDictionary()
        dict.removeValue(forKey: "timestamp")
        return dict
    }
}
