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
    
    var application: XCUIApplication?
    
    func loadSteps() {
    
        let screen = ScreenRobot()
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("scrollView") ?? false {
                let url = "http://localhost:8080/scrollView"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
                
        Given("^that I'm on the scrollview screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SCROLLVIEW_SCREEN_HEADER.element.exists)
        }
        
        //ScrollView 01
        When("^I press on text scroll horizontal \"([^\\\"]*)\"$") { args, _ -> Void in
            guard let text: String = args?[0],
                  let element = ScreenElements(rawValue: text)
                  else {
                XCTFail("text element not found")
                return
            }
            screen.clickOnText(textOption: element)
            XCTest.accessibilityScroll(UIAccessibilityScrollDirection.right)

        }
        
        Then("^the text should change for the next and the scrollview should perform horizontally$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SCROLLVIEW_ELEMENTS_1.element.exists)
        }
        
       
       
        
    }
}
