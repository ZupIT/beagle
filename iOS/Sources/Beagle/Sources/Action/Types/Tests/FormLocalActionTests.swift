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

final class FormLocalActionTests: XCTestCase {

    func testLocalFormHandler() {
        // Given
        let sut = FormLocalAction(name: "custom-action", data: [:])
        let actionSpy = LocalFormHandlerSpy()
        let controller = BeagleControllerStub()
        controller.dependencies = BeagleScreenDependencies(localFormHandler: actionSpy)
        
        // When
        sut.execute(controller: controller, origin: controller.view)
        
        // Then
        XCTAssertEqual(actionSpy.actionsHandledCount, 1)
    }
    
    func testStateChanges() {
        // Given
        let name = "name"
        let error = NSError(domain: "tests", code: 1, description: "FormLocalAction")
        let formLocalAction = FormLocalAction(name: name, data: [:])
        let successAction = ActionSpy()
        
        var statesTracker: [ServerDrivenState] = []
        let controller = BeagleControllerStub()
        let actionHandler = LocalFormHandling(handlers: [
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
            localFormHandler: actionHandler
        )

        // When
        formLocalAction.execute(controller: controller, origin: controller.view)

        // Then
        assertSnapshot(matching: statesTracker, as: .dump)
    }
}

// MARK: - Test helpers

class LocalFormHandlerSpy: LocalFormHandler {
    private(set) var actionsHandledCount = 0

    func handle(action: FormLocalAction, controller: BeagleController, listener: @escaping Listener) {
        actionsHandledCount += 1
    }
}

class ActionSpy: Action {
    var analytics: ActionAnalyticsConfig? { return nil }
    private(set) var executionCount = 0
    private(set) var lastController: BeagleController?
    private(set) var lastOrigin: UIView?

    func execute(controller: BeagleController, origin: UIView) {
        executionCount += 1
        lastController = controller
        lastOrigin = origin
    }

    init() {}
    required init(from decoder: Decoder) throws {}
}
