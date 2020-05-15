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

public enum ServerDrivenState {
    case loading(Bool)
    case error(ServerDrivenState.Error)
}

extension ServerDrivenState {
    public enum Error {
        case remoteScreen(Request.Error)
        case action(Swift.Error)
        case lazyLoad(Request.Error)
        case submitForm(Request.Error)
        case declarativeText
    }
}

open class BeagleNavigationController: UINavigationController {
    
    open func serverDrivenStateDidChange(
        to state: ServerDrivenState,
        at screenController: BeagleScreenViewController
    ) {
        switch state {
        case .loading(let loading):
            loading ? view.showLoading(.whiteLarge) : view.hideLoading()
        case .error:
            break
        }
    }
    
}

extension BeagleNavigationController {

    func startLoading(_ screenController: BeagleScreenViewController) {
        serverDrivenStateDidChange(to: .loading(true), at: screenController)
    }

    func stopLoading(_ screenController: BeagleScreenViewController) {
        serverDrivenStateDidChange(to: .loading(false), at: screenController)
    }

}
