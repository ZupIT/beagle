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

final class ListViewTests: XCTestCase {

    private let imageSize = ImageSize.custom(CGSize(width: 300, height: 300))

    private let just3Rows: [ServerDrivenComponent] = [
        Text("Item 1", widgetProperties: .init(style: .init(backgroundColor: "#FF0000"))),
        Text("Item 2", widgetProperties: .init(style: .init(backgroundColor: "#00FF00"))),
        Text("Item 3", widgetProperties: .init(style: .init(backgroundColor: "#0000FF")))
    ]
    
    private let manyRows: [ServerDrivenComponent] = (0..<20).map { i in
        return ListViewTests.createText("Item \(i)", position: Double(i) / 19)
    }
    
    private let manyLargeRows: [ServerDrivenComponent] = (0..<20).map { i in
        return ListViewTests.createText(
            "< \(i) \(String(repeating: "-", count: 22)) \(i) >",
            position: Double(i) / 19
        )
    }
    
    private let rowsWithDifferentSizes: [ServerDrivenComponent] = (0..<20).map { i in
        return ListViewTests.createText(
            "< \(i) ---\(i % 3 == 0 ? "/↩\n↩\n /" : "")--- \(i) >",
            position: Double(i) / 19
        )
    }
    
    private lazy var controller = BeagleScreenViewController(ComponentDummy())
    
    private func renderListView(_ listComponent: ListView) -> UIView {
        let host = ComponentHostController(listComponent, renderer: controller.renderer)
        return host.view
    }
    
    func createListView(
        direction: ListView.Direction,
        contextValue: DynamicObject,
        onInit: [Action]? = nil,
        onScrollEnd: [Action]? = nil
    ) -> ListView {
        return ListView(
            context: Context(
                id: "initialContext",
                value: contextValue
            ),
            onInit: onInit,
            dataSource: Expression("@{initialContext}"),
            direction: direction,
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
            widgetProperties: WidgetProperties(
                style: Style(
                    backgroundColor: "#206a5d",
                    flex: Flex().grow(1)
                )
            )
        )
    }
    
    // MARK: - Testing Direction
    
    let simpleContext: DynamicObject = ["L", "I", "S", "T", "V", "I", "E", "W"]
    
    func testHorizontalDirection() {
        // Given
        let component = createListView(
            direction: .horizontal,
            contextValue: simpleContext
        )
        
        // When
        let view = renderListView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }
    
    func testVerticalDirection() {
        // Given
        let component = createListView(
            direction: .vertical,
            contextValue: simpleContext
        )
        
        // When
        let view = renderListView(component)
        
        // Then
        assertSnapshotImage(view, size: imageSize)
    }
    
    // MARK: - Testing Context With Different Sizes
    
    let contextDifferentSizes: DynamicObject = ["LIST", "VIEW", "1", "LIST VIEW", "TEST 1", "TEST LIST VIEW", "12345"]
    
    func testHorizontalDirectionWithDifferentSizes() {
        // Given
        let component = createListView(
            direction: .horizontal,
            contextValue: contextDifferentSizes
        )
        
        // When
        let view = renderListView(component)
        
        // Then
        assertSnapshotImage(view, size: imageSize)
    }
    
    func testVerticalDirectionWithDifferentSizes() {
        // Given
        let component = createListView(
            direction: .vertical,
            contextValue: contextDifferentSizes
        )
        
        // When
        let view = renderListView(component)
        
        // Then
        assertSnapshotImage(view, size: imageSize)
    }
    
    // MARK: - Testing Execute Action onScrollEnd
    
    func testVerticalWithAction() {
        // Given
        let expectation = XCTestExpectation(description: "Execute onScrollEnd")
        let action = ActionStub { _, _ in
            expectation.fulfill()
        }
        let component = createListView(
            direction: .vertical,
            contextValue: [.empty],
            onScrollEnd: [action]
        )
        
        // When
        let view = renderListView(component) as? ListViewUIComponent
        view?.frame = CGRect(x: 0, y: 0, width: 300, height: 300)
        view?.layoutIfNeeded()
        
        // Then
        wait(for: [expectation], timeout: 1.0)
        XCTAssertEqual(view?.onScrollEndExecuted, true)
    }
    
    func testHorizontalWithAction() {
        // Given
        let expectation = XCTestExpectation(description: "Execute onScrollEnd")
        let action = ActionStub { _, _ in
            expectation.fulfill()
        }
        let component = createListView(
            direction: .horizontal,
            contextValue: [.empty],
            onScrollEnd: [action]
        )
        
        // When
        let view = renderListView(component) as? ListViewUIComponent
        view?.frame = CGRect(x: 0, y: 0, width: 300, height: 300)
        view?.layoutIfNeeded()
        
        // Then
        wait(for: [expectation], timeout: 1.0)
        XCTAssertEqual(view?.onScrollEndExecuted, true)
    }
    
    func testSetupSizeDefaultListView() {
        // Given
        let component = ListView(
            dataSource: .value([.empty]),
            template: ComponentDummy()
        )
        
        // When
        _ = renderListView(component)
        
        // Then
        XCTAssertNil(component.widgetProperties.style?.flex?.grow)
    }
    
}

// MARK: - Testing Helpers

private struct ActionStub: Action {
    
    var analytics: ActionAnalyticsConfig? { return nil }
    let execute: ((BeagleController, UIView) -> Void)?
    
    init(execute: @escaping (BeagleController, UIView) -> Void) {
        self.execute = execute
    }
    
    init(from decoder: Decoder) throws {
        execute = nil
    }
    
    func execute(controller: BeagleController, origin: UIView) {
        execute?(controller, origin)
    }
}

// MARK: - Tests deprecated
extension ListViewTests {
    
    func testDirectionHorizontal() throws {
        // Given
        let component = ListView(
            children: just3Rows,
            direction: .horizontal
        )

        // When
        let view = renderListView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVertical() throws {
        // Given
        let component = ListView(
            children: just3Rows,
            direction: .vertical
        )

        // When
        let view = renderListView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }

    // MARK: - Many Rows

    func testDirectionHorizontalWithManyRows() {
        // Given
        let component = ListView(
            children: manyRows,
            direction: .horizontal
        )

        // When
        let view = renderListView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVerticalWithManyRows() {
        // Given
        let component = ListView(
            children: manyRows,
            direction: .vertical
        )

        // When
        let view = renderListView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }

    // MARK: - Many Large Rows

    func testDirectionHorizontalWithManyLargeRows() {
        // Given
        let component = ListView(
            children: manyLargeRows,
            direction: .horizontal
        )

        // When
        let view = renderListView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVerticalWithManyLargeRows() {
        // Given
        let component = ListView(
            children: manyLargeRows,
            direction: .vertical
        )

        // When
        let view = renderListView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }

    // MARK: Rows with Different Sizes

    func testDirectionHorizontalWithRowsWithDifferentSizes() {
        // Given
        let component = ListView(
            children: rowsWithDifferentSizes,
            direction: .horizontal
        )

        // When
        let view = renderListView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVerticalWithRowsWithDifferentSizes() {
        // Given
        let component = ListView(
            children: rowsWithDifferentSizes,
            direction: .vertical
        )

        // When
        let view = renderListView(component)

        // Then
        assertSnapshotImage(view, size: imageSize)
    }
    
    func test_whenDecodingJson_thenItShouldReturnAListView() throws {
        let component: ListView = try componentFromJsonFile(fileName: "listViewComponent")
        assertSnapshot(matching: component, as: .dump)
    }
    
    // MARK: - Helper

    private static func createText(_ string: String, position: Double) -> Text {
        let text = Int(round(position * 255))
        let textColor = "#\(String(repeating: String(format: "%02X", text), count: 3))"
        let background = 255 - text
        let backgroundColor = "#\(String(repeating: String(format: "%02X", background), count: 3))"
        return Text(
            .value(string),
            textColor: .value(textColor),
            widgetProperties: .init(style: Style(backgroundColor: backgroundColor))
        )
    }
}
