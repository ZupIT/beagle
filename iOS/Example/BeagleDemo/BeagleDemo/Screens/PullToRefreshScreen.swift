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

let pullToRefreshScreen: Screen = {
    return Screen(
        navigationBar: NavigationBar(title: "SampleScreen"),
        child: PullToRefresh(
            context: Context(id: "isRefreshing", value: false),
            onPull: [
                CustomAsyncAction(
                    onFinish: [
                        SetContext(contextId: "isRefreshing", value: false),
                        SetContext(
                            contextId: "content",
                            value: [
                                "color": "#0000FF",
                                "list": [
                                    "updated 0",
                                    "updated 1",
                                    "updated 2",
                                    "updated 3",
                                    "updated 4",
                                    "updated 5",
                                    "updated 6",
                                    "updated 7",
                                    "updated 8",
                                    "updated 9"
                                ]
                            ]
                        )
                    ]
                )
            ],
            isRefreshing: "@{isRefreshing}",
            color: "@{content.color}",
            child: ListView(
                dataSource: "@{content.list}",
                templates: [
                    Template(
                        view: Text("@{item}", widgetProperties: WidgetProperties(style: .init(margin: .init(all: 5))))
                    )
                ],
                widgetProperties: WidgetProperties(style: .init(size: .init(width: 100%, height: 100%)))
            )
        ), context: Context(
            id: "content",
            value: [
                "color": "#FF0000",
                "list": ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]
            ]
        )
    )
}()

struct CustomAsyncAction: AsyncAction, AutoDecodable {
    var onFinish: [Action]?
    
    func execute(controller: BeagleController, origin: UIView) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            controller.execute(actions: self.onFinish, event: "onFinish", origin: origin)
        }
    }
}
