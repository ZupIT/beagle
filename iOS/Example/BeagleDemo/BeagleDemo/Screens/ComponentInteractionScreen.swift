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
import Beagle
import BeagleSchema
import UIKit

let componentInteractionScreen: Screen = {
    return Screen(navigationBar: NavigationBar(title: "Component Interaction", showBackButton: true)) {
        Container {
            Button(
                text: "Declarative",
                onPress: [Navigate.pushView(.declarative(declarativeScreen))]
            )
            Button(
                text: "Text (JSON)",
                onPress: [Navigate.openNativeRoute(.init(route: .COMPONENT_INTERACTION_ENDPOINT))]
            )
        }
    }
}()

let declarativeScreen: Screen = {
    return Screen(navigationBar: NavigationBar(title: "Component Interaction", showBackButton: true)) {
        Container(context: Context(id: "context2", value: nil)) {
            Container(context: Context(id: "context1", value: ["name": "name"])) {
                TextInput(
                    onChange: [
                        SetContext(
                            contextId: "context1",
                            value: "@{onChange.value}"
                        )
                    ]
                )
                Text("teste é: @{context1} + @{context2}")
                Text("@{context1}")
                Button(
                    text: "1",
                    onPress: [
                        SetContext(
                            contextId: "context1",
                            value: ["name": "nameUpdated"]
                        )
                    ]
                )
                Button(
                    text: "2",
                    onPress: [
                        SetContext(
                            contextId: "context2",
                            value: "update"
                        )
                    ]
                )
                MyComponent(person: "@{context1}", personOpt: nil, action: nil, widgetProperties: WidgetProperties())
            }
        }
    }
}()

struct ComponentInteractionText: DeeplinkScreen {
    
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
                   "_beagleComponent_" : "beagle:container",
                   "children" : [ {
                     "_beagleComponent_" : "beagle:textInput",
                     "value" : "@{address.data.zip}",
                     "placeholder" : "CEP",
                     "onChange" : [ {
                       "_beagleAction_" : "beagle:setContext",
                       "contextId" : "address",
                       "value" : "@{onChange.value}",
                       "path" : "data.zip"
                     } ],
                     "onBlur" : [ {
                       "_beagleAction_" : "beagle:sendRequest",
                       "url" : "https://viacep.com.br/ws/@{onBlur.value}/json",
                       "method" : "GET",
                       "onSuccess" : [ {
                         "_beagleAction_" : "beagle:setContext",
                         "contextId" : "address",
                         "value" : {
                           "zip" : "@{onBlur.value}",
                           "street" : "@{onSuccess.data.logradouro}",
                           "number" : "@{address.data.number}",
                           "neighborhood" : "@{onSuccess.data.bairro}",
                           "city" : "@{onSuccess.data.localidade}",
                           "state" : "@{onSuccess.data.uf}",
                           "complement" : "@{address.data.complement}"
                         },
                         "path" : "data"
                       } ]
                     } ],
                     "style" : {
                       "margin" : {
                         "bottom" : {
                           "value" : 15.0,
                           "type" : "REAL"
                         }
                       }
                     }
                   }, {
                     "_beagleComponent_" : "beagle:textInput",
                     "value" : "@{address.data.street}",
                     "placeholder" : "Rua",
                     "onChange" : [ {
                       "_beagleAction_" : "beagle:setContext",
                       "contextId" : "address",
                       "value" : "@{onChange.value}",
                       "path" : "data.street"
                     } ],
                     "style" : {
                       "margin" : {
                         "bottom" : {
                           "value" : 15.0,
                           "type" : "REAL"
                         }
                       }
                     }
                   }, {
                     "_beagleComponent_" : "beagle:textInput",
                     "value" : "@{address.data.number}",
                     "placeholder" : "Número",
                     "onChange" : [ {
                       "_beagleAction_" : "beagle:setContext",
                       "contextId" : "address",
                       "value" : "@{onChange.value}",
                       "path" : "data.number"
                     } ],
                     "style" : {
                       "margin" : {
                         "bottom" : {
                           "value" : 15.0,
                           "type" : "REAL"
                         }
                       }
                     }
                   }, {
                     "_beagleComponent_" : "beagle:textInput",
                     "value" : "@{address.data.neighborhood}",
                     "placeholder" : "Bairro",
                     "onChange" : [ {
                       "_beagleAction_" : "beagle:setContext",
                       "contextId" : "address",
                       "value" : "@{onChange.value}",
                       "path" : "data.neighborhood"
                     } ],
                     "style" : {
                       "margin" : {
                         "bottom" : {
                           "value" : 15.0,
                           "type" : "REAL"
                         }
                       }
                     }
                   }, {
                     "_beagleComponent_" : "beagle:textInput",
                     "value" : "@{address.data.city}",
                     "placeholder" : "Cidade",
                     "onChange" : [ {
                       "_beagleAction_" : "beagle:setContext",
                       "contextId" : "address",
                       "value" : "@{onChange.value}",
                       "path" : "data.city"
                     } ],
                     "style" : {
                       "margin" : {
                         "bottom" : {
                           "value" : 15.0,
                           "type" : "REAL"
                         }
                       }
                     }
                   }, {
                     "_beagleComponent_" : "beagle:textInput",
                     "value" : "@{address.data.state}",
                     "placeholder" : "Estado",
                     "onChange" : [ {
                       "_beagleAction_" : "beagle:setContext",
                       "contextId" : "address",
                       "value" : "@{onChange.value}",
                       "path" : "data.state"
                     } ],
                     "style" : {
                       "margin" : {
                         "bottom" : {
                           "value" : 15.0,
                           "type" : "REAL"
                         }
                       }
                     }
                   }, {
                     "_beagleComponent_" : "beagle:textInput",
                     "value" : "@{address.data.complement}",
                     "placeholder" : "Complemento",
                     "onChange" : [ {
                       "_beagleAction_" : "beagle:setContext",
                       "contextId" : "address",
                       "value" : "@{onChange.value}",
                       "path" : "data.complement"
                     } ],
                     "style" : {
                       "margin" : {
                         "bottom" : {
                           "value" : 15.0,
                           "type" : "REAL"
                         }
                       }
                     }
                   } ],
                   "context" : {
                     "id" : "address",
                     "value" : {
                       "data" : {
                         "zip" : "",
                         "street" : "",
                         "number" : "",
                         "neighborhood" : "",
                         "city" : "",
                         "state" : "",
                         "complement" : ""
                       }
                     }
                   }
                 }
               }
        """
            ))
    }
}

struct Person {
    let name: String
}

extension Person: Decodable {}

struct MyComponent {
    
    let person: Expression<Person>
    let personOpt: Expression<Person>?
    
    let action: Expression<CustomConsoleLogAction>?
    
    var widgetProperties: WidgetProperties

}

extension MyComponent: Widget {
    
    func toView(renderer: BeagleRenderer) -> UIView {
        let view = UILabel()
        renderer.observe(person, andUpdate: \.text, in: view) { $0?.name }
        return view
    }

}
