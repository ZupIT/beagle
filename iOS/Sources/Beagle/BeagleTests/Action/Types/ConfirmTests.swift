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
@testable import Beagle

final class ConfirmTests: XCTestCase {

    func testAlertController() {
        // Given
        let onPressOkAction = ActionSpy()
        let onPressCancelAction = ActionSpy()
        
        let confirmAction = Confirm(
            title: "Title",
            message: "Message",
            onPressOk: onPressOkAction,
            onPressCancel: onPressCancelAction,
            labelOk: "Ok",
            labelCancel: "Cancel"
        )
        let view = UIView()
        let controller = BeagleControllerNavigationSpy()

        // When
        confirmAction.execute(controller: controller, origin: view)

        // Then
        XCTAssertTrue(controller.viewControllerToPresent is UIAlertController)
    }
}
