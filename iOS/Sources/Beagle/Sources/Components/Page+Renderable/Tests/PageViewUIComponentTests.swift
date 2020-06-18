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

class PageViewUIComponentTests: XCTestCase {

    private lazy var pageView = PageViewUIComponent(
        model: .init(pages: pages),
        indicatorView: PageIndicatorUIComponent(selectedColor: nil, unselectedColor: nil)
    )

    private lazy var pages: [BeagleScreenViewController] = [
        makeScreen(Text("Index: 1")),
        makeScreen(Text("Index: 2")),
        makeScreen(Text("Index: 3"))
    ]

    private func makeScreen(_ component: ServerDrivenComponent) -> BeagleScreenViewController {
        return Beagle.screen(.declarative(component.toScreen()))
    }

    private lazy var pager = pageView.pageViewController

    func test_whenChangingPagesFromUi_thenShouldUpdateModelOnlyWhenFinishedAnimationAndTransition() {
        pageView.pageViewController(pager, willTransitionTo: [pages[1]])
        pageView.pageViewController(
            pager,
            didFinishAnimating: true,
            previousViewControllers: [],
            transitionCompleted: true
        )
        XCTAssert(pageView.model.currentPage == 1)

        pageView.pageViewController(pager, willTransitionTo: [pages[2]])
        pageView.pageViewController(
            pager,
            didFinishAnimating: true,
            previousViewControllers: [],
            transitionCompleted: true
        )
        XCTAssert(pageView.model.currentPage == 2)
    }

    func test_whenChangingPagesFromUi_thenShouldReturnTheRightNextPage() {
        // Forward / After
        XCTAssert(pageView.pageViewController(pager, viewControllerAfter: pages[0]) === pages[1])
        XCTAssert(pageView.pageViewController(pager, viewControllerAfter: pages[1]) === pages[2])
        XCTAssert(pageView.pageViewController(pager, viewControllerAfter: pages[2]) === nil)

        // Backward / Before
        XCTAssert(pageView.pageViewController(pager, viewControllerBefore: pages[0]) === nil)
        XCTAssert(pageView.pageViewController(pager, viewControllerBefore: pages[1]) === pages[0])
        XCTAssert(pageView.pageViewController(pager, viewControllerBefore: pages[2]) === pages[1])
    }

    func test_whenCallingSwipePage_thenShouldUpdateModel() {
        for index in pages.indices {
            pageView.swipeToPage(at: index)
            XCTAssert(pageView.model.currentPage == index)
        }
    }

}
