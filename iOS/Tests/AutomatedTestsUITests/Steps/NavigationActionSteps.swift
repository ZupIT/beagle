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
        // MARK: - Before
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
        
        // Scenarios 1 and 2
        When("^I press a navigation button \"([^\\\"]*)\"$") { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }
        
        // Scenarios 3, 4, 5, 6 and 7
        When("^I navigate to another screen using the \"([^\\\"]*)\" action and I press a button with the \"([^\\\"]*)\" action$") { args, _ -> Void in
            let text1 = args![0]
            self.application.buttons[text1].tap()
            XCTAssertTrue(ScreenElements.SAMPLE_NAVIGATION_SCREEN_TITLE.element.exists)
            let text2 = args![1]
            self.application.buttons[text2].tap()
        }
        
        // MARK: - Then
        
        // Scenario 1
        Then("^the screen should navigate to another screen with the text label \"([^\\\"]*)\"$") { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
        
        // Scenario 2
        Then("^the screen should not navigate to another screen with the text label \"([^\\\"]*)\"$") { args, _ -> Void in
            let text = args![0]
            XCTAssertFalse(self.application.staticTexts[text].exists)
        }
        
        // Scenario 3
        Then("^the app should dismiss the current view$") { _, _ -> Void in
            XCTAssertFalse(ScreenElements.SAMPLE_NAVIGATION_SCREEN_TITLE.element.exists)
        }
        
        // Scenario 4
        Then("^the app should not dismiss the current view$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SAMPLE_NAVIGATION_SCREEN_TITLE.element.exists)
        }

        // Scenario 5
        Then("^the application should navigate back to a specific screen and remove from the stack the other screens loaded from the current screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.NAVIGATION_SCREEN_TITLE.element.exists)
            XCTAssertFalse(ScreenElements.SAMPLE_NAVIGATION_SCREEN_TITLE.element.exists)
        }

        // Scenario 6
        Then("^the app should navigate to a specified screen and cleans up the entire stack of the previously loaded views$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.RESET_NAVIGATION_SCREEN_TITLE.element.exists)
            XCTAssertFalse(ScreenElements.NAVIGATION_SCREEN_TITLE.element.exists)
            XCTAssertFalse(ScreenElements.SAMPLE_NAVIGATION_SCREEN_TITLE.element.exists)
        }
        
        // Scenario 7
        Then("^the app should clean up the entire stack and the application should enter in the foreground state$") { _, _ -> Void in
            XCTAssertEqual(self.application.state, XCUIApplication.State.runningForeground)
        }
    }
}
