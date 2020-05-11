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

final class ListViewTests: XCTestCase {

    private let imageSize = CGSize(width: 300, height: 300)

    // MARK: - 3 Children

    private let just3Children: [ServerDrivenComponent] = [
        Text("Item 1", widgetProperties: .init(appearance: .init(backgroundColor: "#FF0000"))),
        Text("Item 2", widgetProperties: .init(appearance: .init(backgroundColor: "#00FF00"))),
        Text("Item 3", widgetProperties: .init(appearance: .init(backgroundColor: "#0000FF")))
    ]

    func testDirectionHorizontal() throws {
        let component = ListView(
            children: just3Children,
            direction: .horizontal
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVertical() throws {
        let component = ListView(
            children: just3Children,
            direction: .vertical
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    // MARK: - Many Children

    private let manyChildren: [ServerDrivenComponent] = (0..<20).map { i in
        return ListViewTests.createText("Item \(i)", position: Double(i) / 19)
    }

    func testDirectionHorizontalWithManyRows() {
        let component = ListView(
            children: manyChildren,
            direction: .horizontal
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVerticalWithManyChildren() {
        let component = ListView(
            children: manyChildren,
            direction: .vertical
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    // MARK: - Many Large Children

    private let manyLargeChildren: [ServerDrivenComponent] = (0..<20).map { i in
        return ListViewTests.createText(
            "< \(i) \(String(repeating: "-", count: 22)) \(i) >",
            position: Double(i) / 19
        )
    }

    func testDirectionHorizontalWithManyLargeChildren() {
        let component = ListView(
            children: manyLargeChildren,
            direction: .horizontal
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVerticalWithManyLargeChildren() {
        let component = ListView(
            children: manyLargeChildren,
            direction: .vertical
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    // MARK: Children with Different Sizes

    private let childrenWithDifferentSizes: [ServerDrivenComponent] = (0..<20).map { i in
        return ListViewTests.createText(
            "< \(i) ---\(i % 3 == 0 ? "/↩\n↩\n /" : "")--- \(i) >",
            position: Double(i) / 19
        )
    }

    func testDirectionHorizontalWithChildrenWithDifferentSizes() {
        let component = ListView(
            children: childrenWithDifferentSizes,
            direction: .horizontal
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }

    func testDirectionVerticalWithChildrenWithDifferentSizes() {
        let component = ListView(
            children: childrenWithDifferentSizes,
            direction: .vertical
        )

        let view = makeListUiView(component)

        assertSnapshotImage(view, size: imageSize)
    }
    
    func test_whenDecodingJson_thenItShouldReturnAListView() throws {
        let component: ListView = try componentFromJsonFile(fileName: "listViewComponent")
        assertSnapshot(matching: component, as: .dump)
    }

    // MARK: - Helper

    private func makeListUiView(_ listComponent: ListView) -> UIView {
        return listComponent.toView(
            context: BeagleContextDummy(),
            dependencies: BeagleDependencies()
        )
    }

    private static func createText(_ string: String, position: Double) -> Text {
        let text = Int(round(position * 255))
        let textColor = "#\(String(repeating: String(format: "%02X", text), count: 3))"
        let background = 255 - text
        let backgroundColor = "#\(String(repeating: String(format: "%02X", background), count: 3))"
        return Text(
            string,
            textColor: textColor,
            widgetProperties: .init( appearance: Appearance(backgroundColor: backgroundColor))
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
