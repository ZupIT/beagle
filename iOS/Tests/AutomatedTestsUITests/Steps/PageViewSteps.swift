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

class PageViewSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("pageview") ?? false {
                let url = "http://localhost:8080/pageview"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^the app did load pageview screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.PAGEVIEW_SCREEN_HEADER.element.exists)
        }
        
        Then("^pageview should render correctly$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.PAGE_1_TEXT.element.exists)
            self.application?.swipeLeft()
            XCTAssertTrue(ScreenElements.PAGE_2_TEXT.element.exists)
            self.application?.swipeLeft()
            XCTAssertTrue(ScreenElements.PAGE_3_TEXT.element.exists)
            
            self.application?.swipeRight()
            self.application?.swipeRight()
            
        }
    }
}
