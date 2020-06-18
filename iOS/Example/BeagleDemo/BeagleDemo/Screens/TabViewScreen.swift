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

struct TabViewScreen: DeeplinkScreen {
    init(path: String, data: [String: String]?) {
    }

    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(screen))
    }
    
    var screen: Screen {
        let tab1 = TabItem(icon: "beagle", child:
            Container(children: [
                Text("Blaaslkdjfaskldjfalskdjfasldjfasldfj"),
                NetworkImage(path: .NETWORK_IMAGE_BEAGLE),
                Text("Blaaslkdjfaskldjfalskdjfasldjfasldfj")
            ]).applyFlex(Flex().alignContent(.center))
        )

        let tab2 = TabItem(title: "Tab 2 com titulo", child:
            Container(children: [
                Text("Text1 Tab 2"),
                Text("Text2 Tab 2")
            ]).applyFlex(Flex().justifyContent(.center).alignItems(.center))
        )

        let tab3 = TabItem(title: "Tab 3", child:
            Container(children: [
                Text("Text1 Tab 3"),
                Text("Text2 Tab 3")
            ]).applyFlex(Flex().justifyContent(.flexStart))
        )

        let tab4 = TabItem(icon: "beagle", title: "Tab 4", child:
            Container(children: [
                Text("Text1 Tab 4"),
                Text("Text2 Tab 4")
            ]).applyFlex(Flex().alignItems(.center))
        )
        return Screen(navigationBar: NavigationBar(title: "TabView"), child: TabView(children: [tab1, tab2, tab3, tab4], styleId: .TAB_VIEW_STYLE))
    }
}
