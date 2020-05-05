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

public protocol ActionExecutor {
    func doAction(_ action: Action, sender: Any, context: BeagleContext)
}

public protocol DependencyActionExecutor {
    var actionExecutor: ActionExecutor { get }
}

final class ActionExecuting: ActionExecutor {

    public typealias Dependencies =
        DependencyNavigation
        & DependencyCustomActionHandler

    let dependencies: Dependencies
    
    init(dependencies: Dependencies) {
        self.dependencies = dependencies
    }
    
    func doAction(_ action: Action, sender: Any, context: BeagleContext) {
        if let navigation = action as? Navigate {
            navigate(navigation, context: context)
        } else if let formValidation = action as? FormValidation {
            validateForm(formValidation, sender: sender, context: context)
        } else if let nativeDialog = action as? ShowNativeDialog {
            showNativeDialog(nativeDialog, context: context)
        } else if let custom = action as? CustomAction {
            customAction(custom, context: context)
        }
    }
    
    private func navigate(_ action: Navigate, context: BeagleContext) {
        dependencies.navigation.navigate(action: action, context: context, animated: true)
    }
    
    private func validateForm(_ action: FormValidation, sender: Any, context: BeagleContext) {
        let inputViews = (sender as? SubmitFormGestureRecognizer)?.formInputViews()
        for error in action.errors {
            let errorListener = inputViews?.first { view in
                (view.beagleFormElement as? FormInput)?.name == error.inputName
            } as? ValidationErrorListener
            errorListener?.onValidationError(message: error.message)
        }
    }
    
    private func showNativeDialog(_ action: ShowNativeDialog, context: BeagleContext) {
        let alert = UIAlertController(title: action.title, message: action.message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: action.buttonText, style: .default))
        context.screenController.present(alert, animated: true)
    }
    
    private func customAction(_ action: CustomAction, context: BeagleContext) {
        dependencies.customActionHandler?.handle(context: context, action: action) {
            switch $0 {
            case .start:
                context.screenController.viewModel.state = .loading
            case .error(let error):
                context.screenController.viewModel.state = .failure(.action(error))
            case .success(let action):
                context.screenController.viewModel.state = .success
                self.doAction(action, sender: self, context: context)
            }
        }
    }
}
