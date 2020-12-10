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
class PageViewSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication!
    
    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("pageview") ?? false {
                let url = "http://localhost:8080/pageview"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        Given(#"^that I'm on the pageview screen$"#) { _, _ -> Void in
            XCTAssertTrue(ScreenElements.PAGEVIEW_SCREEN_HEADER.element.exists)
        }
        
        // MARK: - When
        
        // Scenarios 1 and 2
        When(#"^I swipe left$"#) { _, _ in
            self.application.swipeLeft()
        }
        
        // Scenarios 3
        When(#"^I press a button with the "([^\"]*)" title$"#) { args, _ -> Void in
            let title = args![0]

            let button = self.application.buttons[title]
            XCTAssertTrue(button.exists)
            button.tap()
        }
        
        // MARK: - Then
        
        // Scenario 1, 2 ans 4
        Then(#"^checks that the text "([^\"]*)" is on the screen$"#) { args, _ in
            let text = args![0]
            
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
        
        // Scenario 2
        Then(#"^checks that the text "([^\"]*)" is not on the screen$"#) { args, _ in
            let text = args![0]
            
            XCTAssertFalse(self.application.staticTexts[text].exists)
        }
        
        // Scenario 3
        Then(#"^checks that the page with text "([^\"]*)" is not displayed$"#) { args, _ in
            let text = args![0]
            
            XCTAssertFalse(self.application.staticTexts[text].exists)
        }
    }
}
