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

extension SubmitForm {
    public func execute(controller: BeagleController, origin: UIView) {
        var view: UIView? = origin
        while view != nil {
            if let simpleForm = view?.beagleFormElement as? SimpleForm {
                if verifyFormValidation(from: view) {
                    controller.execute(actions: simpleForm.onSubmit, event: "onSubmit", origin: origin)
                } else {
                    controller.execute(actions: simpleForm.onValidationError, event: "onValidationError", origin: origin)
                }
                break
            }
            view = view?.superview
        }
    }
    
    private func verifyFormValidation(from origin: UIView?) -> Bool {
        var valid: [Bool] = []
        origin?.allSubviews.forEach { childView in
            guard let textField = childView as? TextInput.TextInputView else { return }
            if let expression = (textField.beagleFormElement as? TextInput)?.error {
                let error: String = expression.evaluate(with: origin) ?? ""
                valid.append(error.isEmpty)
            }
        }
        return !valid.contains(false) || valid.isEmpty
    }
}
