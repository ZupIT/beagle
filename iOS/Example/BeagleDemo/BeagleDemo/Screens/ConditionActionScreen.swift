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

import Beagle
import BeagleSchema
import UIKit

let conditionActionScreen: Screen = {
    Screen(navigationBar: NavigationBar(title: "Conditional Action", showBackButton: true)) {
        Container(context: Context(id: "context", value: true)) {
            Button(
                text: "Declarative",
                onPress: [Navigate.pushView(.declarative(conditionActionDeclarativeScreen))]
            )
            Button(
                text: "Text (JSON)",
                onPress: [Navigate.openNativeRoute(.init(route: .conditionActionEndpoint))]
            )
        }
    }
}()

private let conditionActionDeclarativeScreen: Screen = {
    let title = "The condition is..."
    
    return Screen(navigationBar: NavigationBar(title: "Conditional Action Declarative", showBackButton: true)) {
        Container(context: Context(id: "context", value: true)) {
            Button(
                text: "Togle Value",
                onPress: [SetContext(contextId: "context", value: "@{not(context)}")]
            )
            Button(
                text: "Show Condition",
                onPress: [
                    Condition(
                        condition: "@{context}",
                        onTrue: [Alert(title: .value(title), message: "TRUE")],
                        onFalse: [Alert(title: .value(title), message: "FALSE")]
                    )
                ]
            )
        }
    }
}()

struct ConditionActionText: DeeplinkScreen {
    
    init(path: String, data: [String: String]?) {
    }
    
    func screenController() -> UIViewController {
        return BeagleScreenViewController(.declarativeText(
            """
            {
                "_beagleComponent_" : "beagle:screenComponent",
                "navigationBar" : {
                  "title" : "Text (JSON)",
                  "showBackButton" : true
                },
                "child" : {
                  "_beagleComponent_": "beagle:container",
                  "context": {
                    "id": "user",
                    "value": {
                      "numberOfBeers": 0,
                      "age": 0
                    }
                  },
                  "children": [
                    {
                      "_beagleComponent_": "beagle:text",
                      "text": "You have @{user.numberOfBeers} beer(s)",
                      "textColor": "#0000FF",
                      "style": {
                        "margin": {
                          "bottom": {
                            "value": 20,
                            "type": "REAL"
                          }
                        }
                      }
                    },
                    {
                      "_beagleComponent_": "beagle:text",
                      "text": "How old are you?"
                    },
                    {
                        "_beagleComponent_": "beagle:button",
                        "text": "Bellow 18",
                        "onPress": [
                            {
                              "_beagleAction_": "beagle:setContext",
                              "contextId": "user",
                              "path": "age",
                              "value": "@{17}"
                            }
                        ]
                    },
                    {
                        "_beagleComponent_": "beagle:button",
                        "text": "18 or older",
                        "onPress": [
                            {
                              "_beagleAction_": "beagle:setContext",
                              "contextId": "user",
                              "path": "age",
                              "value": "@{18}"
                            }
                        ]
                    },
                    {
                      "_beagleComponent_": "beagle:text",
                      "text": "@{user.age}"
                    },
                    {
                      "_beagleComponent_": "beagle:button",
                      "text": "Buy a beer",
                      "onPress": [
                        {
                          "_beagleAction_": "beagle:condition",
                          "condition": "@{gte(user.age, 18)}",
                          "onTrue": [
                            {
                              "_beagleAction_": "beagle:setContext",
                              "contextId": "user",
                              "path": "numberOfBeers",
                              "value": "@{sum(user.numberOfBeers, 1)}"
                            }
                          ],
                          "onFalse": [
                            {
                              "_beagleAction_": "beagle:alert",
                              "message": "You're not old enough to drink!"
                            }
                          ]
                        }
                      ]
                    }
                  ]
                }
            }
            """
            ))
    }
}
