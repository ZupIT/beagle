//
//  ConditionalActionSteps.swift
//  AutomatedTestsUITests
//
//  Created by Caio Ortu on 10/14/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

// swiftlint:disable implicitly_unwrapped_optional
// swiftlint:disable force_unwrapping
class ConditionalActionSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication!
    
    func loadSteps() {
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("conditional") ?? false {
                let url = "http://localhost:8080/conditional"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        
        Given(#"^the Beagle application did launch with the conditional screen url$"#) { _, _ -> Void in
            XCTAssertTrue(ScreenElements.CONDITIONAL_ACTION_SCREEN_HEADER.element.exists)
        }
        
        // MARK: - When
        
        // Scenario 1 and 2, 3, 4
        When(#"^I click in a conditional button with "([^\"]*)" title$"#) { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }

        // MARK: - Then
        
        // Scenario 1 and 2, 3, 4
        Then(#"^an Alert action should pop up with a "([^\"]*)" message$"#) { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.alerts.element.staticTexts[text].exists)
        }
    }
}
