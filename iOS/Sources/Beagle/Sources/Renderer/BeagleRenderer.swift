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
        if let context = (component as? HasContext)?.context {
            view.setContext(context)
        }
    }
}

// MARK: - Observe Expressions

public extension BeagleRenderer {

    typealias Mapper<From, To> = (From) -> To

    // MARK: Property of same Expression's Value

    func observe<Value, View: UIView>(
        _ expression: Expression<Value>?,
        andUpdate keyPath: ReferenceWritableKeyPath<View, Value>,
        in view: View,
        map: Mapper<Value?, Value>? = nil
    ) {
        if let expression = expression {
            expression.observe(view: view, controller: controller) { value in
                view[keyPath: keyPath] = map?(value) ?? value
            }
        } else if let map = map {
            view[keyPath: keyPath] = map(nil)
        }
    }

    func observe<Value, View: UIView>(
        _ expression: Expression<Value>?,
        andUpdate keyPath: ReferenceWritableKeyPath<View, Value?>,
        in view: View,
        map: Mapper<Value?, Value?>? = nil
    ) {
        if let expression = expression {
            expression.observe(view: view, controller: controller) { value in
                view[keyPath: keyPath] = map?(value) ?? value
            }
        } else if let map = map {
            view[keyPath: keyPath] = map(nil)
        }
    }

    // MARK: Property with different type than expression

    func observe<Value, View: UIView, Property>(
        _ expression: Expression<Value>,
        andUpdate keyPath: ReferenceWritableKeyPath<View, Property>,
        in view: View,
        map: @escaping Mapper<Value, Property>
    ) {
        expression.observe(view: view, controller: controller) {
            view[keyPath: keyPath] = map($0)
        }
    }

    func observe<Value, View: UIView, Property>(
        _ expression: Expression<Value>?,
        andUpdate keyPath: ReferenceWritableKeyPath<View, Property>,
        in view: View,
        map: @escaping Mapper<Value?, Property>
    ) {
        observe(expression, andUpdateManyIn: view) {
            view[keyPath: keyPath] = map($0)
        }
    }

    // MARK: Update various properties (not just 1) in view

    /// will call `updateFunction` even when `expression` is nil
    func observe<Value>(
        _ expression: Expression<Value>?,
        andUpdateManyIn view: UIView,
        updateFunction: @escaping (Value?) -> Void
    ) {
        if let exp = expression {
            exp.observe(view: view, controller: controller, updateFunction: updateFunction)
        } else {
            updateFunction(nil)
        }
    }

    func observe<Value>(
        _ expression: Expression<Value>,
        andUpdateManyIn view: UIView,
        updateFunction: @escaping (Value) -> Void
    ) {
        expression.observe(view: view, controller: controller, updateFunction: updateFunction)
    }

    // TODO: should we make `observeMany` to simplify this to users?
}
