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

struct MainScreen: DeeplinkScreen {
    init() {}
    init(path: String, data: [String: String]?) {}
    
    func screenController() -> UIViewController {
        let screen = Screen(
            navigationBar: buildNavigationBar(),
            child: buildChild()
        )

        return BeagleScreenViewController(.declarative(screen))
    }
    
    private func buildChild() -> ScrollView {
        return ScrollView(
            children: [
                Button(
                    text: "Navigator",
                    onPress: [Navigate.pushView(.remote(.NAVIGATE_ENDPOINT, shouldPrefetch: true))]
                ),
                Button(
                    text: "Form & Lazy Component",
                    onPress: [Navigate.openNativeRoute(.LAZY_COMPONENTS_ENDPOINT)]
                ),
                Button(
                    text: "Page View",
                    onPress: [Navigate.openNativeRoute(.PAGE_VIEW_ENDPOINT)]
                ),
                Button(
                    text: "Tab View",
                    onPress: [Navigate.openNativeRoute(.TAB_VIEW_ENDPOINT)]
                ),
                Button(
                    text: "List View",
                    onPress: [Navigate.openNativeRoute(.LIST_VIEW_ENDPOINT)]
                ),
                Button(
                    text: "Form",
                    onPress: [Navigate.openNativeRoute(.FORM_ENDPOINT)]
                ),
                Button(
                    text: "Custom Component",
                    onPress: [Navigate.openNativeRoute(.CUSTOM_COMPONENT_ENDPOINT)]
                ),
                Button(
                    text: "Web View",
                    onPress: [Navigate.openNativeRoute(.WEB_VIEW_ENDPOINT)]
                ),
                Button(
                    text: "Send Request",
                    onPress: [Navigate.pushView(.declarative(sendRequestDeclarativeScreen))]
                ),
                Button(
                    text: "Component Interaction",
                    onPress: [Navigate.pushView(.declarative(componentInteractionScreen))]
                ),
                Button(
                    text: "Sample BFF",
                    onPress: [Navigate.pushView(.remote(.COMPONENTS_ENDPOINT))]
                )
            ]
        )
    }
    
    private func buildNavigationBar() -> NavigationBar {
        return NavigationBar(
            title: "Beagle Demo"
        )
    }

}
