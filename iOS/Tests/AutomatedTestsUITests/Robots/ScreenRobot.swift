//
//  ButtonScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/20/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation

public class ScreenRobot {
    
    
    func checkViewContainsHeader() {
        CucumberishInitializer.waitForElementToAppear(ScreenElements.MAIN_HEADER.element)
        XCTAssertTrue(ScreenElements.MAIN_HEADER.element.exists)
    }
    
    func renderTextAttributeCorrectly() {
        XCTAssertTrue(ScreenElements.BUTTON_DEFAULT.element.exists)
        XCTAssertTrue(ScreenElements.BUTTON_WITH_STYLE.element.exists)
        XCTAssertTrue(ScreenElements.BUTTON_WITH_APPEARANCE.element.exists)
        XCTAssertTrue(ScreenElements.BUTTON_WITH_APPEARANCE_AND_STYLE.element.exists)
    }
        
    func renderActionAttributeCorrectly() {
        XCTAssertTrue(ScreenElements.ACTION_CLICK_HEADER.element.exists)
        XCTAssertTrue(ScreenElements.ACTION_CLICK_TEXT.element.exists)
    }
    
    func clickOnText(textOption: ScreenElements) {
        textOption.element.tap()
}
    
    func selectedTextIsPresented(selectedText1: String, selectedText2: String) {
        XCTAssertTrue(XCUIApplication().staticTexts[selectedText1].exists)
        XCTAssertTrue(XCUIApplication().staticTexts[selectedText2].exists)
    }

    
    func clickOnButton(button: ScreenElements) {
        CucumberishInitializer.waitForElementToAppear(ScreenElements.BUTTON_SCREEN_HEADER.element)
        button.element.tap()
    }
    
}
