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

import Foundation

public struct TabBar: RawComponent, AutoInitiable, HasContext {
    public let children: [TabBarItem]
    public let styleId: String?
    public let context: Context?

// sourcery:inline:auto:TabBar.Init
    public init(
        children: [TabBarItem],
        styleId: String? = nil,
        context: Context? = nil
    ) {
        self.children = children
        self.styleId = styleId
        self.context = context
    }
// sourcery:end
}

public struct TabBarItem: Decodable, AutoInitiable {
    public let icon: String?
    public let title: String?

// sourcery:inline:auto:TabBarItem.Init
    public init(
        icon: String? = nil,
        title: String? = nil
    ) {
        self.icon = icon
        self.title = title
    }
// sourcery:end
}
