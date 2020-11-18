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

import Foundation
import XCTest

// swiftlint:disable implicitly_unwrapped_optional
// swiftlint:disable force_unwrapping
class ConfirmActionSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication!
    
    func loadSteps() {
                
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("confirm") ?? false {
                let url = "http://localhost:8080/confirm"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^the Beagle application did launch with the confirm screen url$") { _, _ -> Void in
            XCTAssertTrue(self.application.staticTexts[ConfirmConstants.title].exists)
        }
        
        // Scenario 1 & 2
        When(#"^I press a confirm button with the "([^\"]*)" title$"#) { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }
        
        // Scenario 3, 4, 5
        When(#"^I press an confirm button with the "([^\"]*)" title$"#) { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }

        // Scenario 3, 4, 5
        Then(#"^I press the confirmation "([^\"]*)" button on the confirm component$"#) { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }
        
        // Scenario 1, 3, 4
        Then(#"^a confirm with the "([^\"]*)" message should appear on the screen$"#) { args, _ -> Void in
            let text = args![0]
            let element = self.application.staticTexts[text]
            let result = element.waitForExistence(timeout: 3)
            XCTAssertNotNil(result)
            XCTAssertTrue(element.exists)
        }
        
        // Scenario 2
        Then(#"^a confirm with the "([^\"]*)" and "([^\"]*)" should appear on the screen$"#) { args, _ -> Void in
            let title = args![0]
            let message = args![1]
            XCTAssertTrue(self.application.staticTexts[title].exists)
            XCTAssertTrue(self.application.staticTexts[message].exists)
        }
        
        // Scenario 5
        Then(#"^a confirm with the "([^\"]*)" and "([^\"]*)" buttons should appear on the screen$"#) { args, _ -> Void in
            let customOkText = args![0]
            let customCancelText = args![1]
            XCTAssertTrue(self.application.buttons[customOkText].exists)
            XCTAssertTrue(self.application.buttons[customCancelText].exists)
        }
    }
}

private struct ConfirmConstants {
    static let title = "Confirm Screen"
}
