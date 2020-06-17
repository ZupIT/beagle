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

struct ListViewScreen: DeeplinkScreen {
    
    init(path: String, data: [String: String]?) {
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
            Touchable(action: Navigate.pushView(.remote(.NAVIGATE_ENDPOINT)), child: Text("0000")),
            Text("0001", widgetProperties: .init(flex: Flex().size(Size().width(100).height(100)))),
            Text("0002"),
            Text("0003"),
            Text("0004"),
            LazyComponent(
                path: .TEXT_LAZY_COMPONENTS_ENDPOINT,
                initialState: Text("Loading LazyComponent...")
            ),
            Text("0005"),
            Text("0006"),
            Text("0007"),
            Text("0008"),
            Text("0009"),
            Text("0010"),
            Text("0011"),
            Text("0012"),
            Text("0013"),
            Image(name: "beagle"),
            Text("0014"),
            Text("0015"),
            Text("0016"),
            NetworkImage(path: .NETWORK_IMAGE_BEAGLE),
            Text("0017"),
            Text("0018"),
            Text("0019"),
            Text("0020"),
            Container(children: [Text("Text1"), Text("Text2")])
        ],
        direction: .horizontal
    )
}
