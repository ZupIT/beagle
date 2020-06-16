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

struct WebViewScreen: DeeplinkScreen {
    init(path: String, data: [String: String]?) {}
    
    func screenController() -> UIViewController {
        return BeagleScreenViewController(.declarative(screen))
    }
    
    var screen: Screen {
        return Screen(
            navigationBar: NavigationBar(title: "WebView", showBackButton: true),
            child: webView
        )
    }
    
    var webView: BeagleUI.ServerDrivenComponent {
        return WebView(url: .WEB_VIEW_URL, flex: Flex().grow(1))
    }
}
