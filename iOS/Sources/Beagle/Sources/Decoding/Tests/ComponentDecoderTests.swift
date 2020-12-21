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
    
    func testRegisterAndDecodeCustomComponent() throws {
        // Given
        let expectedText = "something"
        let jsonData = """
        {
            "_beagleComponent_": "custom:newcomponent",
            "text": "\(expectedText)"
        }
        """.data(using: .utf8)!

        // When
        sut.register(component: NewComponent.self)
        let component = try sut.decodeComponent(from: jsonData) as? NewComponent
        
        // Then
        XCTAssertEqual(component?.text, expectedText)
    }
    
    func testRegisterComponentWithCustomTypeName() throws {
        // Given
        let sut = ComponentDecoder()

        // When
        sut.register(component: NewComponent.self, named: "NewCustomComponent")
        let componentDecoder = sut.componentDecoders["custom:newcustomcomponent"]
        
        // Then
        XCTAssertNotNil(componentDecoder)
        XCTAssert(componentDecoder is NewComponent.Type)
    }
    
    func testRegisterActionWithCustomTypeName() throws {
        // Given
        let sut = ComponentDecoder()

        // When
        sut.register(action: TestAction.self, named: "NewCustomAction")
        let actionDecoder = sut.actionDecoders["custom:newcustomaction"]
        
        // Then
        XCTAssertNotNil(actionDecoder)
        XCTAssert(actionDecoder is TestAction.Type)
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

    func testUnknownTypeIsDecodeShouldReturnNil() throws {
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
        XCTAssertEqual(unknown?.type, "beagle:unknown")
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
            "_beagleAction_":"custom:testaction",
            "value": 42
        }
        """.data(using: .utf8)!

        sut.register(action: TestAction.self)
        let action = try sut.decodeAction(from: data)
        let testAction = action as? TestAction

        XCTAssertNotNil(testAction)
        XCTAssertEqual(testAction?.value, 42)
    }

    private class TestAction: Action {
        var analytics: ActionAnalyticsConfig? { return nil }
        let value: Int
        
        func execute(controller: BeagleController, origin: UIView) {
            // Intentionally unimplemented...
        }
    }
}

// MARK: - Testing Helpers
struct NewComponent: ServerDrivenComponent {
    var text: String
    
    func toView(renderer: BeagleRenderer) -> UIView {
        return UIView()
    }
}

struct Unknown: ServerDrivenComponent, Equatable {
    func toView(renderer: BeagleRenderer) -> UIView {
        return UIView()
    }
}
