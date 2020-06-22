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

struct CustomComponentScreen: DeeplinkScreen {
    init(path: String, data: [String: String]?) {
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(screen))
    }
    
    var screen: Screen {
        return Screen(
            navigationBar: NavigationBar(title: "Custom Component"),
            child: Container(
                children: [
                    Text("Here its a custom component\n in this case a Collection View", alignment: .center),
                    DSCollection(
                        dataSource: DSCollectionDataSource(cards: [
                            DSCollectionDataSource.Card(name: "Pocas", age: 22),
                            DSCollectionDataSource.Card(name: "Borracha", age: 40),
                            DSCollectionDataSource.Card(name: "Gotto", age: 42),
                            DSCollectionDataSource.Card(name: "Tulio", age: 38)
                        ]),
                        widgetProperties: .init(
                            style: Style(
                                size: Size().width(100%).height(300)
                            )
                        )
                    )
                ]
            )
        )
    }
}
