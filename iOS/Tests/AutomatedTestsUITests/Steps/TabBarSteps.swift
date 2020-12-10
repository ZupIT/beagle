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
class TabBarSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication!
    
    func loadSteps() {
            
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("tabbar") ?? false {
                let url = "http://localhost:8080/tabbar"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^that I'm on the tabBar screen$") { _, _ -> Void in
            XCTAssertTrue(TabBarScreenElements.SCREEN_HEADER.element.exists)
        }

        // MARK: - When

        // Scenario 2, 6
        When(#"^I click on button "([^\"]*)" to change tab$"#) { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }

        // Scenario 3
        When(#"^I click in a tab with text "([^\"]*)"$"#) { args, _ -> Void in
            let text = args![0]
            self.application.staticTexts[text].tap()
        }
        
        // MARK: - Then
        
        // Scenario 1
        Then(#"^I click on each tabBarItem and confirm its position$"#) { _, _ -> Void in
            for i in 0...9 {
                let tabTitle = "Tab\(i + 1)"
                let positionText = "Tab position \(i)"
                self.application.staticTexts[tabTitle].tap()
                XCTAssertTrue(self.application.staticTexts[positionText].exists)
            }
        }

        // Scenario 2
        Then(#"^the tab with text "([^\"]*)" must be on screen$"#) { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
        
        // Scenario 3
        Then(#"^the tab position should have its text changed to "([^\"]*)"$"#) { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
        
        // Scenario 4, 6
        Then(#"^check tab with "([^\"]*)" icon is on screen$"#) { args, _ -> Void in
            let icon = args![0]
            XCTAssertTrue(self.application.images[icon].exists)
        }

        // Scenario 5
        Then(#"^check tab with text "([^\"]*)" and "([^\"]*)" icon are on screen$"#) { args, _ -> Void in
            let text = args![0]
            let icon = args![1]
            XCTAssertTrue(self.application.staticTexts[text].exists)
            XCTAssertTrue(self.application.images[icon].exists)
        }
        
        // Scenario 6
        Then(#"^the tab with text "([^\"]*)" and "([^\"]*)" icon will change icon to "([^\"]*)" icon$"#) { args, _ -> Void in
            let text = args![0]
            let changedIcon = args![2]
            XCTAssertTrue(self.application.staticTexts[text].exists)
            XCTAssertTrue(self.application.images[changedIcon].exists)
        }

    }

    private enum TabBarScreenElements: String {
        case SCREEN_HEADER = "TabBar Screen"
        
        var element: XCUIElement {
            return XCUIApplication().staticTexts[self.rawValue]
        }
    }
}
