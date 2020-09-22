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

class TabBarUIComponentTests: XCTestCase {
    
    private lazy var controller = BeagleControllerStub()
    private lazy var renderer = BeagleRenderer(controller: controller)
    
    private lazy var model = TabBarUIComponent.Model(
        tabIndex: 0,
        tabBarItems: [
            TabBarItem(icon: "beagle", title: "Tab 1"),
            TabBarItem(icon: "beagle", title: "Tab 2")
        ],
        renderer: renderer
    )
    
    private lazy var sut = TabBarUIComponent(model: model)
    
    func test_didSelectItemAt_shouldCallOnTabSelectionClosure() {
        // Given
        let collectionView = sut.collectionView
        let index = 1
        let indexPath = IndexPath(item: index, section: 0)
        var didCalledOnTabSelection = false
        var passedIndex: Int?
        
        // When
        sut.onTabSelection = { index in
            didCalledOnTabSelection = true
            passedIndex = index
        }
        
        sut.collectionView(collectionView, didSelectItemAt: indexPath)
        
        // Then
        XCTAssert(didCalledOnTabSelection)
        XCTAssert(passedIndex == index)
    }

}
