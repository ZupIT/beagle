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
import BeagleUI
import BeagleSchema
import UIKit

let componentInteractionScreen: Screen = {
    return Screen(
        navigationBar: NavigationBar(title: "Component Interaction", showBackButton: true),
        child: Container(children: [
            Button(
                text: "Declarative",
                action: Navigate.pushView(.declarative(declarativeScreen))
            ),
            Button(
                text: "Text (JSON)",
                action: Navigate.openNativeRoute("componentInteractionText")
            )
        ])
    )
}()

let contextValue: DynamicObject = [
    "int": 2,
    "double": 2.2,
    "string": "string",
    "expression": "${myContext}",
    "array": [
        "1", 2, 2.2, "${myContext}", nil
    ],
    "dictionary": [
        "int": 2,
        "double": 2.2,
        "string": "string",
        "expression": "${myContext}"
    ]
]

let declarativeScreen: Screen = {
    return Screen(
        navigationBar: NavigationBar(title: "Component Interaction", showBackButton: true),
        child: Container(
            children:
            [
                TextInput(
                    label: "asd",
                    onChange: [
                        SetContext(
                            context: "myContext",
                            value: ["a": ["c": "${onChange.value}"], "d": "${onChange.value}${onChange.value}"]
                        )
                    ]
                ),
                Text("${myContext.a.b}"),
                Text("${myContext.b}"),
                Button(
                    text: "ok",
                    action: SetContext(
                        context: "myContext",
                        value: ["b": "ok tapped!"]
                    )
                )
            ],
            context: Context(id: "myContext", value: ["a": ["b": "123123"]])
        )
    )
}()

struct ComponentInteractionText: DeeplinkScreen {

    init(path: String, data: [String : String]?) {
    }

    func screenController() -> UIViewController {
        return BeagleScreenViewController(.declarativeText(
                """
                {
                  "_beagleComponent_": "beagle:screencomponent",
                  "navigationBar": {
                    "title": "Component Interaction",
                    "showBackButton": true
                  },
                  "child": {
                    "_beagleComponent_": "beagle:container",
                    "_context_": {
                      "id": "myContext",
                      "value": ""
                    },
                    "children": [
                    {
                      "_beagleComponent_": "custom:textinput",
                      "label": "label",
                        "onChange": [
                        {
                          "_beagleAction_": "beagle:setcontext",
                          "context": "myContext",
                          "value": "${onChange.value}"
                        }
                        ]
                    },
                      {
                        "_beagleComponent_": "beagle:text",
                        "text": "${myContext}"
                      },
                      {
                        "_beagleComponent_": "beagle:button",
                        "text": "ok",
                        "action": {
                          "_beagleAction_": "beagle:setcontext",
                          "context": "myContext",
                          "value": "2"
                        }
                      }
                    ]
                  }
                }
        """
        ))
    }
}

struct TextInput: Widget {
    var widgetProperties: WidgetProperties = WidgetProperties()
    var label: Expression<String>

    var onChange: [Action]?
    var onFocus: [Action]?
    var onBlur: [Action]?
    
    func toView(renderer: BeagleRenderer) -> UIView {
        let view = TextInputView(widget: self, controller: renderer.context)
        view.text = get(label, with: view, controller: renderer.context) { string in view.text = string }
        view.beagle.setup(self)
        return view
    }
}

class TextInputView: UITextField, UITextFieldDelegate {
    var widget: TextInput
    weak var controller: BeagleContext?
    
    init(widget: TextInput, controller: BeagleContext) {
        self.widget = widget
        self.controller = controller
        super.init(frame: .zero)
        self.delegate = self
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        // TODO: arrumar execute
//        let context = Context(id: "onFocus", value: ["value": textField.text])
        
        let context = Context(id: "onFocus", value: .dictionary(["value": .string(textField.text ?? "")]))
        controller?.actionManager.execute(actions: widget.onFocus, with: context, sender: self, controller: controller)
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
//        let context = Context(id: "onBlur", value: ["value": textField.text])
        
        let context = Context(id: "onBlur", value: .dictionary(["value": .string(textField.text ?? "")]))
        controller?.actionManager.execute(actions: widget.onBlur, with: context, sender: self, controller: controller)
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool
    {
        var updatedText: String? = nil
        if let text = textField.text,
           let textRange = Range(range, in: text) {
           updatedText = text.replacingCharacters(in: textRange,
                                                       with: string)
        }
        textField.text = updatedText
//        let context = Context(id: "onChange", value: ["value": updatedText])
        
        let context = Context(id: "onChange", value: .dictionary(["value": .string(updatedText ?? "")]))
        controller?.actionManager.execute(actions: widget.onChange, with: context, sender: self, controller: controller)
        
        return false
    }
}

// MARK: Decode
extension TextInput {
    enum CodingKeys: String, CodingKey {
        case label
        case onChange
        case onFocus
        case onBlur
        case widgetProperties
    }

    internal init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        widgetProperties = try WidgetProperties(from: decoder)
        label = try container.decode(Expression<String>.self, forKey: .label)
        onChange = try container.decode(forKey: .onChange)
        onFocus = try container.decode(forKey: .onFocus)
        onBlur = try container.decode(forKey: .onBlur)
    }
}
