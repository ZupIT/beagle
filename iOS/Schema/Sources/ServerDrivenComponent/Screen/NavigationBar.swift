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
import UIKit

public struct NavigationBar: Decodable, AutoInitiable {

    public let title: String
    public let styleId: String?
    public let showBackButton: Bool?
    public let backButtonAccessibility: Accessibility?
    public let navigationBarItems: [NavigationBarItem]?

// sourcery:inline:auto:NavigationBar.Init
    public init(
        title: String,
        styleId: String? = nil,
        showBackButton: Bool? = nil,
        backButtonAccessibility: Accessibility? = nil,
        navigationBarItems: [NavigationBarItem]? = nil
    ) {
        self.title = title
        self.styleId = styleId
        self.showBackButton = showBackButton
        self.backButtonAccessibility = backButtonAccessibility
        self.navigationBarItems = navigationBarItems
    }
// sourcery:end
}

public struct NavigationBarItem: AutoInitiableAndDecodable, AccessibilityComponent, IdentifiableComponent {
    
    public let id: String?
    public let image: String?
    public let text: String
    public let action: RawAction
    public let accessibility: Accessibility?

// sourcery:inline:auto:NavigationBarItem.Init
    public init(
        id: String? = nil,
        image: String? = nil,
        text: String,
        action: RawAction,
        accessibility: Accessibility? = nil
    ) {
        self.id = id
        self.image = image
        self.text = text
        self.action = action
        self.accessibility = accessibility
    }
// sourcery:end
}
