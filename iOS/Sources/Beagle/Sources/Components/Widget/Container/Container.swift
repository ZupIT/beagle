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

public struct Container: Widget, HasContext, InitiableComponent, AutoDecodable {
    // MARK: - Public Properties
    public let children: [ServerDrivenComponent]?
    public var widgetProperties: WidgetProperties
    public let onInit: [Action]?
    public let context: Context?
    public let styleId: String?

    public init(
        children: [ServerDrivenComponent]? = nil,
        widgetProperties: WidgetProperties = WidgetProperties(),
        context: Context? = nil,
        onInit: [Action]? = nil,
        styleId: String? = nil
    ) {
        self.children = children
        self.widgetProperties = widgetProperties
        self.onInit = onInit
        self.context = context
        self.styleId = styleId
    }
    
    public init(
        context: Context? = nil,
        onInit: [Action]? = nil,
        styleId: String? = nil,
        widgetProperties: WidgetProperties = WidgetProperties(),
        @ChildrenBuilder
        _ children: () -> [ServerDrivenComponent]
    ) {
        self.init(children: children(), widgetProperties: widgetProperties, context: context, onInit: onInit, styleId: styleId)
    }
    
    #if swift(<5.3)
    public init(
        context: Context? = nil,
        onInit: [Action]? = nil,
        styleId: String? = nil,
        widgetProperties: WidgetProperties = WidgetProperties(),
        @ChildBuilder
        _ children: () -> ServerDrivenComponent
    ) {
        self.init(children: [children()], widgetProperties: widgetProperties, context: context, onInit: onInit, styleId: styleId)
    }
    #endif
}
