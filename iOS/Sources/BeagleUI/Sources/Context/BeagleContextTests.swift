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

class BeagleContextSpy: BeagleContext {
    
    private(set) var analyticsEventCalled = false
    private(set) var didCallRegisterEvents = false
    private(set) var didCallRegisterFormSubmit = false
    private(set) var didCallLazyLoad = false
    private(set) var didCallDoAction = false
    private(set) var didCallRegisterEnabledWidget = false
    private(set) var actionCalled: Action?
    private(set) var didCallApplyLayout = true

    var screenController: UIViewController = UIViewController()

    func register(events: [Event], inView view: UIView) {
        didCallRegisterEvents = true
    }
    
    func doAnalyticsAction(_ action: AnalyticsClick, sender: Any) {
        analyticsEventCalled = true
    }

    func register(formSubmitEnabledWidget: Widget?, formSubmitDisabledWidget: Widget?) {
        didCallRegisterEnabledWidget = true
    }

    func register(form: Form, formView: UIView, submitView: UIView, validatorHandler validator: ValidatorProvider?) {
        didCallRegisterFormSubmit = true
    }

    func lazyLoad(url: String, initialState: UIView) {
        didCallLazyLoad = true
    }

    func doAction(_ action: Action, sender: Any) {
        didCallDoAction = true
        actionCalled = action
    }

    func applyLayout() {
        didCallApplyLayout = true
    }
}

final class BeagleContextTests: XCTestCase {
    
    func test_screenController_shouldBeBeagleScreenViewController() {
        // Given
        let component = SimpleComponent()
        let sut: BeagleContext = BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(component.content.toScreen()),
            dependencies: BeagleScreenDependencies()
        ))
        
        // Then
        XCTAssertTrue(sut.screenController is BeagleScreenViewController)
    }

    func test_registerAction_shouldAddGestureRecognizer() {
        // Given
        let component = SimpleComponent()
        let sut = BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(component.content.toScreen()),
            dependencies: BeagleScreenDependencies()
        ))
        let view = UILabel()
        let action = Navigate.popView
        
        // When
        sut.register(events: [.action(action)], inView: view)
        
        // Then
        XCTAssertEqual(1, view.gestureRecognizers?.count)
        XCTAssertTrue(view.isUserInteractionEnabled)
    }
    
    func test_action_shouldBeTriggered() {
        // Given
        let component = SimpleComponent()
        let actionExecutorSpy = ActionExecutorSpy()

        let controller = BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(component.content.toScreen()),
            dependencies: BeagleScreenDependencies(
                actionExecutor: actionExecutorSpy
            )
        ))
        
        let navigationController = UINavigationController(rootViewController: controller)
        guard let sut = navigationController.viewControllers.first as? BeagleScreenViewController else {
            XCTFail("Could not find `BeagleScreenViewController`.")
            return
        }
        
        let view = UILabel()
        let action = Navigate.popView
        sut.register(events: [.action(action)], inView: view)
        
        guard let eventsGestureRecognizer = view.gestureRecognizers?.first as? EventsGestureRecognizer else {
            XCTFail("Could not find `EventsGestureRecognizer`.")
            return
        }
        
        // When
        sut.handleGestureRecognizer(eventsGestureRecognizer)
                
        // Then
        XCTAssertTrue(actionExecutorSpy.didCallDoAction)
    }
    
    func test_lazyLoad_shouldReplaceTheInitialContent() {
        let dependencies = BeagleDependencies()
        dependencies.repository = RepositoryStub(
            componentResult: .success(SimpleComponent().content)
        )

        let sut = BeagleScreenViewController(viewModel: .init(
            screenType: .remote(.init(url: "")),
            dependencies: dependencies
        ))

        assertSnapshotImage(sut)
    }
    
    func test_lazyLoad_withUpdatableView_shouldCallUpdate() {
        // Given
        let initialView = OnStateUpdatableViewSpy()
        initialView.yoga.isEnabled = true
        let repositoryStub = RepositoryStub(
            componentResult: .success(ComponentDummy())
        )
        let sut = BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(ComponentDummy().toScreen()),
            dependencies: BeagleScreenDependencies(
                repository: repositoryStub
            )
        ))
        sut.view.addSubview(initialView)
        
        // When
        sut.lazyLoad(url: "URL", initialState: initialView)
        
        // Then
        XCTAssert(initialView.superview != nil)
        XCTAssert(initialView.didCallOnUpdateState)
    }
}

// MARK: - Testing Helpers

class UINavigationControllerSpy: UINavigationController {
    private(set) var popViewControllerCalled = false
    private(set) var presentViewControllerCalled = false
    private(set) var dismissViewControllerCalled = false

    override func popViewController(animated: Bool) -> UIViewController? {
        popViewControllerCalled = true
        return super.popViewController(animated: animated)
    }
    override func present(_ viewControllerToPresent: UIViewController, animated flag: Bool, completion: (() -> Void)? = nil) {
        presentViewControllerCalled = true
        super.present(viewControllerToPresent, animated: flag, completion: completion)
    }
    override func dismiss(animated flag: Bool, completion: (() -> Void)? = nil) {
        dismissViewControllerCalled = true
        super.dismiss(animated: flag, completion: completion)
    }
}

class OnStateUpdatableViewSpy: UIView, OnStateUpdatable {
    private(set) var didCallOnUpdateState = false
    
    func onUpdateState(component: ServerDrivenComponent) -> Bool {
        didCallOnUpdateState = true
        return true
    }
}
