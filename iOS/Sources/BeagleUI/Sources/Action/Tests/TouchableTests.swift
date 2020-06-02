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

final class TouchableTests: XCTestCase {
    
    func testInitFromDecoder() throws {
        let component: Touchable = try componentFromJsonFile(fileName: "TouchableDecoderTest")
        assertSnapshot(matching: component, as: .dump)
    }

    func testTouchableView() throws {
        let touchable = Touchable(action: Navigate.popView, child: Text("Touchable"))
        let view = touchable.toView(context: BeagleContextDummy(), dependencies: BeagleDependencies())

        assertSnapshotImage(view, size: CGSize(width: 100, height: 80))
    }
    
    func testIfAnalyticsClickAndActionShouldBeTriggered() {
        // Given
        let component = SimpleComponent()
        let analyticsExecutorSpy = AnalyticsExecutorSpy()
        let actionExecutorSpy = ActionExecutorSpy()
        let dependencies = BeagleScreenDependencies(
            actionExecutor: actionExecutorSpy,
            analytics: analyticsExecutorSpy
        )
        
        let controller = BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(component.content.toScreen()),
            dependencies: dependencies
        ))
        
        let navigationController = UINavigationController(rootViewController: controller)
        guard let sut = navigationController.viewControllers.first as? BeagleScreenViewController else {
            XCTFail("Could not find `BeagleScreenViewController`.")
            return
        }
        
        let actionDummy = ActionDummy()
        let analyticsAction = AnalyticsClick(category: "some category")
        let touchable = Touchable(action: actionDummy, clickAnalyticsEvent: analyticsAction, child: Text("mocked text"))
        let view = touchable.toView(context: sut, dependencies: dependencies)
        
        sut.actionManager.register(events: [.action(actionDummy), .analytics(analyticsAction)], inView: view)
        
        let gesture = view.gestureRecognizers?.first { $0 is EventsGestureRecognizer }
    
        guard let eventsGestureRecognizer = gesture as? EventsGestureRecognizer else {
            XCTFail("Could not find `EventsGestureRecognizer`")
            return
        }
                
        // When
        guard let actionManager = sut.actionManager as? ActionManager else {
            XCTFail("Action Manager its not expected type")
            return
        }
        actionManager.handleGestureRecognizer(eventsGestureRecognizer)
                
        // Then
        XCTAssertTrue(analyticsExecutorSpy.didTrackEventOnClick)
        XCTAssertTrue(actionExecutorSpy.didCallDoAction)
    }
}
