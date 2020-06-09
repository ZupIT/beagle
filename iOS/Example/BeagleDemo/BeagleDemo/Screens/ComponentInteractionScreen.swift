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
                Text(.expression(Expression(rawValue: "${myContext.c.test2}")!)),
                Button(
                    text: "ok",
                    action: SetContext(
                        context: "myContext",
                        path: "c.test2",
                        value: "2"
                    )
                )
            ],
            context: Context(id: "myContext", value: ["b": ["test": "1"]])
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
                      "value": {
                        "b": {
                          "test": "1"
                        }
                      }
                    },
                    "children": [
                      {
                        "_beagleComponent_": "beagle:text",
                        "text": "${myContext.c.test2}"
                      },
                      {
                        "_beagleComponent_": "beagle:button",
                        "text": "ok",
                        "action": {
                          "_beagleAction_": "beagle:setcontext",
                          "context": "myContext",
                          "path": "c.test2",
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

