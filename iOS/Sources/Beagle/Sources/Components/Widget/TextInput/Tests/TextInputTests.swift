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
import SnapshotTesting
@testable import Beagle

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
    
    func test_whenDecodingJson_shouldReturnAText() throws {
        let component: TextInput = try componentFromJsonFile(fileName: "TextInputComponent")
        assertSnapshot(matching: component, as: .dump)
    }
    
    func testTextInputTypes() {
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

    func test_renderTextInputComponent() {
        // Given
        let textInput = TextInput(
            value: "",
            placeholder: "password",
            type: .value(.password),
            styleId: "test.textInput.style",
            widgetProperties: WidgetProperties(style: .init(size: Size().width(300).height(80))))
        
        // When
        let view = renderer.render(textInput)
        
        // Then
        assertSnapshotImage(view, size: .custom(CGSize(width: 300, height: 80)))
    }
    
    func test_textInputComponent_whenTextValueChanges() {
        // Given
        let textInput = TextInput(
            value: "",
            placeholder: "type here",
            type: .value(.text),
            styleId: "test.textInput.style",
            widgetProperties: WidgetProperties(style: .init(size: Size().width(300).height(80))))
        
        guard let textField = renderer.render(textInput) as? UITextField else {
            XCTFail("Unable to type cast to UITextField.")
            return
        }
        
        // When
        textField.text = "new value"
        
        // Then
        assertSnapshotImage(textField, size: .custom(CGSize(width: 300, height: 80)))
    }
    
    func test_renderTextInputWithValidationComponent() {
        // Given
        let textInput = TextInput(
            value: "k",
            placeholder: "password",
            type: .value(.password),
            error: .value("Password must have 6 characters."),
            showError: .value(true),
            widgetProperties: WidgetProperties(style: .init(size: Size().width(300).height(50)))
        )
                
        // When
        let controller = BeagleScreenViewController(viewModel: .init(screenType: .declarative(textInput.toScreen()), dependencies: dependencies))
        
        // Then
        assertSnapshotImage(controller.view, size: ImageSize.custom(CGSize(width: 300, height: 70)))
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
