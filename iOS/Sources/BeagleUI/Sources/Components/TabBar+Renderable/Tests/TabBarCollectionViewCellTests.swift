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
@testable import BeagleUI
import BeagleSchema

final class TabBarCollectionViewCellTests: XCTestCase {
    
    func test_setupShouldSetTabItems() {
        // Given
        let sut = TabBarCollectionViewCell(frame: .zero)
        
        // When
        sut.setupTab(with: TabItem(icon: "icon", title: "Tab", child:
            Text("Text")
        ))
        
        let innerComponentView = Mirror(reflecting: sut).children.first
        
        // Then
        XCTAssert(innerComponentView?.value is UIStackView)
        XCTAssert(sut.contentView.subviews.count == 1)
    }
    
    func test_setupShouldSetTabItemsWithIconOnly() {
        // Given
        let sut = TabBarCollectionViewCell(frame: .zero)
        
        // When
        sut.setupTab(with: TabItem(icon: "icon", child:
            Text("Text")
        ))
        
        let innerComponentView = Mirror(reflecting: sut).children.first
        let stackView = innerComponentView?.value as? UIStackView
        
        // Then
        XCTAssertNotNil(stackView)
        XCTAssert(stackView?.subviews[0].isHidden == false)
        
    }
    
    func test_setupShouldSetTabItemsWithTitleOnly() {
        // Given
        let sut = TabBarCollectionViewCell(frame: .zero)
        
        // When
        sut.setupTab(with: TabItem(title: "Tab 1", child:
            Text("Text")
        ))
        
        let innerComponentView = Mirror(reflecting: sut).children.first
        let stackView = innerComponentView?.value as? UIStackView
        
        // Then
        XCTAssertNotNil(stackView)
        XCTAssert(stackView?.subviews[1].isHidden == false)
    }
    
}
