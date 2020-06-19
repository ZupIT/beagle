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
import BeagleUI
import BeagleSchema

struct SimpleFormScreen: DeeplinkScreen {
    
    init(path: String, data: [String : String]?) {
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(screen))
    }
    
    var screen: Screen {
        return Screen(
            navigationBar: NavigationBar(title: "Simple Form", showBackButton: true),
            child:Container(children: [
                SimpleForm(
                    _context_: Context(id: "email", value: ["address":"luis@zup.com.br","message":"hello Gabi"]),
                    onSubmit: [
                        Alert(
                        title: "Error!",
                        message: "@{email.address}",
                        onPressOk: FirstAction(),
                        labelOk: "OK"                      )
                    ],
                    children: [Button(text: "olghghgha", action: SubmitForm())]),
                SimpleForm(
                _context_: Context(id: "email", value: ["address":"luis.gustavo@zup.com.br","message":"hello Gabi"]),
                onSubmit: [
                    Alert(
                    title: "Error!",
                    message: "@{email.address}",
                    onPressOk: FirstAction(),
                    labelOk: "OK"
                    ),
                    Navigate.openNativeRoute(.WEB_VIEW_ENDPOINT) ],
                children: [Button(text: "Luis Gustavo", action: SubmitForm())])
                ])
        )
    }
    
}

struct FirstAction: Action {
    func execute(controller: BeagleController, sender: Any) {
        print("FirstAction Executada")
    }
}

struct SecondAction: Action {
    func execute(controller: BeagleController, sender: Any) {
        print("SecondAction Executada")
    }
}
