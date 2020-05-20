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

public protocol ComponentWidget {
    var widgetProperties: WidgetProperties { get set }
}

// MARK: - Widget Properties

public protocol HasWidgetProperties: AppearanceComponent, FlexComponent, AccessibilityComponent, IdentifiableComponent { }

public protocol AppearanceComponent {
    var appearance: Appearance? { get }
}

public protocol FlexComponent {
    var flex: Flex? { get }
}

public protocol AccessibilityComponent {
    var accessibility: Accessibility? { get }
}

public protocol IdentifiableComponent {
    /// string that uniquely identifies a component
    var id: String? { get }
}

/// Properties that all widgets have and are important to Beagle.
public struct WidgetProperties: HasWidgetProperties, AutoDecodable, Equatable, AutoInitiable {

    public var id: String?
    public var appearance: Appearance?
    public var flex: Flex?
    public var accessibility: Accessibility?

// sourcery:inline:auto:WidgetProperties.Init
    public init(
        id: String? = nil,
        appearance: Appearance? = nil,
        flex: Flex? = nil,
        accessibility: Accessibility? = nil
    ) {
        self.id = id
        self.appearance = appearance
        self.flex = flex
        self.accessibility = accessibility
    }
// sourcery:end
}
