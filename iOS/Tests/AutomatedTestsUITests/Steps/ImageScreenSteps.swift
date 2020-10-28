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

class ImageScreenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("image") ?? false {
                let url = "http://localhost:8080/image"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^the app did load image screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.IMAGE_SCREEN_HEADER.element.exists)
        }
        
        Then("^image screen should render all text attributes correctly$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.IMAGE_TEXT_1.element.exists)
            XCTAssertTrue(ScreenElements.IMAGE_TEXT_2.element.exists)
            XCTAssertTrue(ScreenElements.IMAGE_TEXT_3.element.exists)
            XCTAssertTrue(ScreenElements.IMAGE_TEXT_4.element.exists)
            XCTAssertTrue(ScreenElements.IMAGE_TEXT_5.element.exists)
        }
    }
}
