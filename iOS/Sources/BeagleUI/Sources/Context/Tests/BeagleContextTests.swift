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

final class BeagleContextTests: XCTestCase {
    
    func test_screenController_shouldBeBeagleScreenViewController() {
        // Given
        let component = SimpleComponent()
        let sut: BeagleContext = BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(component.content.toScreen()),
            dependencies: BeagleScreenDependencies()
        ))
        
        // Then
        XCTAssertTrue(sut.screenController is BeagleScreenViewController)
    }
}

// MARK: - Testing Helpers
class UINavigationControllerSpy: BeagleScreenViewController {

    private(set) var presentViewControllerCalled = false
    private(set) var dismissViewControllerCalled = false

    override func present(_ viewControllerToPresent: UIViewController, animated flag: Bool, completion: (() -> Void)? = nil) {
        presentViewControllerCalled = true
        super.present(viewControllerToPresent, animated: flag, completion: completion)
    }
    override func dismiss(animated flag: Bool, completion: (() -> Void)? = nil) {
        dismissViewControllerCalled = true
        super.dismiss(animated: flag, completion: completion)
    }
}

class OnStateUpdatableViewSpy: UIView, OnStateUpdatable {
    private(set) var didCallOnUpdateState = false
    
    func onUpdateState(component: ServerDrivenComponent) -> Bool {
        didCallOnUpdateState = true
        return true
    }
}
