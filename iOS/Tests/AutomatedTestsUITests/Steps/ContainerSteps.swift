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
class ContainerSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    
    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("container") ?? false {
                let url = "http://localhost:8080/container-test"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        Given("^the Beagle application did launch with the container screen url$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.CONTAINER_SCREEN_TITLE.element.exists)
        }
        
        // MARK: - Then
        
        // Scenario 1
        Then("^the screen should contain three texts: \"([^\\\"]*)\", \"([^\\\"]*)\" and \"([^\\\"]*)\"$") { args, _ -> Void in
            let texts = [args![0], args![1], args![2]]
            for text in texts {
                XCTAssertTrue(self.application.staticTexts[text].exists)
            }
        }
        
        // Scenario 2
        Then("^the text \"([^\\\"]*)\" and the text \"([^\\\"]*)\" set via context should be displayed$") { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
        
        // Scenario 3
        Then("^the text \"([^\\\"]*)\" and the text \"([^\\\"]*)\" set via context should be displayed$") { args, _ -> Void in
            let texts = [args![0], args![1]]
            for text in texts {
                XCTAssertTrue(self.application.staticTexts[text].exists)
            }
        }
    }
}
