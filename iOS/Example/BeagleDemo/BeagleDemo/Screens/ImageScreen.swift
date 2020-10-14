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

struct ImageScreen: DeeplinkScreen {
    
    init(path: String, data: [String: String]?) {
        // Intentionally unimplemented...
    }
    
    func screenController() -> UIViewController {
        return Beagle.screen(.declarative(screen))
    }
    
    var screen: Screen {
        return Screen(
            navigationBar: NavigationBar(title: "Image"),
            child: container()
        )
    }
    
    private let sizeImage = WidgetProperties(
        style: Style(
            size: Size().height(100).width(100),
            flex: Flex().alignSelf(.center)
            )
    )
 
    private func container() -> Container {
        return Container(context: Context(
            id: "img",
            value: [
                "remote": .string(.networkImageBeagle),
                "local": "imageBeagle",
                "pathLocal": ["_beagleImagePath_": "local", "mobileId": "imageBeagle"],
                "pathRemote": ["_beagleImagePath_": "remote", "url": .string(.networkImageBeagle)]
            ]
        )) {
            ScrollView {
                createText(text: "Image url with context!")
                Image(
                    .remote(Image.Remote(url: "@{img.remote}")),
                    widgetProperties: sizeImage
                )
                
                createText(text: "Image mobileId with context!")
                Image(
                    .local("@{img.local}"),
                    widgetProperties: sizeImage
                )
                createText(text: "Image with type remote path with context!")
                Image(
                    "@{img.pathRemote}",
                    widgetProperties: sizeImage
                )
                createText(text: "Image with type local path with context!")
                Image(
                    "@{img.pathLocal}",
                    widgetProperties: sizeImage
                )
                createText(text: "Image url without context!")
                Image(
                    .remote(.init(url: .networkImageBeagle)),
                    widgetProperties: sizeImage
                )
                
                createText(text: "Image mobileId without context!")
                Image(
                    .local("beagle"),
                    widgetProperties: sizeImage
                )
                
                Button(
                    text: "Chage Context",
                    onPress: [
                        SetContext(
                            contextId: "img",
                            value: [
                                "remote": "https://cdn.eso.org/images/screen/eso1907a.jpg",
                                "local": "beagle",
                                "pathLocal": ["_beagleImagePath_": "local", "mobileId": "beagle"],
                                "pathRemote": ["_beagleImagePath_": "remote", "url": "https://cdn.eso.org/images/screen/eso1907a.jpg"]
                            ]
                        )
                    ]
                )
            }
        }
    }
    
    private func createText(text: String) -> Text {
        return Text(
            .value(text),
            widgetProperties: WidgetProperties(
                style: Style(
                    margin: EdgeValue().vertical(10),
                    flex: Flex().alignSelf(.center)
                )
            )
        )
    }
}
