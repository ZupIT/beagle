//
//  SimpleFormScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 8/3/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class SimpleFormScreenSteps: NSObject {
    
    func SimpleFormScreenSteps() {
    
        let screen = ScreenRobot()
                
        MatchAll("^App is running$") { (args, userInfo) -> Void in
            screen.checkViewContainsHeader()
        }
        
        Given("^Given the app will load http://localhost:8080/simpleform$") { (args, userInfo) -> Void in
            screen.checkViewContainsHeader()
            XCTAssertTrue(ScreenElements.SIMPLE_FORM_SCREEN_HEADER.element.exists)
        }

        When("^I click on text field \"([^\\\"]*)\"$") { (args, userInfo) -> Void in
            let text:ScreenElements = ScreenElements(rawValue: (args?[0])!)!
            screen.clickOnText(textOption: text)
        }

        And("^insert text \"([^\\\"]*)\"$") { (args, userInfo) -> Void in
            let text: String = (args?[0])!
            screen.typeTextIntoField(insertText: text)
        }
        
        
        Then("^all my simple form components should render their respective text attributes correctly$") { (args, userInfo) -> Void in
            XCTAssertTrue(ScreenElements.SIMPLE_FORM_SCREEN_HEADER.element.exists)
            XCTAssertTrue(ScreenElements.ZIP_FIELD.element.exists)
            XCTAssertTrue(ScreenElements.STREET_FIELD.element.exists)

        }
        
        Then("confirm popup should appear correctly$") { (args, userInfo) -> Void in
            screen.confirmPopupCorrectly()
         }
        
    }

}


