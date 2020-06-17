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

let sendRequestScreen: Screen = {
    return Screen(
        navigationBar: NavigationBar(title: "Send Request", showBackButton: true),
        child: Container(children: [
            Button(
                text: "Declarative",
                action: Navigate.pushView(.declarative(sendRequestDeclarativeScreen))
            ),
            Button(
                text: "Text (JSON)",
                action: Navigate.openNativeRoute("sendRequestText")
            )
        ])
    )
}()

let sendRequestDeclarativeScreen: Screen = {
    return Screen(
        navigationBar: NavigationBar(title: "Send Request", showBackButton: true),
        child: Container(
            children:
            [
                Text("${myContext.a.b}"),
                Button(
                    text: "ok",
                    action: SendRequest(
                        url: "https://httpbin.org/post",
                        method: .post,
                        data: [
                            "id": 10,
                            "sample": "data"
                        ],
                        headers: [
                            "Content-Type": "application/json",
                            "sample-header-1": "HeaderContent1",
                            "Sample-Header-2": "HeaderContent2"
                        ],
                        onSuccess: [
                            ShowNativeDialog(
                                title: "Success!",
                                message: "${onSuccess.data.json.data}",
                                buttonText: "ok"
                            )
                        ],
                        onError: [
                            ShowNativeDialog(
                                title: "Error!",
                                message: "error sending request",
                                buttonText: "ok"
                            )
                        ],
                        onFinish: [
                            CustomConsoleLogAction()
                        ]
                    )
                )
            ],
            context: Context(id: "myContext", value: [:])
        )
    )
}()

struct CustomConsoleLogAction: Action {
    func execute(controller: BeagleController, sender: Any) {
        print("SendRequestScreen.CustomConsoleAction")
    }
}
