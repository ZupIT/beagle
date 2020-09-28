//
//  ListViewScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/23/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class ListViewScreenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("listview") ?? false {
                let url = "http://localhost:8080/listview"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^the app did load listview screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.LISTVIEW_SCREEN_HEADER.element.exists)
        }
        
        When("^I have a vertical list configured$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.STATIC_LISTVIEW_TEXT_1.element.exists)
        }
        
        Then("^listview screen should render all text attributes correctly$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.STATIC_LISTVIEW_TEXT_1.element.exists)
            XCTAssertTrue(ScreenElements.STATIC_LISTVIEW_TEXT_2.element.exists)
            XCTAssertTrue(ScreenElements.DYNAMIC_LISTVIEW_TEXT_1.element.exists)
        }
    
        Then("^listview screen should perform the scroll action vertically$") { _, _ -> Void in
            XCUIApplication().scrollToElement(element: ScreenElements.DYNAMIC_LISTVIEW_TEXT_2.element)
        }
        
    }

}
