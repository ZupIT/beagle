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

public struct TabItem: AutoInitiableAndDecodable {

    public let icon: String?
    public let title: String?
    public let content: RawComponent

// sourcery:inline:auto:TabItem.Init
    public init(
        icon: String? = nil,
        title: String? = nil,
        content: RawComponent
    ) {
        self.icon = icon
        self.title = title
        self.content = content
    }
// sourcery:end
}

public struct TabView: RawComponent, AutoInitiable {
    public let tabItems: [TabItem]
    public let styleId: String?

// sourcery:inline:auto:TabView.Init
    public init(
        tabItems: [TabItem],
        styleId: String? = nil
    ) {
        self.tabItems = tabItems
        self.styleId = styleId
    }
// sourcery:end
}
