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

/// GridView is a Layout component that will define a list of views natively. These views could be any ServerDrivenComponent.
public struct GridView: Widget, HasContext, InitiableComponent {
    
    /// Defines the context of the component.
    public var context: Context?
    
    /// Allows to define a list of actions to be performed when the ListView is displayed.
    public let onInit: [Action]?
    
    /// It's an expression that points to a list of values used to populate the ListView.
    public let dataSource: Expression<[DynamicObject]>
    
    /// Points to a unique value present in each dataSource item used as a suffix in the component ids within the ListView.
    public let key: String?
    
    /// Sets number of columns
    public let numColumns: Int
    
    /// Represents each cell in the list through a ServerDrivenComponent.
    public let template: ServerDrivenComponent
    
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
    
    public init(
        context: Context? = nil,
        onInit: [Action]? = nil,
        dataSource: Expression<[DynamicObject]>,
        key: String? = nil,
        numColumns: Int,
        template: ServerDrivenComponent,
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
        self.template = template
        self.iteratorName = iteratorName
        self.onScrollEnd = onScrollEnd
        self.scrollEndThreshold = scrollEndThreshold
        self.isScrollIndicatorVisible = isScrollIndicatorVisible
        self.widgetProperties = widgetProperties
    }
}

extension GridView: Decodable {

    enum CodingKeys: String, CodingKey {
        case children
        case context
        case onInit
        case dataSource
        case key
        case numColumns
        case template
        case iteratorName
        case onScrollEnd
        case scrollEndThreshold
        case isScrollIndicatorVisible
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        iteratorName = try container.decodeIfPresent(String.self, forKey: .iteratorName)
        template = try container.decode(forKey: .template)
        dataSource = try container.decode(Expression<[DynamicObject]>.self, forKey: .dataSource)
        numColumns = try container.decode(Int.self, forKey: .numColumns)
        key = try container.decodeIfPresent(String.self, forKey: .key)
        context = try container.decodeIfPresent(Context.self, forKey: .context)
        onInit = try container.decodeIfPresent(forKey: .onInit)
        onScrollEnd = try container.decodeIfPresent(forKey: .onScrollEnd)
        scrollEndThreshold = try container.decodeIfPresent(Int.self, forKey: .scrollEndThreshold)
        isScrollIndicatorVisible = try container.decodeIfPresent(Bool.self, forKey: .isScrollIndicatorVisible)
        widgetProperties = try WidgetProperties(listFrom: decoder)
    }
}
