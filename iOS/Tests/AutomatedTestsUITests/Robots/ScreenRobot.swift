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
    
    func clickOnButton() {
        CucumberishInitializer.waitForElementToAppear(ScreenElements.BUTTON_SCREEN_HEADER.element)
        ScreenElements.BUTTON_DEFAULT_TEXT.element.tap()
    }
    
    func clickOnButtonWithStyle() {
        CucumberishInitializer.waitForElementToAppear(ScreenElements.BUTTON_SCREEN_HEADER.element)
        ScreenElements.BUTTON_WITH_STYLE_TEXT.element.tap()
    }
    
    func clickOnButtonWithAction() {
        CucumberishInitializer.waitForElementToAppear(ScreenElements.BUTTON_SCREEN_HEADER.element)
        ScreenElements.BUTTON_DEFAULT_TEXT.element.tap()
    }
    
    func renderTextAttributeCorrectly() {
        XCTAssertTrue(ScreenElements.BUTTON_DEFAULT_TEXT.element.exists)
        XCTAssertTrue(ScreenElements.BUTTON_WITH_STYLE_TEXT.element.exists)
        XCTAssertTrue(ScreenElements.BUTTON_WITH_APPEARANCE_TEXT.element.exists)
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
