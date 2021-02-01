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

class AnalyticsGeneratorTests: XCTestCase {

    private lazy var sut = AnalyticsService(provider: provider)
    private var provider = AnalyticsProviderStub()

    func testJustUsingGlobalConfig() {
        // Given
        globalConfigWithActionEnabled()
        // And
        actionWithConfig(nil)

        // Then should obey global config
        resultShouldBeEqualTo("""
        {
          "path" : "somePath",
          "platform" : "ios",
          "type" : "action"
        }
        """)
    }

    func testEnabledActionWith1Attribute() {
        // Given
        emptyGlobalConfig()
        // And
        actionWithJust1AttributeEnabled()

        // Then should create a record with just 1 attribute
        resultShouldBeEqualTo("""
        {
          "method" : "DELETE",
          "platform" : "ios",
          "type" : "action"
        }
        """)
    }
    
    func testDisabledActionShouldOverrideGlobalConfig() {
        // Given
        globalConfigWithActionEnabled()
        // And
        actionWithConfig(.disabled)

        // Then should NOT create record
        resultShouldBeEqualTo("""
        null
        """)
    }

    func testWithNoGlobalShouldCreateFullRecord() {
        // Given
        globalConfig(nil)
        // And
        actionWithJust1AttributeEnabled()

        // Then should create a full record
        resultShouldBeEqualTo("""
        {
          "analytics" : null,
          "method" : "DELETE",
          "path" : "PATH",
          "platform" : "ios",
          "type" : "action"
        }
        """)
    }

    func testActionWithEnabledNil() {
        // Given
        emptyGlobalConfig()
        // And
        actionWithConfig(.enabled(nil))

        // Then should have just standard properties
        resultShouldBeEqualTo("""
        {
          "platform" : "ios",
          "type" : "action"
        }
        """)
    }

    func testActionWithEnabledNilAndGlobalConfigEnabled() {
        // Given
        globalConfigWithActionEnabled()
        // And
        actionWithConfig(.enabled(nil))

        // Then should have just standard properties
        resultShouldBeEqualTo("""
        {
          "path" : "somePath",
          "platform" : "ios",
          "type" : "action"
        }
        """)
    }

    // MARK: - Private

    // swiftlint:disable implicitly_unwrapped_optional
    var action: FormRemoteAction!

    private func actionWithConfig(_ config: ActionAnalyticsConfig?) {
        action = FormRemoteAction(
            path: "somePath",
            method: .delete,
            analytics: config
        )
    }

    private func actionWithJust1AttributeEnabled() {
        action = FormRemoteAction(
            path: "PATH",
            method: .delete,
            analytics: .enabled(.init(attributes: ["method"]))
        )
    }

    private func globalConfig(_ config: AnalyticsConfig?) {
        provider.config = config
    }

    private func emptyGlobalConfig() {
        provider.config = .init()
    }

    private func globalConfigWithActionEnabled() {
        globalConfig(.init(actions: [
            "beagle:formremoteaction": ["methods", "path"]
        ]))
    }

    private func resultShouldBeEqualTo(
        _ string: String,
        record: Bool = false,
        testName: String = #function,
        line: UInt = #line
    ) {
        // When
        sut.createRecord(.init(
            action: action,
            event: "event",
            origin: ViewDummy(),
            controller: BeagleScreenViewController(ComponentDummy())
        ))

        let result = resultWithoutDefaultValues()

        // Then
        _assertInlineSnapshot(matching: result, as: .json, with: string, testName: testName, line: line)
    }

    private func resultWithoutDefaultValues() -> AnalyticsRecord? {
        var result = provider.records.first
        let defaultValues = ["beagleAction", "component", "event", "platform", "type"]
        defaultValues.forEach {
            result?.values.removeValue(forKey: $0)
        }
        return result
    }
}
