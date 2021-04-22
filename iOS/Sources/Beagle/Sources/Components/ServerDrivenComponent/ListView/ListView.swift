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

public struct ListView: Widget, HasContext, InitiableComponent {
    
    public var context: Context?
    public let onInit: [Action]?
    public let dataSource: Expression<[DynamicObject]>
    public let key: String?
    public let direction: Direction?
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
        direction: Direction? = nil,
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
        self.direction = direction
        self.template = template
        self.iteratorName = iteratorName
        self.onScrollEnd = onScrollEnd
        self.scrollEndThreshold = scrollEndThreshold
        self.isScrollIndicatorVisible = isScrollIndicatorVisible
        self.widgetProperties = widgetProperties
    }
    
    // MARK: Deprecated initializers
    
    private static func templateFor(children: [ServerDrivenComponent], direction: Direction?) -> ServerDrivenComponent {
        let style = Style(flex: Flex(flexDirection: direction?.flexDirection))
        return Container(children: children, widgetProperties: .init(style: style))
    }
    
    private static func randomIteratorName() -> String {
        return "__list_\(Int.random(in: 0...Int.max))"
    }

    @available(*, deprecated, message: "use the dataSource and template instead of children")
    public init(
        children: [ServerDrivenComponent]? = nil,
        direction: Direction = .vertical
    ) {
        self.init(
            dataSource: .value([.empty]),
            direction: direction,
            template: Self.templateFor(children: children ?? [], direction: direction),
            iteratorName: Self.randomIteratorName()
        )
    }

    #if swift(<5.3)
    @available(*, deprecated, message: "use the dataSource and template instead of children")
    public init(
        direction: Direction = .vertical,
        @ChildBuilder
        _ children: () -> ServerDrivenComponent
    ) {
        self.init(children: [children()], direction: direction)
    }
    #endif

    @available(*, deprecated, message: "use the dataSource and template instead of children")
    public init(
        direction: Direction = .vertical,
        @ChildrenBuilder
        _ children: () -> [ServerDrivenComponent]
    ) {
        self.init(children: children(), direction: direction)
    }
    
}

extension ListView: Decodable {

    enum CodingKeys: String, CodingKey {
        case children
        case context
        case onInit
        case dataSource
        case key
        case direction
        case template
        case iteratorName
        case onScrollEnd
        case scrollEndThreshold
        case isScrollIndicatorVisible
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        
        let iteratorName = try container.decodeIfPresent(String.self, forKey: .iteratorName)
        let direction = try container.decodeIfPresent(Direction.self, forKey: .direction)
        
        self.direction = direction
        key = try container.decodeIfPresent(String.self, forKey: .key)
        context = try container.decodeIfPresent(Context.self, forKey: .context)
        onInit = try container.decodeIfPresent(forKey: .onInit)
        onScrollEnd = try container.decodeIfPresent(forKey: .onScrollEnd)
        scrollEndThreshold = try container.decodeIfPresent(Int.self, forKey: .scrollEndThreshold)
        isScrollIndicatorVisible = try container.decodeIfPresent(Bool.self, forKey: .isScrollIndicatorVisible)
        widgetProperties = try WidgetProperties(listFrom: decoder)
        
        if let template: ServerDrivenComponent = try? container.decode(forKey: .template) {
            dataSource = try container.decode(Expression<[DynamicObject]>.self, forKey: .dataSource)
            self.template = template
            self.iteratorName = iteratorName
        } else {
            template = Self.templateFor(
                children: try container.decodeIfPresent(forKey: .children) ?? [],
                direction: direction
            )
            dataSource = .value([.empty])
            self.iteratorName = iteratorName ?? Self.randomIteratorName()
        }
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

extension ListView {
    public enum Direction: String, Decodable {
        case vertical = "VERTICAL"
        case horizontal = "HORIZONTAL"
    }
}
