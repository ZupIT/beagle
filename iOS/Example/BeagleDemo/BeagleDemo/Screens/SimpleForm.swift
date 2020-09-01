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

import UIKit
import Beagle
import BeagleSchema

struct SimpleFormScreen: DeeplinkScreen {
    
    init(path: String, data: [String: String]?) {
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(screen))
    }
    
    var screen: Screen {
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
    func execute(controller: BeagleController, origin: UIView) {
        print("FirstAction Executed")
    }
}

struct SecondAction: Action {
    func execute(controller: BeagleController, origin: UIView) {
        print("SecondAction Executed")
    }
}
