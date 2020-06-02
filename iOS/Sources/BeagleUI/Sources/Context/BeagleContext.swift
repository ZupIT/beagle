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

public enum Event {
    case action(Action)
    case analytics(AnalyticsClick)
}

protocol ContextErrorHandlingDelegate: AnyObject {
    func handleContextError(_ error: ServerDrivenState.Error)
}

/// Interface to access application specific operations
public protocol BeagleContext: AnyObject {

    var screenController: BeagleScreenViewController { get }
    
    var formManager: FormManaging { get }
    var lazyLoadManager: LazyLoadManaging { get }
    var actionManager: ActionManaging { get }
    
    func applyLayout()
}

extension BeagleScreenViewController: BeagleContext {

    public var screenController: BeagleScreenViewController {
        return self
    }
    
    public var formManager: FormManaging {
        return formContextManager
    }
    
    public var lazyLoadManager: LazyLoadManaging {
        return lazyLoadContextManager
    }
    
    public var actionManager: ActionManaging {
        return actionContextManager
    }

    public func applyLayout() {
        (contentController as? ScreenController)?.layoutManager?.applyLayout()
    }
}

// MARK: - FormHandlerDelegate

extension BeagleScreenViewController: FormManagerDelegate {
    func showLoading() {
        view.showLoading(.whiteLarge)
    }
    
    func hideLoading() {
        view.hideLoading()
    }
    
    func executeAction(_ action: Action, sender: Any) {
        dependencies.actionExecutor.doAction(action, sender: sender, context: self)
    }
}

// MARK: - ContextErrorHandlingDelegate

extension BeagleScreenViewController: ContextErrorHandlingDelegate {
    func handleContextError(_ error: ServerDrivenState.Error) {
        viewModel.state = .failure(error)
    }
}

// MARK: - LazyLoadManagerDelegate

extension BeagleScreenViewController: LazyLoadManagerDelegate {
    internal func replaceView(_ oldView: UIView, with component: ServerDrivenComponent) {
        guard let superview = oldView.superview else { return }

        let newView = component.toView(context: self, dependencies: dependencies)
        newView.frame = oldView.frame
        superview.insertSubview(newView, belowSubview: oldView)
        oldView.removeFromSuperview()

        if oldView.flex.isEnabled && !newView.flex.isEnabled {
            newView.flex.isEnabled = true
        }
    }
}

// MARK: - ActionManagerDelegate

extension BeagleScreenViewController: ActionManagerDelegate {
    
    public func doAnalyticsAction(_ clickEvent: AnalyticsClick, sender: Any) {
        dependencies.analytics?.trackEventOnClick(clickEvent)
    }
    
    public func doAction(_ action: Action, sender: Any) {
        dependencies.actionExecutor.doAction(action, sender: sender, context: self)
    }
}
