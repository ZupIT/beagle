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
    var id: String? { get }
}

public protocol CommonComponents {
    var widgetProperties: WidgetProperties { get set }
}

public protocol Widget: CommonComponents, ServerDrivenComponent { }

public protocol Properties: AppearanceComponent, FlexComponent, AccessibilityComponent, IdentifiableComponent { }

public struct WidgetProperties: Properties, Equatable, Decodable {
    
    public var appearance: Appearance?
    public var flex: Flex?
    public var accessibility: Accessibility?
    public var id: String?
    
    enum WidgetCodingKeys: String, CodingKey {
        case id
        case appearance
        case accessibility
        case flex
    }
    
    /// Initializer for common widget attributes
    /// - Parameters:
    ///   - id: string containing an identifier. Default is nil.
    ///   - appearance: appearance of a widget. Default is nil.
    ///   - flex: flex of a widget . Default is nil.
    ///   - accessibility: accessibility of a widget. Default is nil.
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

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: WidgetCodingKeys.self)
        self.id = try container.decodeIfPresent(String.self, forKey: .id)
        self.appearance = try container.decodeIfPresent(Appearance.self, forKey: .appearance)
        self.flex = try container.decodeIfPresent(Flex.self, forKey: .flex)
        self.accessibility = try container.decodeIfPresent(Accessibility.self, forKey: .accessibility)
    }
}
