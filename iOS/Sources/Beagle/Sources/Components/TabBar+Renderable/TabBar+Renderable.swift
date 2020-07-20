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
        let view = TabBarUIComponent(model: .init(tabIndex: 0, tabViewItems: children))
        
        if let currentTab = currentTab {
            renderer.observe(currentTab, andUpdateManyIn: view) { tab in
                view.scrollTo(page: tab)
            }
        }
        
        view.onTabSelection = { tab in
            let context = Context(id: "onTabSelection", value: .int(tab))
            renderer.controller.execute(actions: self.onTabSelection, with: context, sender: view)
        }
        
        return view
    }
}
