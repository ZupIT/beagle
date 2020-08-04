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

@testable import Beagle
import XCTest
import SnapshotTesting
import BeagleSchema

class ImageTests: XCTestCase {
    
    var dependencies: BeagleDependencies {
        // swiftlint:disable implicit_getter
        get {
            let dependency = BeagleDependencies()
            dependency.appBundle = Bundle(for: ImageTests.self)
            return dependency
        }
    }

    lazy var controller = BeagleControllerStub(dependencies: dependencies)
    lazy var renderer = BeagleRenderer(controller: controller)
    
    func test_toView_shouldReturnTheExpectedView() throws {
        //Given
        let expectedContentMode = UIImageView.ContentMode.scaleToFill
        let component = Image(.value(.local("teste")), mode: .fitXY)
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
        let image: Image = try componentFromJsonFile(fileName: "ImageComponent")
        let view = renderer.render(image)
        assertSnapshotImage(view, size: ImageSize.custom(CGSize(width: 400, height: 400)))
    }
    
    func test_cancelRequestImageRemote() throws {
        //Given
        let image = Image("@{img.path}")
        let dependency = BeagleDependencies()
        let repository = RepositoryStub(imageResult: .success(Data()))
        dependency.repository = repository
        let container = Container(children: [image])
        let controller = BeagleScreenViewController(viewModel: .init(screenType:.declarative(container.toScreen()), dependencies: dependency))
        let action = SetContext(contextId: "img", path: "path", value: ["_beagleImagePath_": "local", "mobileId": "shuttle"])
        
        //When
        let view = image.toView(renderer: controller.renderer)
        view.setContext(Context(id: "img", value: ["path": ["_beagleImagePath_": "remote", "url": "www.com.br"]]))
        controller.configBindings()
        action.execute(controller: controller, origin: view)
        
        // Then
        XCTAssert(repository.token.didCallCancel)
    }
    
    func test_localImageDeserialize() throws {
        let image: Image = try componentFromJsonFile(fileName: "ImageComponent1")
        let path = image.path.evaluate(with: renderer.render(image))
        if case .local(let mobileId) = path {
            XCTAssertEqual(mobileId, "test_image_square-x")
        } else {
            XCTFail("Failed to decode correct image name.")
        }
    }
    
    func test_remoteImageDeserialize() throws {
        let bla = "www.com"
        let image: Image = try componentFromJsonFile(fileName: "ImageComponent2")
        let path = image.path.evaluate(with: renderer.render(image))
        if case .remote(let remote) = path {
            XCTAssertEqual(remote.url, bla)
        } else {
            XCTFail("Failed to decode correct image url.")
        }
    }
    
    func test_withInvalidURL_itShouldNotSetImage() throws {
        // Given
        let component = Image(.value(.remote(.init(url: "www.com"))))
        // When
        guard let imageView = renderer.render(component) as? UIImageView else {
            XCTFail("Build view not returning UIImageView")
            return
        }
        
        // Then
        XCTAssertNil(imageView.image, "Expected image to be nil.")
    }
    
    func test_whenRemoteHasPlaceholder_shouldReturnItAsInitialView() {
        // Given
        let component = Image(.value(.remote(.init(url: "www.com", placeholder: "imageBeagle"))))
 
        // When
        guard let placeholderView = renderer.render(component) as? UIImageView else {
            XCTFail("Renderer not returning Image.")
            return
        }
        
        // Then
        XCTAssertNotNil(placeholderView, "Expected placeholder to not be nil.")
    }
}
