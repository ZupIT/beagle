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

class PageViewTests: XCTestCase {

    func test_whenDecodingInvalidJson() throws {
        XCTAssertThrowsError(
            try componentFromJsonFile(componentType: PageView.self, fileName: "PageViewInvalid")
        )
    }

    private let page = Container(children: [
        Text("First text"),
        Button(text: "Button"),
        Text("Second text")
    ]).applyFlex(Flex(flexDirection: .column, justifyContent: .center))

    func test_viewWithPages() {
        let pageView = PageView(
            pages: Array(repeating: page, count: 5),
            pageIndicator: nil
        )

        let screen = Beagle.screen(.declarative(pageView.toScreen()))
        assertSnapshotImage(screen)
    }

    func test_viewWithPagesAndIndicator() {
        let pageView = PageView(
            pages: Array(repeating: page, count: 5),
            pageIndicator: PageIndicator(selectedColor: "#d1cebd", unselectedColor: "#f6eedf")
        )

        let screen = Beagle.screen(.declarative(pageView.toScreen()))
        assertSnapshotImage(screen)
    }

}
