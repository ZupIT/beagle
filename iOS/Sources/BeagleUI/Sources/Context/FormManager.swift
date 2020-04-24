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
    func handleSubmitFormGesture(_ sender: SubmitFormGestureRecognizer)
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
        & DependencyDataStoreHandling
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
                shouldStoreFields: sender.form.shouldStoreFields,
                result: &$0
            )
        }
        guard inputViews.count == values.count else {
            dependencies.logger.log(Log.form(.divergentInputViewAndValueCount(form: sender.form)))
            return
        }
        
        if let storedParameters = sender.form.storedParameters {
            do {
                try mergeStoredValues(with: &values, storedKeys: storedParameters)
            } catch {
                return
            }
        }
        
        submitAction(sender.form.action, inputs: values, sender: sender)
    }
    
    private func mergeStoredValues(with values: inout [String: String], storedKeys: [String]) throws {
        try storedKeys.forEach {
            guard let value = dependencies.dataStoreHandler.read(storeType: .Form, key: $0) else {
                dependencies.logger.log(Log.form(.keyNotFound(key: $0)))
                throw FormStoredValuesMergingError.couldNotReadValueForKey
            }
            try values.merge([$0: value]) { key, _ in
                dependencies.logger.log(Log.form(.keyDuplication(key: key)))
                throw FormStoredValuesMergingError.duplicatedKey
            }
        }
    }

    private func submitAction(_ action: Action, inputs: [String: String], sender: Any) {
        switch action {
        case let action as FormRemoteAction:
            submitForm(action, inputs: inputs, sender: sender)

        case let action as CustomAction:
            let newAction = CustomAction(name: action.name, data: inputs.merging(action.data) { a, _ in return a })
            delegate?.executeAction(newAction, sender: sender)
        default:
            delegate?.executeAction(action, sender: sender)
        }
    }
    
    private func submitForm(_ remote: FormRemoteAction, inputs: [String: String], sender: Any) {
        delegate?.showLoading()

        let data = Request.FormData(
            method: remote.method,
            values: inputs
        )

        dependencies.repository.submitForm(url: remote.path, additionalData: nil, data: data) {
            [weak self] result in guard let self = self else { return }

            self.delegate?.hideLoading()
            self.handleFormResult(result, sender: sender)
        }
        dependencies.logger.log(Log.form(.submittedValues(values: inputs)))
    }

    private func validate(
        formInput view: UIView,
        formSubmit submitView: UIView?,
        validatorHandler: ValidatorProvider?,
        shouldStoreFields: Bool,
        result: inout [String: String]
    ) {
        guard
            let formInput = view.beagleFormElement as? FormInputComponent,
            let inputValue = view as? InputValue
        else { return }
        
        let value = inputValue.getValue()
        
        if let defaultFormInput = formInput as? FormInput, defaultFormInput.required ?? false {
            guard let validator = getValidator(for: defaultFormInput, with: validatorHandler) else { return }

            if validator.isValid(input: value) {
                appendFormInputToResults(value: value, formInput: defaultFormInput, results: &result, shouldStoreFields: shouldStoreFields)
            } else {
                handleValidationError(with: defaultFormInput, inputValue: inputValue)
            }
        } else {
            appendFormInputToResults(value: value, formInput: formInput, results: &result, shouldStoreFields: shouldStoreFields)
        }
    }
    
    private func handleValidationError(with formInput: FormInput, inputValue: InputValue) {
        if let errorListener = inputValue as? ValidationErrorListener {
            errorListener.onValidationError(message: formInput.errorMessage)
        }
        dependencies.logger.log(Log.form(.validationInputNotValid(inputName: formInput.name)))
    }
    
    private func appendFormInputToResults(value: Any, formInput: FormInputComponent, results: inout [String: String], shouldStoreFields: Bool) {
        let value = String(describing: value)
        let key = formInput.name
        let overrideName = (formInput as? FormInput)?.overrideStoredName
        handleDataStoreSaving(overrideName: overrideName, key: key, value: value, shouldStoreFields: shouldStoreFields)
        results[formInput.name] = value
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
    
    private func handleDataStoreSaving(overrideName: String? = nil, key: String, value: String, shouldStoreFields: Bool) {
        if let overrideKey = overrideName {
            dependencies.dataStoreHandler.save(storeType: .Form, key: overrideKey, value: String(describing: value))
        } else if shouldStoreFields {
            dependencies.dataStoreHandler.save(storeType: .Form, key: key, value: String(describing: value))
        }
    }

    private func handleFormResult(_ result: Result<Action, Request.Error>, sender: Any) {
        switch result {
        case .success(let action):
            delegate?.executeAction(action, sender: sender)
        case .failure(let error):
            delegate?.handleError(.submitForm(error))
        }
    }
    
    enum FormStoredValuesMergingError: Error {
        case couldNotReadValueForKey
        case duplicatedKey
    }
}
