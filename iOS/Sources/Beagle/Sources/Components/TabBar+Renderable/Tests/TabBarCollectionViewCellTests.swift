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

final class TabBarItemUIComponentTests: XCTestCase {
    
    private lazy var controller = BeagleControllerStub()
    private lazy var renderer = BeagleRenderer(controller: controller)

    func test_setupShouldSetTabItems() {
        // Given // When
        let sut = TabBarItemUIComponent(index: 0, renderer: renderer)
        sut.setupTab(with: TabBarItem(icon: "icon", title: "Tab"))
        
        // Then
        XCTAssert(sut.subviews.count == 2)
        
    }
    
//    func test_setupShouldSetTabItemsWithIconOnly() {
//        // Given
//        let sut = TabBarCollectionViewCell(frame: .zero)
//
//        // When
//        sut.setupTab(with: TabBarItem(icon: "icon"))
//
//        let innerComponentView = Mirror(reflecting: sut).children.first
//        let stackView = innerComponentView?.value as? UIStackView
//
//        // Then
//        XCTAssertNotNil(stackView)
//        XCTAssert(stackView?.subviews[0].isHidden == false)
//
//    }
//
//    func test_setupShouldSetTabItemsWithTitleOnly() {
//        // Given
//        let sut = TabBarCollectionViewCell(frame: .zero)
//
//        // When
//        sut.setupTab(with: TabBarItem(title: "Tab 1"))
//
//        let innerComponentView = Mirror(reflecting: sut).children.first
//        let stackView = innerComponentView?.value as? UIStackView
//
//        // Then
//        XCTAssertNotNil(stackView)
//        XCTAssert(stackView?.subviews[1].isHidden == false)
//    }
//
}
