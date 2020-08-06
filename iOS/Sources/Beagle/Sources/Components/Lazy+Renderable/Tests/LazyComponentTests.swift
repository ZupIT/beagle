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
@testable import Beagle
import BeagleSchema

final class LazyComponentTests: XCTestCase {
    
    func test_initWithInitialStateBuilder_shouldReturnExpectedInstance() {
        // Given / When
        let sut = LazyComponent(
            path: "component",
            initialState: Text("text")
        )

        // Then
        XCTAssert(sut.path == "component")
        XCTAssert(sut.initialState is Text)
    }
    
    func test_lazyLoad_shouldReplaceTheInitialContent() {
        var initialState = Text("Loading...")
        initialState.widgetProperties.style = .init(backgroundColor: "#00FF00")
        let sut = LazyComponent(path: "", initialState: initialState)
        let repository = LazyRepositoryStub()
        let dependecies = BeagleDependencies()
        dependecies.repository = repository
        
        let screenController = BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(Screen(child: sut)),
            dependencies: dependecies)
        )
        
        let size = CGSize(width: 75, height: 80)
        assertSnapshotImage(screenController, size: .custom(size))
        
        screenController.view.setContext(Context(id: "ctx", value: "value of ctx"))
        var lazyLoaded = Text("Lazy Loaded! @{ctx}")
        lazyLoaded.widgetProperties.style = .init(backgroundColor: "#FFFF00")
        repository.componentCompletion?(.success(lazyLoaded))
        
        let consumeMainQueue = expectation(description: "consumeMainQueue")
        DispatchQueue.main.async { consumeMainQueue.fulfill() }
        waitForExpectations(timeout: 1, handler: nil)
        
        assertSnapshotImage(screenController, size: .custom(size))
    }

    func test_lazyLoad_withUpdatableView_shouldCallUpdate() {
        let initialView = OnStateUpdatableViewSpy()
        let sut = LazyComponent(
            path: "",
            initialState: ComponentDummy(resultView: initialView)
        )
        let repository = LazyRepositoryStub()
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        controller.dependencies = BeagleScreenDependencies(repository: repository)
        
        let view = sut.toView(renderer: renderer)
        repository.componentCompletion?(.success(ComponentDummy()))
        
        XCTAssertEqual(view, initialView)
        XCTAssertTrue(initialView.didCallOnUpdateState)
    }
    
    func test_whenLoadFail_shouldSetNotifyTheScreen() {
        // Given
        let hostView = UIView()
        let initialView = UIView()
        let sut = LazyComponent(
            path: "",
            initialState: ComponentDummy(resultView: initialView)
        )
        let repository = LazyRepositoryStub()
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        controller.dependencies = BeagleScreenDependencies(repository: repository)
        
        // When
        let view = sut.toView(renderer: renderer)
        hostView.addSubview(view)
        repository.componentCompletion?(.failure(.urlBuilderError))
        
        // Then
        guard case .error(.lazyLoad(.urlBuilderError), let retry) = controller.serverDrivenState else {
            XCTFail("""
            Expected state .error(.lazyLoad(.urlBuilderError), BeagleRetry)
            but found \(controller.serverDrivenState)
            """)
            return
        }
        XCTAssertEqual(view, initialView)
        XCTAssertEqual(view.superview, hostView)
        
        // When
        repository.componentCompletion = nil
        let lazyLoadedContent = UIView()
        retry()
        repository.componentCompletion?(.success(ComponentDummy(resultView: lazyLoadedContent)))
        
        let expect = expectation(description: "consume queue")
        DispatchQueue.main.async { expect.fulfill() }
        waitForExpectations(timeout: 1)
        
        XCTAssertNil(view.superview)
        XCTAssertEqual(lazyLoadedContent.superview, hostView)
    }
    
}

class LazyRepositoryStub: Repository {

    var componentCompletion: ((Result<ServerDrivenComponent, Request.Error>) -> Void)?
    var formCompletion: ((Result<RawAction, Request.Error>) -> Void)?
    var imageCompletion: ((Result<Data, Request.Error>) -> Void)?
    
    private(set) var formData: Request.FormData?

    func fetchComponent(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        useCache: Bool,
        completion: @escaping (Result<ServerDrivenComponent, Request.Error>) -> Void
    ) -> RequestToken? {
        componentCompletion = completion
        return nil
    }

    func submitForm(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        data: Request.FormData,
        completion: @escaping (Result<RawAction, Request.Error>) -> Void
    ) -> RequestToken? {
        formData = data
        formCompletion = completion
        return nil
    }

    func fetchImage(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        completion: @escaping (Result<Data, Request.Error>) -> Void
    ) -> RequestToken? {
        imageCompletion = completion
        return nil
    }
}

class OnStateUpdatableViewSpy: UIView, OnStateUpdatable {
    private (set) var didCallOnUpdateState = false
    
    func onUpdateState(component: ServerDrivenComponent) -> Bool {
        didCallOnUpdateState = true
        return true
    }
}
