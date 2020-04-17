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

final class ComponentDecoderTests: XCTestCase {
    // swiftlint:disable force_unwrapping
    
    private lazy var sut = Beagle.dependencies.decoder

    // TODO: remove this test when using newer versions of SnapshotTesting,
    // because this behaviour will be already tested on BeagleSetupTests.
    func testIfAllDecodersAreBeingRegistered() {
        let sut = ComponentDecoder()
        assertSnapshot(matching: sut.decoders, as: .dump)
    }
    
    func test_whenANewTypeIsRegistered_thenItShouldBeAbleToDecodeIt() throws {
        // Given
        let expectedText = "something"
        let jsonData = """
        {
            "_beagleType_": "custom:component:newcomponent",
            "text": "\(expectedText)"
        }
        """.data(using: .utf8)!

        // When
        sut.register(NewComponent.self, for: "NewComponent")
        let component = try sut.decodeComponent(from: jsonData) as? NewComponent
        
        // Then
        XCTAssertEqual(component?.text, expectedText)
    }

    func testDecodeDefaultType() throws {
        // Given
        let expectedText = "some text"
        let jsonData = """
        {
            "_beagleType_": "beagle:component:text",
            "text": "\(expectedText)"
        }
        """.data(using: .utf8)!

        // When
        let text = try sut.decodeComponent(from: jsonData) as? Text

        // Then
        XCTAssertEqual(text?.text, expectedText)
    }

    func test_whenAnUnknwonTypeIsDecoded_thenItShouldReturnNil() throws {
        // Given
        let jsonData = """
        {
            "_beagleType_": "beagle:component:unknown",
            "text": "some text"
        }
        """.data(using: .utf8)!

        // When
        let unknown = try sut.decodeComponent(from: jsonData) as? UnknownComponent

        // Then
        XCTAssert(unknown?.type == "beagle:component:unknown")
    }

    func testDecodeAction() throws {
        let jsonData = """
        {
            "_beagleType_": "beagle:action:navigate",
            "type": "FINISH_VIEW"
        }
        """.data(using: .utf8)!

        let action = try sut.decodeAction(from: jsonData)

        guard case Navigate.finishView = action else {
            XCTFail("decoding failed"); return
        }
    }
}

// MARK: - Testing Helpers
struct NewComponent: ServerDrivenComponent {
    var text: String
    
    func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        return UIView()
    }
}

struct Unknown: ServerDrivenComponent {
    func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        return UIView()
    }
}
