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
@testable import BeagleUI

final class ContainerTests: XCTestCase {
    
    func test_initWithChildren_shouldReturnContainerAndSetDependenciesProperly() {
        // Given
        let sut = Container(children: [
            Text("Some texts."),
            Text("More texts.")
        ], widgetProperties: .init(flex: Flex()))
        
        let mirror = Mirror(reflecting: sut)
        // When
        let flex = mirror.firstChild(of: WidgetProperties.self)?.flex
        let component = mirror.firstChild(of: [ServerDrivenComponent].self)
        // Then
        XCTAssertTrue(sut.children.count == 2)
        XCTAssertNotNil(flex)
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
        XCTAssertNotNil(container.flex)
    }
    
    func test_toView_shouldReturnTheExpectedView() throws {
        //Given
        let dependencies = BeagleScreenDependencies()
        let numberOfChilds = 3
        let containerChilds = Array(repeating: ComponentDummy(), count: numberOfChilds)
        let container = Container(children: containerChilds)
        
        // When
        let resultingView = container.toView(context: BeagleContextDummy(), dependencies: dependencies)
        
        //Then
        XCTAssert(resultingView.subviews.count == numberOfChilds)
    }
    
    func test_whenDecodingJson_shouldReturnAContainer() throws {
        let component: Container = try componentFromJsonFile(fileName: "Container")
        assertSnapshot(matching: component, as: .dump)
    }
    
    func test_renderContainer() throws {
        let component: Container = try componentFromJsonFile(fileName: "Container")
        let screen = Beagle.screen(.declarative(component.toScreen()))
        assertSnapshotImage(screen, size: ViewImageConfig.iPhoneXr.size!)
    }
}
