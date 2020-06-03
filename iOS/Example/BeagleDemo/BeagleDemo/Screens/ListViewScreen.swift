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

struct ListViewScreen: DeeplinkScreen {
    
    init(path: String, data: [String : String]?) {
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(screen))
    }
    
    var screen: Screen {
        return Screen(
            navigationBar: NavigationBar(title: "ListView"),
            child: listView
        )
    }
    
    var listView = ListView(
        rows: [
            Touchable(action: Navigate.pushView(.remote(.NAVIGATE_ENDPOINT)), child: Text(.value("0000"))),
            Text(.value("0001"), widgetProperties: .init(flex: Flex().size(Size().width(100).height(100)))),
            Text(.value("0002")),
            Text(.value("0003")),
            Text(.value("0004")),
            LazyComponent(
                path: .TEXT_LAZY_COMPONENTS_ENDPOINT,
                initialState: Text(.value("Loading LazyComponent..."))
            ),
            Text(.value("0005")),
            Text(.value("0006")),
            Text(.value("0007")),
            Text(.value("0008")),
            Text(.value("0009")),
            Text(.value("0010")),
            Text(.value("0011")),
            Text(.value("0012")),
            Text(.value("0013")),
            Image(name: "beagle"),
            Text(.value("0014")),
            Text(.value("0015")),
            Text(.value("0016")),
            NetworkImage(path: .NETWORK_IMAGE_BEAGLE),
            Text(.value("0017")),
            Text(.value("0018")),
            Text(.value("0019")),
            Text(.value("0020")),
            Container(children: [Text(.value("Text1")), Text(.value("Text2"))], widgetProperties: .init(flex: Flex()))],
        direction: .horizontal)
}

