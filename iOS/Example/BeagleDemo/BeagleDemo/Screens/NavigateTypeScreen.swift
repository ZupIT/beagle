//
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

struct NavigateStep1Screen: DeeplinkScreen {
    init(path: String, data: [String: String]?) {
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(step1Screen))
    }
}

struct NavigateStep2Screen: DeeplinkScreen {
    init(path: String, data: [String: String]?) {
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(step2Screen))
    }
}

private var step1Screen =
    Screen(navigationBar: NavigationBar(title: "Step 1")) {
        Container {
            createButton(text: "PopView", action: Navigate.popView, backgroundColor: "#0f4c81")
            createButton(text: "PushView (Step 2)", action: Navigate.openNativeRoute(.init(route: .NAVIGATE_STEP2_ENDPOINT)), backgroundColor: "#ed6663")
        }
}

private var step2Screen =
    Screen(navigationBar: NavigationBar(title: "Step 2")) {
        Container {
            createButton(text: "PopView", action: Navigate.popView, backgroundColor: "#0f4c81")
            createButton(text: "PushView (Step 3)", action: Navigate.pushView(.declarative(step3Screen)), backgroundColor: "#ed6663")
            createButton(text: "PushStack", action: Navigate.pushStack(.declarative(presentView)), backgroundColor: "#ffa372")
        }
}

private var step3Screen =
    Screen(navigationBar: NavigationBar(title: "Step 3")) {
        Container {
            createButton(text: "PopView", action: Navigate.popView, backgroundColor: "#0f4c81")
            createButton(text: "ResetStack (Step 1)", action: Navigate.resetStack(.declarative(step1Screen)), backgroundColor: "#b7472a")
            createButton(text: "ResetApplication (Step 1)", action: Navigate.resetApplication(.declarative(step1Screen)), backgroundColor: "#ed6663")
            createButton(text: "PushView (Step 1)", action: Navigate.pushView(.declarative(step1Screen)), backgroundColor: "#c81912")
        }
}

private var presentView =
    Screen(navigationBar: NavigationBar(title: "Present")) {
        Container {
            createButton(text: "PushView (Step 1)", action: Navigate.pushView(.declarative(step1Screen)), backgroundColor: "#ed6663")
            createButton(text: "PopStack", action: Navigate.popStack, backgroundColor: "#2a7886")
        }
}

private func createButton(text: String, action: Navigate, backgroundColor: String) -> Button {
    Button(text: .value(text),
           styleId: "DesignSystem.Stylish.ButtonAndAppearance",
           onPress: [action],
           widgetProperties: WidgetProperties(style: Style()
            .backgroundColor(backgroundColor)
            .cornerRadius(.init(radius: 10.0))
            .margin(EdgeValue().left(30).right(30).top(10))))
}
