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

public struct TabItem: Decodable {

    public let icon: StringOrExpression?
    public let title: String?
    public let child: RawComponent

    public init(
        icon: StringOrExpression? = nil,
        title: String? = nil,
        child: RawComponent
    ) {
        self.icon = icon
        self.title = title
        self.child = child
    }
    
    public init(
        icon: StringOrExpression? = nil,
        title: String? = nil,
        @ChildBuilder
        _ child: () -> RawComponent
    ) {
        self.init(icon: icon, title: title, child: child())
    }

    enum CodingKeys: String, CodingKey {
        case icon
        case title
        case child
    }
    
    enum LocalImageCodingKey: String, CodingKey {
        case mobileId
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let nestedContainer = try? container.nestedContainer(keyedBy: LocalImageCodingKey.self, forKey: .icon)
        icon = try nestedContainer?.decodeIfPresent(String.self, forKey: .mobileId)
        title = try container.decodeIfPresent(String.self, forKey: .title)
        child = try container.decode(forKey: .child)
    }
}

@available(*, deprecated, message: "Since version 1.1. Will be deleted in version 2.0. Consider replacing this component for a tabBar with a pageview.")
public struct TabView: RawComponent, AutoInitiable, HasContext {
    public let children: [TabItem]
    public let styleId: String?
    public let context: Context?

// sourcery:inline:auto:TabView.Init
    public init(
        children: [TabItem],
        styleId: String? = nil,
        context: Context? = nil
    ) {
        self.children = children
        self.styleId = styleId
        self.context = context
    }
// sourcery:end
    
    public init(
        context: Context? = nil,
        styleId: String? = nil,
        @TabItemsBuilder
        _ children: () -> [TabItem]
    ) {
        self.init(children: children(), styleId: styleId, context: context)
    }
    
    #if swift(<5.3)
    public init(
        context: Context? = nil,
        styleId: String? = nil,
        @TabItemBuilder
        _ children: () -> TabItem
    ) {
        self.init(children: [children()], styleId: styleId, context: context)
    }
    #endif
}
