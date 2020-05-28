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

import Foundation
import UIKit
import Schema

public protocol DependencyRenderer {
    var renderer: (BeagleContext, RenderableDependencies) -> BeagleRenderer { get set }
}

public class BeagleRenderer {

    let context: BeagleContext
    let dependencies: RenderableDependencies

    internal init(context: BeagleContext, dependencies: RenderableDependencies) {
        self.context = context
        self.dependencies = dependencies
    }

    open func render(_ component: Schema.ServerDrivenComponent) -> UIView {
        guard let renderable = component as? Renderable else {
            assertionFailure("This should never happen since we ensure users only subscribe components that are Renderable")
            let type = String(describing: component.self)
            dependencies.logger.log(Log.decode(.decodingError(type: type)))
            return UnknownComponent(type: type).makeView()
        }

        let view = renderable.toView(renderer: self)

        // this switch could actually be inside the ViewConfigurator
        if let widget = renderable as? Widget {
            view.beagle.setup(widget)
        } else {
            if let c = renderable as? AccessibilityComponent {
                view.beagle.setup(accessibility: c.accessibility)
            }
            if let c = renderable as? IdentifiableComponent {
                view.beagle.setup(id: c.id)
            }
            if let c = renderable as? AppearanceComponent {
                view.beagle.setup(appearance: c.appearance)
            }
            if let c = renderable as? FlexComponent {
                view.flex.setup(c.flex)
            }
        }

        return view
    }

    open func render(_ children: [Schema.ServerDrivenComponent]) -> [UIView] {
        return children.map { render($0) }
    }
}
