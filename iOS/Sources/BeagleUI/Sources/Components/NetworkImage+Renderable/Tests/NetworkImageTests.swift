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

final class NetworkImageTests: XCTestCase {

    private let dependencies = BeagleScreenDependencies()
    
    func test_withInvalidURL_itShouldNotSetImage() throws {
        // Given
        let component = NetworkImage(path: "www.com")
        let renderer = BeagleRenderer(context: BeagleContextDummy(), dependencies: BeagleScreenDependencies())
        
        // When
        guard let imageView = renderer.render(component) as? UIImageView else {
            XCTFail("Renderer not returning UIImageView")
            return
        }
        
        // Then
        XCTAssertNil(imageView.image, "Expected image to be nil.")
    }
    
    func test_whenRequestSucceds_shouldReturnNetworkImage() {
        // Given
        let component = NetworkImage(path: "url")
        let repositoryStub = RepositoryStub(imageResult: .success(Data()))
        let dependencies = BeagleScreenDependencies(repository: repositoryStub)
        let renderer = BeagleRenderer(context: BeagleContextDummy(), dependencies: dependencies)

        // When
        guard let imageView = renderer.render(component) as? UIImageView else {
            XCTFail("Renderer not returning UIImageView")
            return
        }
        
        // Then
        XCTAssertNil(imageView.image, "Expected image to be nil, because of dispatch async.")
    }
    
    func test_whenHasPlaceholder_shouldReturnItAsInitialView() {
        // Given
        let component = NetworkImage(path: "url", placeholder: Image(name: "imageBeagle"))
        let repositoryStub = RepositoryStub(imageResult: .success(Data()))
        let dependencies = BeagleScreenDependencies(repository: repositoryStub)
        let renderer = BeagleRenderer(context: BeagleContextSpy(), dependencies: dependencies)

        // When
        guard let placeholderView = renderer.render(component) as? UIImageView else {
            XCTFail("Renderer not returning Image.")
            return
        }
        
        // Then
        XCTAssertNotNil(placeholderView, "Expected placeholder to not be nil.")
    }
}
