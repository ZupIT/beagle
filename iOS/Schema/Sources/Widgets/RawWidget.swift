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

import Foundation

public protocol RawWidget: RawComponent, HasWidgetProperties {

    var widgetProperties: WidgetProperties { get set }
}

// MARK: - Widget Properties
public extension RawWidget {

    var style: Style? {
        get { return widgetProperties.style }
        set { widgetProperties.style = newValue }
    }

    var accessibility: Accessibility? {
        get { return widgetProperties.accessibility }
        set { widgetProperties.accessibility = newValue }
    }

    var id: String? {
        get { return widgetProperties.id }
        set { widgetProperties.id = newValue }
    }
}

public protocol HasWidgetProperties: StyleComponent, AccessibilityComponent, IdentifiableComponent { }

public protocol HasContext {
    var context: Context? { get }
}

public protocol StyleComponent {
    var style: Style? { get }
}

public protocol AccessibilityComponent {
    var accessibility: Accessibility? { get }
}

public protocol IdentifiableComponent {
    /// string that uniquely identifies a component
    var id: String? { get }
}

public protocol InitiableComponent {
    var onInit: [RawAction]? { get }
}

/// Properties that all widgets have and are important to Beagle.
public struct WidgetProperties: HasWidgetProperties, AutoDecodable, Equatable, AutoInitiable {

    public var id: String?
    public var style: Style?
    public var accessibility: Accessibility?

// sourcery:inline:auto:WidgetProperties.Init
    public init(
        id: String? = nil,
        style: Style? = nil,
        accessibility: Accessibility? = nil
    ) {
        self.id = id
        self.style = style
        self.accessibility = accessibility
    }
// sourcery:end
    
    public init(
        _ flex: Flex
    ) {
        self.init(style: Style(flex: flex))
    }
}
