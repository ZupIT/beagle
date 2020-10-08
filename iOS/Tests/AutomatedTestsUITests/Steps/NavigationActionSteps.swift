//
//  NavigationActionSteps.swift
//  AutomatedTestsUITests
//
//  Created by Lucas Cesar on 05/10/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class NavigationActionSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    
    func loadSteps() {        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("navigation") ?? false {
                let url = "http://localhost:8080/navigate-actions"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        // MARK: - Given
        
        Given("^the Beagle application did launch with the navigation screen url$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.NAVIGATION_SCREEN_TITLE.element.exists)
        }
        
        // MARK: - When
        
        /// Scenarios 1 and 2
        When("^I press a navigation button \"([^\\\"]*)\"$") { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }
        
        /// Scenarios 3 and 4
        When("^I navigate to another screen and I press a button with the \"([^\\\"]*)\" action$") { args, _ -> Void in
            ScreenElements.PUSH_VIEW_REMOTE_BUTTON.element.tap()
            XCTAssertTrue(ScreenElements.SAMPLE_NAVIGATION_SCREEN_TITLE.element.exists)
            let text = args![0]
            self.application.buttons[text].tap()
        }
        
        // MARK: - Then
        
        /// Scenario 1
        Then("^the screen should navigate to another screen with the text label \"([^\\\"]*)\"$") { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
        
        /// Scenario 2
        Then("^the screen should not navigate to another screen with the text label \"([^\\\"]*)\"$") { args, _ -> Void in
            let text = args![0]
            XCTAssertFalse(self.application.staticTexts[text].exists)
        }
        
        /// Scenario 3
        Then("^the app should dismiss the current view$") { _, _ -> Void in
            XCTAssertFalse(ScreenElements.SAMPLE_NAVIGATION_SCREEN_TITLE.element.exists)
        }

        /// Scenario 4
        Then("^the application should navigate back to a specific screen and remove from the stack the other screens loaded from the current screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.NAVIGATION_SCREEN_TITLE.element.exists)
            XCTAssertFalse(ScreenElements.SAMPLE_NAVIGATION_SCREEN_TITLE.element.exists)
        }
        
        /// Scenario 5
        Then("^the app should cleans up the entire stack$") { _, _ -> Void in
            XCTAssertEqual(self.application.state, XCUIApplication.State.runningForeground)
        }
    }
}
