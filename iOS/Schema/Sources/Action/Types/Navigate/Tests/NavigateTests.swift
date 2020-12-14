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
@testable import BeagleSchema

final class NavigateTests: XCTestCase {

    func testDecodingOpenExternalUrl() throws {
        let action: Navigate = try actionFromString("""
        {
            "_beagleAction_": "beagle:openexternalurl",
            "url": "schema://domain/path"
        }
        """)
        
        _assertInlineSnapshot(matching: action, as: .dump, with: """
        ▿ Navigate
          - openExternalURL: "schema://domain/path"
        """)
    }
    
    func testDecodingOpenNativeRoute() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "opennativeroute")
        _assertInlineSnapshot(matching: action, as: .dump, with: """
        ▿ Navigate
          ▿ openNativeRoute: OpenNativeRoute
            ▿ data: Optional<Dictionary<String, String>>
              ▿ some: 1 key/value pair
                ▿ (2 elements)
                  - key: "a"
                  - value: "value a"
            - route: "deeplink"
            - shouldResetApplication: true
        """)
    }
    
    func testDecodingResetApplication() throws {
        let action: Navigate = try actionFromString("""
        {
            "_beagleAction_": "beagle:resetapplication",
            "route": {
                "url": "schema://path"
            }
        }
        """)

        _assertInlineSnapshot(matching: action, as: .dump, with: """
        ▿ Navigate
          ▿ resetApplication: (2 elements)
            ▿ .0: Route
              ▿ remote: NewPath
                - fallback: Optional<Screen>.none
                - shouldPrefetch: false
                ▿ url: Expression<String>
                  - value: "schema://path"
            - controllerId: Optional<String>.none
        """)
    }
    
    func testDecodingResetStack() throws {
        let action: Navigate = try actionFromString("""
        {
            "_beagleAction_": "beagle:resetstack",
            "route": {
                "url": "schema://path"
            }
        }
        """)
        
        _assertInlineSnapshot(matching: action, as: .dump, with: """
        ▿ Navigate
          ▿ resetStack: Route
            ▿ remote: NewPath
              - fallback: Optional<Screen>.none
              - shouldPrefetch: false
              ▿ url: Expression<String>
                - value: "schema://path"
        """)
    }
    
    func testDecodingPushStack() throws {
        let action: Navigate = try actionFromString("""
        {
            "_beagleAction_": "beagle:pushStack",
            "route": {
                "screen": {
                    "child" : {
                      "_beagleComponent_" : "custom:beagleschematestscomponent"
                    }
                }
            }
        }
        """)

        assertSnapshot(matching: action, as: .dump)
    }
    
    func testDecodingPushStackWithControllerId() throws {
        let action: Navigate = try actionFromString("""
        {
            "_beagleAction_": "beagle:pushStack",
            "route": {
                "url": "schema://path"
            },
            "controllerId": "customid"
        }
        """)

        _assertInlineSnapshot(matching: action, as: .dump, with: """
        ▿ Navigate
          ▿ pushStack: (2 elements)
            ▿ .0: Route
              ▿ remote: NewPath
                - fallback: Optional<Screen>.none
                - shouldPrefetch: false
                ▿ url: Expression<String>
                  - value: "schema://path"
            ▿ controllerId: Optional<String>
              - some: "customid"
        """)
    }
    
    func testDecodingPopStack() throws {
        let action: Navigate = try actionFromString("""
        {
            "_beagleAction_": "beagle:popstack"
        }
        """)

        _assertInlineSnapshot(matching: action, as: .dump, with: """
        - Navigate.popStack
        """)
    }
    
    func testDecodingPushView() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "pushview")
        assertSnapshot(matching: action, as: .dump)
    }
    
    func testDecodingPushViewWithContext() throws {
        let action: Navigate = try actionFromString("""
        {
          "_beagleAction_": "beagle:pushView",
          "route": {
            "url": "@{test}",
            "shouldPrefetch": false
          }
        }
        """)
        assertSnapshot(matching: action, as: .dump)
    }
    
    func testDecodingPopView() throws {
        let action: Navigate = try actionFromString("""
        {
            "_beagleAction_": "beagle:popView"
        }
        """)

        _assertInlineSnapshot(matching: action, as: .dump, with: """
        - Navigate.popView
        """)
    }
    
    func testDecodingPopToView() throws {
        let action: Navigate = try actionFromString("""
        {
            "_beagleAction_": "beagle:popToView",
            "route": "viewId"
        }
        """)

        _assertInlineSnapshot(matching: action, as: .dump, with: """
        ▿ Navigate
          - popToView: "viewId"
        """)
    }
    
}
