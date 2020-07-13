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

final class SubmitFormGestureRecognizer: UITapGestureRecognizer {
    
    let form: Deprecated.Form
    weak var formView: UIView?
    weak var formSubmitView: UIView?
    weak var controller: BeagleController?
    
    init(form: Deprecated.Form, formView: UIView, formSubmitView: UIView, controller: BeagleController) {
        self.form = form
        self.formView = formView
        self.formSubmitView = formSubmitView
        self.controller = controller
        super.init(target: nil, action: nil)
        self.setupFormObservables()
        addTarget(self, action: #selector(submitForm))
    }
    
    @objc private func submitForm() {
        FormManager(sender: self)?.submitForm()
    }
    
    private func setupFormObservables() {
        guard let observer = formSubmitView?.superview as? Observer else { return }
        formView?.allSubviews
            .compactMap { $0 as? WidgetStateObservable }
            .forEach { $0.observable.addObserver(observer) }
    }
    
    func formInputViews() -> [UIView] {
        var inputViews = [UIView]()
        var pendingViews = [UIView]()
        if let formView = formView {
            pendingViews.append(formView)
        }
        while let view = pendingViews.popLast() {
            if view.beagleFormElement is Deprecated.FormInput {
                inputViews.append(view)
            } else {
                pendingViews.append(contentsOf: view.subviews)
            }
        }
        return inputViews
    }
    
    func updateSubmitView() {
        guard let control = formSubmitView as? UIControl else { return }
        let formSubmitEnabled = (formSubmitView?.beagleFormElement as? Deprecated.FormSubmit)?.enabled ?? true
        control.isEnabled = formSubmitEnabled || formIsValid()
    }
    
    private func formIsValid() -> Bool {
        for inputView in formInputViews() {
            guard
                let formInput = inputView.beagleFormElement as? Deprecated.FormInput,
                let inputValue = inputView as? InputValue else {
                    return false
            }
            if formInput.required ?? false {
                guard
                    let validatorName = formInput.validator,
                    let validatorProvider = controller?.dependencies.validatorProvider,
                    let validator = validatorProvider.getValidator(name: validatorName),
                    validator.isValid(input: inputValue.getValue()) else {
                        return false
                }
            }
        }
        return true
    }
    
}
