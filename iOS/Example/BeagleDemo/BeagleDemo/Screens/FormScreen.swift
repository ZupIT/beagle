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

import UIKit
import BeagleUI
import BeagleSchema

struct FormScreen: DeeplinkScreen {
    static var textValidatorName: String { return "text-is-not-blank" }
    static var textValidator: (Any) -> Bool {
        return {
            let trimmed = ($0 as? String)?.trimmingCharacters(in: CharacterSet.whitespacesAndNewlines) ?? ""
            return !trimmed.isEmpty
        }
    }
    
    init() {}
    
    init(path: String, data: [String: String]?) {}
    
    func screenController() -> UIViewController {
        let flexHorizontalMargin = Flex().margin(EdgeValue().all(10))
        let form = Form(
            action: FormRemoteAction(path: .TEXT_FORM_ENDPOINT, method: .post),
            child: Container(
                children: [
                    FormInput(
                        name: "optional-field",
                        child: DemoTextField(
                            placeholder: "Optional field",
                            widgetProperties: .init(flex: flexHorizontalMargin)
                        )
                    ),
                    FormInput(
                        name: "required-field",
                        required: true,
                        validator: FormScreen.textValidatorName,
                        child: DemoTextField(
                            placeholder: "Required field",
                            widgetProperties: .init(flex: flexHorizontalMargin)
                        )
                    ),
                    FormInput(
                        name: "another-required-field",
                        required: true,
                        validator: FormScreen.textValidatorName,
                        child: DemoTextField(
                            placeholder: "Another required field",
                            widgetProperties: .init(flex: flexHorizontalMargin)
                            
                        )
                    ),
                    Container(children: [], widgetProperties: .init(flex: Flex(grow: 1))),
                    FormSubmit(
                        child: Button(text: "Submit Form", styleId: .FORM_SUBMIT_STYLE, widgetProperties: .init(flex: flexHorizontalMargin)),
                        enabled: false
                    )
                ],
                widgetProperties: .init(flex: Flex().grow(1).padding(EdgeValue().all(10)))
            )
        )
        let screen = Screen(
            navigationBar: NavigationBar(title: "Form"),
            child: form
        )
        return Beagle.screen(.declarative(screen))
    }
    
}

extension DemoTextField: Renderable {
    func toView(renderer: BeagleRenderer) -> UIView {
        let textField = View()
        textField.borderStyle = .roundedRect
        textField.placeholder = placeholder

        textField.beagle.setup(self)

        return textField
    }
}

struct DemoTextField: BeagleUI.Widget, AutoInitiableAndDecodable {
    
    var placeholder: String
    var widgetProperties: WidgetProperties

// sourcery:inline:auto:DemoTextField.Init
    internal init(
        placeholder: String,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.placeholder = placeholder
        self.widgetProperties = widgetProperties
    }
// sourcery:end
}

extension DemoTextField {
    final class View: UITextField, UITextFieldDelegate, InputValue, WidgetStateObservable {
        
        var observable = Observable<WidgetState>(value: WidgetState(value: text))
        
        override init(frame: CGRect) {
            super.init(frame: frame)
            delegate = self
            addTarget(self, action: #selector(textChanged), for: .editingChanged)
        }

        @available(*, unavailable)
        required init?(coder aDecoder: NSCoder) {
            fatalError("init(coder:) has not been implemented")
        }
        
        func getValue() -> Any {
            return text ?? ""
        }
        
        func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            endEditing(true)
            return true
        }

        @objc private func textChanged() {
            observable.value.value = text
        }
    }
}
