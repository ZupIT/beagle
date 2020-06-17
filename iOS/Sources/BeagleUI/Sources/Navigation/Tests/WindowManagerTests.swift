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

class WindowManagerTests: XCTestCase {

    func test_validUIWindow_shouldReturnAWindowInstance() {

        // Given
        let windowManager = WindowManagerDefault()

        // When
        let window = windowManager.window

        // Then
        assertSnapshot(matching: window, as: .dump)
    }

}

class WindowManagerDumb: WindowManager {

    var window: WindowProtocol?

    init(window: WindowProtocol = WindowMock()) {
        self.window = window
    }
}

class WindowMock: WindowProtocol {

    var hasInvokedReplaceRootViewController = false

    func replace(rootViewController viewController: UIViewController, animated: Bool, completion: ((Bool) -> Void)?) {
        hasInvokedReplaceRootViewController = true
    }
}

fileprivate extension WindowManagerDefault {
    var window: UIWindow? { return UIWindow(frame: .zero) }
}
