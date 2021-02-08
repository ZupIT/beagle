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
        // When
        let record = makeScreenRecord(screen: remoteScreen, globalConfigIsEnabled: true)

        // Then
        _assertInlineSnapshot(matching: record, as: .json, with: """
        {
          "dependsOnFutureGlobalConfig" : false,
          "record" : {
            "platform" : "ios",
            "screen" : "REMOTE",
            "type" : "screen"
          }
        }
        """)
    }

    func testScreenDeclarative() {
        // Given
        let declarative = ScreenType.declarative(Screen(
            identifier: "DECLARATIVE",
            child: ComponentDummy()
        ))

        // When
        let record = makeScreenRecord(screen: declarative, globalConfigIsEnabled: true)

        // Then
        _assertInlineSnapshot(matching: record, as: .json, with: """
        {
          "dependsOnFutureGlobalConfig" : false,
          "record" : {
            "platform" : "ios",
            "screen" : "DECLARATIVE",
            "type" : "screen"
          }
        }
        """)
    }

    func testConfigDisabled() {
        // When
        let record = makeScreenRecord(screen: remoteScreen, globalConfigIsEnabled: false)

        // Then
        _assertInlineSnapshot(matching: record, as: .json, with: """
        null
        """)
    }

    func testConfigNil() {
        // When
        let record = makeScreenRecord(screen: remoteScreen, globalConfigIsEnabled: nil)

        // Then
        _assertInlineSnapshot(matching: record, as: .json, with: """
        {
          "dependsOnFutureGlobalConfig" : true,
          "record" : {
            "platform" : "ios",
            "screen" : "REMOTE",
            "type" : "screen"
          }
        }
        """)
    }

    private var remoteScreen: ScreenType { .remote(.init(url: "REMOTE")) }
}
