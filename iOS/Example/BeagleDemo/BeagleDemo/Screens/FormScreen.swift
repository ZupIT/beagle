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
import Beagle
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
        let styleHorizontalMargin = Style().margin(EdgeValue().all(10))
        let form = Form(
            onSubmit: [FormRemoteAction(path: .TEXT_FORM_ENDPOINT, method: .post)],
            child: Container(
                children: [
                    FormInput(
                        name: "optional-field",
                        child: TextInput(
                            placeholder: .value("Optional field"),
                            widgetProperties: .init(style: styleHorizontalMargin)
                        )
                    ),
                    FormInput(
                        name: "required-field",
                        required: true,
                        validator: FormScreen.textValidatorName,
                        child: TextInput(
                            placeholder: .value("Required field"),
                            widgetProperties: .init(style: styleHorizontalMargin)
                        )
                    ),
                    FormInput(
                        name: "another-required-field",
                        required: true,
                        validator: FormScreen.textValidatorName,
                        child: TextInput(
                            placeholder: .value("Another required field"),
                            widgetProperties: .init(style: styleHorizontalMargin)
                            
                        )
                    ),
                    Container(children: [], widgetProperties: .init(style: Style(flex: Flex(grow: 1)))),
                    FormSubmit(
                        child: Button(text: "Submit Form", styleId: .FORM_SUBMIT_STYLE, widgetProperties: .init(style: styleHorizontalMargin)),
                        enabled: false
                    )
                ],
                widgetProperties: .init(style: Style(padding: EdgeValue().all(10)))
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

        return textField
    }
}

struct DemoTextField: Widget, AutoInitiableAndDecodable {
    
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
