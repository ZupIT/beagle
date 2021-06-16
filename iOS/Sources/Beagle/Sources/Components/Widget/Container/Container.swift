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

/// The container component is a general container that can hold other components inside.
public struct Container: Widget, HasContext, InitiableComponent, AutoDecodable {
    
    /// Defines a list of components that are part of the container.
    public let children: [ServerDrivenComponent]?
    
    /// Properties that all widgets have in common.
    public var widgetProperties: WidgetProperties
    
    /// it is a parameter that allows you to define a list of actions to be performed when the Widget is displayed.
    public let onInit: [Action]?
    
    /// Defines the contextData that be set to container.
    public let context: Context?
    
    /// References a native style configured to be applied on this container.
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
