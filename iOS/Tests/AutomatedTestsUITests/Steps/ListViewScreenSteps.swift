//
//  ListViewScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/23/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class ListViewScreenSteps: NSObject {
    
    func ListViewScreenSteps() {
    
        let screen = ScreenRobot()
                
        MatchAll("^App is running$") { (args, userInfo) -> Void in
            screen.checkViewContainsHeader()
        }
        
        Given("^Given the app will load http://localhost:8080/listview$") { (args, userInfo) -> Void in
            screen.checkViewContainsHeader()
            XCTAssertTrue(ScreenElements.LISTVIEW_SCREEN_HEADER.element.exists)
        }
        
        When("I have a vertical list configured$") { (args, userInfo) -> Void in
            XCTAssertTrue(ScreenElements.STATIC_LISTVIEW_TEXT_1.element.exists)
        }
        

         Then("listview screen should render all text attributes correctly$") { (args, userInfo) -> Void in
            XCTAssertTrue(ScreenElements.STATIC_LISTVIEW_TEXT_1.element.exists)
            XCTAssertTrue(ScreenElements.STATIC_LISTVIEW_TEXT_2.element.exists)
            XCTAssertTrue(ScreenElements.DYNAMIC_LISTVIEW_TEXT_1.element.exists)

        }
    
        
    }

}


