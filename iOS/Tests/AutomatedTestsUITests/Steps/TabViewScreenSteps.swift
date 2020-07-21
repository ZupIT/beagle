//
//  TabViewScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/21/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class TabViewScreenSteps: NSObject {
    
    func TabViewScreenSteps() {
    
        let screen = ScreenRobot()
                
        MatchAll("^App is running$") { (args, userInfo) -> Void in
            screen.checkViewContainsHeader()
        }
        
        Given("^Given the app will load http://localhost:8080/tabview$") { (args, userInfo) -> Void in
            screen.checkViewContainsHeader()
            XCTAssertTrue(ScreenElements.TABVIEW_SCREEN_HEADER.element.exists)
        }
        

        Then("^my tabview components should render their respective tabs attributes correctly$")  { (args, userInfo) -> Void in
            XCTAssertTrue(ScreenElements.TAB_1.element.exists)
            XCTAssertTrue(ScreenElements.TAB_1_TEXT.element.exists)
            XCTAssertTrue(ScreenElements.TAB_1_TEXT_2.element.exists)
            XCUIApplication().swipeLeft()

            XCTAssertTrue(ScreenElements.TAB_2.element.exists)
            XCTAssertTrue(ScreenElements.TAB_2_TEXT.element.exists)
            XCTAssertTrue(ScreenElements.TAB_2_TEXT_2.element.exists)
            XCUIApplication().swipeLeft()

            XCTAssertTrue(ScreenElements.TAB_3.element.exists)
            XCTAssertTrue(ScreenElements.TAB_3_TEXT.element.exists)
            XCTAssertTrue(ScreenElements.TAB_3_TEXT_2.element.exists)
            XCUIApplication().swipeLeft()
                
            XCTAssertTrue(ScreenElements.TAB_4.element.exists)
            XCTAssertTrue(ScreenElements.TAB_4_TEXT.element.exists)
            XCTAssertTrue(ScreenElements.TAB_4_TEXT_2.element.exists)
            
            XCUIApplication().swipeRight()
            XCUIApplication().swipeRight()
            XCUIApplication().swipeRight()
           
        }

        When("^I click on \"([^\\\"]*)\"$")  { (args, userInfo) -> Void in
           let text:ScreenElements = ScreenElements(rawValue: (args?[0])!)!
            screen.clickOnText(textOption: text)
           }

        Then("^my tab should render the text \"([^\\\"]*)\" and \"([^\\\"]*)\" correctly$")  { (args, userInfo) -> Void in
            let text1: String = (args?[0])!
            let text2: String = (args?[1])!

            screen.selectedTextIsPresented(selectedText1: text1, selectedText2: text2)
        }
        
    }
}

