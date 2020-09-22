//
//  ButtonScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/20/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class ButtonScreenSteps: CucumberStepsDefinition {
    
    var application : XCUIApplication!
    
    func loadSteps() {
    
        let screen = ScreenRobot()
        
        before { (scenarioDefinition) in
            if scenarioDefinition?.tags.contains("button") ?? false {
                let url = "http://localhost:8080/button"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
                
        Given("^the app did load buttons screen$") { (args, userInfo) -> Void in
            XCTAssertTrue(ScreenElements.BUTTON_SCREEN_HEADER.element.exists)
        }

        When("I click on button \"([^\\\"]*)\"$") { (args, userInfo) -> Void in
            let button: String = (args?[0])!
            screen.clickOnButton(button: ScreenElements(rawValue: button)!)
        }

         Then("all my button components should render their respective text attributes correctly$") { (args, userInfo) -> Void in
            screen.renderTextAttributeCorrectly()
         }

         Then("component should render the action attribute correctly$") { (args, userInfo) -> Void in
            screen.renderActionAttributeCorrectly()
         }
        
    }

}

