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
class SendRequestActionSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication!
    
    func loadSteps() {
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("sendrequest") ?? false {
                let url = "http://localhost:8080/send-request"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        Given(#"^the Beagle application did launch with the send request screen url$"#) { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SEND_REQUEST_SCREEN_TITLE.element.exists)
        }
        
        // MARK: - When
        
        // Scenarios 1 and 2
        When(#"^I press the "([^\"]*)" button$"#) { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }
        
        // MARK: - Then
        
        // Scenario 1
        Then(#"^the screen should show some alert with "([^\"]*)" title$"#) { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.alerts.element.staticTexts[text].exists)
        }
        
        // Scenario 2
        Then(#"^the pressed button changes its "([^\"]*)" title to "([^\"]*)"$"#) { args, _ -> Void in
            let oldButtonTitle = args![0]
            let currentButtonTitle = args![1]
            XCTAssertFalse(self.application.buttons[oldButtonTitle].exists)
            XCTAssertTrue(self.application.buttons[currentButtonTitle].exists)
        }
    }
}
