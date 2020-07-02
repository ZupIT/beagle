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
@testable import BeagleSchema
import SnapshotTesting

public final class ComponentDecoderTests: XCTestCase {
    // swiftlint:disable force_unwrapping
    
    private lazy var sut = dependencies.decoder

    func testIfAllComponentsAreBeingRegistered() {
        let sut = ComponentDecoder()
        assertSnapshot(matching: sut.componentDecoders, as: .dump)
    }
    
    func testIfAllActionsAreBeingRegistered() {
        let sut = ComponentDecoder()
        assertSnapshot(matching: sut.actionDecoders, as: .dump)
    }
    
    func test_whenANewTypeIsRegistered_thenItShouldBeAbleToDecodeIt() throws {
        // Given
        let expectedText = "something"
        let jsonData = """
        {
            "_beagleComponent_": "custom:newcomponent",
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
            "_beagleComponent_": "beagle:text",
            "text": "\(expectedText)"
        }
        """.data(using: .utf8)!

        // When
        let text = try sut.decodeComponent(from: jsonData) as? Text

        // Then
        guard case let .value(string) = text?.text else {
            XCTFail("Expected a `.value` property, but got \(String(describing: text?.text)).")
            return
        }
        XCTAssertEqual(string, expectedText)
    }

    func test_whenAnUnknownTypeIsDecoded_thenItShouldReturnNil() throws {
        // Given
        let jsonData = """
        {
            "_beagleComponent_": "beagle:unknown",
            "text": "some text"
        }
        """.data(using: .utf8)!

        // When
        let unknown = try sut.decodeComponent(from: jsonData) as? UnknownComponent

        // Then
        XCTAssert(unknown?.type == "beagle:unknown")
    }

    func testDecodeAction() throws {
        let jsonData = """
        {
            "_beagleAction_": "beagle:popStack"
        }
        """.data(using: .utf8)!

        let action = try sut.decodeAction(from: jsonData)

        guard case Navigate.popStack = action else {
            XCTFail("decoding failed"); return
        }
    }
    
    func testRegisterAndDecodeCustomAction() throws {
        let data = """
        {
            "_beagleAction_":"custom:testcustomaction",
            "value": 42
        }
        """.data(using: .utf8)!

        sut.register(TestAction.self, for: "TestCustomAction")
        let action = try sut.decodeAction(from: data)
        let testAction = action as? TestAction

        XCTAssertNotNil(testAction)
        XCTAssertEqual(testAction?.value, 42)
    }

    private class TestAction: RawAction {
        let value: Int
    }
}

// MARK: - Testing Helpers
struct NewComponent: RawComponent {
    var text: String
    
}

struct Unknown: RawComponent, Equatable {
    
}
