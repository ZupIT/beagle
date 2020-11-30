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
class LazyComponentSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication!
    
    func loadSteps() {
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("lazycomponent") ?? false {
                let url = "http://localhost:8080/lazycomponent"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }

        Given("^the Beagle application did launch with the LazyComponent Screen$") { _, _ -> Void in
            XCTAssertTrue(self.application.staticTexts[LazyComponentConstants.title].exists)
        }

        // MARK: - When
        
        //Scenario 1, 2
        When(#"^I click on lazy button "([^\"]*)"$"#) { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }
        
        // MARK: - Then
        
        //Scenario 1, 2
        Then(#"^an screen with an element "([^\"]*)" should be visible$"#) { args, _ -> Void in
            let text = args![0]
            let element = self.application.staticTexts[text]
            let result = element.waitForExistence(timeout: 2)
            XCTAssertNotNil(result)
            XCTAssertTrue(element.exists)
        }
    }
    
    private struct LazyComponentConstants {
        static let title = "LazyComponent Screen"
    }
}
