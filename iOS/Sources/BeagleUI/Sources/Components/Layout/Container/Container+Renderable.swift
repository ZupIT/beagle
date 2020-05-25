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
import Schema

// MARK: - Configuration
extension Container {
    
    public func applyFlex(_ flex: Flex) -> Container {
        return Container(
            children: children,
            widgetProperties: .init(
                id: widgetProperties.id,
                appearance: widgetProperties.appearance,
                flex: flex,
                accessibility: widgetProperties.accessibility
            ))
    }
}

//TODO: avoid casting to serverDrivenComponent
extension Container: Widget {
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let containerView = UIView()
        
        children.forEach {
            if let childView = ($0 as? ServerDrivenComponent)?.toView(context: context, dependencies: dependencies) {
                containerView.addSubview(childView)
                childView.flex.isEnabled = true
            }
        }

        containerView.beagle.setup(self)
        
        return containerView
    }
}
