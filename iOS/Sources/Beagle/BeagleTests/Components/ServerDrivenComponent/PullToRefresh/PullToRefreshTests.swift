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
@testable import Beagle

class PullToRefreshTests: XCTestCase {
    
    private let imageSize = ImageSize.custom(CGSize(width: 80, height: 40))
    
    func testDecodingPullToRefresh() throws {
        let component: PullToRefresh = try componentFromJsonFile(fileName: "PullToRefresh")
        assertSnapshot(matching: component, as: .dump)
    }

    func testPullToRefreshSnapshot() throws {
        // Given // When
        let component = PullToRefresh(
            isRefreshing: false,
            color: "#FF0000",
            child: Text("Text")
        )
        
        let controller = BeagleScreenViewController(component)
        
        // Then
        assertSnapshotImage(controller, size: imageSize)
    }
    
    func testPullToRefreshConfigWithList() {
        // Given // When
        let controller = BeagleScreenViewController(ComponentDummy())
        let view = PullToRefresh(
            child: ListView { Text("text") }
        ).toView(renderer: controller.renderer)
        
        // Then
        XCTAssert(view is ListViewUIComponent)
    }
    
    func testPullToRefreshConfigWithScroll() {
        // Given // When
        let controller = BeagleScreenViewController(ComponentDummy())
        let view = PullToRefresh(
            child: ScrollView { Text("text") }
        ).toView(renderer: controller.renderer)
        
        // Then
        XCTAssert(view is BeagleContainerScrollView)
        XCTAssert(view.subviews[0].subviews[0] is UITextView)
    }
    
    func testPullToRefreshConfigWithoutScroll() {
        // Given // When
        let controller = BeagleScreenViewController(ComponentDummy())
        let view = PullToRefresh(
            child: Text("text")
        ).toView(renderer: controller.renderer)
        
        // Then
        XCTAssert(view is BeagleContainerScrollView)
        XCTAssert(view.subviews[0].subviews[0] is UITextView)
    }

}
