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

final class LazyLoadManagerTests: XCTestCase {
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
        sut.lazyLoadManager.lazyLoad(url: "URL", initialState: initialView)
        
        // Then
        XCTAssert(initialView.superview != nil)
        XCTAssert(initialView.didCallOnUpdateState)
    }
}
