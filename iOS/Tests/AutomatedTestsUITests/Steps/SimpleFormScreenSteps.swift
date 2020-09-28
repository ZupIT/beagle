//
//  SimpleFormScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 8/3/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class SimpleFormScreenSteps: CucumberStepsDefinition {
    
    var application: XCUIApplication?
    
    func loadSteps() {
        
        let screen = ScreenRobot()
        
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("simpleform") ?? false {
                let url = "http://localhost:8080/simpleform"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        Given("^the app did load simpleform screen$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SIMPLE_FORM_SCREEN_HEADER.element.exists)
        }

        When("^I click on text field \"([^\\\"]*)\"$") { args, _ -> Void in
            guard let param = args?[0],
                  let text: ScreenElements = ScreenElements(rawValue: param) else {
                return
            }
            screen.clickOnText(textOption: text)
        }

        When("^insert text \"([^\\\"]*)\"$") { args, _ -> Void in
            guard let text: String = (args?[0]) else { return }
            screen.typeTextIntoField(insertText: text)
        }
        
        Then("^all my simple form components should render their respective text attributes correctly$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.SIMPLE_FORM_TITLE.element.exists)
            XCTAssertTrue(ScreenElements.ZIP_FIELD.element.exists)
            XCTAssertTrue(ScreenElements.STREET_FIELD.element.exists)

        }
        
        Then("confirm popup should appear correctly$") { _, _ -> Void in
            screen.confirmPopupCorrectly()
        }
        
    }

}
