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
@testable import BeagleUI
import BeagleSchema

final class ButtonTests: XCTestCase {

    private let dependencies = BeagleScreenDependencies()
    
    func test_toView_shouldSetRightButtonTitle() {
        //Given
        let buttonTitle = "title"
        let component = Button(text: buttonTitle)

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
        let dependencies = BeagleScreenDependencies(theme: theme)
        let renderer = BeagleRenderer(context: BeagleContextDummy(), dependencies: dependencies)
        
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
        let dependencies = BeagleScreenDependencies(preFetchHelper: prefetch)
        let renderer = BeagleRenderer(context: BeagleContextDummy(), dependencies: dependencies)
        
        let navigatePath = "path-to-prefetch"
        let navigate = Navigate.pushStack(.remote(navigatePath))
        let button = Button(text: "prefetch", action: navigate)

        // When
        _ = renderer.render(button)

        // Then
        XCTAssertEqual([navigatePath], prefetch.prefetched)
    }
    
    func test_action_shouldBeTriggered() {
        // Given
        let action = ActionDummy()
        let button = Button(text: "Trigger Action", action: action)
        let actionManager = ActionManagerSpy()
        let context = BeagleContextSpy(actionManager: actionManager)
        let renderer = BeagleRenderer(context: context, dependencies: dependencies)

        // When
        let view = renderer.render(button)
        (view as? Button.BeagleUIButton)?.triggerTouchUpInsideActions()

        // Then
        XCTAssertEqual(actionManager.actionCalled as? ActionDummy, action)
    }
    
    func test_analytics_click_shouldBeTriggered() {
        // Given
        var dependencies = BeagleScreenDependencies()
        dependencies.analytics = AnalyticsExecutorSpy()
        let button = Button(text: "Trigger analytics click", clickAnalyticsEvent: .init(category: "some category"))
        let actionManager = ActionManagerSpy()
        let context = BeagleContextSpy(actionManager: actionManager)
        let renderer = BeagleRenderer(context: context, dependencies: dependencies)

        // When
        let view = renderer.render(button)
        (view as? Button.BeagleUIButton)?.triggerTouchUpInsideActions()
        
        // Then
        XCTAssertTrue(actionManager.analyticsEventCalled)
    }
    
    func test_analytics_click_and_action_shouldBeTriggered() {
        // Given
        var dependencies = BeagleScreenDependencies()
        let action = ActionDummy()
        dependencies.analytics = AnalyticsExecutorSpy()
        let actionManager = ActionManagerSpy()
        let context = BeagleContextSpy(actionManager: actionManager)
        let renderer = BeagleRenderer(context: context, dependencies: dependencies)
        let button = Button(text: "Trigger analytics click", action: action, clickAnalyticsEvent: .init(category: "some category"))

        // When
        let view = renderer.render(button)
        (view as? Button.BeagleUIButton)?.triggerTouchUpInsideActions()
        
        // Then
        XCTAssertEqual(actionManager.actionCalled as? ActionDummy, action)
        XCTAssertTrue(actionManager.analyticsEventCalled)

    }
}
