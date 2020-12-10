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
class ConditionalActionSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication!
    
    func loadSteps() {
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("conditional") ?? false {
                let url = "http://localhost:8080/conditional"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        
        Given(#"^the Beagle application did launch with the conditional screen url$"#) { _, _ -> Void in
            XCTAssertTrue(ScreenElements.CONDITIONAL_ACTION_SCREEN_HEADER.element.exists)
        }
        
        // MARK: - When
        
        // Scenario 1 and 2, 3, 4
        When(#"^I click in a conditional button with "([^\"]*)" title$"#) { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }

        // MARK: - Then
        
        // Scenario 1 and 2, 3, 4
        Then(#"^an Alert action should pop up with a "([^\"]*)" message$"#) { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.alerts.element.staticTexts[text].exists)
        }
    }
}
