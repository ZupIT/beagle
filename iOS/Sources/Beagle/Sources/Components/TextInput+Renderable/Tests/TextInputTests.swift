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

import XCTest
@testable import Beagle
import BeagleSchema

class TextInputTests: XCTestCase {

    private lazy var theme = AppTheme(styles: [
        "test.textInput.style": textStyle
    ])
    
    private func textStyle() -> (UITextField?) -> Void {
        return {
            $0?.textColor = .orange
            $0?.borderStyle = .roundedRect
        }
    }

    private lazy var dependencies = BeagleScreenDependencies(
        theme: theme
    )

    private lazy var controller = BeagleControllerStub(dependencies: dependencies)
    private lazy var renderer = BeagleRenderer(controller: controller)
    
    func testTextInputTypes() throws {
        // Given
        let component = TextInput(value: "", placeholder: "", type: .value(.password))
        
        // When
        guard let textField = renderer.render(component) as? UITextField else {
            XCTFail("Unable to type cast to UITextField.")
            return
        }
        let inputType = inputTypeToKeyboardType(component.type?.evaluate(with: textField))
        
        // Then
        XCTAssertEqual(textField.keyboardType, inputType)
    }

    func test_renderTextInputComponent() throws {
        let textInput = TextInput(
            value: "",
            placeholder: "password",
            disabled: .value(false),
            readOnly: .value(false),
            type: .value(.password),
            hidden: .value(false),
            styleId: "test.textInput.style",
            onChange: [ActionDummy()],
            onBlur: [ActionDummy()],
            onFocus: [ActionDummy()],
            widgetProperties: WidgetProperties(style: .init(size: Size().width(300).height(80))))
        
        let view = renderer.render(textInput)
        
        assertSnapshotImage(view, size: .custom(CGSize(width: 300, height: 80)))
    }
    
    func test_textInputComponent_whenTextValueChanges() throws {
        let textInput = TextInput(
            value: "",
            placeholder: "type here",
            disabled: .value(false),
            readOnly: .value(false),
            type: .value(.text),
            hidden: .value(false),
            styleId: "test.textInput.style",
            onChange: [ActionDummy()],
            onBlur: [ActionDummy()],
            onFocus: [ActionDummy()],
            widgetProperties: WidgetProperties(style: .init(size: Size().width(300).height(80))))
        
        guard let textField = renderer.render(textInput) as? UITextField else {
            XCTFail("Unable to type cast to UITextField.")
            return
        }
        textField.text = "new value"
        
        assertSnapshotImage(textField, size: .custom(CGSize(width: 300, height: 80)))
    }

    private func inputTypeToKeyboardType(_ inputType: TextInputType?) -> UIKeyboardType {
        guard let inputType = inputType else { return .default }
        switch inputType {
        case .date, .number: return .numberPad
        case .email: return .emailAddress
        case .password, .text: return .default
        }
    }
}
