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
import Beagle
import SnapshotTesting

final class ActionAnalyticsConfigTests: XCTestCase {

    func testEnabled() throws {
        let enabled = try analyticsFromJson("true")

        assert(enabled, equalsTo: "enabled(nil)")
        assert(enabled, transformsToJson: "true")
    }

    func testDisabled() throws {
        let disabled = try analyticsFromJson("false")

        assert(disabled, equalsTo: "disabled")
        assert(disabled, transformsToJson: "false")
    }

    func testFromEmptyObject() throws {
        let emptyObject = try analyticsFromJson("{ }")

        assert(emptyObject, equalsTo: """
        enabled(Optional(Beagle.ActionAnalyticsConfig.Attributes(attributes: nil, additionalEntries: nil)))
        """)

        assert(emptyObject, transformsToJson: """
        {

        }
        """)
    }

    func testWithAttributes() throws {
        let attributes = try analyticsFromJson("""
        {
          "attributes" : [
            "componentId",
            "mode",
            "value[0].text"
          ]
        }
        """)

        assert(attributes, equalsTo: """
            enabled(Optional(Beagle.ActionAnalyticsConfig.Attributes(attributes: Optional(["componentId", "mode", "value[0].text"]), additionalEntries: nil)))
        """)

        assert(attributes, transformsToJson: """
        {
          "attributes" : [
            "componentId",
            "mode",
            "value[0].text"
          ]
        }
        """)
    }

    func testWithAdditionalEntries() throws {
        let entries = try analyticsFromJson("""
        {
            "additionalEntries": {
                "text": "a text",
                "object": {
                    "key": 2
                }
            }
        }
        """)

        XCTAssert(entries == .enabled(.init(additionalEntries: [
            "text": "a text",
            "object": ["key": 2]
        ])))

        assert(entries, transformsToJson: """
        {
          "additionalEntries" : {
            "object" : {
              "key" : 2
            },
            "text" : "a text"
          }
        }
        """)
    }

    // MARK: - Aux

    private let decoder = JSONDecoder()

    private func analyticsFromJson(_ string: String) throws -> ActionAnalyticsConfig {
        let data = try XCTUnwrap(string.data(using: .utf8))
        return try decoder.decode(ActionAnalyticsConfig.self, from: data)
    }

    private func assert(_ analytics: ActionAnalyticsConfig, equalsTo string: String, line: UInt = #line) {
        _assertInlineSnapshot(matching: analytics, as: .description, with: string, line: line)
    }

    private func assert(_ analytics: ActionAnalyticsConfig, transformsToJson string: String, line: UInt = #line) {
        _assertInlineSnapshot(matching: analytics, as: .json, with: string, line: line)
    }
}
