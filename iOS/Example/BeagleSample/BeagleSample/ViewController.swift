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

struct MainScreen {

    func screenController() -> UIViewController {
        let screen = Screen(
            navigationBar: buildNavigationBar(),
            child: buildChild()
        )

        return BeagleScreenViewController(.declarative(screen))
    }
    
    private func buildChild() -> Container {
        return Container(
            children: [
                Text("Beagle Integration Tests",
                     widgetProperties: .init(
                        style: Style()
                            .flex(Flex().alignSelf(.center)))),
                Button(text: "Go to next Page", onPress: [
                    Navigate.pushView(Route.remote("https://run.mocky.io/v3/bcb3b03e-8e96-4554-aa71-7c2cf8bbc875", shouldPrefetch: false))
                ], widgetProperties: .init(
                    style: Style()
                        .backgroundColor("#2B2D2A")
                        .cornerRadius(CornerRadius(radius: 10))
                        .margin(.init(top: 15))
                        .size(Size().height(50).width(200))
                        .flex(Flex().alignSelf(.center)))
                )
            ], widgetProperties: .init(
                style: Style().flex(Flex().justifyContent(.center).grow(1)))
        )
    }
    
    private func buildNavigationBar() -> NavigationBar {
        return NavigationBar(
            title: "Beagle Sample"
        )
    }
}
