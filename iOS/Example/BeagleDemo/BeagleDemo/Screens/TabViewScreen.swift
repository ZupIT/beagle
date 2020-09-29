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

struct TabViewScreen: DeeplinkScreen {
    init(path: String, data: [String: String]?) {
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(screen))
    }
    
    var screen = Screen(navigationBar: NavigationBar(title: "TabView")) {
        Container(context:
            Context(id: "tab",
                    value: [
                        "currentTab": 2,
                        "icon": [
                            "tab1": "beagle",
                            "tab2": "imageBeagle",
                            "tab3": "informationImage"
                        ]
                    ]
        ), widgetProperties: .init(Flex().grow(1))) {
            TabBar(
                items: [
                    TabBarItem(icon: "@{tab.icon.tab1}"),
                    TabBarItem(title: "Tab 1"),
                    TabBarItem(title: "Tab 2"),
                    TabBarItem(icon: "@{tab.icon.tab2}", title: "Tab 3"),
                    TabBarItem(icon: "@{tab.icon.tab3}"),
                    TabBarItem(title: "Tab 4"),
                    TabBarItem(title: "Tab 5"),
                    TabBarItem(icon: "@{tab.icon.tab1}", title: "Tab 6"),
                    TabBarItem(icon: "@{tab.icon.tab2}"),
                    TabBarItem(title: "Tab 7"),
                    TabBarItem(title: "Tab 8"),
                    TabBarItem(icon: "@{tab.icon.tab3}", title: "Tab 9"),
                    TabBarItem(icon: "@{tab.icon.tab1}"),
                    TabBarItem(title: "Tab 10"),
                    TabBarItem(title: "Tab 12"),
                    TabBarItem(icon: "@{tab.icon.tab2}", title: "Tab 12"),
                    TabBarItem(icon: "@{tab.icon.tab3}"),
                    TabBarItem(title: "Tab 14"),
                    TabBarItem(title: "Tab 15"),
                    TabBarItem(icon: "@{tab.icon.tab1}", title: "Tab 16")
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
                    Text("Text1 Tab 0")
                    Image(.remote(.init(url: .networkImageBeagle, placeholder: "imageBeagle")))
                    Text("Text2 Tab 0")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "informationImage", "tab2": "beagle", "tab3": "imageBeagle"])])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.center).alignItems(.center))) {
                    Text("Text1 Tab 1")
                    Text("Text2 Tab 1")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "beagle", "tab2": "imageBeagle", "tab3": "informationImage"])])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.flexStart))) {
                    Text("Text1 Tab 2")
                    Text("Text2 Tab 2")
                }
                Container(widgetProperties: .init(Flex().alignItems(.center))) {
                    Text("Text1 Tab 3")
                    Text("Text2 Tab 3")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "imageBeagle", "tab2": "informationImage", "tab3": "beagle"])])
                }
                Container(widgetProperties: .init(Flex().alignContent(.center))) {
                    Text("Text1 Tab 0")
                    Image(.remote(.init(url: .networkImageBeagle, placeholder: "imageBeagle")))
                    Text("Text2 Tab 0")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "informationImage", "tab2": "beagle", "tab3": "imageBeagle"])])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.center).alignItems(.center))) {
                    Text("Text1 Tab 1")
                    Text("Text2 Tab 1")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "beagle", "tab2": "imageBeagle", "tab3": "informationImage"])])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.flexStart))) {
                    Text("Text1 Tab 2")
                    Text("Text2 Tab 2")
                }
                Container(widgetProperties: .init(Flex().alignItems(.center))) {
                    Text("Text1 Tab 3")
                    Text("Text2 Tab 3")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "imageBeagle", "tab2": "informationImage", "tab3": "beagle"])])
                }
                Container(widgetProperties: .init(Flex().alignContent(.center))) {
                    Text("Text1 Tab 0")
                    Image(.remote(.init(url: .networkImageBeagle, placeholder: "imageBeagle")))
                    Text("Text2 Tab 0")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "informationImage", "tab2": "beagle", "tab3": "imageBeagle"])])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.center).alignItems(.center))) {
                    Text("Text1 Tab 1")
                    Text("Text2 Tab 1")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "beagle", "tab2": "imageBeagle", "tab3": "informationImage"])])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.flexStart))) {
                    Text("Text1 Tab 2")
                    Text("Text2 Tab 2")
                }
                Container(widgetProperties: .init(Flex().alignItems(.center))) {
                    Text("Text1 Tab 3")
                    Text("Text2 Tab 3")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "imageBeagle", "tab2": "informationImage", "tab3": "beagle"])])
                }
                Container(widgetProperties: .init(Flex().alignContent(.center))) {
                    Text("Text1 Tab 0")
                    Image(.remote(.init(url: .networkImageBeagle, placeholder: "imageBeagle")))
                    Text("Text2 Tab 0")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "informationImage", "tab2": "beagle", "tab3": "imageBeagle"])])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.center).alignItems(.center))) {
                    Text("Text1 Tab 1")
                    Text("Text2 Tab 1")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "beagle", "tab2": "imageBeagle", "tab3": "informationImage"])])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.flexStart))) {
                    Text("Text1 Tab 2")
                    Text("Text2 Tab 2")
                }
                Container(widgetProperties: .init(Flex().alignItems(.center))) {
                    Text("Text1 Tab 3")
                    Text("Text2 Tab 3")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "imageBeagle", "tab2": "informationImage", "tab3": "beagle"])])
                }
                Container(widgetProperties: .init(Flex().alignContent(.center))) {
                    Text("Text1 Tab 0")
                    Image(.remote(.init(url: .networkImageBeagle, placeholder: "imageBeagle")))
                    Text("Text2 Tab 0")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "informationImage", "tab2": "beagle", "tab3": "imageBeagle"])])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.center).alignItems(.center))) {
                    Text("Text1 Tab 1")
                    Text("Text2 Tab 1")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "beagle", "tab2": "imageBeagle", "tab3": "informationImage"])])
                }
                Container(widgetProperties: .init(Flex().justifyContent(.flexStart))) {
                    Text("Text1 Tab 2")
                    Text("Text2 Tab 2")
                }
                Container(widgetProperties: .init(Flex().alignItems(.center))) {
                    Text("Text1 Tab 3")
                    Text("Text2 Tab 3")
                    Button(text: "change Context", onPress: [SetContext(contextId: "tab", path: "icon", value: ["tab1": "imageBeagle", "tab2": "informationImage", "tab3": "beagle"])])
                }

            }
        }
    }
}
