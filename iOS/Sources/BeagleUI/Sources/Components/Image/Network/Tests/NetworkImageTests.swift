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

final class NetworkImageTests: XCTestCase {

    private let dependencies = BeagleScreenDependencies()
    
    func test_whenDecodingJson_thenItShouldReturnNetworkImage() throws {
        let component: NetworkImage = try componentFromJsonFile(fileName: "networkImageComponent")
        assertSnapshot(matching: component, as: .dump)
    }
    
    func test_withInvalidURL_itShouldNotSetImage() throws {
        // Given
        let component = NetworkImage(path: "www.com")
        
        // When
        guard let imageView = component.toView(context: BeagleContextDummy(), dependencies: dependencies) as? UIImageView else {
            XCTFail("Build view not returning UIImageView")
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
        
        // When
        guard let imageView = component.toView(context: BeagleContextDummy(), dependencies: dependencies) as? UIImageView else {
            XCTFail("Build view not returning UIImageView")
            return
        }
        
        // Then
        XCTAssertNil(imageView.image, "Expected image to be nil, because of dispatch async.")
    }
    
    func test_whenHasPlaceholder_shouldReturnItAsInitialView() {
        // Given
        let component = NetworkImage(path: "url", placeholder: Text("Loading .."))
        let context = BeagleContextSpy()
        let repositoryStub = RepositoryStub(imageResult: .success(Data()))
        let dependencies = BeagleScreenDependencies(repository: repositoryStub)
        
        // When
        guard let placeholderView = component.toView(context: context, dependencies: dependencies) as? UITextView else {
            XCTFail("Build view not returning Text.")
            return
        }
        
        // Then
        XCTAssert(context.didCallLazyLoadImage)
        XCTAssertNotNil(placeholderView.text, "Expected placeholder to not be nil.")
    }
}
