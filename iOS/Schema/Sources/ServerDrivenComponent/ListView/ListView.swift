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

public struct ListView: RawWidget, HasContext, InitiableComponent {
    
    public var context: Context?
    public let onInit: [RawAction]?
    public let dataSource: Expression<[DynamicObject]>
    public let key: String?
    public let direction: Direction?
    public let template: RawComponent
    public let iteratorName: String?
    public let onScrollEnd: [RawAction]?
    public let scrollThreshold: Int?
    public let useParentScroll: Bool?
    public var widgetProperties: WidgetProperties
    
    public init(
        context: Context? = nil,
        onInit: [RawAction]? = nil,
        dataSource: Expression<[DynamicObject]>,
        key: String? = nil,
        direction: Direction? = nil,
        template: RawComponent,
        iteratorName: String? = nil,
        onScrollEnd: [RawAction]? = nil,
        scrollThreshold: Int? = nil,
        useParentScroll: Bool? = nil,
        widgetProperties: WidgetProperties = WidgetProperties(style: Style())
    ) {
        self.context = context
        self.onInit = onInit
        self.dataSource = dataSource
        self.key = key
        self.direction = direction
        self.template = template
        self.iteratorName = iteratorName
        self.onScrollEnd = onScrollEnd
        self.scrollThreshold = scrollThreshold
        self.useParentScroll = useParentScroll
        self.widgetProperties = widgetProperties
    }
    
    // MARK: Deprecated initializers
    
    private static func templateFor(children: [RawComponent], direction: Direction?) -> RawComponent {
        let style = Style(flex: Flex(flexDirection: direction?.flexDirection))
        return Container(children: children, widgetProperties: .init(style: style))
    }
    
    private static func randomIteratorName() -> String {
        return "__list_\(Int.random(in: 0...Int.max))"
    }

    @available(*, deprecated, message: "use the dataSource and template instead of children")
    public init(
        children: [RawComponent],
        direction: Direction = .vertical
    ) {
        self.init(
            dataSource: .value([.empty]),
            direction: direction,
            template: Self.templateFor(children: children, direction: direction),
            iteratorName: Self.randomIteratorName()
        )
    }

    @available(*, deprecated, message: "use the dataSource and template instead of children")
    public init(
        direction: Direction = .vertical,
        @ChildBuilder
        _ children: () -> RawComponent
    ) {
        self.init(children: [children()], direction: direction)
    }

    @available(*, deprecated, message: "use the dataSource and template instead of children")
    public init(
        direction: Direction = .vertical,
        @ChildrenBuilder
        _ children: () -> [RawComponent]
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
        case scrollThreshold
        case useParentScroll
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
        scrollThreshold = try container.decodeIfPresent(Int.self, forKey: .scrollThreshold)
        useParentScroll = try container.decodeIfPresent(Bool.self, forKey: .useParentScroll)
        widgetProperties = try WidgetProperties(listFrom: decoder)
        
        if container.contains(.children) {
            template = Self.templateFor(
                children: try container.decode(forKey: .children),
                direction: direction
            )
            dataSource = .value([.empty])
            self.iteratorName = iteratorName ?? Self.randomIteratorName()
        } else {
            dataSource = try container.decode(Expression<[DynamicObject]>.self, forKey: .dataSource)
            template = try container.decode(forKey: .template)
            self.iteratorName = iteratorName
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
        
        public var flexDirection: Flex.FlexDirection {
            switch self {
            case .vertical:
                return .column
            case .horizontal:
                return .row
            }
        }
    }
}
