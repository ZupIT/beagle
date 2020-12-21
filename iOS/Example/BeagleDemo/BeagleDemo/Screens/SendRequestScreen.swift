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

let sendRequestDeclarativeScreen: Screen = {
    let containerStyle = Style().size(Size().width(100%).height(50%)).flex(Flex().alignItems(.center).justifyContent(.spaceEvenly))
 
    return Screen(
        navigationBar: NavigationBar(title: "Send Request", showBackButton: true),
        context: Context(id: "myContext", value: "initial value")
    ) {
        Container(widgetProperties: .init(style: containerStyle)) {
            Button(
                text: "SendRequest Context on header",
                styleId: "DesignSystem.Stylish.ButtonAndAppearance",
                onPress: [
                    Navigate.pushView(.remote(.init(url: "https://run.mocky.io/v3/5f028242-dfb7-43af-8b55-bac79d7332c6")))
                ],
                widgetProperties: .init(style: Style().backgroundColor(.salmonButton).size(Size().width(80%)).cornerRadius(.init(radius: 10)))
            )
            Button(
                text: "do request",
                styleId: "DesignSystem.Stylish.ButtonAndAppearance",
                onPress: [
                    SendRequest(
                        url: "https://httpbin.org/post",
                        method: .value(.post),
                        data: "@{myContext}",
                        headers: .value([
                            "Content-Type": "application/json",
                            "sample-header-1": "HeaderContent1",
                            "Sample-Header-2": "HeaderContent2"
                        ]),
                        onSuccess: [
                            Alert(
                                title: "Success!",
                                message: "request data: @{onSuccess.data.json}",
                                onPressOk: Alert(message: "request data: @{onSuccess.data.json}"),
                                labelOk: "Cancel"
                            )
                        ],
                        onError: [
                            Confirm(
                                title: "Error!",
                                message: "error sending request",
                                onPressOk: OkAction(),
                                onPressCancel: CancelAction(),
                                labelOk: "Cancel",
                                labelCancel: "Ok"
                            )
                        ],
                        onFinish: [
                            CustomConsoleLogAction()
                        ]
                    )
                ],
                widgetProperties: .init(style: Style().backgroundColor(.blueButton).size(Size().width(60%)).cornerRadius(.init(radius: 10)))
            )
        }
    }
}()

struct CustomConsoleLogAction: Action {
    var analytics: ActionAnalyticsConfig? { return nil }
    
    func execute(controller: BeagleController, origin: UIView) {
        print("SendRequestScreen.CustomConsoleAction")
    }
}

struct OkAction: Action {
    var analytics: ActionAnalyticsConfig? { return nil }
    
    func execute(controller: BeagleController, origin: UIView) {
        print("onPressOk from Alert clicked")
    }
}

struct CancelAction: Action {
    var analytics: ActionAnalyticsConfig? { return nil }
    
    func execute(controller: BeagleController, origin: UIView) {
        print("onPressCancel from Confirm clicked")
    }
}
