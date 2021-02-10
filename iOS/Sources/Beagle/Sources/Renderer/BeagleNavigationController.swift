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

// swiftlint:disable pattern_matching_keywords

public typealias BeagleRetry = () -> Void

public enum ServerDrivenState {
    case started
    case finished
    case success
    case error(ServerDrivenState.Error, BeagleRetry? = nil)
}

extension ServerDrivenState {
    public enum Error: Swift.Error {
        case remoteScreen(Request.Error)
        case action(Swift.Error)
        case lazyLoad(Request.Error)
        case submitForm(Request.Error)
        case webView(Swift.Error)
        case declarativeText
    }
}

open class BeagleNavigationController: UINavigationController {
    
    private var errorView = BeagleErrorView(message: nil) { }

    /// This method is the entry point to handle screen state changes.
    /// The default implemetation shows an `ActivityIndicator` when screen is
    /// loading and does nothing when error happens; override this method to handle
    /// errors properly.
    /// When overriding, if you want to preserve loading and error behavior, `super` implementation should be called,
    /// or you can customize these behaviors yourself.
    ///
    /// - Parameters:
    ///   - state: new state that tells if screen is loading or any error happened
    ///   - screenController: controller that triggered the state change
    open func serverDrivenStateDidChange(
        to state: ServerDrivenState,
        at screenController: BeagleController
    ) {
        switch state {
        case .started:
            view.showLoading(.whiteLarge)
        case .finished:
            view.hideLoading()
        case .error(let serverDrivenError, let retry):
            let message = getServerDrivenErrorMessage(from: serverDrivenError)
            guard let retry = retry else { return }
            if !view.subviews.contains(errorView) {
                errorView = BeagleErrorView(message: message, retry: retry)
                errorView.present(in: view)
            } else {
                errorView.addRetry(retry)
            }
        case .success:
            break
        }
    }
    
    private func getServerDrivenErrorMessage(from serverDrivenError: ServerDrivenState.Error) -> String {
        #if DEBUG
        switch serverDrivenError {
        case .remoteScreen(let error), .lazyLoad(let error), .submitForm(let error):
            switch error {
            case .networkError(let messageError):
                return messageError.error.localizedDescription
            case .decoding(let messageError):
                return messageError.localizedDescription
            case .loadFromTextError, .urlBuilderError, .networkClientWasNotConfigured:
                return error.localizedDescription
            }
        case .action(let error):
            return error.localizedDescription
            
        default:
            return "Unknown Error."
        }
        
        #else
        return "An unknown error occurred."
        
        #endif
    }
}
