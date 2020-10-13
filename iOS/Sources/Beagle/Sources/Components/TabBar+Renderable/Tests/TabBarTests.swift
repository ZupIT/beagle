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
import SnapshotTesting
import BeagleSchema

class TabBarTests: XCTestCase {

    var dependencies: BeagleDependencies {
        // swiftlint:disable implicit_getter
        get {
            let dependency = BeagleDependencies()
            dependency.appBundle = Bundle(for: TabBarTests.self)
            return dependency
        }
    }

    lazy var controller = BeagleControllerStub(dependencies: dependencies)
    lazy var renderer = BeagleRenderer(controller: controller)

    private let imageSize = ImageSize.custom(CGSize(width: 150, height: 80))

    func testCurrentTabWithContext() {
          // Given
          let screen = Screen(
              child: TabBar(
                items: [
                    TabBarItem(icon: "shuttle"),
                    TabBarItem(icon: "shuttle", title: "Tab 2")
                ],
                currentTab: "@{tab}"),
              context: Context(id: "tab", value: 1)
          )

          // When
          let controller = BeagleScreenViewController(viewModel: .init(
              screenType: .declarative(screen),
              dependencies: dependencies
          ))

          // Then
          assertSnapshotImage(controller.view, size: imageSize)
      }

    func testImageWithContext() {
        // Given
        let screen = Screen(
            child: TabBar(
                items: [
                    TabBarItem(icon: "@{image}"),
                    TabBarItem(icon: "@{image}", title: "Tab 2")
                ]),
            context: Context(id: "image", value: "shuttle")
        )

        // When
        let controller = BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(screen),
            dependencies: dependencies
        ))

        // Then
        assertSnapshotImage(controller.view, size: imageSize)
    }

    func testTabSelection() {
        // Given
        let index = 1
        var didCalledOnTabSelection = false
        var passedIndex: Int?

        let sut = TabBarUIComponent(model: .init(
            tabIndex: 0,
            tabBarItems: [
                 TabBarItem(icon: "shuttle"),
                 TabBarItem(icon: "shuttle", title: "Tab 2")
            ],
            renderer: renderer)
        )

        sut.setupTabBarItems()
        
        // When
        sut.onTabSelection = { index in
            didCalledOnTabSelection = true
            passedIndex = index
        }

        guard let gestureRecognizer = sut.tabItemViews[index]?.gestureRecognizers?.first as? UITapGestureRecognizer else {
            XCTFail("TabBarItem of index 1 has no gesture recognizer.")
            return
        }
        sut.didSelectTabItem(sender: gestureRecognizer)

        // Then
        XCTAssert(didCalledOnTabSelection)
        XCTAssert(passedIndex == index)
    }
}
