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

@testable import BeagleUI
import UIKit

class FormManagerSpy: FormManaging {
    private(set) var didCallRegisterFormSubmit = false

    func register(form: Form, formView: UIView, submitView: UIView, validatorHandler: ValidatorProvider?) {
        didCallRegisterFormSubmit = true
    }
}

class LazyLoadManagerSpy: LazyLoadManaging {
    private(set) var didCallLazyLoad = false

    func lazyLoad(url: String, initialState: UIView) {
        didCallLazyLoad = true
    }
}

class ActionManagerSpy: ActionManaging {
    private(set) var actionCalled: Action?
    private(set) var didCallDoAction = false
    private(set) var didCallRegisterEvents = false
    private(set) var analyticsEventCalled = false
    
    func doAction(_ action: Action, sender: Any) {
        didCallDoAction = true
        actionCalled = action
    }
    
    func register(events: [Event], inView view: UIView) {
        didCallRegisterEvents = true
    }
    
    func doAnalyticsAction(_ action: AnalyticsClick, sender: Any) {
        analyticsEventCalled = true
    }
}

class BeagleScreenViewControllerDummy: BeagleScreenViewController {
    
}

class BeagleContextSpy: BeagleContext {
    
    let actionManagerSpy: ActionManaging
    let formManagerSpy: FormManaging
    let lazyLoadManagerSpy: LazyLoadManaging
    
    var formManager: FormManaging { formManagerSpy }
    var lazyLoadManager: LazyLoadManaging { lazyLoadManagerSpy }
    var actionManager: ActionManaging { actionManagerSpy }
    var screenController: BeagleScreenViewController = BeagleScreenViewControllerDummy(viewModel: .init(screenType: .declarative(ComponentDummy().toScreen())))
    
    init(
        actionManager: ActionManaging = ActionManagerSpy(),
        formManager: FormManaging = FormManagerSpy(),
        lazyLoadManager: LazyLoadManaging = LazyLoadManagerSpy()
    ) {
        self.actionManagerSpy = actionManager
        self.lazyLoadManagerSpy = lazyLoadManager
        self.formManagerSpy = formManager
    }
    
    private(set) var didCallRegisterEnabledWidget = false
    private(set) var didCallApplyLayout = true

    func register(formSubmitEnabledWidget: Widget?, formSubmitDisabledWidget: Widget?) {
        didCallRegisterEnabledWidget = true
    }

    func applyLayout() {
        didCallApplyLayout = true
    }
}
