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

/// GridView is a Layout component that will define a list of views natively. These views could be any ServerDrivenComponent.
public struct GridView: Widget, HasContext, InitiableComponent, AutoInitiableAndDecodable {
    
    /// Defines the context of the component.
    public var context: Context?
    
    /// Allows to define a list of actions to be performed when the GridView is displayed.
    public let onInit: [Action]?
    
    /// It's an expression that points to a list of values used to populate the GridView.
    public let dataSource: Expression<[DynamicObject]>
    
    /// Points to a unique value present in each dataSource item used as a suffix in the component ids within the GridView.
    public let key: String?
    
    /// Number of columns in the grid.
    public let numColumns: Int
    
    /// Templates available to the grid items.
    /// The grid will use the first template which matches the `Template.case`.
    /// When there is no match, the first template without a `case` will be used.
    public let templates: [Template]
    
    /// Is the context identifier of each cell.
    public let iteratorName: String?
    
    /// List of actions performed when the list is scrolled to the end.
    public let onScrollEnd: [Action]?
    
    /// Sets the scrolled percentage of the list to trigger onScrollEnd.
    public let scrollEndThreshold: Int?
    
    /// This attribute enables or disables the scroll indicator.
    public let isScrollIndicatorVisible: Bool?
    
    /// Properties that all widgets have in common.
    public var widgetProperties: WidgetProperties
    
// sourcery:inline:auto:GridView.Init
    public init(
        context: Context? = nil,
        onInit: [Action]? = nil,
        dataSource: Expression<[DynamicObject]>,
        key: String? = nil,
        numColumns: Int,
        templates: [Template],
        iteratorName: String? = nil,
        onScrollEnd: [Action]? = nil,
        scrollEndThreshold: Int? = nil,
        isScrollIndicatorVisible: Bool? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.context = context
        self.onInit = onInit
        self.dataSource = dataSource
        self.key = key
        self.numColumns = numColumns
        self.templates = templates
        self.iteratorName = iteratorName
        self.onScrollEnd = onScrollEnd
        self.scrollEndThreshold = scrollEndThreshold
        self.isScrollIndicatorVisible = isScrollIndicatorVisible
        self.widgetProperties = widgetProperties
    }
// sourcery:end
}
