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

final class TabViewTests: XCTestCase {
    
    func test_whenDecodingJson_thenItShouldReturnATabView() throws {
        let component: Deprecated.TabView = try componentFromJsonFile(fileName: "TabView")
        assertSnapshot(matching: component, as: .dump)
    }
    
    func test_viewWithTabView() {
        let tabView = Deprecated.TabView(children: [
            tabItem(index: 1, flex: Flex(alignContent: .center)),
            tabItem(index: 2, flex: Flex(justifyContent: .center, alignContent: .center))
        ])
        
        let screen = Beagle.screen(.declarative(tabView.toScreen()))
        assertSnapshotImage(screen)
    }
    
    func test_initWithSingleComponentBuilder_shouldReturnExpectedInstance() {
        // Given / When
        let component = Deprecated.TabView(children: [
            tabItem(index: 1, flex: Flex(alignContent: .center))
        ])
        // Then
        XCTAssert(component.children.count > 0)
        XCTAssert(component.children[safe: 0]?.child is Container)
    }

    private func tabItem(index: Int, flex: Flex) -> TabItem {
        return TabItem(title: "Tab \(index)", child:
            Container(children: [
                Text(.value("Text Tab \(index)")),
                Text(.value("Text 2 Tab \(index)"))
            ])
            .applyFlex(flex)
        )
    }
}

extension TabItem: Equatable {
    public static func == (lhs: TabItem, rhs: TabItem) -> Bool {
        return lhs.title == rhs.title && lhs.icon == rhs.icon
    }
}
