//
//  ConditionalActionScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Caio Ortu on 10/14/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class ConditionalActionScreenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication!
    
    func loadSteps() {
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("conditionalaction") ?? false {
                let url = "http://localhost:8080/conditional-action"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        
        Given("^the Beagle application did launch with the conditional action screen url$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.CONDITIONAL_ACTION_SCREEN_HEADER.element.exists)
        }
        
        // MARK: - When
        
        When("^I press the \"([^\\\"]*)\" button$") { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }

        // MARK: - Then
        
        Then("^the screen should show some alert with \"([^\\\"]*)\" title$") { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.alerts.element.staticTexts[text].exists)
        }
    }
}
