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
    
    var screen =
        Screen(navigationBar: NavigationBar(title: "TabView")) {
            TabView {
                TabItem(icon: "beagle") {
                    Container(widgetProperties: .init(Flex().alignContent(.center))) {
                        Text("Blaaslkdjfaskldjfalskdjfasldjfasldfj")
                        Image(.value(.remote(.init(url: .NETWORK_IMAGE_BEAGLE, placeholder: "imageBeagle"))))
                        Text("Blaaslkdjfaskldjfalskdjfasldjfasldfj")
                    }
                }
                
                TabItem(title: "Tab 2 com titulo") {
                    Container(widgetProperties: .init(Flex().justifyContent(.center).alignItems(.center))) {
                        Text("Text1 Tab 2")
                        Text("Text2 Tab 2")
                    }
                }
                
                TabItem(title: "Tab 3") {
                    Container(widgetProperties: .init(Flex().justifyContent(.flexStart))) {
                        Text("Text1 Tab 3")
                        Text("Text2 Tab 3")
                    }
                }
                
                TabItem(icon: "beagle", title: "Tab 4") {
                    Container(widgetProperties: .init(Flex().alignItems(.center))) {
                        Text("Text1 Tab 4")
                        Text("Text2 Tab 4")
                    }
                }
                
                
                TabItem(icon: "beagle", title: "Tab with Tab view") {
                    Container(widgetProperties: .init(
                                style: .init(size: Size().width(400),
                                             flex: Flex().alignSelf(.center).alignItems(.center).grow(1)))) {
                            TabView {
                                TabItem(title: "Sub tab 1") {
                                    Container(widgetProperties: .init(Flex().alignItems(.center).grow(1))) {
                                        Text("Subtext 1 Tab 1")
                                        Text("Subtext 2 Tab 1")
                                    }
                                }
                                TabItem(title: "Sub tab 2") {
                                    Container(widgetProperties: .init(Flex().alignItems(.center).grow(1))) {
                                        Text("Subtext 1 Tab 2")
                                        Text("Subtext 2 Tab 2")
                                    }
                                }

                            }
                        }
                    }
            }
    }
}


