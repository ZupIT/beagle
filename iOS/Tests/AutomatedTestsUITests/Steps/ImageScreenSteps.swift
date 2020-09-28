//
//  ImageScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/21/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class ImageScreenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
  
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("image") ?? false {
                let url = "http://localhost:8080/image"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^the app did load image screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.IMAGE_SCREEN_HEADER.element.exists)
        }

        Then("^image screen should render all text attributes correctly$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.IMAGE_TEXT_1.element.exists)
            XCTAssertTrue(ScreenElements.IMAGE_TEXT_2.element.exists)
            XCTAssertTrue(ScreenElements.IMAGE_TEXT_3.element.exists)
            XCTAssertTrue(ScreenElements.IMAGE_TEXT_4.element.exists)
            XCTAssertTrue(ScreenElements.IMAGE_TEXT_5.element.exists)
        }
    }
}
