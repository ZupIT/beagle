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

import UIKit

public struct Container: RawWidget, HasContext, InitiableComponent, AutoDecodable {
    // MARK: - Public Properties
    public let children: [RawComponent]
    public var widgetProperties: WidgetProperties
    public let onInit: [RawAction]?
    public let context: Context?
    
    public init(
        children: [RawComponent],
        widgetProperties: WidgetProperties = WidgetProperties(),
        context: Context? = nil,
        onInit: [RawAction]? = nil
    ) {
        self.children = children
        self.widgetProperties = widgetProperties
        self.onInit = onInit
        self.context = context
    }
    
    public init(
        context: Context? = nil,
        onInit: [RawAction]? = nil,
        widgetProperties: WidgetProperties = WidgetProperties(),
        @ChildrenBuilder
        _ children: () -> [RawComponent]
    ) {
        self.init(children: children(), widgetProperties: widgetProperties, context: context, onInit: onInit)
    }
    
    public init(
        context: Context? = nil,
        onInit: [RawAction]? = nil,
        widgetProperties: WidgetProperties = WidgetProperties(),
        @ChildBuilder
        _ children: () -> RawComponent
    ) {
        self.init(children: [children()], widgetProperties: widgetProperties, context: context, onInit: onInit)
    }
}
