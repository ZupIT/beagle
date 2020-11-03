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

class ScrollViewSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    
    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("scrollview") ?? false {
                let url = "http://localhost:8080/scrollview"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        // MARK: - Given
        Given("^the Beagle application did launch with the scrollview screen url$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SCROLLVIEW_SCREEN_HEADER.element.exists)
        }
       
        //MARK: - When
        
        //Scenario 1
        When(#"^I press on text scroll "([^\"]*)"$"#) { args, _ -> Void in
            let text = args![0]
            self.application.staticTexts[text].tap()
        }
        
        //MARK: - Then
        
        //Scenario 1
        Then(#"^the current text "([^\"]*)" should be replaced for a large text and the scrollview should perform in the specified orientation "([^\"]*)"$"#) { args, _ -> Void in
            let currentText = args![0]
            let orientation = args![1]
            
            XCTAssertFalse(self.application.staticTexts[currentText].exists)
            
            let element = self.elementFrom(string: ScreenElements.SCROLLVIEW_LARGE_TEXT.rawValue)
            XCTAssertNotNil(element)
            
            let button = self.application.buttons[orientation]
            button.tap()
        }
    }
    
    //MARK: - Helper method
    func elementFrom(string: String) -> XCUIElement? {
        let predicate = NSPredicate(format: "staticTexts CONTAINS[c] %@", string)
        return self.application.staticTexts.element(matching: predicate)
    }
}
