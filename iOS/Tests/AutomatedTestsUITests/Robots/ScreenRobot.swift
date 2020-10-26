/*
* Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

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
    
    func typeTextIntoField(insertText: String) {
        ScreenElements.ZIP_FIELD.element.typeText(insertText)
    }
    
    func selectedTextIsPresented(selectedText1: String, selectedText2: String) {
        XCTAssertTrue(XCUIApplication().staticTexts[selectedText1].exists)
        XCTAssertTrue(XCUIApplication().staticTexts[selectedText2].exists)
    }
    
    func isPresenting(text: String) {
        XCTAssertTrue(XCUIApplication().staticTexts[text].exists)
    }
    
    func clickOnButton(button: ScreenElements) {
        button.element.tap()
    }
    
    func confirmPopupCorrectly() {
        XCTAssertTrue(ScreenElements.OK_BUTTON.element.exists)
        XCTAssertTrue(ScreenElements.CANCEL_BUTTON.element.exists)
    }
    
}
