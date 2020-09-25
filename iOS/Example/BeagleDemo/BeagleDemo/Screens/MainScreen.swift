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
                Text("@{global.button}")
                Button(
                    text: "LIMPAR CONTEXTO",
                    onPress: [ClearGlobalContextAction()]
                )
                Button(
                    text: "Navigator",
                    onPress: [Navigate.openNativeRoute(.init(route: .navigateStep1Endpoint)), SetContext(contextId: "global", path: "button", value: "Navigator")]
                )
                Button(
                    text: "Form & Lazy Component",
                    onPress: [Navigate.openNativeRoute(.init(route: .lazyComponentEndpoint)), SetContext(contextId: "global", path: "button", value: "Form & Lazy Component")]
                )
                Button(
                    text: "Page View",
                    onPress: [Navigate.openNativeRoute(.init(route: .pageViewEndpoint)), SetContext(contextId: "global", path: "button", value: "Page View")]
                )
                Button(
                    text: "Tab View",
                    onPress: [Navigate.openNativeRoute(.init(route: .tabViewEndpoint)), SetContext(contextId: "global", path: "button", value: "Tab View")]
                )
                Button(
                    text: "List View",
                    onPress: [Navigate.openNativeRoute(.init(route: .listViewEndpoint)), SetContext(contextId: "global", path: "button", value: "List View")]
                )
                Button(
                    text: "Form",
                    onPress: [Navigate.openNativeRoute(.init(route: .formEndpoint)), SetContext(contextId: "global", path: "button", value: "Form")]
                )
                Button(
                    text: "Custom Component",
                    onPress: [Navigate.openNativeRoute(.init(route: .customComponentEndpoint)), SetContext(contextId: "global", path: "button", value: "Custom Component")]
                )
                Button(
                    text: "Web View",
                    onPress: [Navigate.openNativeRoute(.init(route: .webViewEndpoint)), SetContext(contextId: "global", path: "button", value: "Web View")]
                )
                Button(
                    text: "Send Request",
                    onPress: [Navigate.pushView(.declarative(sendRequestDeclarativeScreen)), SetContext(contextId: "global", path: "button", value: "Send Request")]
                )
                Button(
                    text: "Component Interaction",
                    onPress: [Navigate.pushView(.declarative(componentInteractionScreen)), SetContext(contextId: "global", path: "button", value: "Component Interaction")]
                )
                Button(
                    text: "Context Operations",
                    onPress: [Navigate.pushView(.declarative(operationsMenuScreen)), SetContext(contextId: "global", path: "button", value: "Context Operations")]
                )
                Button(
                    text: "Conditional Action",
                    onPress: [Navigate.pushView(.declarative(conditionActionScreen)), SetContext(contextId: "global", path: "button", value: "Conditional Action")]
                )
                Button(
                    text: "Simple Form",
                    onPress: [Navigate.openNativeRoute(.init(route: .simpleFormEndpoint)), SetContext(contextId: "global", path: "button", value: "Simple Form")]
                )
                Button(
                    text: "Add Children",
                    onPress: [Navigate.pushView(.declarative(addChildrenScreen)), SetContext(contextId: "global", path: "button", value: "Add Children")]
                )
                Button(
                    text: "Sample BFF",
                    onPress: [Navigate.pushView(.remote(.init(url: .value(.componentsEndpoint)))), SetContext(contextId: "global", path: "button", value: "Sample BFF")]
                )
        }
    }
    
    private func buildNavigationBar() -> NavigationBar {
        return NavigationBar(
            title: "Beagle Demo"
        )
    }
}

struct ClearGlobalContextAction: Action {
    func execute(controller: BeagleController, origin: UIView) {
        dependencies.globalContext.clear(path: "button")
        controller.view.setNeedsLayout()
    }
}
