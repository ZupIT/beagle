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
import SnapshotTesting
@testable import Beagle
import BeagleSchema

final class ButtonTests: XCTestCase {

    private let dependencies = BeagleScreenDependencies()
    
    func test_toView_shouldSetRightButtonTitle() {
        //Given
        let buttonTitle = "title"
        let component = Button(text: Expression.value(buttonTitle))
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)

        //When
        guard let button = renderer.render(component) as? UIButton else {
            XCTFail("Build View not returning UIButton")
            return
        }
        
        // Then
        XCTAssertEqual(button.titleLabel?.text, buttonTitle)
    }
    
    func test_toView_shouldApplyButtonStyle() {
        // Given
        let theme = ThemeSpy()
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        controller.dependencies = BeagleScreenDependencies(theme: theme)
        
        let style = "test.button.style"
        let button = Button(text: "apply style", styleId: style)

        // When
        let view = renderer.render(button)

        // Then
        XCTAssertEqual(view, theme.styledView)
        XCTAssertEqual(style, theme.styleApplied)
    }
    
    func test_toView_shouldPrefetchNavigateAction() {
        // Given
        let prefetch = BeaglePrefetchHelpingSpy()
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        controller.dependencies = BeagleScreenDependencies(preFetchHelper: prefetch)
        
        let navigatePath = "path-to-prefetch"
        let navigate = Navigate.pushStack(.remote(.init(url: navigatePath)))
        let button = Button(text: "prefetch", onPress: [navigate])

        // When
        _ = renderer.render(button)

        // Then
        XCTAssertEqual([navigatePath], prefetch.prefetched)
    }
    
    func test_action_shouldBeTriggered() {
        // Given
        let action = ActionSpy()
        let button = Button(text: "Trigger Action", onPress: [action])
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)

        // When
        let view = renderer.render(button)
        (view as? Button.BeagleUIButton)?.triggerTouchUpInsideActions()

        // Then
        XCTAssertEqual(action.executionCount, 1)
        XCTAssert(action.lastOrigin as AnyObject === view)
    }
    
    func test_analytics_click_shouldBeTriggered() {
        // Given
        let analytics = AnalyticsExecutorSpy()
        let button = Button(text: "Trigger analytics click", clickAnalyticsEvent: .init(category: "some category"))
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        controller.dependencies = BeagleScreenDependencies(analytics: analytics)

        // When
        let view = renderer.render(button)
        (view as? Button.BeagleUIButton)?.triggerTouchUpInsideActions()
        
        // Then
        XCTAssertTrue(analytics.didTrackEventOnClick)
    }
    
    func test_analytics_click_and_action_shouldBeTriggered() {
        // Given
        let action = ActionSpy()
        let analytics = AnalyticsExecutorSpy()
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        controller.dependencies = BeagleScreenDependencies(analytics: analytics)
        
        let button = Button(text: "Trigger analytics click", onPress: [action], clickAnalyticsEvent: .init(category: "some category"))

        // When
        let view = renderer.render(button)
        (view as? Button.BeagleUIButton)?.triggerTouchUpInsideActions()
        
        // Then
        XCTAssertTrue(analytics.didTrackEventOnClick)
        XCTAssertEqual(action.executionCount, 1)
        XCTAssert(action.lastOrigin as AnyObject === view)
    }
}
