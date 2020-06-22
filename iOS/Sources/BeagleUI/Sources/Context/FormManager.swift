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

class FormManager {
    
    private let sender: SubmitFormGestureRecognizer
    private let controller: BeagleController
    
    // MARK: - Init
    
    init?(sender: SubmitFormGestureRecognizer) {
        guard let controller = sender.controller else { return nil }
        self.sender = sender
        self.controller = controller
    }
        
    // MARK: - Functions

    public func submitForm() {
        let isSubmitEnabled = (sender.formSubmitView as? UIControl)?.isEnabled ?? true
        guard isSubmitEnabled else { return }

        let inputViews = sender.formInputViews()
        if inputViews.isEmpty {
            controller.dependencies.logger.log(Log.form(.inputsNotFound(form: sender.form)))
        }
        var values = inputViews.reduce(into: [:]) {
            self.validate(
                formInput: $1,
                result: &$0
            )
        }
        guard inputViews.count == values.count else {
            controller.dependencies.logger.log(Log.form(.divergentInputViewAndValueCount(form: sender.form)))
            return
        }
        
        mergeWithStoredValues(values: &values, group: sender.form.group)
        merge(values: &values, with: sender.form.additionalData)
        if sender.form.shouldStoreFields {
            saveFormData(values: values, group: sender.form.group)
        }
        sender.form.onSubmit?.forEach { action in
            submitAction(action, inputs: values, sender: sender, group: sender.form.group)
        }
    }
    
    private func merge(values: inout [String: String], with additionalData: [String: String]?) {
        if let additionalData = additionalData {
            values.merge(additionalData) { _, new in
                controller.dependencies.logger.log(Log.form(.keyDuplication(data: additionalData)))
                return new
            }
        }
    }
    
    private func mergeWithStoredValues(values: inout [String: String], group: String?) {
        if let group = group, let storedValues = controller.dependencies.formDataStoreHandler.read(group: group) {
            values.merge(storedValues) { current, _ in
                controller.dependencies.logger.log(Log.form(.keyDuplication(data: storedValues)))
                return current
            }
        }
    }
    
    private func saveFormData(values: [String: String], group: String?) {
        if let group = group {
            controller.dependencies.formDataStoreHandler.save(data: values, group: group)
        } else {
            controller.dependencies.logger.log(Log.form(.unableToSaveData))
        }
    }

    private func submitAction(_ action: RawAction, inputs: [String: String], sender: Any, group: String?) {
        switch action {
        case let action as FormRemoteAction:
            submitForm(action, inputs: inputs, sender: sender, group: group)

        case let action as FormLocalAction:
            let newAction = FormLocalAction(name: action.name, data: inputs.merging(action.data) { a, _ in return a })
            controller.execute(action: newAction, sender: sender)
        default:
            controller.execute(action: action, sender: sender)
        }
    }
    
    private func submitForm(_ remote: FormRemoteAction, inputs: [String: String], sender: Any, group: String?) {
        controller.serverDrivenState = .loading(true)

        let data = Request.FormData(
            method: remote.method,
            values: inputs
        )

        controller.dependencies.repository.submitForm(url: remote.path, additionalData: nil, data: data) {
            result in
            self.controller.serverDrivenState = .loading(false)
            self.handleFormResult(result, sender: sender, group: group)
        }
        controller.dependencies.logger.log(Log.form(.submittedValues(values: inputs)))
    }

    private func validate(
        formInput view: UIView,
        result: inout [String: String]
    ) {
        guard
            let formInput = view.beagleFormElement as? FormInput,
            let inputValue = view as? InputValue
        else { return }
        
        let value = inputValue.getValue()
        
        if formInput.required ?? false {
            guard let validator = getValidator(for: formInput) else { return }
            
            if validator.isValid(input: value) {
                result[formInput.name] = String(describing: value)
            } else {
                handleValidationError(with: formInput, inputValue: inputValue)
            }
        } else {
            result[formInput.name] = String(describing: value)
        }
    }
    
    private func handleValidationError(with formInput: FormInput, inputValue: InputValue) {
        if let errorListener = inputValue as? ValidationErrorListener {
            errorListener.onValidationError(message: formInput.errorMessage)
        }
        controller.dependencies.logger.log(Log.form(.validationInputNotValid(inputName: formInput.name)))
    }
    
    private func getValidator(for formInput: FormInput) -> Validator? {
        guard
            let validatorName = formInput.validator,
            let handler = controller.dependencies.validatorProvider,
            let validator = handler.getValidator(name: validatorName)
        else {
            if let validatorName = formInput.validator {
                controller.dependencies.logger.log(Log.form(.validatorNotFound(named: validatorName)))
            }
            return nil
        }
        return validator
    }

    private func handleFormResult(_ result: Result<RawAction, Request.Error>, sender: Any, group: String?) {
        switch result {
        case .success(let action):
            controller.dependencies.formDataStoreHandler.formManagerDidSubmitForm(group: group)
            controller.execute(action: action, sender: sender)
        case .failure(let error):
            controller.serverDrivenState = .error(.submitForm(error))
        }
    }
}
