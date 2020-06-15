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

import XCTest
import SnapshotTesting
@testable import BeagleUI
import BeagleSchema

final class FormTests: XCTestCase {
    
    private lazy var formView = renderer.render(form)
    
    private lazy var renderer = BeagleRenderer(context: screen, dependencies: dependencies)
    
    private lazy var screen = BeagleScreenViewController(viewModel: .init(
        screenType: .declarative(SimpleComponent().content.toScreen()),
        dependencies: dependencies
        ))
    
    private lazy var dependencies = BeagleScreenDependencies(
        actionExecutor: actionExecutorSpy,
        repository: repositoryStub,
        validatorProvider: validator
    )
    
    private lazy var repositoryStub = RepositoryStub()
    private lazy var actionExecutorSpy = ActionExecutorSpy()
    private lazy var validator = ValidatorProviding()
    
    private lazy var form: Form = {
        let action = FormRemoteAction(path: "submit", method: .post)
        let form = Form(action: action, child: Container(children: [
            FormInput(name: "name", child: InputComponent(value: "John Doe")),
            FormSubmit(child: Button(text: "Add"), enabled: true)
        ]))
        return form
        }()
    
    func test_buildView_shouldRegisterFormSubmit() throws {
        // Given
        let formManager = FormManagerSpy()
        let context = BeagleContextSpy(formManager: formManager)
        let renderer = BeagleRenderer(context: context, dependencies: dependencies)
    
        // When
        _ = renderer.render(form)
        
        // Then
        XCTAssertTrue(formManager.didCallRegisterFormSubmit)
    }
    
    private func findSubmitView(in view: UIView) -> FormSubmit.FormSubmitView? {
        if let submit = view as? FormSubmit.FormSubmitView {
            return submit
        } else {
            return view.subviews.first { findSubmitView(in: $0) != nil } as? FormSubmit.FormSubmitView
        }
    }
}

// MARK: - Stubs

    private struct InputComponent: BeagleUI.ServerDrivenComponent {
        let value: String
        
        func toView(renderer: BeagleRenderer) -> UIView {
            return InputStub(value: value)
        }
    }

    private class InputStub: UIView, InputValue, ValidationErrorListener, WidgetStateObservable {
        var observable = Observable<WidgetState>(value: WidgetState(value: false))
        
        let value: String
        
        init(value: String = "") {
            self.value = value
            super.init(frame: .zero)
        }
        
        required init?(coder: NSCoder) {
            BeagleUI.fatalError("init(coder:) has not been implemented")
        }
        
        func getValue() -> Any {
            return value
        }
        func onValidationError(message: String?) {
        }
    }
