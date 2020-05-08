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
    public let style: String?
    public let showBackButton: Bool?
    public let backButtonAccessibility: Accessibility?
    public let navigationBarItems: [NavigationBarItem]?

// sourcery:inline:auto:NavigationBar.Init
    public init(
        title: String,
        style: String? = nil,
        showBackButton: Bool? = nil,
        backButtonAccessibility: Accessibility? = nil,
        navigationBarItems: [NavigationBarItem]? = nil
    ) {
        self.title = title
        self.style = style
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
    public let action: Action
    public let accessibility: Accessibility?

// sourcery:inline:auto:NavigationBarItem.Init
    public init(
        id: String? = nil,
        image: String? = nil,
        text: String,
        action: Action,
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

extension NavigationBarItem {
    
    public func toBarButtonItem(
        context: BeagleContext,
        dependencies: RenderableDependencies
    ) -> UIBarButtonItem {
        let barButtonItem = NavigationBarButtonItem(barItem: self, context: context, dependencies: dependencies)
        return barButtonItem
    }
    
    final private class NavigationBarButtonItem: UIBarButtonItem {
        
        private let barItem: NavigationBarItem
        private weak var context: BeagleContext?
        
        init(
            barItem: NavigationBarItem,
            context: BeagleContext,
            dependencies: RenderableDependencies
        ) {
            self.barItem = barItem
            self.context = context
            super.init()
            if let imageName = barItem.image {
                image = UIImage(named: imageName, in: dependencies.appBundle, compatibleWith: nil)?.withRenderingMode(.alwaysOriginal)
                accessibilityHint = barItem.text
            } else {
                title = barItem.text
            }
            accessibilityIdentifier = barItem.id
            target = self
            action = #selector(triggerAction)
            ViewConfigurator.applyAccessibility(barItem.accessibility, to: self)
        }
        
        required init?(coder aDecoder: NSCoder) {
            fatalError("init(coder:) has not been implemented")
        }
        
        @objc private func triggerAction() {
            context?.actionManager.doAction(barItem.action, sender: self)
        }
    }
}
