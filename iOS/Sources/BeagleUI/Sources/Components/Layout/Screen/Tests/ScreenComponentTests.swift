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
@testable import BeagleUI

final class ScreenComponentTests: XCTestCase {

    func test_initWithBuilders_shouldReturnExpectedInstance() {
        // Given / When
        let component = ScreenComponent(
            child: Text("text")
        )

        // Then
        XCTAssert(component.child is Text)
    }
    
    func test_contentShouldUseOnlyTheSpaceRequiredByFlexRules() {
        let component = ScreenComponent(
            safeArea: SafeArea.all,
            navigationBar: .init(title: "Test Flex"),
            child: Container(
                children: [
                    Container(
                        children: [Text("Line 0,\nLine 1,\nLine 2,\nLine 3,\nLine 4.")],
                        widgetProperties: .init(appearance: .init(backgroundColor: "#FF0000"), flex: .init(alignSelf: .center, size: .init(width: 50%, height: 75%)))
                    )
                ],
                widgetProperties: .init(appearance: .init(backgroundColor: "#00FF00"), flex: .init(justifyContent: .center))
            )
        )

        let viewController = Beagle.screen(.declarative(component.toScreen()))
        assertSnapshotImage(viewController, size: CGSize(width: 200, height: 150))
    }
    
    func test_navigationBarButtonItemWithImage() {
        let dependencies = BeagleDependencies()
        dependencies.appBundle = Bundle(for: ScreenComponentTests.self)
        Beagle.dependencies = dependencies
        addTeardownBlock {
            Beagle.dependencies = BeagleDependencies()
        }
        
        let barItem = NavigationBarItem(image: "shuttle", text: "shuttle", action: ActionDummy())
        
        let component = ScreenComponent(
            safeArea: SafeArea.none,
            navigationBar: .init(title: "title", showBackButton: true, navigationBarItems: [barItem]),
            child: Text("")
        )
        
        let viewController = Beagle.screen(.declarative(component.toScreen()))
        assertSnapshotImage(viewController, size: CGSize(width: 300, height: 200))
    }
    
    func test_navigationBarButtonItemWithText() {
        let barItem = NavigationBarItem(text: "shuttle", action: ActionDummy())
        
        let component = ScreenComponent(
            safeArea: SafeArea.all,
            navigationBar: .init(title: "title", showBackButton: true, navigationBarItems: [barItem]),
            child: Text("test")
        )
        
        let viewController = Beagle.screen(.declarative(component.toScreen()))
        assertSnapshotImage(viewController, size: CGSize(width: 300, height: 200))
    }
    
    func test_action_shouldBeTriggered() {
        // Given
        let action = ActionDummy()
        let barItem = NavigationBarItem(text: "shuttle", action: action)
        let actionManager = ActionManagerSpy()
        let context = BeagleContextSpy(actionManager: actionManager)
        
        // When
        let resultingView = barItem.toBarButtonItem(context: context, dependencies: BeagleScreenDependencies())
        _ = resultingView.target?.perform(resultingView.action)
        
        // Then
        XCTAssertTrue(actionManager.didCallDoAction)
        XCTAssertEqual(actionManager.actionCalled as? ActionDummy, action)
    }
    
    func test_shouldPrefetchNavigateAction() {
        let prefetch = BeaglePrefetchHelpingSpy()
        let dependencies = BeagleScreenDependencies(preFetchHelper: prefetch)
        
        let navigatePath = "button-item-prefetch"
        let navigate = Navigate.pushView(.remote(navigatePath, shouldPrefetch: true))
        let barItem = NavigationBarItem(text: "Item", action: navigate)
        let screen = ScreenComponent(
            navigationBar: NavigationBar(title: "Prefetch", navigationBarItems: [barItem]),
            child: ComponentDummy()
        )
        
        _ = screen.toView(context: BeagleContextDummy(), dependencies: dependencies)
        XCTAssertEqual([navigatePath], prefetch.prefetched)
    }
    
    func test_whenDecodingJson_thenItShouldReturnAScreen() throws {
        let component: ScreenComponent = try componentFromJsonFile(fileName: "screenComponent")
        assertSnapshot(matching: component, as: .dump)
    }
    
    func testIfAnalyticsScreenShouldBeTriggered() {
        // Given
        let analyticsEvent = AnalyticsScreen(screenName: "screen name")
        let component = ScreenComponent(
            screenAnalyticsEvent: analyticsEvent,
            child: Text("")
        )
        
        let analyticsExecutorSpy = AnalyticsExecutorSpy()
        let dependencies = BeagleScreenDependencies(
            analytics: analyticsExecutorSpy
        )
        
        let context = BeagleContextDummy()
        let controller = ScreenController(
            screen: component.toScreen(),
            context: context,
            dependencies: dependencies
        )
        
        // When
        controller.beginAppearanceTransition(true, animated: false)
        controller.endAppearanceTransition()
        
        // Then
        XCTAssertTrue(analyticsExecutorSpy.didTrackEventOnScreenAppeared)
        XCTAssertFalse(analyticsExecutorSpy.didTrackEventOnScreenDisappeared)
        
        // When
        controller.beginAppearanceTransition(false, animated: false)
        controller.endAppearanceTransition()
        
        // Then
        XCTAssertTrue(analyticsExecutorSpy.didTrackEventOnScreenAppeared)
        XCTAssertTrue(analyticsExecutorSpy.didTrackEventOnScreenDisappeared)
    }
}

// MARK: - Testing Helpers

final class ActionExecutorDummy: ActionExecutor {
    func doAction(_ action: Action, sender: Any, context: BeagleContext) {
    }
}

final class ActionExecutorSpy: ActionExecutor {
    private(set) var didCallDoAction = false
    
    func doAction(_ action: Action, sender: Any, context: BeagleContext) {
        didCallDoAction = true
    }
}
