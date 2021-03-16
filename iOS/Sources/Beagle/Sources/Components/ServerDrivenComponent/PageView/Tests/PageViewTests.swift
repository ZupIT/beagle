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
@testable import Beagle

class PageViewTests: XCTestCase {
    
    func test_whenDecodingJson_thenItShouldReturnAPageView() throws {
        let component: PageView = try componentFromJsonFile(fileName: "PageViewWith3Pages")
        _assertInlineSnapshot(matching: component, as: .dump, with: """
        ▿ PageView
          ▿ children: 3 elements
            ▿ UnknownComponent
              - type: "custom:beagleschematestscomponent"
            ▿ UnknownComponent
              - type: "custom:beagleschematestscomponent"
            ▿ UnknownComponent
              - type: "custom:beagleschematestscomponent"
          - context: Optional<Context>.none
          - currentPage: Optional<Expression<Int>>.none
          - onPageChange: Optional<Array<Action>>.none
          - pageIndicator: Optional<PageIndicatorComponent>.none
        """)
    }

    func test_whenDecodingJson_thenItShouldReturnPageViewWithIndicator() throws {
        let component: PageView = try componentFromJsonFile(fileName: "PageViewWith3PagesAndIndicator")
        _assertInlineSnapshot(matching: component, as: .dump, with: """
        ▿ PageView
          ▿ children: 3 elements
            ▿ UnknownComponent
              - type: "custom:beagleschematestscomponent"
            ▿ UnknownComponent
              - type: "custom:beagleschematestscomponent"
            ▿ UnknownComponent
              - type: "custom:beagleschematestscomponent"
          - context: Optional<Context>.none
          - currentPage: Optional<Expression<Int>>.none
          - onPageChange: Optional<Array<Action>>.none
          ▿ pageIndicator: Optional<PageIndicatorComponent>
            ▿ some: PageIndicator
              - currentPage: Optional<Expression<Int>>.none
              - numberOfPages: Optional<Int>.none
              - selectedColor: Optional<String>.none
              - unselectedColor: Optional<String>.none
        """)
    }

    func test_whenDecodingInvalidJson() throws {
        XCTAssertThrowsError(
            try componentFromJsonFile(componentType: PageView.self, fileName: "PageViewInvalid")
        )
    }
    
    private let indicator = PageIndicator(selectedColor: "#d1cebd", unselectedColor: "#f6eedf")

    private let page = Container(children: [
        Text("First text"),
        Button(text: "Button"),
        Text("Second text")
    ]).applyFlex(Flex(flexDirection: .column, justifyContent: .center))

    func test_viewWithPages() {
        let pageView = PageView(
            children: Array(repeating: page, count: 5),
            pageIndicator: nil
        )

        let screen = Beagle.screen(.declarative(pageView.toScreen()))
        assertSnapshotImage(screen)
    }

    func test_viewWithPagesAndIndicator() {
        let pageView = PageView(
            children: Array(repeating: page, count: 5),
            pageIndicator: indicator
        )

        let screen = Beagle.screen(.declarative(pageView.toScreen()))
        assertSnapshotImage(screen)
    }
    
    func test_viewWithNoPages() {
        let pageView = PageView(
            children: [],
            pageIndicator: indicator
        )

        let screen = Beagle.screen(.declarative(pageView.toScreen()))
        assertSnapshotImage(screen)
    }
    
    func test_pageViewWithContext() {
        let pageView = PageView(
            children: [Text("Context: @{ctx}")],
            context: Context(id: "ctx", value: "value of ctx")
        )
        
        let screen = Beagle.screen(.declarative(pageView.toScreen()))
        assertSnapshotImage(screen, size: .custom(CGSize(width: 100, height: 50)))
    }
    
    func test_PageViewChildShouldNotCreateNewNavigationController() {
        let navigation = BeagleNavigationController()
        let controller = BeagleScreenViewController(ComponentDummy())
        navigation.viewControllers = [controller]
        
        let pageView = PageView(children: [ComponentDummy()], pageIndicator: nil)
        let view = pageView.toView(renderer: controller.renderer)
        let page = (view as? PageViewUIComponent)?.pageViewController.viewControllers?.first
        
        XCTAssertEqual(page?.navigationController, navigation)
    }
    
    func test_viewShouldBeReleased() {
        // Given
        let controller = BeagleScreenViewController(ComponentDummy())
        var strongReference: UIView? = PageView(
            children: [ComponentDummy()],
            context: .init(id: "context", value: 0),
            onPageChange: [ActionDummy()],
            currentPage: "@{context}"
        ).toView(renderer: controller.renderer)
        
        // When
        weak var weakReference = strongReference
        // Then
        XCTAssertNotNil(weakReference)
        
        // When
        strongReference = nil
        // Then
        XCTAssertNil(weakReference)
    }

}
