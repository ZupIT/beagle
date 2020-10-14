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

final class TabViewUIComponentTests: XCTestCase {
    
    // MARK: - Variables
    private lazy var controller = BeagleControllerStub()
    private lazy var renderer = BeagleRenderer(controller: controller)
    
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
    
    private lazy var model = TabViewUIComponent.Model(tabIndex: 0, tabViewItems: component.children, renderer: renderer)

    private lazy var sut = TabViewUIComponent(model: model, renderer: .init(controller: controllerStub))

    private lazy var controllerStub = BeagleControllerStub()

    private func makeScreen(_ component: ServerDrivenComponent) -> BeagleScreenViewController {
        return Beagle.screen(.declarative(component.toScreen()))
    }

    // MARK: - Unit testing Functions
    
    func test_whenChangedPages_shouldCallChangeFunction() {
        // Given
        let pages: [BeagleScreenViewController] = [
            makeScreen(Text("Index: 1")),
            makeScreen(Text("Index: 2")),
            makeScreen(Text("Index: 3"))
        ]
        
        let pageView = PageViewUIComponent(
            model: .init(pages: pages),
            indicatorView: PageIndicatorUIComponent(selectedColor: "", unselectedColor: ""),
            controller: BeagleControllerStub()
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
        XCTAssert(spy.changedCurrentPageFunctionCalled)
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
        XCTAssert(sut.contentView.model.currentPage == sut.tabBar.model.tabIndex)
    }
    
    func test_whenChangedTabs_shouldChangeCurrentPage() {
        // Given
        sut.tabBar.setupTabBarItems()
        let tabItem = sut.tabBar.tabItemViews[1]
        
        guard let gestureRecognizer = tabItem?.gestureRecognizers?.first as? UITapGestureRecognizer else {
            XCTFail("TabItem of index 1 has no gesture recognizer.")
            return
        }
        
        // When
        sut.tabBar.didSelectTabItem(sender: gestureRecognizer)
        
        // Then
        XCTAssert(sut.contentView.model.currentPage == 1)
    }
}

private class PageViewUIComponentSpyDelegate: PageViewUIComponentDelegate {
    var changedCurrentPageFunctionCalled = false
    
    func changedCurrentPage(_ currentPage: Int) {
        changedCurrentPageFunctionCalled = true
    }
}
