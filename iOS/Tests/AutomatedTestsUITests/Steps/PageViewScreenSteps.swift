//
//  PageViewScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/21/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class PageViewScreenSteps: NSObject {
    
    func PageViewScreenSteps() {
    
        let screen = ScreenRobot()
                
        MatchAll("^App is running$") { (args, userInfo) -> Void in
            XCTAssertTrue(ScreenElements.MAIN_HEADER.element.exists)
        }
        
        Given("^Given the app will load http://localhost:8080/pageview$") { (args, userInfo) -> Void in
            screen.checkViewContainsHeader()
            XCTAssertTrue(ScreenElements.PAGEVIEW_SCREEN_HEADER.element.exists)

        }

        Then("^my pageview components should render their respective pages attributes correctly$") { (args, userInfo) -> Void in
            XCTAssertTrue(ScreenElements.PAGE_1_TEXT.element.exists)
            XCUIApplication().swipeLeft()
            XCTAssertTrue(ScreenElements.PAGE_2_TEXT.element.exists)
            XCUIApplication().swipeLeft()
            XCTAssertTrue(ScreenElements.PAGE_3_TEXT.element.exists)
            
            XCUIApplication().swipeRight()
            XCUIApplication().swipeRight()



        }
    }
}
