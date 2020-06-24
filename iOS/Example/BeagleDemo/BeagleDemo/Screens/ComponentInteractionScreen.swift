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
                onPress: [Navigate.pushView(.declarative(declarativeScreen))]
            ),
            Button(
                text: "Text (JSON)",
                onPress: [Navigate.openNativeRoute(.COMPONENT_INTERACTION_ENDPOINT)]
            )
        ])
    )
}()

let declarativeScreen: Screen = {
    return Screen(
        navigationBar: NavigationBar(title: "Component Interaction", showBackButton: true),
        child: Container(children:[
            TextInput(
                value: "",
                onChange: [
                    SetContext(
                        context: "myContext",
                        path: "text",
                        value: [
                            "text": "@{onChange.value}",
                            "alignment": "RIGHT",
                            "textColor": "#FF2020"
                        ]
                    )
                ]
            ),
            Text("@{myContext.text}", alignment: "@{myContext.alignment}", textColor: "@{myContext.textColor}"),
            Button(
                text: "ok",
                onPress: [SetContext(
                    context: "myContext",
                    value: [
                        "text": "button value",
                        "alignment": "RIGHT",
                        "textColor": "#2020FF"
                    ]
                )]
            )
            ],
            context: Context(id: "myContext", value: "")
        )
    )
}()

struct ComponentInteractionText: DeeplinkScreen {

    init(path: String, data: [String: String]?) {
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
                        "onPress": [{
                          "_beagleAction_": "beagle:setcontext",
                          "context": "myContext",
                          "value": "2"
                        }]
                      }
                    ]
                  }
                }
        """
        ))
    }
}
