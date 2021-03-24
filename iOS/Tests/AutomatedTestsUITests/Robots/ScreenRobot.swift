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

    func clickOnText(textOption: ScreenElements) {
        textOption.element.tap()
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
    
}
