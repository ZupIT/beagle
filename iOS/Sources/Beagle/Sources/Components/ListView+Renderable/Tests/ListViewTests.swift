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
@testable import Beagle
import SnapshotTesting
import BeagleSchema

final class ListViewTests: XCTestCase {

    private let imageSize = ImageSize.custom(CGSize(width: 300, height: 300))

    // MARK: - 3 Rows

    private let just3Rows: [ServerDrivenComponent] = [
        Text("Item 1", widgetProperties: .init(style: .init(backgroundColor: "#FF0000"))),
        Text("Item 2", widgetProperties: .init(style: .init(backgroundColor: "#00FF00"))),
        Text("Item 3", widgetProperties: .init(style: .init(backgroundColor: "#0000FF")))
    ]

    func testDirectionHorizontal() throws {
        let component = ListView(
            children: just3Rows,
            direction: .horizontal
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVertical() throws {
        let component = ListView(
            children: just3Rows,
            direction: .vertical
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    // MARK: - Many Rows

    private let manyRows: [ServerDrivenComponent] = (0..<20).map { i in
        return ListViewTests.createText("Item \(i)", position: Double(i) / 19)
    }

    func testDirectionHorizontalWithManyRows() {
        let component = ListView(
            children: manyRows,
            direction: .horizontal
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVerticalWithManyRows() {
        let component = ListView(
            children: manyRows,
            direction: .vertical
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    // MARK: - Many Large Rows

    private let manyLargeRows: [ServerDrivenComponent] = (0..<20).map { i in
        return ListViewTests.createText(
            "< \(i) \(String(repeating: "-", count: 22)) \(i) >",
            position: Double(i) / 19
        )
    }

    func testDirectionHorizontalWithManyLargeRows() {
        let component = ListView(
            children: manyLargeRows,
            direction: .horizontal
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVerticalWithManyLargeRows() {
        let component = ListView(
            children: manyLargeRows,
            direction: .vertical
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    // MARK: Rows with Different Sizes

    private let rowsWithDifferentSizes: [ServerDrivenComponent] = (0..<20).map { i in
        return ListViewTests.createText(
            "< \(i) ---\(i % 3 == 0 ? "/↩\n↩\n /" : "")--- \(i) >",
            position: Double(i) / 19
        )
    }

    func testDirectionHorizontalWithRowsWithDifferentSizes() {
        let component = ListView(
            children: rowsWithDifferentSizes,
            direction: .horizontal
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVerticalWithRowsWithDifferentSizes() {
        let component = ListView(
            children: rowsWithDifferentSizes,
            direction: .vertical
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }
    
    // MARK: - Helper

    private func makeListUiView(_ listComponent: ListView) -> UIView {
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        return renderer.render(listComponent)
    }

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

// MARK: - Testing Helpers

private class ComponentWithRequestViewSpy: UIView, HTTPRequestCanceling {

    private(set) var cancelHTTPRequestCalled = false
    
    func cancelHTTPRequest() {
        cancelHTTPRequestCalled = true
    }

}
