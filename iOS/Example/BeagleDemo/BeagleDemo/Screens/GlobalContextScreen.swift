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

struct GlobalContexScreen: DeeplinkScreen {
    
    init(path: String, data: [String: String]?) {
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(screen))
    }
    
    var screen: Screen {
        return Screen(navigationBar: NavigationBar(title: "Global Context", showBackButton: true)) {
            Container {
                Text("@{global.button}")
                Button(
                    text: "Click me",
                    onPress: [SetContext(contextId: "global", path: "button", value: "clicked")]
                )
                Button(
                    text: "Clear Global Context",
                    onPress: [ClearGlobalContextAction()]
                )
            }
        }
    }
    
}

struct ClearGlobalContextAction: Action {
    var analytics: ActionAnalyticsConfig? { return nil }
    
    func execute(controller: BeagleController, origin: UIView) {
        dependencies.globalContext.clear()
        controller.view.setNeedsLayout()
    }
}
