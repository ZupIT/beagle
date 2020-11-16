//
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
import XCTest

class SetContextSteps: CucumberStepsDefinition {
    var application: XCUIApplication!
    
    func loadSteps() {
        // MARK: - Before
        before { scenarioDefinition in
            if scenarioDefinition?.tags.contains("setcontext") ?? false {
                let url = "http://localhost:8080/set-context"
                self.application = TestUtils.launchBeagleApplication(url: url)
            }
        }
        
        // MARK: - Given
        Given(#"^the Beagle application did launch with the SetContext screen url$"#) { _, _ -> Void in
            XCTAssertTrue(Seeds.SET_CONTEXT_TITLE.element.exists)
        }
        
        // MARK: - When
        
        // Scenario 01, 02
        When(#"^I press a SetContext button with the "([^\"]*)" title$"#)  { args, _ -> Void in
            let text = args![0]
            self.application.buttons[text].tap()
        }
        
        // MARK: - Then
        
        // Scenario 01, 02
        Then(#"^a text with the "([^\"]*)" message should appear on the screen$"#) { args, _ -> Void in
            let text = args![0]
            XCTAssertTrue(self.application.staticTexts[text].exists)
        }
        
    }
}

fileprivate enum Seeds: String {
    case SET_CONTEXT_TITLE = "SetContext Screen"
    
    case SET_CONTEXT_HARDCODED_VALUE = "ValueHardcoded"
    case SET_CONTEXT_HARDCODED_PATH_VALUE = "ValueHardcodedPath"
    case SET_CONTEXT_EXPRESSION_VALUE = "ValueExpression"
    case SET_CONTEXT_EXPRESSION_PATH_VALUE = "ValueExpressionPath"
    
    case SET_CONTEXT_HARDCODED_BUTTON = "HardcodedValue"
    case SET_CONTEXT_HARDCODED_PATH_BUTTON = "HardcodedPathValue"
    case SET_CONTEXT_EXPRESSION_BUTTON = "ExpressionValue"
    case SET_CONTEXT_EXPRESSION_PATH_BUTTON = "ExpressionPathValue"
    
    var element: XCUIElement {
        switch self {
        case .SET_CONTEXT_TITLE, .SET_CONTEXT_HARDCODED_VALUE, .SET_CONTEXT_HARDCODED_PATH_VALUE, .SET_CONTEXT_EXPRESSION_VALUE, .SET_CONTEXT_EXPRESSION_PATH_VALUE:
            return XCUIApplication().staticTexts[self.rawValue]
        case .SET_CONTEXT_HARDCODED_BUTTON, .SET_CONTEXT_HARDCODED_PATH_BUTTON, .SET_CONTEXT_EXPRESSION_BUTTON, .SET_CONTEXT_EXPRESSION_PATH_BUTTON:
            return XCUIApplication().buttons[self.rawValue]
        }
    }
}
