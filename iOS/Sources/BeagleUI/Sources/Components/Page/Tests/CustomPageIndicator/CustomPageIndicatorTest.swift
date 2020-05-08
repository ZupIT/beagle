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

import Foundation
@testable import BeagleUI
import XCTest
import SnapshotTesting

class CustomPageIndicatorTest: XCTestCase {

    private static let typeName = "CustomPageIndicator"
    
    override func setUp() {
        super.setUp()
        Beagle.dependencies = BeagleDependencies()
        Beagle.dependencies.decoder.register(
            CustomPageIndicator.self,
            for: CustomPageIndicatorTest.typeName
        )
    }
    
    override func tearDown() {
        Beagle.dependencies = BeagleDependencies()
        super.tearDown()
    }

    private lazy var decoder: ComponentDecoding = {
        Beagle.dependencies.decoder
    }()

    let indicator = CustomPageIndicator(
        selectedColor: "selectedColor",
        defaultColor: "defaultColor"
    )

    private lazy var dependencies = BeagleScreenDependencies()

    func test_indicator_decoder() throws {
        let component: CustomPageIndicator = try componentFromJsonFile(
            fileName: CustomPageIndicatorTest.typeName,
            decoder: decoder
        )
        assertSnapshot(matching: component, as: .dump)
    }

    func test_indicator_render() {
        let view = indicator.toView(context: BeagleContextDummy(), dependencies: dependencies)
        assertSnapshotImage(view, size: .init(width: 200, height: 30))
    }

    func test_pageViewWithCustomIndicator_decoder() throws {
        let component: PageView = try componentFromJsonFile(
            fileName: "PageViewWithCustomIndicator",
            decoder: decoder
        )
        assertSnapshot(matching: component.pageIndicator, as: .dump)
    }

    func test_pageViewWithCustomIndicator_render() {
        let page = Text("Page")

        let component = PageView(
            children: Array(repeating: page, count: 3),
            pageIndicator: indicator
        )

        let screen = BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(component.toScreen()),
            dependencies: dependencies
        ))

        assertSnapshotImage(screen)
    }
}
