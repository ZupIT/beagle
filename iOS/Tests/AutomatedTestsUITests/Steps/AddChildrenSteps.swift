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

class AddChildrenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
        
        let screen = ScreenRobot()
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("addChildren") ?? false {
                let url = "http://localhost:8080/add-children"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^that I'm on the addChildren Screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.ADD_CHILDREN_HEADER.element.exists)
        }
        
        When("^I click on button \"([^\\\"]*)\"$") { args, _ -> Void in
            guard let button: String = args?[0],
                  let element = ScreenElements(rawValue: button)
                  else {
                XCTFail("button element not found")
                return
            }
            screen.clickOnButton(button: element)
        }
        
        Then("^A Text need to be added after the already exist one$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TEXT_ADDED.element.exists)
        }
        
        Then("^A Text need to be added before the already exist one$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TEXT_FIXED.element.exists)
        }
        
        Then("^A Text need to replace the already exist one$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TEXT_ADDED.element.exists)
            XCTAssertFalse(ScreenElements.TEXT_FIXED.element.exists)
        }
        
        Then("^Nothing should happen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TEXT_FIXED.element.exists)
            XCTAssertFalse(ScreenElements.TEXT_ADDED.element.exists)
        }
        
    }
    
}
