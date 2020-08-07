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
    
    func testContentMode() {
        //Given
        let value = ImageContentMode.center
        let expected = UIImageView.ContentMode.center
                
        //When
        let result = value.toUIKit()
        
        //Then
        XCTAssertEqual(result, expected)
    }
    
    func testRenderImage() throws {
        //Given
        let image: Image = try componentFromJsonFile(fileName: "ImageComponent")
        
        //When
        let view = renderer.render(image)
        
        //Then
        assertSnapshotImage(view, size: ImageSize.custom(CGSize(width: 400, height: 400)))
    }
    
    func testCancelRequest() {
        //Given
        let image = Image("@{img.path}")
        let dependency = BeagleDependencies()
        let repository = RepositoryStub(imageResult: .success(Data()))
        dependency.repository = repository
        let container = Container(children: [image])
        let controller = BeagleScreenViewController(viewModel: .init(screenType:.declarative(container.toScreen()), dependencies: dependency))
        let action = SetContext(contextId: "img", path: "path", value: ["_beagleImagePath_": "local", "mobileId": "shuttle"])
        let view = image.toView(renderer: controller.renderer)
        
        //When
        view.setContext(Context(id: "img", value: ["path": ["_beagleImagePath_": "remote", "url": "www.com.br"]]))
        controller.configBindings()
        action.execute(controller: controller, origin: view)
        
        // Then
        XCTAssertTrue(repository.token.didCallCancel)
    }
    
    func testInvalidURL() {
        // Given
        let component = Image(.value(.remote(.init(url: "www.com"))))
        // When
         let imageView = renderer.render(component) as? UIImageView
        
        // Then
        XCTAssertNotNil(imageView)
        XCTAssertNil(imageView?.image)
    }
    
    func testPlaceholder() {
        // Given
        let component = Image(.value(.remote(.init(url: "www.com", placeholder: "imageBeagle"))))
 
        // When
        let placeholderView = renderer.render(component)
        
        // Then
        assertSnapshotImage(placeholderView, size: ImageSize.custom(CGSize(width: 400, height: 400)))
    }
    
    func testImageLeak() {
        // Given
        let component = Image("@{img.path}")
        let controller = BeagleScreenViewController(viewModel: .init(screenType:.declarative(component.toScreen()), dependencies: BeagleDependencies()))
        
        var view = component.toView(renderer: controller.renderer)
        weak var weakView = view
    
        // When
        controller.configBindings()
        view = UIView()
        
        // Then
        XCTAssertNil(weakView)
    }
}
