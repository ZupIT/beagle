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

public struct GridView: Widget, HasContext, InitiableComponent {
    
    public var context: Context?
    public let onInit: [Action]?
    public let dataSource: Expression<[DynamicObject]>
    public let key: String?
    public let numColumns: Int
    public let template: ServerDrivenComponent
    public let iteratorName: String?
    public let onScrollEnd: [Action]?
    public let scrollEndThreshold: Int?
    public let isScrollIndicatorVisible: Bool?
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

extension WidgetProperties {
   private enum DefaultCodingKeys: String, CodingKey {
        case id
        case style
        case accessibility
    }

    fileprivate init(listFrom decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: DefaultCodingKeys.self)

        id = try container.decodeIfPresent(String.self, forKey: .id)
        style = try container.decodeIfPresent(Style.self, forKey: .style) ?? Style()
        accessibility = try container.decodeIfPresent(Accessibility.self, forKey: .accessibility)
    }

}

extension GridView {
    public enum Direction: String, Decodable {
        case vertical = "VERTICAL"
        case horizontal = "HORIZONTAL"
    }
}
