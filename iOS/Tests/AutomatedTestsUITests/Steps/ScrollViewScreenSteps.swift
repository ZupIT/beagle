//
//  ScrollViewScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/23/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class ScrollViewScreenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
    
        let screen = ScreenRobot()
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("scrollView") ?? false {
                let url = "http://localhost:8080/scrollView"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
                
        Given("^that I'm on the scrollview screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SCROLLVIEW_SCREEN_HEADER.element.exists)
        }
        
        //ScrollView 01
        When("^I press on text scroll horizontal \"([^\\\"]*)\"$") { args, _ -> Void in
            guard let text: String = args?[0],
                  let element = ScreenElements(rawValue: text)
                  else {
                XCTFail("text element not found")
                return
            }
            screen.clickOnText(textOption: element)
            screen.clickOnText(textOption: element)
            
        }
        
        Then("^the text should change for the next and the scrollview should perform horizontally \"([^\\\"]*)\"$") { _, _ -> Void in
            XCTest.accessibilityScroll(UIAccessibilityScrollDirection.right)
            XCTAssertTrue(ScreenElements.SCROLLVIEW_ELEMENTS_1.element.exists)
        }
        
       
       
        
    }
}
