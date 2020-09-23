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

public protocol ViewConfiguratorProtocol: AnyObject {
    var view: UIView? { get set }

    func setup(style: Style?)
    func setup(id: String?)
    func setup(accessibility: Accessibility?)
    func applyStyle<T: UIView>(for view: T, styleId: String, with controller: BeagleController?)
    func setupView(of component: BeagleSchema.RawComponent)
}

public protocol DependencyViewConfigurator {
    var viewConfigurator: (UIView) -> ViewConfiguratorProtocol { get }
}

public extension UIView {

    var beagle: ViewConfiguratorProtocol {
        return Beagle.dependencies.viewConfigurator(self)
    }
}

class ViewConfigurator: ViewConfiguratorProtocol {

    weak var view: UIView?

    init(view: UIView) {
        self.view = view
    }
    
    func setupView(of component: BeagleSchema.RawComponent) {
        view?.style.isFlexEnabled = true
        
        if let c = component as? AccessibilityComponent {
            setup(accessibility: c.accessibility)
        }
        
        if let c = component as? StyleComponent {
            setup(style: c.style)
            view?.style.setup(c.style)
        }
    }

    func setup(style: Style?) {
        if let hex = style?.backgroundColor {
            view?.backgroundColor = UIColor(hex: hex)
        }
        if let cornerRadius = style?.cornerRadius {
            view?.layer.masksToBounds = true
            view?.layer.cornerRadius = CGFloat(cornerRadius.radius)
        }
    }

    func setup(id: String?) {
        if let id = id {
            view?.accessibilityIdentifier = id
        }
    }

    func applyStyle<T: UIView>(for view: T, styleId: String, with controller: BeagleController?) {
        controller?.dependencies.theme.applyStyle(for: view, withId: styleId)
    }
    
    func setup(accessibility: Accessibility?) {
        ViewConfigurator.applyAccessibility(accessibility, to: view)
    }
    
    static func applyAccessibility(_ accessibility: Accessibility?, to object: NSObject?) {
        guard let accessibility = accessibility else { return }
        if let label = accessibility.accessibilityLabel {
            object?.accessibilityLabel = label
        }
        object?.isAccessibilityElement = accessibility.accessible
    }
}
