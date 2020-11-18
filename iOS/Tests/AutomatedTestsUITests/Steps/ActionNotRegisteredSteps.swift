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
class ActionNotRegisteredSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    
    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("actionregistration") ?? false {
                let url = "http://localhost:8080/action-not-registered"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        Given("^the Beagle application did launch with the action not registered screen url$") { _, _ -> Void in
            XCTAssertTrue(self.application.staticTexts[Constants.screenTitle].exists)
        }
        
        // MARK: - When
        
        When(#"^I press the "([^\"]*)" button$"#) { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }

        // MARK: - Then

        Then(#"^nothing happens and the "([^\"]*)" title still appears on screen$"#) { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
    }
}

fileprivate struct Constants {
    static let screenTitle = "ActionNotRegistered Screen"
}
