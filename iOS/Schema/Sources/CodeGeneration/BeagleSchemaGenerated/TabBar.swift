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

///  TabBar is a component responsible to display a tab layout. It works by displaying tabs that can change a context when clicked.
public struct TabBar: RawComponent, AutoDecodable {

    /// define your tabs title and icon
    public var items: [TabBarItem]
    /// reference a native style in your local styles file to be applied on this view.
    public var styleId: String?
    /// define the expression that is observer to change the current tab selected
    public var currentTab: Expression<Int>?
    /// define a list of action that will be executed when a tab is selected
    public var onTabSelection: [RawAction]?

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

}
