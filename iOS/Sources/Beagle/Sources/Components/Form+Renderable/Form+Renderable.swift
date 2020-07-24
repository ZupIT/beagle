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

extension Deprecated.Form: ServerDrivenComponent {

    public func toView(renderer: BeagleRenderer) -> UIView {
        let childView = renderer.render(child)
        var hasFormSubmit = false
        
        func registerFormSubmit(view: UIView) {
            if view.beagleFormElement is Deprecated.FormSubmit {
                hasFormSubmit = true
                register(formView: childView, submitView: view, controller: renderer.controller)
            }
            for subview in view.subviews {
                registerFormSubmit(view: subview)
            }
        }
        
        registerFormSubmit(view: childView)
        if !hasFormSubmit {
            renderer.controller.dependencies.logger.log(Log.form(.submitNotFound(form: self)))
        }
        return childView
    }
    
    private func register(formView: UIView, submitView: UIView, controller: BeagleController) {
        let gestureRecognizer = SubmitFormGestureRecognizer(
            form: self,
            formView: formView,
            formSubmitView: submitView,
            controller: controller
        )
        if let control = submitView as? UIControl,
            let formSubmit = submitView.beagleFormElement as? Deprecated.FormSubmit,
            let enabled = formSubmit.enabled {
            control.isEnabled = enabled
        }
        
        submitView.addGestureRecognizer(gestureRecognizer)
        submitView.isUserInteractionEnabled = true
        gestureRecognizer.updateSubmitView()
    }
}

extension UIView {
    private struct AssociatedKeys {
        static var FormElement = "beagleUI_FormElement"
    }
    
    private class ObjectWrapper<T> {
        let object: T?
        
        init(_ object: T?) {
            self.object = object
        }
    }
    
    var beagleFormElement: ServerDrivenComponent? {
        get {
            return (objc_getAssociatedObject(self, &AssociatedKeys.FormElement) as? ObjectWrapper)?.object
        }
        set {
            objc_setAssociatedObject(self, &AssociatedKeys.FormElement, ObjectWrapper(newValue), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
}
