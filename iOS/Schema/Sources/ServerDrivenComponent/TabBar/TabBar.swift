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

public struct TabBar: RawComponent, AutoInitiableAndDecodable {
    public let items: [TabBarItem]
    public let styleId: String?
    public let currentTab: Expression<Int>?
    public let onTabSelection: [RawAction]?

// sourcery:inline:auto:TabBar.Init
    public init(
        items: [TabBarItem],
        styleId: String? = nil,
        currentTab: Expression<Int>? = nil,
        onTabSelection: [RawAction]? = nil
    ) {
        self.items = items
        self.styleId = styleId
        self.currentTab = currentTab
        self.onTabSelection = onTabSelection
    }
// sourcery:end
}

public struct TabBarItem: Decodable, AutoInitiable {
    public let icon: StringOrExpression?
    public let title: String?

// sourcery:inline:auto:TabBarItem.Init
    public init(
        icon: StringOrExpression? = nil,
        title: String? = nil
    ) {
        self.icon = icon
        self.title = title
    }
// sourcery:end
    
    enum CodingKeys: String, CodingKey {
        case icon
        case title
    }
    
    enum LocalImageCodingKey: String, CodingKey {
        case mobileId
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let nestedContainer = try? container.nestedContainer(keyedBy: LocalImageCodingKey.self, forKey: .icon)
        icon = try nestedContainer?.decodeIfPresent(String.self, forKey: .mobileId)
        title = try container.decodeIfPresent(String.self, forKey: .title)
    }
}
