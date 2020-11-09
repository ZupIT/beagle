//
//  AddChildrenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Victor on 15/10/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class AddChildrenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
        
        let screen = ScreenRobot()
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("addChildren") ?? false {
                let url = "http://localhost:8080/add-children"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^that I'm on the addChildren Screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.ADD_CHILDREN_HEADER.element.exists)
        }
        
        When("^I click on button \"([^\\\"]*)\"$") { args, _ -> Void in
            guard let button: String = args?[0],
                  let element = ScreenElements(rawValue: button)
                  else {
                XCTFail("button element not found")
                return
            }
            screen.clickOnButton(button: element)
        }
        
        Then("^A Text need to be added after the already exist one$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TEXT_ADDED.element.exists)
        }
        
        Then("^A Text need to be added before the already exist one$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TEXT_FIXED.element.exists)
        }
        
        Then("^A Text need to replace the already exist one$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TEXT_ADDED.element.exists)
            XCTAssertFalse(ScreenElements.TEXT_FIXED.element.exists)
        }
        
        Then("^Nothing should happen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TEXT_FIXED.element.exists)
            XCTAssertFalse(ScreenElements.TEXT_ADDED.element.exists)
        }
        
    }
    
}
