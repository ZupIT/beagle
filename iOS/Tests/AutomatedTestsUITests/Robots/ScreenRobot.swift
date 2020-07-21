//
//  ButtonScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/20/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation

public class ScreenRobot {
    
    
    func checkViewContainsText() {
        CucumberishInitializer.waitForElementToAppear(ScreenElements.MAIN_HEADER.element)
    }
    
    func clickOnButton() {
        ScreenElements.BUTTON_DEFAULT_TEXT.element.tap()
    }
    
    func clickOnButtonWithStyle() {
        ScreenElements.BUTTON_WITH_STYLE_TEXT.element.tap()
    }
    
    func clickOnButtonWithAction() {
        ScreenElements.BUTTON_DEFAULT_TEXT.element.tap()
    }
    
    func renderTextAttributeCorrectly() {
        XCTAssertTrue(ScreenElements.BUTTON_DEFAULT_TEXT.element.exists)
        XCTAssertTrue(ScreenElements.BUTTON_WITH_STYLE_TEXT.element.exists)
        XCTAssertTrue(ScreenElements.BUTTON_WITH_APPEARANCE_TEXT.element.exists)
    }
        
    func renderActionAttributeCorrectly() {
        XCTAssertTrue(ScreenElements.MAIN_HEADER.element.exists)
        XCTAssertTrue(ScreenElements.ACTION_CLICK_TEXT_1.element.exists)
        XCTAssertTrue(ScreenElements.ACTION_CLICK_TEXT_2.element.exists)
    }
    
    func checkTabViewRendersTabs() {
        XCTAssertTrue(ScreenElements.TAB_1.element.exists)
        XCTAssertTrue(ScreenElements.TAB_1_TEXT.element.exists)
        XCUIApplication().swipeLeft()

        XCTAssertTrue(ScreenElements.TAB_2.element.exists)
        XCTAssertTrue(ScreenElements.TAB_2_TEXT.element.exists)
        XCUIApplication().swipeLeft()

        XCTAssertTrue(ScreenElements.TAB_3.element.exists)
        XCTAssertTrue(ScreenElements.TAB_3_TEXT.element.exists)
        XCUIApplication().swipeLeft()
            
        XCTAssertTrue(ScreenElements.TAB_4.element.exists)
        XCTAssertTrue(ScreenElements.TAB_4_TEXT.element.exists)
        
        XCUIApplication().swipeRight()
        XCUIApplication().swipeRight()
        XCUIApplication().swipeRight()

    }
    
    func clickOnText(textOption: ScreenElements) {
        textOption.element.tap()
}
    
    func selectedTextIsPresented(selectedText1: String, selectedText2: String) {
        XCTAssertTrue(XCUIApplication().staticTexts[selectedText1].exists)
        XCTAssertTrue(XCUIApplication().staticTexts[selectedText2].exists)
    }

}
