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

public struct TabItem: AutoInitiableAndDecodable {

    public let icon: String?
    public let title: String?
    public let content: ServerDrivenComponent

// sourcery:inline:auto:TabItem.Init
    public init(
        icon: String? = nil,
        title: String? = nil,
        content: ServerDrivenComponent
    ) {
        self.icon = icon
        self.title = title
        self.content = content
    }
// sourcery:end
}

public struct TabView: ServerDrivenComponent, AutoInitiable {
    public let tabItems: [TabItem]
    public let style: String?

// sourcery:inline:auto:TabView.Init
    public init(
        tabItems: [TabItem],
        style: String? = nil
    ) {
        self.tabItems = tabItems
        self.style = style
    }
// sourcery:end
}

extension TabView: Renderable {
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let model = TabViewUIComponent.Model(tabIndex: 0, tabViewItems: tabItems)
        let tabView = TabViewUIComponent(model: model)
        if let style = style {
            dependencies.theme.applyStyle(for: tabView as UIView, withId: style)
        }
        tabView.flex.setup(Flex(grow: 1))
        return tabView
    }
}
