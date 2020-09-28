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
    
    var application: XCUIApplication?
    
    func loadSteps() {
    
        let screen = ScreenRobot()
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("button") ?? false {
                let url = "http://localhost:8080/button"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
                
        Given("^the app did load buttons screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.BUTTON_SCREEN_HEADER.element.exists)
        }

        When("I click on button \"([^\\\"]*)\"$") { args, _ -> Void in
            guard let button: String = args?[0],
                  let element = ScreenElements(rawValue: button)
                  else {
                XCTFail("button element not found")
                return
            }
            screen.clickOnButton(button: element)
        }

         Then("all my button components should render their respective text attributes correctly$") { _, _ -> Void in
            screen.renderTextAttributeCorrectly()
         }

         Then("component should render the action attribute correctly$") { _, _ -> Void in
            screen.renderActionAttributeCorrectly()
         }
        
    }

}
