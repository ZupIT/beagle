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

public struct TabItem {

    public let icon: String?
    public let title: String?
    public let content: ServerDrivenComponent
    
    public init(
        icon: String? = nil,
        title: String? = nil,
        content: ServerDrivenComponent
    ) {
        self.icon = icon
        self.title = title
        self.content = content
    }
}

extension TabItem: Decodable {
    enum CodingKeys: String, CodingKey {
        case icon
        case title
        case content
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.icon = try container.decodeIfPresent(String.self, forKey: .icon)
        self.title = try container.decodeIfPresent(String.self, forKey: .title)
        self.content = try container.decode(forKey: .content)
    }
}

public struct TabView: ServerDrivenComponent {
    public let tabItems: [TabItem]
    public let style: String?
    
    public init(
        tabItems: [TabItem],
        style: String? = nil
    ) {
        self.tabItems = tabItems
        self.style = style
    }
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
