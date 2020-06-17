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
import BeagleSchema
@testable import BeagleUI

final class ShowNativeDialogTests: XCTestCase {

    func test_whenShowNativeDialog_shouldPresentAlertController() {
        // Given
        let action = ShowNativeDialog(
            title: "Title",
            message: "Message",
            buttonText: "Button"
        )

        let controller = BeagleControllerNavigationSpy()

        // When
        action.execute(controller: controller, sender: self)

        // Then
        XCTAssertTrue(controller.viewControllerToPresent is UIAlertController)
    }
}
