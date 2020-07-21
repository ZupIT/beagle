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
            XCTAssertTrue(ScreenElements.MAIN_HEADER.element.exists)
        }
        
        Given("^Given the app will load http://localhost:8080/tabview$") { (args, userInfo) -> Void in
            XCTAssertTrue(ScreenElements.MAIN_HEADER.element.exists)
            XCTAssertTrue(ScreenElements.TABVIEW_SCREEN_HEADER.element.exists)
        }
        

        Then("^my tabview components should render their respective tabs attributes correctly$")  { (args, userInfo) -> Void in
            screen.checkTabViewRendersTabs()
           
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

