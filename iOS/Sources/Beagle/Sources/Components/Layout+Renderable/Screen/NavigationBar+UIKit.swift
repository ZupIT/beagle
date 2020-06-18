//
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
import BeagleSchema

extension NavigationBarItem {
    
    public func toBarButtonItem(
        controller: BeagleController
    ) -> UIBarButtonItem {
        let barButtonItem = NavigationBarButtonItem(barItem: self, controller: controller)
        return barButtonItem
    }
    
    final private class NavigationBarButtonItem: UIBarButtonItem {
        
        private let barItem: NavigationBarItem
        private weak var controller: BeagleController?
        
        init(
            barItem: NavigationBarItem,
            controller: BeagleController
        ) {
            self.barItem = barItem
            self.controller = controller
            super.init()
            if let imageName = barItem.image {
                image = UIImage(
                    named: imageName,
                    in: controller.dependencies.appBundle,
                    compatibleWith: nil
                )?.withRenderingMode(.alwaysOriginal)
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
            controller?.execute(action: barItem.action, sender: self)
        }
    }
}
