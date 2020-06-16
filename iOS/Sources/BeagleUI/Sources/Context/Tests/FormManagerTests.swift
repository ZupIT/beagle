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
@testable import BeagleUI
import SnapshotTesting
import BeagleSchema

final class FormManagerTests: XCTestCase {
    
    private lazy var form: Form = {
        let action = FormRemoteAction(path: "submit", method: .post)
        let form = Form(action: action, child: Container(children: [
            FormInput(name: "name", child: InputComponent(value: "John Doe")),
            FormSubmit(child: Button(text: "Add"), enabled: true)
        ]))
        return form
    }()
    
    private lazy var controller: BeagleController = {
        let controller = BeagleControllerStub()
        controller.dependencies = dependencies
        return controller
    }()
    
    private lazy var formView = form.toView(renderer: BeagleRenderer(controller: controller))

    private lazy var dependencies = BeagleScreenDependencies(
        repository: repositoryStub,
        validatorProvider: validator,
        formDataStoreHandler: dataStoreStub
    )

    private lazy var repositoryStub = LazyRepositoryStub()
    private lazy var validator = ValidatorProviding()
    private lazy var dataStoreStub = DataStoreHandlerStub()

    private func submitGesture(in formView: UIView) -> SubmitFormGestureRecognizer {
        // swiftlint:disable force_unwrapping force_cast
        let submit = findSubmitView(in: formView)!
        return submit.childView.gestureRecognizers!.first as! SubmitFormGestureRecognizer
    }

    private func findSubmitView(in view: UIView) -> FormSubmit.FormSubmitView? {
        if let submit = view as? FormSubmit.FormSubmitView {
            return submit
        } else {
            return view.subviews.first { findSubmitView(in: $0) != nil } as? FormSubmit.FormSubmitView
        }
    }
    
    override func setUp() {
        setValidator3()
        super.setUp()
    }
    
    override func tearDown() {
        dataStoreStub.resetStub()
        super.tearDown()
    }

    func test_formSubmit_shouldValidateInputs() throws {
        // Given
        let validator1 = "valid"
        let validator2 = "invalid"
        var validationCount = 0
        validator[validator1] = { _ in
            validationCount += 1
            return true
        }
        validator[validator2] = { _ in
            validationCount += 1
            return false
        }

        let view = Form(action: ActionDummy(), child: Container(children: [
            FormInput(name: "name", required: true, validator: validator1, child: InputComponent(value: "John Doe")),
            FormInput(name: "password", required: true, validator: validator2, child: InputComponent(value: "password")),
            FormSubmit(child: Button(text: "Add"))
        ])).toView(renderer: BeagleRenderer(controller: controller))

        let gesture = submitGesture(in: view)

        // When
        FormManager(sender: gesture)?.submitForm()

        // Then
        XCTAssert(validationCount == 2)
    }

    func test_formSubmit_shouldExecuteResponseAction() throws {
        // Given
        let gesture = submitGesture(in: formView)
        let action = ActionSpy()
        var isLoading = false

        // When
        FormManager(sender: gesture)?.submitForm()
        if case .loading(let loading) = controller.serverDrivenState {
            isLoading = loading
        }
        // Then
        XCTAssertTrue(isLoading)
        
        // When
        repositoryStub.formCompletion?(.success(action))
        if case .loading(let loading) = controller.serverDrivenState {
            isLoading = loading
        }
        // Then
        XCTAssertEqual(action.executionCount, 1)
        XCTAssertTrue(action.lastSender as AnyObject === gesture)
        XCTAssertFalse(isLoading)
    }

    func test_formSubmit_shouldPassRightDataToBeSubmitted() throws {
        // Given
        let gesture = submitGesture(in: formView)

        // When
        FormManager(sender: gesture)?.submitForm()

        // Then
        let submittedData = repositoryStub.formData

        assertSnapshot(matching: submittedData, as: .dump)
    }

    func test_formSubmitError_shouldNotExecuteAction() throws {
        // Given
        let gesture = submitGesture(in: formView)

        // When
        FormManager(sender: gesture)?.submitForm()
        repositoryStub.formCompletion?(.failure(.urlBuilderError))

        // Then
        var error: Request.Error?
        if case .error(let serverDrivenError) = controller.serverDrivenState {
            if case .submitForm(let requestError) = serverDrivenError {
                if case .urlBuilderError = requestError {
                    error = requestError
                }
            }
        }
        XCTAssertNotNil(error)
    }
    
    // MARK: - Form Storage Logic Tests
    
    let validator3 = "validator3"
        
    private let group = "group"
    
    private lazy var formViewWithStorage = Form(
        action: FormRemoteAction(path: "submit", method: .post),
        child: Container(children: [
            FormInput(name: "name", required: true, validator: validator3, child: InputComponent(value: "John Doe")),
            FormInput(name: "password", required: true, validator: validator3, child: InputComponent(value: "password")),
            FormSubmit(child: Button(text: "Add"))
        ]),
        group: group,
        additionalData: ["id": "111111"],
        shouldStoreFields: true
    ).toView(renderer: BeagleRenderer(controller: controller))
    
    private func setValidator3() {
        validator[validator3] = { _ in
            return true
        }
    }
    
    func test_formSubmit_shouldIncludeStoredValuesToSubmission() {
        // Given
        let formData = FormData()
        formData.data = ["age": "12", "name": "yan dias"]
        dataStoreStub.dataStore[group] = formData

        let gesture = submitGesture(in: formViewWithStorage)

        // When
        FormManager(sender: gesture)?.submitForm()
        
        // Then
        XCTAssert(dataStoreStub.didCallRead == true)
        assertSnapshot(matching: repositoryStub.formData?.values, as: .dump)
    }
    
    func test_formManagerShouldNotifyFormDataStoreAboutSubmission() {
        // Given
        let formData = FormData()
        formData.data = ["age": "12", "name": "yan dias"]
        dataStoreStub.dataStore[group] = formData
        let gesture = submitGesture(in: formViewWithStorage)

        // When
        FormManager(sender: gesture)?.submitForm()
        repositoryStub.formCompletion?(.success(CustomAction(name: "custom", data: [:])))
        
        // Then
        XCTAssert(dataStoreStub.didCallformManagerDidSubmitForm)
    }
    
    func test_formSubmit_shouldSaveFormInputs() {
        // Given
        let gesture = submitGesture(in: formViewWithStorage)

        // When
        FormManager(sender: gesture)?.submitForm()

        // Then
        XCTAssert(dataStoreStub.didCallSave)
        assertSnapshot(matching: dataStoreStub.passedDataToSave, as: .dump)
    }
}

// MARK: - Stubs
private class DataStoreHandlerStub: FormDataStoreHandling {

    private(set) var didCallRead: Bool = false
    private(set) var didCallSave: Bool = false
    private(set) var didCallformManagerDidSubmitForm: Bool = false
    private(set) var passedDataToSave: [String: String] = [:]
    
    var dataStore: [String: FormData] = [:]
    
    func save(data: [String: String], group: String) {
        didCallSave = true
        passedDataToSave = data
    }
    
    func read(group: String) -> [String: String]? {
        didCallRead = true
        return dataStore[group]?.data
    }
    
    func formManagerDidSubmitForm(group: String?) {
        didCallformManagerDidSubmitForm = true
    }

    func resetStub() {
        didCallRead = false
        didCallSave = false
        dataStore = [:]
    }
}

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

private class SubmitStub: UIView, Observer, WidgetStateObservable {
    var observable: Observable<WidgetState> = Observable<WidgetState>(value: WidgetState(value: false))
    var didCallChangeValue = false

    init(_ formSubmit: FormSubmit) {
        super.init(frame: .zero)
        self.beagleFormElement = formSubmit
    }

    required init?(coder: NSCoder) {
        BeagleUI.fatalError("init(coder:) has not been implemented")
    }

    func didChangeValue(_ value: Any?) {
        didCallChangeValue = true
    }
}
