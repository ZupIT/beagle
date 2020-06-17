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
@testable import BeagleUI
import SnapshotTesting
import BeagleSchema

final class TabViewTests: XCTestCase {
    
    func test_viewWithTabView() {
        let tabView = TabView(tabItems: [
            tabItem(index: 1, flex: Flex(alignContent: .center)),
            tabItem(index: 2, flex: Flex(justifyContent: .center, alignContent: .center))
        ])
        
        let screen = Beagle.screen(.declarative(tabView.toScreen()))
        assertSnapshotImage(screen)
    }
    
    func test_initWithSingleComponentBuilder_shouldReturnExpectedInstance() {
        // Given / When
        let component = TabView(tabItems: [
            tabItem(index: 1, flex: Flex(alignContent: .center))
        ])
        // Then
        XCTAssert(component.tabItems.count > 0)
        XCTAssert(component.tabItems[safe: 0]?.content is Container)
    }
    
    func test_toView_shouldReturnTheExpectedView() {
        // Given
        let component = TabView(tabItems: [
             TabItem(title: "Tab 1", content:
                 Container(children: [
                     Text("Blaaslkdjfaskldjfalskdjfasldjfasldfj"),
                     Text("Blaaslkdjfaskldjfalskdjfasldjfasldfj")
                 ])
                .applyFlex(Flex(alignContent: .center))
             ),
             TabItem(title: "Tab 2", content:
                 Container(children: [
                     Text("Text1 Tab 2"),
                     Text("Text2 Tab 2")
                 ])
                 .applyFlex(Flex(justifyContent: .flexEnd))
             )
        ])
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        
        // When
        let resultingView = renderer.render(component)
        guard let tabViewUIComponent = resultingView as? TabViewUIComponent else {
            XCTFail("Expected `TabViewUIComponent`, but got \(String(describing: resultingView)).")
            return
        }
        
        let model = Mirror(reflecting: tabViewUIComponent).firstChild(of: TabViewUIComponent.Model.self)
        
        // Then
        XCTAssert(component.tabItems == model?.tabViewItems)
    }

    private func tabItem(index: Int, flex: Flex) -> TabItem {
        return TabItem(title: "Tab \(index)", content:
            Container(children: [
                Text("Text Tab \(index)"),
                Text("Text 2 Tab \(index)")
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
