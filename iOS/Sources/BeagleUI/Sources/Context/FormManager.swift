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

public protocol FormManaging {
    func register(form: Form, formView: UIView, submitView: UIView, validatorHandler: ValidatorProvider?)
}

protocol FormManagerDelegate: AnyObject {
    func executeAction(_ action: Action, sender: Any)
    func showLoading()
    func hideLoading()
}

class FormManager: FormManaging {
    
    // MARK: - Dependencies
    
    typealias Dependencies =
        DependencyActionExecutor
        & DependencyRepository
        & DependencyFormDataStoreHandler
        & DependencyLogger
    
    var dependencies: Dependencies
        
    // MARK: - Delegate
    
    typealias delegates =
        FormManagerDelegate
        & ContextErrorHandlingDelegate
    
    public weak var delegate: delegates?
    
    // MARK: - Init
    
    init(
        dependencies: Dependencies,
        delegate: delegates? = nil
    ) {
        self.dependencies = dependencies
        self.delegate = delegate
    }
    
    // MARK: - Functions
    
    public func register(form: Form, formView: UIView, submitView: UIView, validatorHandler: ValidatorProvider?) {
        let gestureRecognizer = SubmitFormGestureRecognizer(
            form: form,
            formView: formView,
            formSubmitView: submitView,
            validator: validatorHandler,
            target: self,
            action: #selector(handleSubmitFormGesture(_:))
        )
        if let control = submitView as? UIControl,
           let formSubmit = submitView.beagleFormElement as? FormSubmit,
           let enabled = formSubmit.enabled {
            control.isEnabled = enabled
        }

        submitView.addGestureRecognizer(gestureRecognizer)
        submitView.isUserInteractionEnabled = true
        gestureRecognizer.updateSubmitView()
    }

    @objc func handleSubmitFormGesture(_ sender: SubmitFormGestureRecognizer) {
        let isSubmitEnabled = (sender.formSubmitView as? UIControl)?.isEnabled ?? true
        guard isSubmitEnabled else { return }

        let inputViews = sender.formInputViews()
        if inputViews.isEmpty {
            dependencies.logger.log(Log.form(.inputsNotFound(form: sender.form)))
        }
        var values = inputViews.reduce(into: [:]) {
            self.validate(
                formInput: $1,
                formSubmit: sender.formSubmitView,
                validatorHandler: sender.validator,
                result: &$0
            )
        }
        guard inputViews.count == values.count else {
            dependencies.logger.log(Log.form(.divergentInputViewAndValueCount(form: sender.form)))
            return
        }
        
        mergeWithStoredValues(values: &values, group: sender.form.group)
        merge(values: &values, with: sender.form.additionalData)
        if sender.form.shouldStoreFields {
            saveFormData(values: values, group: sender.form.group)
        }
        submitAction(sender.form.action, inputs: values, sender: sender, group: sender.form.group)
    }
    
    private func merge(values: inout [String: String], with additionalData: [String: String]?) {
        if let additionalData = additionalData {
            values.merge(additionalData) { _, new in
                dependencies.logger.log(Log.form(.keyDuplication(data: additionalData)))
                return new
            }
        }
    }
    
    private func mergeWithStoredValues(values: inout [String: String], group: String?) {
        if let group = group, let storedValues = dependencies.formDataStoreHandler.read(group: group) {
            values.merge(storedValues) { current, _ in
                dependencies.logger.log(Log.form(.keyDuplication(data: storedValues)))
                return current
            }
        }
    }
    
    private func saveFormData(values: [String: String], group: String?) {
        if let group = group {
            dependencies.formDataStoreHandler.save(data: values, group: group)
        } else {
            dependencies.logger.log(Log.form(.unableToSaveData))
        }
    }

    private func submitAction(_ action: Action, inputs: [String: String], sender: Any, group: String?) {
        switch action {
        case let action as FormRemoteAction:
            submitForm(action, inputs: inputs, sender: sender, group: group)

        case let action as CustomAction:
            let newAction = CustomAction(name: action.name, data: inputs.merging(action.data) { a, _ in return a })
            delegate?.executeAction(newAction, sender: sender)
        default:
            delegate?.executeAction(action, sender: sender)
        }
    }
    
    private func submitForm(_ remote: FormRemoteAction, inputs: [String: String], sender: Any, group: String?) {
        delegate?.showLoading()

        let data = Request.FormData(
            method: remote.method,
            values: inputs
        )

        dependencies.repository.submitForm(url: remote.path, additionalData: nil, data: data) {
            [weak self] result in guard let self = self else { return }
            self.delegate?.hideLoading()
            self.handleFormResult(result, sender: sender, group: group)
        }
        dependencies.logger.log(Log.form(.submittedValues(values: inputs)))
    }

    private func validate(
        formInput view: UIView,
        formSubmit submitView: UIView?,
        validatorHandler: ValidatorProvider?,
        result: inout [String: String]
    ) {
        guard
            let formInput = view.beagleFormElement as? FormInput,
            let inputValue = view as? InputValue
        else { return }
        
        let value = inputValue.getValue()
        
        if formInput.required ?? false {
            guard let validator = getValidator(for: formInput, with: validatorHandler) else { return }
            
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
        dependencies.logger.log(Log.form(.validationInputNotValid(inputName: formInput.name)))
    }
    
    private func getValidator(for formInput: FormInput, with handler: ValidatorProvider?) -> Validator? {
        guard
            let validatorName = formInput.validator,
            let handler = handler,
            let validator = handler.getValidator(name: validatorName)
        else {
            if let validatorName = formInput.validator {
                dependencies.logger.log(Log.form(.validatorNotFound(named: validatorName)))
            }
            return nil
        }
        return validator
    }

    private func handleFormResult(_ result: Result<Action, Request.Error>, sender: Any, group: String?) {
        switch result {
        case .success(let action):
            self.dependencies.formDataStoreHandler.formManagerDidSubmitForm(group: group)
            delegate?.executeAction(action, sender: sender)
        case .failure(let error):
            delegate?.handleContextError(.submitForm(error))
        }
    }
}
