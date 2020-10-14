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

class SimpleFormScreenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
        
        let screen = ScreenRobot()
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("simpleform") ?? false {
                let url = "http://localhost:8080/simpleform"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^the app did load simpleform screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SIMPLE_FORM_SCREEN_HEADER.element.exists)
        }
        
        When("^I click on text field \"([^\\\"]*)\"$") { args, _ -> Void in
            guard let param = args?[0],
                let text: ScreenElements = ScreenElements(rawValue: param) else {
                    return
            }
            screen.clickOnText(textOption: text)
        }
        
        When("^insert text \"([^\\\"]*)\"$") { args, _ -> Void in
            guard let text: String = (args?[0]) else { return }
            screen.typeTextIntoField(insertText: text)
        }
        
        Then("^all my simple form components should render their respective text attributes correctly$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SIMPLE_FORM_TITLE.element.exists)
            XCTAssertTrue(ScreenElements.ZIP_FIELD.element.exists)
            XCTAssertTrue(ScreenElements.STREET_FIELD.element.exists)
            
        }
        
        Then("confirm popup should appear correctly$") { _, _ -> Void in
            screen.confirmPopupCorrectly()
        }
        
    }
    
}
