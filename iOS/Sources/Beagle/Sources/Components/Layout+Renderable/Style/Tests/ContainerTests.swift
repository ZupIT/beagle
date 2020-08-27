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

// swiftlint:disable force_unwrapping

import XCTest
import SnapshotTesting
@testable import Beagle
import BeagleSchema

final class ContainerTests: XCTestCase {
    
    func test_initWithChildren_shouldReturnContainerAndSetDependenciesProperly() {
        // Given
        let sut = Container(children: [
            Text("Some texts."),
            Text("More texts.")
        ], widgetProperties: .init(style: Style(flex: Flex())))
        
        let mirror = Mirror(reflecting: sut)
        
        // When
        let style = mirror.firstChild(of: WidgetProperties.self)?.style
        let component = mirror.firstChild(of: [ServerDrivenComponent].self)

        // Then
        XCTAssertTrue(sut.children.count == 2)
        XCTAssertNotNil(style)
        XCTAssertNotNil(component)
    }
    
    func test_applyFlex_shouldReturnContainer() {
        // Given
        let component = Container(children: [
            Text("Some texts")
        ])
        // When
        let container = component.applyFlex(Flex(justifyContent: .center))
        // Then
        XCTAssertNotNil(container.style?.flex)
    }
    
    func test_toView_shouldReturnTheExpectedView() throws {
        //Given
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        let numberOfChildren = 3
        let containerChildren = Array(repeating: ComponentDummy(), count: numberOfChildren)
        let container = Container(children: containerChildren)
        
        // When
        let resultingView = renderer.render(container)
        
        //Then
        XCTAssert(resultingView.subviews.count == numberOfChildren)
    }
    
    func test_renderContainer() throws {
        let container = Container(
            children: [
                Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum et felis tortor. Maecenas laoreet metus in augue mattis, quis imperdiet urna ornare. Sed commodo fringilla massa, sit amet molestie nunc. Quisque euismod eros felis. Nam dapibus venenatis consequat."),
                Text("Donec orci elit, scelerisque vel mattis at, ornare in libero. Cras vestibulum justo et lacus accumsan malesuada. Pellentesque gravida risus tincidunt sapien commodo iaculis. Praesent eget consectetur ligula, vitae fringilla urna. Donec erat arcu, fermentum sed orci in, euismod dictum augue. Aenean ac ullamcorper ante, sit amet commodo elit. Mauris et nibh ac ante luctus fermentum varius nec mauris."),
                Text("Sed vel nisl tortor. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Donec fringilla velit vulputate ultricies auctor. Sed et enim lacinia risus hendrerit efficitur vitae vel tellus.")
            ],
            widgetProperties: .init(
                style: .init(
                    backgroundColor: "#0000FF50",
                    cornerRadius: .init(radius: 30.0),
                    margin: EdgeValue().horizontal(20),
                    flex: Flex().grow(1).justifyContent(.spaceEvenly)
                )
            )
        )

        let screen = Beagle.screen(.declarative(container.toScreen()))
        assertSnapshotImage(screen, size: .custom(ViewImageConfig.iPhoneXr.size!))
    }
    
    func test_testActionExecuting() {
        //Given
        
        let expectation = self.expectation(description: "ActionExetuting")
        let controllerSpy = BeagleControllerSpy()
        controllerSpy.expectation = expectation
        
        let renderer = BeagleRenderer(controller: controllerSpy)
        let sut = Container(children: [Text("Lorem ipsum")], onInit: [ActionDummy()])
        
        //When

        _ = sut.toView(renderer: renderer)
        
        //Then
        
        waitForExpectations(timeout: 5, handler: nil)
        XCTAssert(controllerSpy.didCalledExecute)
    }
}

class BeagleControllerSpy: BeagleController {
    
    var dependencies: BeagleDependenciesProtocol = Beagle.dependencies
    var serverDrivenState: ServerDrivenState = .finished
    var screenType: ScreenType = .declarativeText("")
    var screen: Screen?
    
    var expectation: XCTestExpectation?
    
    func addBinding(_ update: @escaping () -> Void) { }
    func execute(action: RawAction, sender: Any) { }
    
    private(set) var didCalledExecute = false
    private(set) var lastImplicitContext: Context?
    
    func execute(actions: [RawAction]?, origin: UIView) {
        didCalledExecute = true
        expectation?.fulfill()
    }
    
    func execute(actions: [RawAction]?, with contextId: String, and contextValue: DynamicObject, origin: UIView) {
        lastImplicitContext = Context(id: contextId, value: contextValue)
        didCalledExecute = true
        expectation?.fulfill()
    }
    
}
