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
import BeagleSchema
@testable import Beagle

final class AddChildrenTests: XCTestCase {

    func testAddChildrenDefault() {
        // Given
        let addChildren = AddChildren(componentId: "", value: [Text("text 1"), Text("text 2")])
        let view = UIView()
        let controller = BeagleControllerNavigationSpy()

        // When
        addChildren.execute(controller: controller, origin: view)

        // Then
//        TODO: take snapshot
    }
    
    func testAddChildrenPrepend() {
        // Given
        let addChildren = AddChildren(componentId: "", value: [Text("text")], mode: .prepend)
        let view = UIView()
        let controller = BeagleControllerNavigationSpy()

        // When
        addChildren.execute(controller: controller, origin: view)

        // Then
//        TODO: take snapshot
    }
    
    func testAddChildrenReplace() {
        // Given
        let addChildren = AddChildren(componentId: "", value: [Text("text")], mode: .replace)
        let view = UIView()
        let controller = BeagleControllerNavigationSpy()

        // When
        addChildren.execute(controller: controller, origin: view)

        // Then
//        TODO: take snapshot
    }
    
}
