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
import BeagleSchema
@testable import Beagle

final class AddChildrenTests: XCTestCase {

    func testModeAppend() {
        runTest(mode: .append)
    }
    
    func testModePrepend() {
        runTest(mode: .prepend)
    }
    
    func testModeReplace() {
        runTest(mode: .replace)
    }

    func testModeAppendWithContext() {
        runTest(mode: .append, text: Text("@{contextId}"))
    }
    
    func testModeReplaceWithContext() {
        runTest(mode: .replace, text: Text("@{contextId}"))
    }

    func testIfDefaultIsAppend() {
        let sut = AddChildren(componentId: "id", value: [])
        XCTAssertEqual(sut.mode, .append)
    }

    private func runTest(
        mode: AddChildren.Mode,
        text: Text = Text("NEW"),
        testName: String = #function,
        line: UInt = #line
    ) {
        // Given
        let sut = AddChildren(componentId: "componentId", value: [text], mode: mode)

        let controller = BeagleScreenViewController(Container(
            context: Context(id: "contextId", value: "CONTEXT"),
            widgetProperties: WidgetProperties(id: "componentId")
        ) {
            Text("initial")
        })

        assertSnapshotImage(controller, size: imageSize, testName: testName, line: line)

        // When
        sut.execute(controller: controller, origin: UIView())

        // Then
        assertSnapshotImage(controller, size: imageSize, testName: testName, line: line)
    }

    private let imageSize = ImageSize.custom(CGSize(width: 80, height: 60))
}
