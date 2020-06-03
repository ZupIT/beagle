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

final class ActionManagerTests: XCTestCase {
    func test_registerAction_shouldAddGestureRecognizer() {
        // Given
        let sut = ActionManager()
        let view = UILabel()
        let action = Navigate.popView
        
        // When
        sut.register(events: [.action(action)], inView: view)
        
        // Then
        XCTAssertEqual(1, view.gestureRecognizers?.count)
        XCTAssertTrue(view.isUserInteractionEnabled)
    }
    
    func test_action_shouldBeTriggered() {
        // Given
        let component = SimpleComponent()
        let actionExecutorSpy = ActionExecutorSpy()

        let controller = BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(component.content.toScreen()),
            dependencies: BeagleScreenDependencies(
                actionExecutor: actionExecutorSpy
            )
        ))
        
        let navigationController = UINavigationController(rootViewController: controller)
        guard let sut = navigationController.viewControllers.first as? BeagleScreenViewController else {
            XCTFail("Could not find `BeagleScreenViewController`.")
            return
        }
        
        let view = UILabel()
        let action = Navigate.popView
        sut.actionManager.register(events: [.action(action)], inView: view)
        
        guard let eventsGestureRecognizer = view.gestureRecognizers?.first as? EventsGestureRecognizer else {
            XCTFail("Could not find `EventsGestureRecognizer`.")
            return
        }
        
        // When
        guard let actionManager = sut.actionManager as? ActionManager else {
            XCTFail("Action Manager its not expected type")
            return
        }
        actionManager.handleGestureRecognizer(eventsGestureRecognizer)
                
        // Then
        XCTAssertTrue(actionExecutorSpy.didCallDoAction)
    }
}
