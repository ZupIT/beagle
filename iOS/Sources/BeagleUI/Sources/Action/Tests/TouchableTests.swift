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
import SnapshotTesting
import BeagleSchema

final class TouchableTests: XCTestCase {

    func testTouchableView() throws {
        let touchable = Touchable(action: Navigate.popView, child: Text("Touchable"))
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        let view = renderer.render(touchable)

        assertSnapshotImage(view, size: .custom(CGSize(width: 100, height: 80)))
    }
    
    func testIfAnalyticsClickAndActionShouldBeTriggered() {
        // Given
        let analyticsExecutorSpy = AnalyticsExecutorSpy()
        let dependencies = BeagleScreenDependencies(
            analytics: analyticsExecutorSpy
        )
        
        let controller = BeagleControllerStub()
        controller.dependencies = dependencies
        
        let action = ActionSpy()
        let analyticsAction = AnalyticsClick(category: "some category")
        let touchable = Touchable(action: action, clickAnalyticsEvent: analyticsAction, child: Text("mocked text"))
        let view = touchable.toView(renderer: BeagleRenderer(controller: controller))
        
        let eventsGesture = view.gestureRecognizers?
            .compactMap { $0 as? EventsGestureRecognizer }
            .first
        
        // When
        eventsGesture?.triggerEvents()
        
        // Then
        XCTAssertTrue(analyticsExecutorSpy.didTrackEventOnClick)
        XCTAssertEqual(action.executionCount, 1)
        XCTAssertTrue(action.lastSender as AnyObject === view)
    }
}
