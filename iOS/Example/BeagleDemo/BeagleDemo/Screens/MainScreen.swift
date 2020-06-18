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
                    action: Navigate.pushView(.remote(.NAVIGATE_ENDPOINT, shouldPrefetch: true))
                ),
                Button(
                    text: "Form & Lazy Component",
                    action: Navigate.openNativeRoute(.LAZY_COMPONENTS_ENDPOINT)
                ),
                Button(
                    text: "Page View",
                    action: Navigate.openNativeRoute(.PAGE_VIEW_ENDPOINT)
                ),
                Button(
                    text: "Tab View",
                    action: Navigate.openNativeRoute(.TAB_VIEW_ENDPOINT)
                ),
                Button(
                    text: "List View",
                    action: Navigate.openNativeRoute(.LIST_VIEW_ENDPOINT)
                ),
                Button(
                    text: "Form",
                    action: Navigate.openNativeRoute(.FORM_ENDPOINT)
                ),
                Button(
                    text: "Custom Component",
                    action: Navigate.openNativeRoute(.CUSTOM_COMPONENT_ENDPOINT)
                ),
                Button(
                    text: "Web View",
                    action: Navigate.openNativeRoute(.WEB_VIEW_ENDPOINT)
                ),
                Button(
                    text: "Component Interaction",
                    action: Navigate.pushView(.declarative(componentInteractionScreen))
                ),
                Button(
                    text: "Send Request",
                    action: Navigate.pushView(.declarative(sendRequestDeclarativeScreen))
                ),
                Button(
                    text: "Sample BFF",
                    action: Navigate.pushView(.remote(.COMPONENTS_ENDPOINT))
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
