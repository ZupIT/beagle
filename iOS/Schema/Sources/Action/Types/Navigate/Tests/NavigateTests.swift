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

    func test_decoding_openExternalUrl() throws {
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
    
    func test_decoding_openNativeRoute() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "opennativeroute")
        _assertInlineSnapshot(matching: action, as: .dump, with: """
        ▿ Navigate
          ▿ openNativeRoute: (3 elements)
            - .0: "deeplink"
            ▿ data: Optional<Dictionary<String, String>>
              ▿ some: 1 key/value pair
                ▿ (2 elements)
                  - key: "a"
                  - value: "value a"
            - shouldResetApplication: true
        """)
    }
    
    func test_decoding_resetApplication() throws {
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
          ▿ resetApplication: Route
            ▿ remote: (3 elements)
              - .0: "schema://path"
              - shouldPrefetch: false
              - fallback: Optional<Screen>.none
        """)
    }
    
    func test_decoding_resetStack() throws {
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
            ▿ remote: (3 elements)
              - .0: "schema://path"
              - shouldPrefetch: false
              - fallback: Optional<Screen>.none
        """)
    }
    
    func test_decoding_pushStack() throws {
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
    
    func test_decoding_popStack() throws {
        let action: Navigate = try actionFromString("""
        {
            "_beagleAction_": "beagle:popstack"
        }
        """)

        _assertInlineSnapshot(matching: action, as: .dump, with: """
        - Navigate.popStack
        """)
    }
    
    func test_decoding_pushView() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "pushview")
        assertSnapshot(matching: action, as: .dump)
    }
    
    func test_decoding_popView() throws {
        let action: Navigate = try actionFromString("""
        {
            "_beagleAction_": "beagle:popView"
        }
        """)

        _assertInlineSnapshot(matching: action, as: .dump, with: """
        - Navigate.popView
        """)
    }
    
    func test_decoding_popToView() throws {
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
    
    func testNullNewPathInNavigation() {
        //given
        let arrayWithNullNewPaths: [Navigate] = [
            .openExternalURL(""),
            .openNativeRoute("", data: nil, shouldResetApplication: false),
            .popStack,
            .popView,
            .popToView("")
        ]
        
        // then
        XCTAssert(arrayWithNullNewPaths.filter { $0.newPath == nil }.count == arrayWithNullNewPaths.count)
    }
    
    func testNotNullNewPathsInNavigation() {
        // given
        let routeMockRemote: Route = .remote("", shouldPrefetch: false, fallback: Screen(child: DumbComponent()))
        let routeMockDeclarative: Route = .declarative(Screen(child: DumbComponent()))
        let array: [Navigate] = [
            .resetApplication(routeMockRemote),
            .resetStack(routeMockRemote),
            .pushStack(routeMockRemote),
            .pushView(routeMockRemote),
            .resetStack(routeMockDeclarative)
        ]
        
        // then
        XCTAssert(array.filter { $0.newPath != nil }.count == array.count - 1)
    }
    
}

private struct DumbComponent: RawComponent { }
