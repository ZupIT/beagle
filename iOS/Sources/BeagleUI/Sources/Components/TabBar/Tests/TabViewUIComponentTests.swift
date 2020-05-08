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

final class TabViewUIComponentTests: XCTestCase {
    
    // MARK: - Variables
    private lazy var component = TabView(children: [
        TabItem(icon: "beagle", title: "Tab 1", child:
            Container(children: [
                Text("Blaaslkdjfaskldjfalskdjfasldjfasldfj"),
                Text("Blaaslkdjfaskldjfalskdjfasldjfasldfj")
            ])
            .applyFlex(Flex(alignContent: .center))
        ),
        TabItem(icon: "beagle", title: "Tab 2", child:
            Container(children: [
                Text("Text1 Tab 2"),
                Text("Text2 Tab 2")
            ])
            .applyFlex(Flex(justifyContent: .flexEnd))
        )
    ])
    
    private lazy var model = TabViewUIComponent.Model(tabIndex: 0, tabViewItems: component.children)

    private lazy var sut = TabViewUIComponent(model: model)

    private func makeScreen(_ component: ServerDrivenComponent) -> BeagleScreenViewController {
        return Beagle.screen(.declarative(component.toScreen()))
    }

    // MARK: - Unit testing Functions
    func test_initShouldSetupCollectionView() {
        // Given / When
        guard let collectionView = sut.subviews.first(where: { $0 is UICollectionView }) as? UICollectionView else {
            XCTFail("Could not find `collectionView`.")
            return
        }
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
        guard let collectionView = sut.subviews.first(where: { $0 is UICollectionView }) as? UICollectionView else {
            XCTFail("Could not retrieve `collectionView`.")
            return
        }
        let indexPath = IndexPath(item: 0, section: 0)
        
        // When
        let cell = sut.collectionView(collectionView, cellForItemAt: indexPath)
        
        // Then
        XCTAssert(cell is TabBarCollectionViewCell)
        XCTAssert(cell.contentView.subviews.count == 1)
    }
    
    func test_whenChangedPages_shouldCallChangeFunction() {
        // Given
        let pages: [BeagleScreenViewController] = [
            makeScreen(Text("Index: 1")),
            makeScreen(Text("Index: 2")),
            makeScreen(Text("Index: 3"))
        ]
        
        let pageView = PageViewUIComponent(
            model: .init(pages: pages),
            indicatorView: PageIndicatorUIComponent(selectedColor: "", unselectedColor: "")
        )
        
        // When
        let spy = PageViewUIComponentSpyDelegate()
        pageView.pageViewDelegate = spy
        pageView.pageViewController(pageView.pageViewController, willTransitionTo: [pages[1]])
        pageView.pageViewController(
            pageView.pageViewController,
            didFinishAnimating: true,
            previousViewControllers: [],
            transitionCompleted: true
        )
        
        // Then
        XCTAssert(spy.changedCurrentPageFuncionCalled)
    }
    
    func test_whenChangedPages_shouldChangeCollectionTab() {
        // Given / When
        sut.contentView.pageViewController(sut.contentView.pageViewController, willTransitionTo: [sut.contentView.model.pages[1]])
        sut.contentView.pageViewController(
            sut.contentView.pageViewController,
            didFinishAnimating: true,
            previousViewControllers: [],
            transitionCompleted: true
        )
        // Then
        XCTAssert(sut.contentView.model.currentPage == sut.model.tabIndex)
    }
    
    func test_whenChangedTabs_shouldChangeCurrentPage() {
        sut.collectionView(sut.collectionView, didSelectItemAt: IndexPath(item: 1, section: 0))
        XCTAssert(sut.contentView.model.currentPage == 1)
        sut.collectionView(sut.collectionView, didSelectItemAt: IndexPath(item: 0, section: 0))
        XCTAssert(sut.contentView.model.currentPage == 0)
    }
}

private class PageViewUIComponentSpyDelegate: PageViewUIComponentDelegate {
    var changedCurrentPageFuncionCalled = false
    
    func changedCurrentPage(_ currentPage: Int) {
        changedCurrentPageFuncionCalled = true
    }
}
