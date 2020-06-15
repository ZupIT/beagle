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
    
    func test_toView_shouldReturnTheExpectedView() {
        // Given
        let lazyComponent = LazyComponent(path: "path", initialState: ComponentDummy())
        let lazyLoadManagerSpy = LazyLoadManagerSpy()
        let context = BeagleContextSpy(lazyLoadManager: lazyLoadManagerSpy)
        let renderer = BeagleRenderer(context: context, dependencies: BeagleScreenDependencies())

        // When
        _ = renderer.render(lazyComponent)
        
        // Then
        XCTAssertTrue(lazyLoadManagerSpy.didCallLazyLoad)
    }
    
    func test_loadUnknowComponent_shouldRenderTheError() {
        let lazyComponent = LazyComponent(path: "unknow-widget", initialState: Text("Loading..."))
        let size = ImageSize.custom(CGSize(width: 300, height: 75))
        let repository = LazyRepositoryStub()
        let dependecies = BeagleDependencies()
        dependecies.repository = repository
        
        let screen = BeagleScreenViewController(
            viewModel: .init(
                screenType: .declarative(lazyComponent.toScreen()),
                dependencies: dependecies
            )
        )
        
        assertSnapshotImage(screen, size: size)
        repository.componentCompletion?(.success(UnknownComponent(type: "LazyError") as BeagleUI.ServerDrivenComponent))
        assertSnapshotImage(screen, size: size)
    }

}

class LazyRepositoryStub: Repository {

    var componentCompletion: ((Result<BeagleUI.ServerDrivenComponent, Request.Error>) -> Void)?
    var formCompletion: ((Result<Action, Request.Error>) -> Void)?
    var imageCompletion: ((Result<Data, Request.Error>) -> Void)?

    func fetchComponent(url: String, additionalData: RemoteScreenAdditionalData?, completion: @escaping (Result<BeagleUI.ServerDrivenComponent, Request.Error>) -> Void) -> RequestToken? {
        componentCompletion = completion
        return nil
    }

    func submitForm(url: String, additionalData: RemoteScreenAdditionalData?, data: Request.FormData, completion: @escaping (Result<Action, Request.Error>) -> Void) -> RequestToken? {
        formCompletion = completion
        return nil
    }

    func fetchImage(url: String, additionalData: RemoteScreenAdditionalData?, completion: @escaping (Result<Data, Request.Error>) -> Void) -> RequestToken? {
        imageCompletion = completion
        return nil
    }
}
