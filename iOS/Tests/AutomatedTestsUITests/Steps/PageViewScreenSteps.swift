//
//  PageViewScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/21/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class PageViewScreenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
    
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("pageview") ?? false {
                let url = "http://localhost:8080/pageview"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^the app did load pageview screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.PAGEVIEW_SCREEN_HEADER.element.exists)
        }

        Then("^pageview should render correctly$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.PAGE_1_TEXT.element.exists)
            self.application?.swipeLeft()
            XCTAssertTrue(ScreenElements.PAGE_2_TEXT.element.exists)
            self.application?.swipeLeft()
            XCTAssertTrue(ScreenElements.PAGE_3_TEXT.element.exists)
            
            self.application?.swipeRight()
            self.application?.swipeRight()

        }
    }
}
