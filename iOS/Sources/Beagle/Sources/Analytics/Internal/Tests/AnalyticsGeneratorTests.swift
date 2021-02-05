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

    private lazy var sut = AnalyticsGenerator(info: info, globalConfig: _globalConfig)

    func testJustUsingGlobalConfig() {
        // Given
        globalConfigWithActionEnabled()
        // And
        actionWithConfig(nil)

        // Then should obey global config
        recordShouldBeEqualTo("""
        {
          "path" : "PATH"
        }
        """)
    }

    func testEnabledActionWith1Attribute() {
        // Given
        emptyGlobalConfig()
        // And
        actionWithJust1AttributeEnabled()

        // Then should create a record with just 1 attribute
        recordShouldBeEqualTo("""
        {
          "method" : "DELETE"
        }
        """)
    }
    
    func testDisabledActionShouldOverrideGlobalConfig() {
        // Given
        globalConfigWithActionEnabled()
        // And
        actionWithConfig(.disabled)

        // Then should NOT create record
        recordShouldBeEqualTo("""
        null
        """)
    }

    func testWithNoGlobalShouldRecordAllAttributes() {
        // Given
        nilGlobalConfig()
        // And
        actionWithJust1AttributeEnabled()

        // Then should create a record with all attributes
        recordShouldBeEqualTo("""
        {
          "method" : "DELETE"
        }
        """)
    }

    func testShouldDependOnFutureGlobalConfig() {
        // Given
        nilGlobalConfig()
        // And
        actionWithConfig(nil)

        // Then should depend on future global config
        recordShouldBeEqualTo("""
        {
          "analytics" : null,
          "dependsOnFutureGlobalConfig" : true,
          "method" : "DELETE",
          "path" : "PATH"
        }
        """)
    }

    func testActionWithEnabledNil() {
        // Given
        emptyGlobalConfig()
        // And
        actionWithConfig(.enabled(nil))

        // Then should have just standard properties
        recordShouldBeEqualTo("""
        {
        
        }
        """)
    }

    func testActionWithEnabledNilAndGlobalConfigEnabled() {
        // Given
        globalConfigWithActionEnabled()
        // And
        actionWithConfig(.enabled(nil))

        // Then should use global config
        recordShouldBeEqualTo("""
        {
        
        }
        """)
    }

    func testAdditionalEntriesShouldTakePrecedence() {
        // Given
        emptyGlobalConfig()
        // And
        actionWithConfig(.enabled(.init(
            attributes: ["path"],
            additionalEntries: ["path": "NEW PATH"]
        )))

        // Then should use NEW PATH
        recordShouldBeEqualTo("""
        {
          "path" : "NEW PATH"
        }
        """)
    }

    func testRecordWithAllDefaultProperties() throws {
        // Given
        emptyGlobalConfig()
        // And
        actionWithConfig(.enabled(nil))
        // And
        try prepareComponentHierarchy()

        // When
        let record = sut.makeRecord()

        // Then should have all default properties
        _assertInlineSnapshot(matching: record, as: .json, with: """
        {
          "dependsOnFutureGlobalConfig" : false,
          "record" : {
            "beagleAction" : "beagle:formremoteaction",
            "component" : {
              "id" : "test-component-id",
              "position" : {
                "x" : 0,
                "y" : 0
              },
              "type" : "custom:analyticstestcomponent"
            },
            "event" : "event",
            "platform" : "ios",
            "screen" : "analytics-actions",
            "type" : "action"
          }
        }
        """)
    }

    // MARK: - Private

    // swiftlint:disable implicitly_unwrapped_optional
    var action: FormRemoteAction!

    private lazy var info = AnalyticsService.ActionInfo(
        action: action,
        event: "event",
        origin: ViewDummy(),
        controller: BeagleScreenViewController(ComponentDummy())
    )

    private lazy var _globalConfig: AnalyticsConfig.AttributesByActionName? = nil

    private func actionWithConfig(_ config: ActionAnalyticsConfig?) {
        action = FormRemoteAction(
            path: "PATH",
            method: .delete,
            analytics: config
        )
    }

    private func actionWithJust1AttributeEnabled() {
        actionWithConfig(.enabled(.init(attributes: ["method"])))
    }

    private func nilGlobalConfig() {
        _globalConfig = nil
    }

    private func emptyGlobalConfig() {
        _globalConfig = [:]
    }

    private func globalConfigWithActionEnabled() {
        _globalConfig = [
            "beagle:formremoteaction": ["methods", "path"]
        ]
    }

    private func recordShouldBeEqualTo(
        _ string: String,
        record: Bool = false,
        testName: String = #function,
        line: UInt = #line
    ) {
        // When
        let result = sut.makeRecord()
            .flatMap(resultWithoutDefaultValues(_:))

        // Then
        _assertInlineSnapshot(matching: result, as: .json, record: record, with: string, testName: testName, line: line)
    }

    private func resultWithoutDefaultValues(_ result: AnalyticsService.Cache) -> [String: DynamicObject]? {
        var dict = result.record.values

        if result.dependsOnFutureGlobalConfig {
            dict["dependsOnFutureGlobalConfig"] = .bool(true)
        }

        return dict
    }

    private func prepareComponentHierarchy() throws {
        let (view, controller) = try analyticsViewHierarchyWith(context: nil)
        info = .init(action: action, event: "event", origin: view, controller: controller)
    }
}
