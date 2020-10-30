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

class ScrollViewScreenSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    
    func loadSteps() {
        let screen = ScreenRobot()
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("scrollview") ?? false {
                let url = "http://localhost:8080/scrollview"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        // MARK: - Given
        Given("^that I'm on the scrollview screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SCROLLVIEW_SCREEN_HEADER.element.exists)
        }
       
        //MARK: - When
        
        //Senario 1
        When("^I have a horizontal scroll configured$") { _, _ -> Void in
            self.application.staticTexts[ScreenElements.CLICK_SCROLLVIEW_HORIZONTAL.rawValue].scrollViews
        }
        
        //Senario 2
        When("^I press on text scroll horizontal \"([^\\\"]*)\"$") { args, _ -> Void in
            let text: String = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
            self.application.staticTexts[text].tap()
        }
        
        //Senario 3
        When("^I press on text to be scrolled and rotated \"([^\\\"]*)\"$") { args, _ -> Void in
            let text: String = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
            XCUIDevice.shared.orientation = .landscapeLeft
            self.application.staticTexts[text].tap()
        }
        
        //Senario 4
        When("^I have a vertical scroll configured$") { _, _ -> Void in
            self.application.staticTexts[ScreenElements.CLICK_SCROLLVIEW_VERTICAL.rawValue].scrollViews
        }
        
        //Senario 5
        When("^I press on text scrollview vertical \"([^\\\"]*)\"$") { args, _ -> Void in
            let text: String = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
            self.application.staticTexts[text].tap()
        }
        
        //Senario 6
        When("^I press on text scrollview to be rotate \"([^\\\"]*)\"$") { args, _ -> Void in
            let text: String = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
            XCUIDevice.shared.orientation = .landscapeLeft
            self.application.staticTexts[text].tap()
        }
        
        
        //MARK: - Then
        
        //Senario 1
        Then("^scrollview screen should perform the scroll action horizontally$") { _, _ -> Void in
            XCTAssertFalse(ScreenElements.SCROLLVIEW_ELEMENTS_1.element.exists)
        }
        
        //Senario 2
        Then("^the text should change for the next and the scrollview should perform horizontally \"([^\\\"]*)\"$") {args, _ -> Void in
            let text: String = args![0]
            XCTAssertFalse(self.application.staticTexts[text].exists)
            XCTAssertTrue(self.elementContains(string: ScreenElements.SCROLLVIEW_ELEMENTS_1.rawValue))
        }
        
        //Senario 3
        Then("^the text horizontal of scrollview rotate should change$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SCROLLVIEW_ELEMENTS_1.element.exists)
        }
        
        Then("^the scrollview rotate should perform horizontally \"([^\\\"]*)\"$") {args, _ -> Void in
            let text: String = args![0]
            XCTAssertFalse(self.application.staticTexts[text].exists)
        }
        
        Then("^even if the screen is rotated the scrollview must be perform horizontally \"([^\\\"]*)\"$") {args, _ -> Void in
            let text: String = args![0]
            XCTAssertFalse(self.application.staticTexts[text].exists)
            XCTAssertTrue(self.elementContains(string: ScreenElements.SCROLLVIEW_ELEMENTS_1.rawValue))
        }
        
        //Senario 4
        Then("^scrollview screen should perform the scroll action vertically$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SCROLLVIEW_ELEMENTS_1.element.exists)
        }
        
        //Senario 5
        Then("^the text should change$") { _, _ -> Void in
            XCTAssertFalse(ScreenElements.CLICK_SCROLLVIEW_VERTICAL.element.exists)
        }
        
        Then("^the scrollview should perform vertically \"([^\\\"]*)\"$") {args, _ -> Void in
            let text: String = args![0]
            XCTAssertFalse(self.application.staticTexts[text].exists)
            XCTAssertTrue(self.elementContains(string: ScreenElements.SCROLLVIEW_ELEMENTS_1.rawValue))
        }
        
        //Senario 6
        Then("^the text vertical of scrollview rotate should change$") { _, _ -> Void in
            XCTAssertFalse(ScreenElements.CLICK_SCROLLVIEW_VERTICAL.element.exists)
        }
        
        Then("^the scrollview rotate should perform vertically \"([^\\\"]*)\"$") {args, _ -> Void in
            let text: String = args![0]
            XCTAssertFalse(self.application.staticTexts[text].exists)
            XCTAssertTrue(self.elementContains(string: ScreenElements.SCROLLVIEW_ELEMENTS_1.rawValue))
        }
        
        Then("^even if the screen is rotated the scrollview must be perform vertically \"([^\\\"]*)\"$") {args, _ -> Void in
            let text: String = args![0]
            XCTAssertFalse(self.application.staticTexts[text].exists)
            XCTAssertTrue(self.elementContains(string: ScreenElements.SCROLLVIEW_ELEMENTS_1.rawValue))
        }
        
    }
    //MARK: - Funções
    
    func elementContains(string: String) -> Bool {
        let predicate = NSPredicate(format: "staticTexts CONTAINS[c] %@", string)
        let result = self.application?.staticTexts.element(matching: predicate)
        return ((result?.exists) != nil)
    }
}
