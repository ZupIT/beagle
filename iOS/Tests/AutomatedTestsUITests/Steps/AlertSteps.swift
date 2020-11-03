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

class AlertSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    
    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("alert") ?? false {
                let url = "http://localhost:8080/alert"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        Given("^the Beagle application did launch with the alert screen url$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.ALERT_SCREEN_TITLE.element.exists)
        }
        
        // MARK: - When
        
        // Scenarios 1 and 2
        When(#"^I press an alert button with the "([^\"]*)" title$"#) { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }
        
        // MARK: - Then
        
        // Scenario 1
        Then(#"^an alert with the "([^\"]*)" message should appear on the screen$"#) { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
        
        // Scenario 2
        Then(#"^an alert with the "([^\"]*)" title and the "([^\"]*)" message should appear on the screen$"#) { args, _ -> Void in
            let title = args![0]
            let message = args![1]
            XCTAssertTrue(self.application.staticTexts[title].exists)
            XCTAssertTrue(self.application.staticTexts[message].exists)
        }
    }
}

