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
@testable import BeagleUI

final class TextTests: XCTestCase {
    
    private lazy var theme = AppTheme(styles: [
        "test.text.style": textStyle
    ])
    
    private func textStyle() -> (UITextView?) -> Void {
        return BeagleStyle.text(font: .boldSystemFont(ofSize: 20), color: .blue)
            <> BeagleStyle.backgroundColor(withColor: .black)
    }

    private lazy var dependencies = BeagleScreenDependencies(
        theme: theme
    )
    
    func testEqualTextContent() throws {
        // Given
        let component = Text("Test")
        let context = BeagleContextDummy()
        
        // When
        guard let label = component.toView(context: context, dependencies: dependencies) as? UITextView else {
            XCTFail("Unable to type cast to UITextView.")
            return
        }
        
        // Then
        XCTAssertEqual(component.text, label.text)
    }
    
    func testTextWithRightAlignment() throws {
        // Given
        let component = Text("Test")
        let context = BeagleContextDummy()
        
        // When
        guard let label = component.toView(context: context, dependencies: dependencies) as? UITextView else {
            XCTFail("Unable to type cast to UITextView.")
            return
        }
        
        // Then
        XCTAssertEqual(label.textAlignment, NSTextAlignment.natural)
    }
    
    func testTextWithLeftAlignment() throws {
        // Given
        let component = Text("Test", alignment: .left)
        let context = BeagleContextDummy()
        
        // When
        guard let label = component.toView(context: context, dependencies: dependencies) as? UITextView else {
            XCTFail("Unable to type cast to UITextView.")
            return
        }
        
        // Then
        XCTAssertEqual(label.textAlignment, NSTextAlignment.left)
    }
    
    func test_whenDecodingJson_shouldReturnAText() throws {
        let component: Text = try componentFromJsonFile(fileName: "TextComponent")
        assertSnapshot(matching: component, as: .dump)
    }
    
    func test_renderTextComponent() throws {
        let component: Text = try componentFromJsonFile(fileName: "TextComponent")
        let view = component.toView(context: BeagleContextDummy(), dependencies: dependencies)
        assertSnapshotImage(view, size: CGSize(width: 300, height: 150))
    }

}
