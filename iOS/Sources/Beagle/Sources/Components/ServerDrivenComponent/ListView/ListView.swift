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

/// ListView is a Layout component that will define a list of views natively. These views could be any ServerDrivenComponent.
public struct ListView: Widget, HasContext, InitiableComponent, AutoInitiable {
    
    public typealias Direction = ScrollAxis
    
    /// Defines the context of the component.
    public var context: Context?
    
    /// Allows to define a list of actions to be performed when the ListView is displayed.
    public let onInit: [Action]?
    
    /// It's an expression that points to a list of values used to populate the ListView.
    public let dataSource: Expression<[DynamicObject]>
    
    /// Points to a unique value present in each dataSource item used as a suffix in the component ids within the ListView.
    public let key: String?
    
    /// Direction of the list scroll.
    public let direction: Direction?
    
    /// Templates available to the list items.
    /// The list will use the first template which matches the `Template.case`.
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
    
// sourcery:inline:auto:ListView.Init
    public init(
        context: Context? = nil,
        onInit: [Action]? = nil,
        dataSource: Expression<[DynamicObject]>,
        key: String? = nil,
        direction: Direction? = nil,
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
        self.direction = direction
        self.templates = templates
        self.iteratorName = iteratorName
        self.onScrollEnd = onScrollEnd
        self.scrollEndThreshold = scrollEndThreshold
        self.isScrollIndicatorVisible = isScrollIndicatorVisible
        self.widgetProperties = widgetProperties
    }
// sourcery:end
    
    // MARK: Deprecated initializers
    
    @available(*, deprecated, message: "use the templates instead of template")
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
        self.init(
            context: context,
            onInit: onInit,
            dataSource: dataSource,
            key: key,
            direction: direction,
            templates: [Template(view: template)],
            iteratorName: iteratorName,
            onScrollEnd: onScrollEnd,
            scrollEndThreshold: scrollEndThreshold,
            isScrollIndicatorVisible: isScrollIndicatorVisible,
            widgetProperties: widgetProperties
        )
    }

    private static func templateFor(children: [ServerDrivenComponent], direction: Direction?) -> ServerDrivenComponent {
        let style = Style(flex: Flex(flexDirection: direction?.flexDirection))
        return Container(children: children, widgetProperties: .init(style: style))
    }
    
    private static func randomIteratorName() -> String {
        return "__list_\(Int.random(in: 0...Int.max))"
    }

    /// Creates a list with content of the children. Deprecated: use the dataSource and template instead of children.
    /// - Parameters:
    ///   - children: Defines each cell of the ListView.
    ///   - direction: Defines the list direction.
    @available(*, deprecated, message: "use the dataSource and templates instead of children")
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
    @available(*, deprecated, message: "use the dataSource and templates instead of children")
    public init(
        direction: Direction = .vertical,
        @ChildBuilder
        _ children: () -> ServerDrivenComponent
    ) {
        self.init(children: [children()], direction: direction)
    }
    #endif

    @available(*, deprecated, message: "use the dataSource and templates instead of children")
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
        case templates
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
        
        if let templates: [Template] = try? container.decode([Template].self, forKey: .templates), !templates.isEmpty {
            self.dataSource = try container.decode(Expression<[DynamicObject]>.self, forKey: .dataSource)
            self.templates = templates
            self.iteratorName = iteratorName
        } else if let template: ServerDrivenComponent = try? container.decode(forKey: .template) {
            self.dataSource = try container.decode(Expression<[DynamicObject]>.self, forKey: .dataSource)
            self.templates = [Template(view: template)]
            self.iteratorName = iteratorName
        } else {
            let view = Self.templateFor(
                children: try container.decodeIfPresent(forKey: .children) ?? [],
                direction: direction
            )
            self.dataSource = .value([.empty])
            self.templates = [Template(view: view)]
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

    internal init(listFrom decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: DefaultCodingKeys.self)

        id = try container.decodeIfPresent(String.self, forKey: .id)
        style = try container.decodeIfPresent(Style.self, forKey: .style) ?? Style()
        accessibility = try container.decodeIfPresent(Accessibility.self, forKey: .accessibility)
    }
}

public struct Template: AutoInitiableAndDecodable {

    public let `case`: Expression<Bool>?
    public let view: ServerDrivenComponent

// sourcery:inline:auto:Template.Init
    public init(
        `case`: Expression<Bool>? = nil,
        view: ServerDrivenComponent
    ) {
        self.`case` = `case`
        self.view = view
    }
// sourcery:end
}
