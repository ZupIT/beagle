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

final class FormValidationTests: XCTestCase {

    func test_whenExecuteFormValidation_shouldCallErrorListener() {
        // Given
        let inputName = "inputName"
        let errorMessage = "Error Message"
        let action = FormValidation(errors: [
            FieldError(inputName: inputName, message: errorMessage)
        ])
        let controller = BeagleControllerStub()
        let formInput = FormInput(name: inputName, child: ComponentDummy())
        let validationSpy = ValidationErrorListenerSpy()
        validationSpy.beagleFormElement = formInput
        let sender = SubmitFormGestureRecognizer(
            form: Form(action: ActionDummy(), child: ComponentDummy()),
            formView: validationSpy,
            formSubmitView: validationSpy,
            controller: controller
        )

        // When
        action.execute(controller: controller, sender: sender)

        // Then
        XCTAssertEqual(validationSpy.validationErrorMessage, errorMessage)
    }
}

// MARK: - Test helpers
class ValidationErrorListenerSpy: UIView, ValidationErrorListener {
    private(set) var validationErrorMessage: String?

    func onValidationError(message: String?) {
        validationErrorMessage = message
    }
}
