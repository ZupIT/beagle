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

import Foundation
import XCTest

// swiftlint:disable implicitly_unwrapped_optional
// swiftlint:disable force_unwrapping
class TextInputSteps: CucumberStepsDefinition {
    var application: XCUIApplication!

    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("textinput") ?? false {
                let url = "http://localhost:8080/textinput"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }

        // MARK: - Given
        Given(#"^the Beagle application did launch with the textInput on screen$"#) { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TEXT_INPUT_SCREEN_TITLE.element.exists)
        }

        // MARK: - When
        
        // Scenarios 5, 6 and 8
        When(#"^I click in the textInput with the placeholder "([^\"]*)"$"#) { args, _ -> Void in
            let placeholder = args![0]

            if let textField = self.application.textFields[placeholder: placeholder] {
                self.application.scrollToElement(element: textField)
                textField.tap()
            }
        }

        // MARK: - Then
        
        // Scenario 1
        Then(#"^I must check if the textInput value "([^\"]*)" appears on the screen$"#) { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.textFields[text].exists)
        }

        // Scenario 2
        Then(#"^I must check if the textInput placeholder "([^\"]*)" appears on the screen$"#) { args, _ -> Void in
            let placeholder = args![0]
            let textField = self.application.textFields[placeholder: placeholder]

            XCTAssertTrue(textField?.exists ?? false)
        }

        // Scenario 3
        Then(#"^verify if the field with the placeholder "([^\"]*)" is disabled$"#) { args, _ -> Void in
            let placeholder = args![0]
            let textField = self.application.textFields[placeholder: placeholder]

            XCTAssertTrue(textField?.exists ?? false)
            XCTAssertFalse(textField?.isEnabled ?? true)
        }

        // Scenario 4
        Then(#"^verify if the field with the value "([^\"]*)" is read only$"#) { args, _ -> Void in
            let text = args![0]
            let textField = self.application.textFields[text]

            XCTAssertTrue(textField.exists)
            XCTAssertFalse(textField.isEnabled)
        }

        // Scenario 5
        Then(#"^verify if the textInput "([^\"]*)" is the first responder$"#) { args, _ -> Void in
            let placeholder = args![0]
            let textField = self.application.textFields[placeholder: placeholder]

            XCTAssertTrue(textField?.exists ?? false)
            XCTAssertEqual(self.application.keyboards.count, 1)
        }

        // Scenario 6
        Then(#"^validate textInput component of type number with text "([^\"]*)"$"#) { args, _ -> Void in
            let placeholder = args![0]
            let textField = self.application.textFields[placeholder: placeholder]

            XCTAssertTrue(textField?.exists ?? false)
            XCTAssertEqual(self.application.keyboards.count, 1)
        }

        // Scenario 7
        Then(#"^change to "([^\"]*)" then to "([^\"]*)" then to "([^\"]*)"$"#) { args, _ -> Void in
            let didOnFocus = args![0]
            let didOnChange = args![1]
            let didOnBlur = args![2]

            XCTAssertTrue(self.application.textFields[didOnFocus].exists)
            self.application.typeText("a")

            XCTAssertTrue(self.application.textFields[didOnChange].exists)
            self.application.typeText("\n")

            XCTAssertTrue(self.application.textFields[didOnBlur].exists)
        }

        // Scenario 8
        Then(#"^change to "([^\"]*)" then to "([^\"]*)" then to "([^\"]*)" in the correct order$"#) { args, _ -> Void in
            let didOnFocus = args![0]
            let didOnChange = args![1]
            let didOnBlur = args![2]

            XCTAssertTrue(self.application.textFields[didOnFocus].exists)
            self.application.typeText("a")

            XCTAssertTrue(self.application.textFields[didOnChange].exists)
            self.application.typeText("\n")

            XCTAssertTrue(self.application.textFields[didOnBlur].exists)

            XCTAssertFalse(self.application.textFields[didOnFocus].exists)
            XCTAssertFalse(self.application.textFields[didOnChange].exists)
        }

        // Scenario 9
        Then(#"^The hidden input fields "([^\"]*)" should not be visible$"#) { args, _ -> Void in
            let text = args![0]
            let staticText = self.application.staticTexts["There are two hidden input fields above"]

            self.application.scrollToElement(element: staticText)
            XCTAssertFalse(self.application.textFields[text].exists)
        }
    }
}

private extension XCUIElementQuery {
    subscript(placeholder value: String) -> XCUIElement? {
        first { $0.placeholderValue == value }
    }
}
