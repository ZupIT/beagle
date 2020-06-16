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

final class ScrollViewTests: XCTestCase {

    func test_initWithChildBuilder_shouldReturnExpectedInstance() {
        // Given / When
        let component = ScrollView(children: [
            Text("text")
        ])
        
        // Then
        XCTAssert(component.children.count == 1)
        XCTAssert(component.children[safe: 0] is Text)
    }
    
    func test_initWithChildrenBuilder_shouldReturnExpectedInstance() {
        // Given / When
        let component = ScrollView(children: [
            Text("text"),
            Button(text: "text")
        ])
        
        // Then
        XCTAssert(component.children.count == 2)
        XCTAssert(component.children[safe: 0] is Text)
        XCTAssert(component.children[safe: 1] is Button)
    }
    
    func test_toView_shouldReturnTheExpectedView() {
        // Given
        let container = ScrollView(children: [
            ComponentDummy()
        ])
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)

        // When
        let resultingView = renderer.render(container)
        
        // Then
        XCTAssert(resultingView.subviews.count == 1)
    }
    
    func test_whenLayoutSubViewsIsCalledOnBagleContainerScrollView_itShouldSetupTheContentSizeCorrectly() {
        // Given
        let subview = UIView(frame: .init(x: 0, y: 0, width: 100, height: 100))
        let sut = BeagleContainerScrollView()
        sut.addSubview(subview)

        // When
        sut.layoutSubviews()

        // Then
        XCTAssert(subview.frame.size == sut.contentSize)
        
    }
    
    func test_renderScrollView() throws {
        let component: ScrollView = try componentFromJsonFile(fileName: "ScrollViewComponent")
        let screen = Beagle.screen(.declarative(component.toScreen()))
        assertSnapshotImage(screen, size: .custom(CGSize(width: 150, height: 300)))
    }

    func test_renderHorizontalScrollView() throws {
        let component: ScrollView = try componentFromJsonFile(fileName: "HorizontalScrollView")
        let screen = Beagle.screen(.declarative(component.toScreen()))
        assertSnapshotImage(screen, size: .custom(CGSize(width: 300, height: 30)))
    }

}
