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
class SetContextSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    
    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("setcontext") ?? false {
                let url = "http://localhost:8080/set-context"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        Given(#"^the Beagle application did launch with the SetContext screen url$"#) { _, _ -> Void in
            XCTAssertTrue(SetContextScreenElements.SET_CONTEXT_TITLE.element.exists)
        }
        
        // MARK: - When
        
        // Scenario 01, 02
        When(#"^I press a SetContext button with the "([^\"]*)" title$"#)  { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }
        
        // MARK: - Then
        
        // Scenario 01, 02
        Then(#"^a text with the "([^\"]*)" message should appear on the screen$"#) { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
        
    }
}

private enum SetContextScreenElements: String {
    case SET_CONTEXT_TITLE = "SetContext Screen"
    
    var element: XCUIElement {
        return XCUIApplication().staticTexts[self.rawValue]
    }
}
