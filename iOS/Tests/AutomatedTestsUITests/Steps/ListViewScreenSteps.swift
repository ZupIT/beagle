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

class ListViewScreenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("listview") ?? false {
                let url = "http://localhost:8080/listview"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^the app did load listview screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.LISTVIEW_SCREEN_HEADER.element.exists)
        }
        
        When("^I have a vertical list configured$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.STATIC_LISTVIEW_TEXT_1.element.exists)
        }
        
        Then("^listview screen should render all text attributes correctly$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.STATIC_LISTVIEW_TEXT_1.element.exists)
            XCTAssertTrue(ScreenElements.STATIC_LISTVIEW_TEXT_2.element.exists)
            XCTAssertTrue(ScreenElements.DYNAMIC_LISTVIEW_TEXT_1.element.exists)
        }
        
        Then("^listview screen should perform the scroll action vertically$") { _, _ -> Void in
            XCUIApplication().scrollToElement(element: ScreenElements.DYNAMIC_LISTVIEW_TEXT_2.element)
        }
        
    }
    
}
