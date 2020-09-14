//
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
@testable import Beagle
import BeagleSchema

class NavigateTests: XCTestCase {

    func testNullNewPathInNavigation() {
        //given
        let arrayWithNullNewPaths: [Navigate] = [
            .openExternalURL(""),
            .openNativeRoute(.init(route: "")),
            .popStack,
            .popView,
            .popToView("")
        ]
        
        // then
        XCTAssertEqual(arrayWithNullNewPaths.filter { $0.newPath == nil }.count, arrayWithNullNewPaths.count)
    }
    
    func testNotNullNewPathsInNavigation() {
        // given
        let routeMockRemote: Route = .remote(.init(url: "", shouldPrefetch: false, fallback: Screen(child: DumbComponent())))
        let routeMockDeclarative: Route = .declarative(Screen(child: DumbComponent()))
        let array: [Navigate] = [
            .resetApplication(routeMockRemote),
            .resetStack(routeMockRemote),
            .pushStack(routeMockRemote),
            .pushStack(routeMockRemote, controllerId: "customid"),
            .pushView(routeMockRemote),
            .resetStack(routeMockDeclarative)
        ]
        
        // then
        XCTAssertEqual(array.filter { $0.newPath != nil }.count, array.count - 1)
    }

}

private struct DumbComponent: RawComponent { }
