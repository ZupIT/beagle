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
    init() {
        // Intentionally unimplemented...
    }
    init(path: String, data: [String: String]?) {
        // Intentionally unimplemented...
    }
    
    func screenController() -> UIViewController {
        let screen = Screen(
            navigationBar: buildNavigationBar(),
            child: buildChild()
        )

        return BeagleScreenViewController(.declarative(screen),
                                          controllerId: "CustomBeagleNavigation")
    }
    
    private func buildChild() -> ScrollView {
        return ScrollView {
                Button(
                    text: "Navigator",
                    onPress: [Navigate.openNativeRoute(.init(route: .navigateStep1Endpoint))]
                )
                Button(
                    text: "Form & Lazy Component",
                    onPress: [Navigate.openNativeRoute(.init(route: .lazyComponentEndpoint))]
                )
                Button(
                    text: "Page View",
                    onPress: [Navigate.openNativeRoute(.init(route: .pageViewEndpoint))]
                )
                Button(
                    text: "Tab Bar",
                    onPress: [Navigate.openNativeRoute(.init(route: .tabBarEndpoint))]
                )
                Button(
                    text: "List View",
                    onPress: [Navigate.openNativeRoute(.init(route: .listViewEndpoint))]
                )
                Button(
                    text: "Image",
                    onPress: [Navigate.openNativeRoute(.init(route: .imageEndpoint))]
                )
                Button(
                    text: "Form",
                    onPress: [Navigate.openNativeRoute(.init(route: .formEndpoint))]
                )
                Button(
                    text: "Custom Component",
                    onPress: [Navigate.openNativeRoute(.init(route: .customComponentEndpoint))]
                )
                Button(
                    text: "Web View",
                    onPress: [Navigate.openNativeRoute(.init(route: .webViewEndpoint))]
                )
                Button(
                    text: "Send Request",
                    onPress: [Navigate.pushView(.declarative(sendRequestDeclarativeScreen))]
                )
                Button(
                    text: "Component Interaction",
                    onPress: [Navigate.pushView(.declarative(componentInteractionScreen))]
                )
                Button(
                    text: "Context Operations",
                    onPress: [Navigate.pushView(.declarative(operationsMenuScreen))]
                )
                Button(
                    text: "Conditional Action",
                    onPress: [Navigate.pushView(.declarative(conditionActionScreen))]
                )
                Button(
                    text: "Simple Form",
                    onPress: [Navigate.openNativeRoute(.init(route: .simpleFormEndpoint))]
                )
                Button(
                    text: "Add Children",
                    onPress: [Navigate.pushView(.declarative(addChildrenScreen))]
                )
                Button(
                    text: "Beagle View",
                    onPress: [Navigate.openNativeRoute(.init(route: .beagleView))]
                )
                Button(
                    text: "Global Context",
                    onPress: [Navigate.openNativeRoute(.init(route: .globalContextEndpoint))]
                )
                Button(
                    text: "Sample BFF",
                    onPress: [Navigate.pushView(.remote(.init(url: .componentsEndpoint)))]
                )
        }
    }
    
    private func buildNavigationBar() -> NavigationBar {
        return NavigationBar(
            title: "Beagle Demo"
        )
    }

}
