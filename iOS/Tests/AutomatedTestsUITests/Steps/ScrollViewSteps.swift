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

class ScrollViewSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    let device = XCUIDevice.shared
    
    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("scrollview") ?? false {
                let url = "http://localhost:8080/scrollview"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        Given("^the Beagle application did launch with the scrollview screen url$") { _, _ -> Void in
            XCTAssertTrue(self.application.staticTexts[Seeds.screenTitle].exists)
        }
        
        // MARK: - When
        
        //Scenario 1
        When(#"^I change the screen orientation to "([^\"]*)" and I press on "([^\"]*)" scrollable text$"#) { args, _ -> Void in
            let orientation = args![0]
            let text = args![1]
            self.device.orientation = orientation == Seeds.horizontalOrientation ? .portrait : .landscapeRight
            self.application.staticTexts[text].tap()
        }
        
        //Scenario 2
        When(#"^I press on text "([^\"]*)" of scrollview$"#) { args, _ -> Void in
            let text = args![0]
            self.application.staticTexts[text].tap()
        }
        
        // MARK: - Then
        
        // Scenario 1, 2
        Then(#"^the current text "([^\"]*)" should be replaced for a large text and It should scroll to the "([^\"]*)" button for tapping it$"#) { args, _ -> Void in
            let text = args![0]
            let buttonTitle = args![1]            
            XCTAssertFalse(self.application.staticTexts[text].exists)
            
            let predicate = NSPredicate(format: "label BEGINSWITH[c] %@", "Lorem Ipsum")
            let element = self.application.staticTexts.containing(predicate).element
            XCTAssertTrue(element.exists)
            
            let button = self.application.buttons[buttonTitle]
            XCTAssertFalse(button.isHittable)
            
            button.tap()
            XCTAssertTrue(button.isHittable)
        }
        
        // MARK: - After
        after { _ in
            self.device.orientation = .portrait
        }
    }
}

// MARK: - Helpers
fileprivate struct Seeds {
    static let screenTitle = "Beagle ScrollView"
    static let horizontalOrientation = "horizontal"
    static let verticalOrientation = "vertical"
}
