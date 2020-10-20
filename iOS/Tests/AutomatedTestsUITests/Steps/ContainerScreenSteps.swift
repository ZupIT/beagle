//
//  ContainerScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by renata.andrade on 19/10/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class ContainerScreenSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    
    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("container") ?? false {
                let url = "http://localhost:8080/container-test"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        Given("^the Beagle application did launch with the container screen url$") { _, _ -> Void in
            XCTAssertTrue(ScreenElements.CONTAINER_SCREEN_TITLE.element.exists)
        }
        
        // MARK: - When

        
        // MARK: - Then
        
        // Scenario 1
        Then("^the screen should contain three texts: \"([^\\\"]*)\", \"([^\\\"]*)\" and \"([^\\\"]*)\"$") { args, _ -> Void in
            let texts = [args![0], args![1], args![2]]
            for text in texts {
                XCTAssertTrue(self.application.staticTexts[text].exists)
            }
        }
    }
}
