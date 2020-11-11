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

class SimpleFormSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
                
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("simpleform") ?? false {
                let url = "http://localhost:8080/simpleform"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }

        Given("^that I'm on the simple form screen$") { _, _ -> Void in
            XCTAssertTrue(self.application!.staticTexts[Seeds.screenTitle].exists)
        }
        
        // Scenario 1
        Then("^all my children components should render their respective attributes correctly$") { _, _ -> Void in
            XCTAssertTrue(self.application!.textFields[placeholder: Seeds.emailPlaceholder]!.exists)
            XCTAssertTrue(self.application!.textFields[placeholder: "Type in your password"]!.exists)
            XCTAssertTrue(self.application!.buttons[Seeds.submitButtonTitle].exists)
        }

        // Scenario 2
        When("^I click on a textInput with email Type and type in my \"([^\\\"]*)\"$") { args, _ -> Void in
            let text = args![0]

            let textField = self.application!.textFields[placeholder: Seeds.emailPlaceholder]!
            textField.tap()
            textField.typeText(text)
        }
        
        When("^I click on a textInput with password Type and type in my \"([^\\\"]*)\"$") { args, _ -> Void in
            let text = args![0]

            let textField = self.application!.textFields[placeholder: "Type in your password"]!
            textField.tap()
            textField.typeText(text)
        }
        
        When("^I click on button with title Click to Submit") { _, _ -> Void in
            let button = self.application!.buttons[Seeds.submitButtonTitle]
            button.tap()
        }

        
        Then(#"^verify if the email: "([^\"]*)" and the password: "([^\"]*)" is appearing correctly$"#) { args, _ -> Void in
            let email = args![0]
            let pass = args![1]
            
            XCTAssertTrue(self.application!.textFields[email].exists)
            XCTAssertTrue(self.application!.textFields[pass].exists)

        }
    }
}

// MARK: - Helpers
fileprivate struct Seeds {
    static let screenTitle = "SimpleForm"
    static let emailPlaceholder = "Type in your email"
    static let submitButtonTitle = "Click to Submit"
}


private extension XCUIElementQuery {
    subscript(placeholder value: String) -> XCUIElement? {
        first { $0.placeholderValue == value }
    }
}
