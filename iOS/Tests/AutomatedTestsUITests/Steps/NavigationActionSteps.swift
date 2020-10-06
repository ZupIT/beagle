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
        
        Given("^the Beagle application did launch with the navigation screen url$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.NAVIGATION_SCREEN_TITLE.element.exists)
        }
        
        When("^I click on a navigate button \"([^\\\"]*)\"$") { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }
        
        Then("^the screen should navigate to another screen with text label \"([^\\\"]*)\"$") { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
        
        Then("^the screen should not navigate to another screen with text label \"([^\\\"]*)\"$") { args, _ -> Void in
            let text = args![0]
            XCTAssertFalse(self.application.staticTexts[text].exists)
        }
    }
}
