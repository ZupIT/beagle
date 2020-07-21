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
    
    private lazy var model = TabBarUIComponent.Model(
        tabIndex: 0,
        tabViewItems: [
            TabBarItem(icon: "beagle", title: "Tab 1"),
            TabBarItem(icon: "beagle", title: "Tab 2")
        ]
    )
    
    private lazy var sut = TabBarUIComponent(model: model)
    
    func test_initShouldSetupCollectionView() {
        // Given / When
        let collectionView = sut.collectionView
        XCTAssertNotNil(collectionView.collectionViewLayout)
    }
    
    func test_numberOfItemsInSection_shouldReturnModelItemsCount() {
        // Given / When
        let mockedCollectionView = UICollectionView(frame: .zero, collectionViewLayout: UICollectionViewFlowLayout())
        
        let count = sut.collectionView(mockedCollectionView, numberOfItemsInSection: 1)
        
        guard (sut.subviews.first(where: { $0 is UICollectionView }) as? UICollectionView) != nil else {
            XCTFail("Could not find `collectionView`.")
            return
        }
        
        // Then
        XCTAssert(model.tabViewItems.count == count)
    }
    
    func test_cellForItemAt_shouldReturnCorrectCell() {
        // Given
        let collectionView = sut.collectionView
        let indexPath = IndexPath(item: 0, section: 0)
        
        // When
        let cell = sut.collectionView(collectionView, cellForItemAt: indexPath)
        
        // Then
        XCTAssert(cell is TabBarCollectionViewCell)
        XCTAssert(cell.contentView.subviews.count == 1)
    }
    
    func test_didSelectItemAt_shouldCallOnTabSelectionClosure() {
        // Given
        let collectionView = sut.collectionView
        let index = 2
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
