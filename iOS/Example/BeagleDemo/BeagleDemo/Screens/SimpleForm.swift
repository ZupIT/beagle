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

struct SimpleFormScreen: DeeplinkScreen {
    
    init(path: String, data: [String: String]?) {
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(simpleFormScreen))
    }
    
    var simpleFormScreen: Screen = {
        return Screen(navigationBar: NavigationBar(title: "Simple Form", showBackButton: true)) {
            Container {
                Button(
                    text: "Declarative Simple Form",
                    onPress: [Navigate.pushView(.declarative(declarativeScreen))]
                )
                Button(
                    text: "Simple Form with validation",
                    onPress: [Navigate.openNativeRoute(.init(route: .simpleFormValidationEndpoint))]
                )
            }
        }
    }()

    
    static var declarativeScreen: Screen {
        return Screen(navigationBar: NavigationBar(title: "Simple Form", showBackButton: true)) {
            Container {
                SimpleForm(
                    context: Context(id: "form", value: ["address": "You address", "message": "You message"]),
                    onSubmit: [
                        Alert(
                            title: "Seu Contexto",
                            message: "Address: @{form.address} \n Message: @{form.message}",
                            onPressOk: FirstAction(),
                            labelOk: "OK")
                    ]
                ) {
                    Button(text: "SimpleForm", onPress: [SubmitForm()])
                }
                SimpleForm(
                    context: Context(id: "form", value: ["address": "beagle@beagle.com.br", "message": "Hello Beagle"]),
                    onSubmit: [
                        Alert(
                            title: "Contexto Beagle",
                            message: "Address: @{form.address} \n Message: @{form.message}",
                            onPressOk: SecondAction(),
                            labelOk: "OK")
                    ]
                ) {
                    Button(text: "SimpleForm", onPress: [SubmitForm()])
                }
            }
        }
    }
}

struct FirstAction: Action {
    var analytics: ActionAnalyticsConfig? {
        return nil
    }
    
    func execute(controller: BeagleController, origin: UIView) {
        print("FirstAction Executed")
    }
}

struct SecondAction: Action {
    var analytics: ActionAnalyticsConfig? {
        return nil
    }
    
    func execute(controller: BeagleController, origin: UIView) {
        print("SecondAction Executed")
    }
}

struct SimpleFormValidationText: DeeplinkScreen {
    
    init(path: String, data: [String: String]?) {
    }
    
    func screenController() -> UIViewController {
        return BeagleScreenViewController(.declarativeText(
        """
          {
             "_beagleComponent_" : "beagle:screenComponent",
             "navigationBar" : {
               "title" : "Beagle Context",
               "showBackButton" : true
             },
             "child" : {
              "_beagleComponent_":"beagle:simpleform",
              "context":{
                "id":"form",
                "value":{
                  "data":{
                    "username":"",
                    "password":"",
                    "passwordConfirmation":""
                  },
                  "showFormErrors": true,
                  "showError": {
                    "username": false,
                    "password": false,
                    "passwordConfirmation": false
                  }
                }
              },
              "onValidationError": [
                {
                  "_beagleAction_":"beagle:setContext",
                  "contextId":"form",
                  "path":"showFormErrors",
                  "value":true
                }
              ],
              "onSubmit":[
                {
                  "_beagleAction_":"beagle:alert",
                  "message":"form submitted!"
                }
              ],
              "children":[
                {
                  "_beagleComponent_":"beagle:container",
                  "children":[
                    {
                      "_beagleComponent_":"beagle:text",
                      "styleId":"title",
                      "text":"Account"
                    },
                    {
                      "_beagleComponent_":"beagle:container",
                      "children":[
                        {
                          "_beagleComponent_":"beagle:textInput",
                          "placeholder":"Username",
                          "value":"@{form.data.username}",
                          "showError":"@{or(form.showFormErrors, form.showError.username)}",
                          "error":"@{condition(isEmpty(form.data.username), 'The username is required.', '')}",
                          "onChange":[
                            {
                              "_beagleAction_":"beagle:setContext",
                              "contextId":"form",
                              "path":"data.username",
                              "value":"@{onChange.value}"
                            }
                          ],
                          "onBlur":[
                            {
                              "_beagleAction_":"beagle:setContext",
                              "contextId":"form",
                              "path":"showError.username",
                              "value":true
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "_beagleComponent_":"beagle:container",
                      "style":{
                        "margin":{
                          "left":{
                            "value":0,
                            "type":"REAL"
                          },
                          "right":{
                            "value":0,
                            "type":"REAL"
                          },
                          "top":{
                            "value":50,
                            "type":"REAL"
                          },
                          "bottom":{
                            "value":30,
                            "type":"REAL"
                          }
                        },
                        "flex":{
                          "flexDirection":"ROW",
                          "grow":1
                        }
                      },
                      "children":[
                        {
                          "style":{
                            "margin":{
                              "left":{
                                "value":0,
                                "type":"REAL"
                              },
                              "right":{
                                "value":15,
                                "type":"REAL"
                              }
                            },
                            "flex":{
                              "grow":1
                            }
                          },
                          "_beagleComponent_":"beagle:textInput",
                          "placeholder":"Password",
                          "value":"@{form.data.password}",
                          "showError":"@{or(form.showFormErrors, form.showError.password)}",
                          "error":"@{condition(lt(length(form.data.password), 6), 'The password must have at least 6 characters.', '')}",
                          "type":"PASSWORD",
                          "onChange":[
                            {
                              "_beagleAction_":"beagle:setContext",
                              "contextId":"form",
                              "path":"data.password",
                              "value":"@{onChange.value}"
                            }
                          ],
                          "onBlur":[
                            {
                              "_beagleAction_":"beagle:setContext",
                              "contextId":"form",
                              "path":"showError.password",
                              "value":true
                            }
                          ]
                        },
                        {
                          "_beagleComponent_":"beagle:textInput",
                          "placeholder":"Confirm your password",
                          "value":"@{form.data.passwordConfirmation}",
                          "showError":"@{or(form.showFormErrors, form.showError.passwordConfirmation)}",
                          "error":"@{condition(eq(form.data.password, form.data.passwordConfirmation), '', 'The password and its confirmation must match.')}",
                          "type":"PASSWORD",
                          "onChange":[
                            {
                              "_beagleAction_":"beagle:setContext",
                              "contextId":"form",
                              "path":"data.passwordConfirmation",
                              "value":"@{onChange.value}"
                            }
                          ],
                          "onBlur":[
                            {
                              "_beagleAction_":"beagle:setContext",
                              "contextId":"form",
                              "path":"showError.passwordConfirmation",
                              "value":true
                            }
                          ]
                        }
                      ]
                    }
                  ]
                },
                {
                  "_beagleComponent_":"beagle:container",
                  "children":[
                    {
                      "_beagleComponent_":"beagle:button",
                      "text":"Previous",
                      "disabled":true
                    },
                    {
                      "_beagleComponent_":"beagle:button",
                      "text":"Next",
                      "onPress":[
                        {
                          "_beagleAction_":"beagle:submitForm"
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
