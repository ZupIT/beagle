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
        let tabBarScroll = TabBarUIComponent(model: .init(tabBarItems: items, renderer: renderer))
        
        tabBarScroll.isScrollEnabled = true
        tabBarScroll.showsHorizontalScrollIndicator = false
        tabBarScroll.showsVerticalScrollIndicator = false
        tabBarScroll.isUserInteractionEnabled = true
        
        if let currentTab = currentTab {
            renderer.observe(currentTab, andUpdateManyIn: tabBarScroll) {
                if let tab = $0 {
                    tabBarScroll.scrollTo(page: tab)
                }
            }
        }

        tabBarScroll.onTabSelection = { tab in
            renderer.controller.execute(actions: self.onTabSelection, with: "onTabSelection", and: .int(tab), origin: tabBarScroll)
        }

        if let styleId = styleId {
            tabBarScroll.beagle.applyStyle(for: tabBarScroll as UIView, styleId: styleId, with: renderer.controller)
        }
        
        tabBarScroll.yoga.overflow = .scroll
        tabBarScroll.style.setup(
            Style(size: Size().width(100%).height(65),
                  flex: Flex().flexDirection(.row))
        )
        return tabBarScroll
    }
}
