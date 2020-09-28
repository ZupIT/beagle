// THIS IS A GENERATED FILE. DO NOT EDIT THIS
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

/// Define the view has in the tab view
public struct TabBarItem: Decodable {

    /// display an icon image on the TabView component. If it is left as null or not declared it won't display any icon.
    public var icon: String?
    /// displays the text on the TabView component. If it is null or not declared it won't display any text.
    public var title: String?

    public init(
        icon: String? = nil,
        title: String? = nil
    ) {
        self.icon = icon
        self.title = title
    }

}
