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

/// Typically displayed at the top of the window, containing buttons for navigating within a hierarchy of screens
public struct NavigationBar: Decodable {

    /// define the Title on the navigation bar
    public var title: String
    /// could define a custom layout for your action bar/navigation  bar
    public var styleId: String?
    /// enable a back button into your action bar/navigation bar
    public var showBackButton: Bool?
    /// define accessibility details for the item
    public var backButtonAccessibility: Accessibility?
    /// defines a List of navigation bar items.
    public var navigationBarItems: [NavigationBarItem]?

    public init(
        title: String,
        styleId: String? = nil,
        showBackButton: Bool? = nil,
        backButtonAccessibility: Accessibility? = nil,
        navigationBarItems: [NavigationBarItem]? = nil
    ) {
        self.title = title
        self.styleId = styleId
        self.showBackButton = showBackButton
        self.backButtonAccessibility = backButtonAccessibility
        self.navigationBarItems = navigationBarItems
    }

}
