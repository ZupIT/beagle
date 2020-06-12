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

let declarativeScreen: Screen = {
    return Screen(
        navigationBar: NavigationBar(title: "Component Interaction", showBackButton: true),
        child: Container(
            children:
            [
                TextInput(
                    label: "value",
                    onChange: [
                        SetContext(
                            context: "myContext",
                            value: .expression(Expression(rawValue: "${onChange.value}")!)
                        )
                    ]
                ),
//                Text(.expression(Expression(rawValue: "${myContext}")!)),
                Button(
                    text: "ok",
                    action: SetContext(
                        context: "myContext",
                        value: .value(AnyDecodable("2"))
                    )
                )
            ],
            context: Context(id: "myContext", value: "")
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
                    "context": {
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

protocol Inputtable {
    var onChange: [Action]? { get }
    var onFocus: [Action]? { get }
    var onBlur: [Action]? { get }
}

struct TextInput: Widget, Inputtable, AutoInitiable {
    var widgetProperties: WidgetProperties
    var label: String?
    
    var onChange: [Action]?
    var onFocus: [Action]?
    var onBlur: [Action]?
    
    func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let view = TextInputView(widget: self, controller: context)
        view.placeholder = label
        view.beagle.setup(self)
        return view
    }

// sourcery:inline:auto:TextInput.Init
    internal init(
        widgetProperties: WidgetProperties = WidgetProperties(),
        label: String? = nil,
        onChange: [Action]? = nil,
        onFocus: [Action]? = nil,
        onBlur: [Action]? = nil
    ) {
        self.widgetProperties = widgetProperties
        self.label = label
        self.onChange = onChange
        self.onFocus = onFocus
        self.onBlur = onBlur
    }
// sourcery:end
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
        guard let controller = controller else { return }
        let context = Context(id: "onFocus", value: ["value": textField.text])
        controller.actionManager.execute(actions: widget.onFocus, with: context, sender: self, controller: controller)
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        guard let controller = controller else { return }
        let context = Context(id: "onBlur", value: ["value": textField.text])
        controller.actionManager.execute(actions: widget.onBlur, with: context, sender: self, controller: controller)
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool
    {
        guard let controller = controller else { return true }
        
        var updatedText: String? = nil
        if let text = textField.text,
           let textRange = Range(range, in: text) {
           updatedText = text.replacingCharacters(in: textRange,
                                                       with: string)
        }
        
        let context = Context(id: "onChange", value: ["value": updatedText])
        controller.actionManager.execute(actions: widget.onChange, with: context, sender: self, controller: controller)
        return true
    }
}

// TODO: usar Sourcery
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
        label = try container.decodeIfPresent(String.self, forKey: .label)
        onChange = try container.decode(forKey: .onChange)
        onFocus = try container.decode(forKey: .onFocus)
        onBlur = try container.decode(forKey: .onBlur)
    }
}
