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
import BeagleSchema
@testable import BeagleUI

final class CustomActionTests: XCTestCase {

    func test_whenExecuteCustomAction_shouldUseActionHandler() {
        // Given
        let sut = CustomAction(name: "custom-action", data: [:])
        let actionSpy = CustomActionHandlerSpy()
        let controller = BeagleControllerStub()
        controller.dependencies = BeagleScreenDependencies(customActionHandler: actionSpy)

        // When
        sut.execute(controller: controller, sender: self)

        // Then
        XCTAssertEqual(actionSpy.actionsHandledCount, 1)
    }

    func test_whenExecuteCustomAction_shouldListenToStateChanges() {
        // Given
        let name = "name"
        let error = NSError(domain: "tests", code: 1, description: "CusomAction")
        let customAction = CustomAction(name: name, data: [:])
        let successAction = ActionSpy()

        var statesTracker: [ServerDrivenState] = []
        let controller = BeagleControllerStub()
        let actionHander = CustomActionHandling(handlers: [
            name: { _, _, listener in
                listener(.start)
                statesTracker.append(controller.serverDrivenState)
                listener(.error(error))
                statesTracker.append(controller.serverDrivenState)
                listener(.success(action: successAction))
                statesTracker.append(controller.serverDrivenState)
            }
        ])
        controller.dependencies = BeagleScreenDependencies(
            customActionHandler: actionHander
        )

        // When
        customAction.execute(controller: controller, sender: self)

        // Then
        assertSnapshot(matching: statesTracker, as: .dump)
    }
}

// MARK: - Test helpers
class CustomActionHandlerSpy: CustomActionHandler {
    private(set) var actionsHandledCount = 0

    func handle(action: CustomAction, controller: BeagleController, listener: @escaping Listener) {
        actionsHandledCount += 1
    }
}

class ActionSpy: Action {
    private(set) var executionCount = 0
    private(set) var lastController: BeagleController?
    private(set) var lastSender: Any?

    func execute(controller: BeagleController, sender: Any) {
        executionCount += 1
        lastController = controller
        lastSender = sender
    }

    init() {}
    required init(from decoder: Decoder) throws {}
}
