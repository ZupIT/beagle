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
        Container(context: Context(id: "currentTab", value: 0), widgetProperties: .init(Flex().grow(1))) {
            TabBar(
                items: [
                    TabBarItem(icon: "beagle"),
                    TabBarItem(title: "Tab 1"),
                    TabBarItem(title: "Tab 2"),
                    TabBarItem(icon: "beagle", title: "Tab 3")
                ],
                currentTab: "@{currentTab}",
                onTabSelection: [SetContext(contextId: "currentTab", value: "@{onTabSelection}")]
            )
            PageView(
                onPageChange: [SetContext(contextId: "currentTab", value: "@{onPageChange}")],
                currentPage: "@{currentTab}"
            ) {
                Container(widgetProperties: .init(Flex().alignContent(.center))) {
                    Text("Text1 Tab 0")
                    Image(.value(.remote(.init(url: .NETWORK_IMAGE_BEAGLE, placeholder: "imageBeagle"))))
                    Text("Text2 Tab 0")
                }
                Container(widgetProperties: .init(Flex().justifyContent(.center).alignItems(.center))) {
                    Text("Text1 Tab 1")
                    Text("Text2 Tab 1")
                }
                Container(widgetProperties: .init(Flex().justifyContent(.flexStart))) {
                    Text("Text1 Tab 2")
                    Text("Text2 Tab 2")
                }
                Container(widgetProperties: .init(Flex().alignItems(.center))) {
                    Text("Text1 Tab 3")
                    Text("Text2 Tab 3")
                }
            }
        }
    }
}
