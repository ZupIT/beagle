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

struct TabBarScreen: DeeplinkScreen {
    init(path: String, data: [String: String]?) {
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(screen))
    }
    
    var screen = Screen(navigationBar: NavigationBar(title: "TabBar")) {
        Container(context:
            Context(id: "tab",
                    value: [
                        "currentTab": 0,
                        "icon": [
                            "tab1": "beagle",
                            "tab4": "imageBeagle",
                            "tab5": "informationImage",
                            "tab8": "blackHole"
                        ]
                    ]
        ), widgetProperties: .init(Flex().grow(1))) {
            TabBar(
                items: [
                    TabBarItem(icon: "@{tab.icon.tab1}"),
                    TabBarItem(title: "Tab 2"),
                    TabBarItem(title: "Tab 3"),
                    TabBarItem(icon: "@{tab.icon.tab4}", title: "Tab 4"),
                    TabBarItem(icon: "@{tab.icon.tab5}"),
                    TabBarItem(title: "Tab 6"),
                    TabBarItem(title: "Tab 7"),
                    TabBarItem(icon: "@{tab.icon.tab8}", title: "Tab 8")
                ],
                styleId: .tabViewStyle,
                currentTab: "@{tab.currentTab}",
                onTabSelection: [SetContext(contextId: "tab", path: "currentTab", value: "@{onTabSelection}")]
            )
            PageView(
                onPageChange: [SetContext(contextId: "tab", path: "currentTab", value: "@{onPageChange}")],
                currentPage: "@{tab.currentTab}"
            ) {
                Container(widgetProperties: .init(Flex().alignContent(.center))) {
                    Text("Text1 Tab 1")
                    Image(.remote(.init(url: .networkImageBeagle, placeholder: "imageBeagle")))
                    Text("Text2 Tab 1")
                    Button(text: "change Context Tab 1", onPress: [
                        SetContext(contextId: "tab", path: "icon", value: [
                        "tab1": "beagle",
                        "tab4": "imageBeagle",
                        "tab5": "informationImage",
                        "tab8": "blackHole"
                        ])
                    ])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.center).alignItems(.center))) {
                    Text("Text1 Tab 2")
                    Text("Text2 Tab 2")
                    Button(text: "change Context Tab 2", onPress: [
                        SetContext(contextId: "tab", path: "icon", value: [
                        "tab1": "blackHole",
                        "tab4": "informationImage",
                        "tab5": "imageBeagle",
                        "tab8": "beagle"
                        ])
                    ])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.flexStart))) {
                    Text("Text1 Tab 3")
                    Text("Text2 Tab 3")
                }
                Container(widgetProperties: .init(Flex().alignItems(.center))) {
                    Text("Text1 Tab 4")
                    Text("Text2 Tab 4")
                    Button(text: "change Context Tab 4", onPress: [
                        SetContext(contextId: "tab", path: "icon", value: [
                        "tab1": "informationImage",
                        "tab4": "blackHole",
                        "tab5": "beagle",
                        "tab8": "imageBeagle"
                        ])
                    ])
                }
                Container(widgetProperties: .init(Flex().alignContent(.center))) {
                    Text("Text1 Tab 5")
                    Image(.remote(.init(url: .networkImageBeagle, placeholder: "imageBeagle")))
                    Text("Text2 Tab 5")
                    Button(text: "change Context Tab 5", onPress: [
                        SetContext(contextId: "tab", path: "icon", value: [
                        "tab1": "beagle",
                        "tab4": "informationImage",
                        "tab5": "beagle",
                        "tab8": "blackHole"
                        ])
                    ])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.center).alignItems(.center))) {
                    Text("Text1 Tab 6")
                    Text("Text2 Tab 6")
                    Button(text: "change Context Tab 6", onPress: [
                        SetContext(contextId: "tab", path: "icon", value: [
                        "tab1": "beagle",
                        "tab4": "informationImage",
                        "tab5": "beagle",
                        "tab8": "blackHole"
                        ])
                    ])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.flexStart))) {
                    Text("Text1 Tab 7")
                    Text("Text2 Tab 7")
                }
                Container(widgetProperties: .init(Flex().alignItems(.center))) {
                    Text("Text1 Tab 8")
                    Text("Text2 Tab 8")
                    Button(text: "change Context tab 8", onPress: [
                        SetContext(contextId: "tab", path: "icon", value: [
                        "tab1": "imageBeagle",
                        "tab4": "beagle",
                        "tab5": "blackHole",
                        "tab8": "informationImage"
                        ])
                    ])
                }
            }
        }
    }
}
