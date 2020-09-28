//
//  TabViewScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/21/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class TabViewScreenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
    
        let screen = ScreenRobot()
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("tabview") ?? false {
                let url = "http://localhost:8080/tabview"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^the app did load tabview screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TAB_1.element.exists)
        }

        Then("^my tabview components should render their respective tabs attributes correctly$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.TAB_1.element.exists)
            XCTAssertTrue(ScreenElements.TAB_1_TEXT.element.exists)
            XCTAssertTrue(ScreenElements.TAB_1_TEXT_2.element.exists)
            self.application?.swipeLeft()

            XCTAssertTrue(ScreenElements.TAB_2.element.exists)
            XCTAssertTrue(ScreenElements.TAB_2_TEXT.element.exists)
            XCTAssertTrue(ScreenElements.TAB_2_TEXT_2.element.exists)
            self.application?.swipeLeft()

            XCTAssertTrue(ScreenElements.TAB_3.element.exists)
            XCTAssertTrue(ScreenElements.TAB_3_TEXT.element.exists)
            XCTAssertTrue(ScreenElements.TAB_3_TEXT_2.element.exists)
            self.application?.swipeLeft()
                
            XCTAssertTrue(ScreenElements.TAB_4.element.exists)
            XCTAssertTrue(ScreenElements.TAB_4_TEXT.element.exists)
            XCTAssertTrue(ScreenElements.TAB_4_TEXT_2.element.exists)
            
            self.application?.swipeRight()
            self.application?.swipeRight()
            self.application?.swipeRight()
           
        }

        When("^I click on \"([^\\\"]*)\"$") { args, _ -> Void in
            guard let param = args?[0],
                  let text: ScreenElements = ScreenElements(rawValue: param) else {
                return
            }
            screen.clickOnText(textOption: text)
        }

        Then("^my tab should render the text \"([^\\\"]*)\" and \"([^\\\"]*)\" correctly$") { args, _ -> Void in
            guard let text1: String = (args?[0]),
                  let text2: String = (args?[1]) else {
                return
            }

            screen.selectedTextIsPresented(selectedText1: text1, selectedText2: text2)
        }
        
    }
}
