//
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

class GridViewTests: XCTestCase {
    
    private let imageSize = ImageSize.custom(CGSize(width: 300, height: 300))
    
    private lazy var controller = BeagleScreenViewController(ComponentDummy())

    let simpleContext: DynamicObject = ["L", "I", "S", "T", "V", "I", "E", "W"]
    
    func testeGridViewTemplateSimple() {
        // Given
        let component = createGridView(
            contextValue: simpleContext,
            numColumns: 2
        )
        
        // When
        let view = renderGridView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }
    
    func testeGridViewWithFourColumns() {
        // Given
        let component = createGridView(
            contextValue: simpleContext,
            numColumns: 4
        )
        
        // When
        let view = renderGridView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }
    
    let contextDifferentSizes: DynamicObject = ["LIST VIEW", "LIST VIEW, LIST VIEW, LIST VIEW", "1", "LIST VIEW", "TEST 1, TEST 1, TEST 1", "TEST LIST VIEW, LIST VIEW, LIST VIEW", "12345, 12345, 12345"]
  
    func testeGridViewTemplateWithDifferentSizes() {
        // Given
        let component = createGridView(
            contextValue: contextDifferentSizes,
            numColumns: 2
        )
        
        // When
        let view = renderGridView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }
    
    func testeGridViewWithNumColumnsZero() {
        // Given
        let component = createGridView(
            contextValue: contextDifferentSizes,
            numColumns: 0
        )
        
        // When
        let view = renderGridView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }
}

extension GridViewTests {
    
    private func renderGridView(_ listComponent: GridView) -> UIView {
        let host = ComponentHostController(listComponent, renderer: controller.renderer)
        return host.view
    }
    
    func createGridView(
        contextValue: DynamicObject? = nil,
        onInit: [Action]? = nil,
        onScrollEnd: [Action]? = nil,
        isScrollIndicatorVisible: Bool? = nil,
        numColumns: Int
    ) -> GridView {
        return GridView(
            context: Context(
                id: "initialContext",
                value: contextValue ?? ""
            ),
            onInit: onInit,
            dataSource: Expression("@{initialContext}"),
            numColumns: numColumns,
            template: Container(
                children: [
                    Text(
                        "@{item}",
                        widgetProperties: WidgetProperties(
                            style: Style(
                                backgroundColor: "#bfdcae"
                            )
                        )
                    )
                ],
                widgetProperties: WidgetProperties(
                    style: Style(
                        backgroundColor: "#81b214",
                        margin: EdgeValue().all(10)
                    )
                )
            ),
            onScrollEnd: onScrollEnd,
            isScrollIndicatorVisible: isScrollIndicatorVisible,
            widgetProperties: WidgetProperties(
                style: Style(
                    backgroundColor: "#206a5d",
                    flex: Flex().grow(1)
                )
            )
        )
    }
}
