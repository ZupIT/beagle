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

class TextSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    
    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("text") ?? false {
                let url = "http://localhost:8080/text"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        Given(#"^the Beagle application did launch with the text url screen$"#) { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TEXT_SCREEN_TITLE.element.exists)
        }
        
        // MARK: - Then
        
        // Scenario 1
        Then(#"^the text "([^\"]*)" should appear on the screen$"#) { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
    }
}
