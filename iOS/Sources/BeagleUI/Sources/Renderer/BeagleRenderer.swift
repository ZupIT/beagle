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
import BeagleSchema

public protocol DependencyRenderer {
    var renderer: (BeagleController) -> BeagleRenderer { get set }
}

/// Use this class whenever you want to transform a Component into a UIView
open class BeagleRenderer {

    public unowned var controller: BeagleController

    internal init(controller: BeagleController) {
        self.controller = controller
    }

    /// main function of this class. Call it to transform a Component into a UIView
    open func render(_ component: BeagleSchema.RawComponent) -> UIView {
        let view = makeView(component: component)

        setupView(view, of: component)

        return view
    }

    open func render(_ children: [BeagleSchema.RawComponent]) -> [UIView] {
        return children.map { render($0) }
    }

    private func makeView(component: BeagleSchema.RawComponent) -> UIView {
        guard let renderable = component as? Renderable else {
            assertionFailure("This should never happen since we ensure users only subscribe components that are Renderable")
            let type = String(describing: component.self)
            controller.dependencies.logger.log(Log.decode(.decodingError(type: type)))
            return UnknownComponent(type: type).makeView()
        }

        return renderable.toView(renderer: self)
    }

    private func setupView(_ view: UIView, of component: BeagleSchema.RawComponent) {
        view.style.isFlexEnabled = true

        // this switch could actually be inside the ViewConfigurator
        if let widget = component as? Widget {
            view.beagle.setup(widget)
            return
        }

        if let c = component as? AccessibilityComponent {
            view.beagle.setup(accessibility: c.accessibility)
        }
        if let c = component as? IdentifiableComponent {
            view.beagle.setup(id: c.id)
        }
        if let c = component as? StyleComponent {
            view.beagle.setup(style: c.style)
            view.style.setup(c.style)
        }
        if let c = component as? HasContext, let context = c._context_ {
            view.setContext(context)
        }
    }
}
