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
import XCTest
import SnapshotTesting
import BeagleSchema

class ImageTests: XCTestCase {

    func test_toView_shouldReturnTheExpectedView() throws {
        //Given
        let expectedContentMode = UIImageView.ContentMode.scaleToFill
        let component = Image(.local("teste"), contentMode: .fitXY)
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)

        //When
        guard let imageView = renderer.render(component) as? UIImageView else {
            XCTFail("Build View not returning UIImageView")
            return
        }
        
        // Then
        XCTAssertEqual(expectedContentMode, imageView.contentMode)
    }
    
    func test_renderImage() throws {
        setupDepedencies()

        let image: Image = try componentFromJsonFile(fileName: "ImageComponent")
        let view = image.toView(context: BeagleContextDummy(), dependencies: dependencies)
        assertSnapshotImage(view, size: CGSize(width: 400, height: 400))
    }
    
    func test_localImageDeserialize() throws {
        setupDepedencies()

        let image: Image = try componentFromJsonFile(fileName: "ImageComponent1")
        if case Image.PathType.local(let name) = image.path {
            XCTAssertEqual(name, "test_image_square-x")
        } else {
            XCTFail("Failed to decode correct image name.")
        }
    }
    
    func test_remoteImageDeserialize() throws {
        setupDepedencies()

        let image: Image = try componentFromJsonFile(fileName: "ImageComponent2")
        if case Image.PathType.network(let url) = image.path {
            XCTAssertEqual(url, "www.com")
        } else {
            XCTFail("Failed to decode correct image url.")
        }
    }
    
    func test_withInvalidURL_itShouldNotSetImage() throws {
        // Given
        let component = Image(.network("www.com"))
        
        // When
        guard let imageView = component.toView(context: BeagleContextDummy(), dependencies: BeagleScreenDependencies()) as? UIImageView else {
            XCTFail("Build view not returning UIImageView")
            return
        }
        
        // Then
        XCTAssertNil(imageView.image, "Expected image to be nil.")
    }
    
    private func setupDepedencies() {
        // Given
        let dependencies = BeagleDependencies()
        dependencies.appBundle = Bundle(for: ImageTests.self)
        let controller = BeagleControllerStub(dependencies: dependencies)
        let renderer = BeagleRenderer(controller: controller)

        // When
        let image: Image = try componentFromJsonFile(fileName: "ImageComponent")
        let view = renderer.render(image)

        // Then
        assertSnapshotImage(view, size: .custom(CGSize(width: 400, height: 400)))
    }
}
