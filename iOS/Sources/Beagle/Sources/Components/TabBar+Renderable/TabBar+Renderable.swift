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
import BeagleSchema

extension TabBar: ServerDrivenComponent {
    public func toView(renderer: BeagleRenderer) -> UIView {
        let view = TabBarUIComponent(model: .init(tabIndex: 0, tabBarItems: items))
        
        if let currentTab = currentTab {
            renderer.observe(currentTab, andUpdateManyIn: view) {
                if let tab = $0 {
                    view.scrollTo(page: tab)
                }
            }
        }
        
        view.onTabSelection = { tab in
            renderer.controller.execute(actions: self.onTabSelection, with: "onTabSelection", and: .int(tab), origin: view)
        }
        
        if let styleId = styleId {
            view.beagle.applyStyle(for: view as UIView, styleId: styleId, with: renderer.controller)
        }

        return view
    }
}
