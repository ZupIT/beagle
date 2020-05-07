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

final class FormManagerTests: XCTestCase {
    
    private lazy var form: Form = {
        let action = FormRemoteAction(path: "submit", method: .post)
        let form = Form(action: action, child: Container(children: [
            FormInput(name: "name", child: InputComponent(value: "John Doe")),
            FormInputHidden(name: "id", value: "123123"),
            FormSubmit(child: Button(text: "Add"), enabled: true)
        ]))
        return form
    }()
    
    private lazy var formManager = screen.formManager as? FormManager
    
    private lazy var formView = form.toView(context: screen, dependencies: dependencies)

    private lazy var screen = BeagleScreenViewController(viewModel: .init(
        screenType: .declarative(SimpleComponent().content.toScreen()),
        dependencies: dependencies
    ))

    private lazy var dependencies = BeagleScreenDependencies(
        actionExecutor: actionExecutorSpy,
        repository: repositoryStub,
        validatorProvider: validator,
        formDataStoreHandler: dataStoreStub
    )

    private lazy var repositoryStub = RepositoryStub()
    private lazy var actionExecutorSpy = ActionExecutorSpy()
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
    
    func test_registerForm_shouldAddGestureRecognizer() throws {
        // Given
        let form = Form(action: ActionDummy(), child: ComponentDummy())
        let formView = UIView()
        let submitView = UILabel()

        // When
        screen.formManager.register(form: form, formView: formView, submitView: submitView, validatorHandler: nil)

        // Then
        XCTAssertEqual(1, submitView.gestureRecognizers?.count)
        XCTAssertTrue(submitView.isUserInteractionEnabled)
    }

    let validator1 = "valid"
    let validator2 = "invalid"

    func test_formSubmit_shouldValidateInputs() throws {
        // Given
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
        ])).toView(context: screen, dependencies: dependencies)

        let gesture = submitGesture(in: view)

        // When
        formManager?.handleSubmitFormGesture(gesture)

        // Then
        XCTAssert(validationCount == 2)
    }

    func test_formSubmit_shouldExecuteResponseAction() throws {
        // Given
        repositoryStub.formResult = .success(CustomAction(name: "custom", data: [:]))
        let gesture = submitGesture(in: formView)

        // When
        formManager?.handleSubmitFormGesture(gesture)

        // Then
        XCTAssert(actionExecutorSpy.didCallDoAction)
    }

    func test_formSubmit_shouldPassRightDataToBeSubmitted() throws {
        // Given
        let gesture = submitGesture(in: formView)

        // When
        formManager?.handleSubmitFormGesture(gesture)

        // Then
        let submittedData = repositoryStub.formData

        XCTAssert(repositoryStub.didCallDispatch)
        assertSnapshot(matching: submittedData, as: .dump)
    }

    func test_formSubmitError_shouldNotExecuteAction() throws {
        // Given
        let gesture = submitGesture(in: formView)

        // When
        formManager?.handleSubmitFormGesture(gesture)

        // Then
        XCTAssertFalse(actionExecutorSpy.didCallDoAction)
    }
    
    // MARK: - Form Storage Logic Tests
    
    let validator3 = "validator3"
    
    private lazy var storedParameters = ["age", "id"]
    
    private let formId = "formId"
    
    private lazy var formViewWithStorage = Form(
        action: FormRemoteAction(path: "submit", method: .post),
        child: Container(children: [
            FormInput(name: "name", required: true, validator: validator3, child: InputComponent(value: "John Doe"), overrideStoredName: "OverridedName"),
            FormInput(name: "password", required: true, validator: validator3, child: InputComponent(value: "password")),
            FormSubmit(child: Button(text: "Add"))
        ]),
        storedParameters: storedParameters,
        shouldStoreFields: true,
        formId: formId
    ).toView(context: screen, dependencies: dependencies)
    
    private func setValidator3() {
        validator[validator3] = { _ in
            return true
        }
    }
    
    func test_formSubmit_shouldIncludeStoredValuesToSubmission() {
        // Given
        storedParameters = ["age", "id"]
        let formData = FormData()
        formData.save(data: (key: "age", value: "12"))
        formData.save(data: (key: "id", value: "1111111"))
        dataStoreStub.dataStore[formId] = formData

        let gesture = submitGesture(in: formViewWithStorage)

        // When
        formManager?.handleSubmitFormGesture(gesture)
        
        // Then
        XCTAssert(dataStoreStub.didCallRead == true)
        XCTAssert(repositoryStub.didCallDispatch)
        assertSnapshot(matching: repositoryStub.formData.values, as: .dump)
    }
    
//    func test_formSubmitMergingStoredValuesErrorKeyNotFound_shouldNotSubmit() {
//        // Given
//        storedParameters = ["age", "id"]
//        dataStoreStub.save(key: "age", value: "12")
//
//        let gesture = submitGesture(in: formViewWithStorage)
//
//        // When
//        formManager?.handleSubmitFormGesture(gesture)
//
//        // Then
//        XCTAssert(repositoryStub.didCallDispatch == false)
//    }
//
//    func test_formSubmitMergingStoredValuesErrorKeyDuplication_shouldNotSubmit() {
//        // Given
//        storedParameters = ["name"]
//        dataStoreStub.save(key: "name", value: "Yan")
//
//        let gesture = submitGesture(in: formViewWithStorage)
//
//        // When
//        formManager?.handleSubmitFormGesture(gesture)
//
//        // Then
//        XCTAssert(repositoryStub.didCallDispatch == false)
//    }
//
//    func test_formSubmit_shouldSaveFormInputs() {
//        // Given
//        let gesture = submitGesture(in: formViewWithStorage)
//
//        // When
//        formManager?.handleSubmitFormGesture(gesture)
//
//        // Then
//        XCTAssert(dataStoreStub.didCallSave)
//        assertSnapshot(matching: dataStoreStub.dataStore, as: .dump)
//    }
}

// MARK: - Stubs

private class DataStoreHandlerStub: FormDataStoreHandling {
    
    private(set) var didCallRead: Bool = false
    private(set) var didCallSave: Bool = false
    
    var dataStore: [String: FormData] = [:]
    
    func save(data: (key: String, value: String), formId: String) {
        didCallSave = true
        if let formData = dataStore[formId] {
            formData.save(data: data)
        } else {
            dataStore[formId] = FormData()
            save(data: data, formId: formId)
        }
    }
    
    func read(key: String) -> FormData? {
        didCallRead = true
        return dataStore[key]
    }

    func resetStub() {
        didCallRead = false
        didCallSave = false
        dataStore = [:]
    }
}

private struct InputComponent: ServerDrivenComponent {
    let value: String

    func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
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

private class HiddenStub: UIView, InputValue {

    let value: String

    init(_ formInputHidden: FormInputHidden, value: String) {
        self.value = value
        super.init(frame: .zero)
        self.beagleFormElement = formInputHidden
    }

    required init?(coder: NSCoder) {
        BeagleUI.fatalError("init(coder:) has not been implemented")
    }

    func getValue() -> Any {
        return value
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
