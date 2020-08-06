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
@testable import Beagle
import BeagleSchema

class TextTests: XCTestCase {

    private lazy var theme = AppTheme(styles: [
        "test.text.style": textStyle
    ])
    
    private func textStyle() -> (UITextView?) -> Void {
        return BeagleStyle.text(font: .boldSystemFont(ofSize: 20), color: .blue)
            <> BeagleStyle.backgroundColor(withColor: .black)
    }

    private lazy var dependencies = BeagleScreenDependencies(theme: theme)

    private lazy var controller = BeagleControllerStub(dependencies: dependencies)
    private lazy var renderer = BeagleRenderer(controller: controller)
    
    func testTextContent() {
        // Given
        let component = Text("Test")
        
        // When
        let label = renderer.render(component) as? UITextView
        
        // Then
        guard case let .value(text) = component.text else {
            XCTFail("Expected a `.value` property, but got \(String(describing: component.text)).")
            return
        }
        XCTAssertEqual(text, label?.text)
    }
    
    func testTextDefaultAlignment() {
        // Given
        let component = Text("Test")
        
        // When
        let label = renderer.render(component) as? UITextView
        
        // Then
        XCTAssertEqual(label?.textAlignment, NSTextAlignment.natural)
    }
    
    func testTextAlignment() {
        // Given
        let component = Text("Test", alignment: Expression.value(.left))
        
        // When
       let label = renderer.render(component) as? UITextView
        
        // Then
        XCTAssertEqual(label?.textAlignment, NSTextAlignment.left)
    }

    func test_renderTextComponent() {
        let text = Text(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            styleId: "test.text.style",
            alignment: Expression.value(.right),
            textColor: "#579F2B",
            widgetProperties: .init(style: Style(
                backgroundColor: "#FFFF00",
                cornerRadius: .init(radius: 30.0)
            ))
        )

        let view = renderer.render(text)
        assertSnapshotImage(view, size: .custom(CGSize(width: 300, height: 150)))
    }
}
