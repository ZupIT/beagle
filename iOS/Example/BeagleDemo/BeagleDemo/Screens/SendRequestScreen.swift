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

let sendRequestDeclarativeScreen: Screen = {
    return Screen(
        navigationBar: NavigationBar(title: "Send Request", showBackButton: true),
        child: Container(
            children:
            [
                Button(
                    text: "do request",
                    action: SendRequest(
                        url: "https://httpbin.org/post",
                        method: .post,
                        data: ["@{myContext}"],
                        headers: [
                            "Content-Type": "application/json",
                            "sample-header-1": "HeaderContent1",
                            "Sample-Header-2": "HeaderContent2"
                        ],
                        onSuccess: [
                            Alert(
                                title: "Success!",
                                message: "Sucess sending Request",
                                onPressOk: OkAction(),
                                labelOk: "OK"
                            )
                        ],
                        onError: [
                            Confirm(
                                title: "Error!",
                                message: "error sending request",
                                onPressOk: OkAction(),
                                onPressCancel: CancelAction(),
                                labelOk: "OK",
                                labelCancel: "Cancel"
                            )
                        ],
                        onFinish: [
                            CustomConsoleLogAction()
                        ]
                    )
                )
            ],
            context: Context(id: "myContext", value: "initial value")
        )
    )
}()

struct CustomConsoleLogAction: Action {
    func execute(controller: BeagleController, sender: Any) {
        print("SendRequestScreen.CustomConsoleAction")
    }
}

struct OkAction: Action {
    func execute(controller: BeagleController, sender: Any) {
        print("onPressOk from Alert clicked")
    }
}

struct CancelAction: Action {
    func execute(controller: BeagleController, sender: Any) {
        print("onPressCancel from Confirm clicked")
    }
}

