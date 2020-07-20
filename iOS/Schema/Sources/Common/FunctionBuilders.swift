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

@_functionBuilder
public final class ComponentBuilder {
    public static func buildBlock(_ component: RawComponent) -> RawComponent {
        return component
    }
}

@_functionBuilder
public final class ComponentsBuilder {
    public static func buildBlock(_ components: RawComponent...) -> [RawComponent] { return components
    }
}

@_functionBuilder
public final class TabItemsBuilder {
    public static func buildBlock(_ tabItems: TabItem...) -> [TabItem] {
        return tabItems
    }
}

@_functionBuilder
public final class TabItemBuilder {
    public static func buildBlock(_ tabItem: TabItem) -> TabItem {
        return tabItem
    }
}
