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
import SnapshotTesting
import BeagleSchema

final class WidgetTests: XCTestCase {

    func testParsingOfWidgetWithAllAttributes() throws {
        let component: Text = try componentFromJsonFile(fileName: "widgetWithAllAttributes")
        assertSnapshot(matching: component, as: .dump)
    }
    
    func testSetOfTextAttributes() throws {
        // given
        var text: Text = try componentFromJsonFile(fileName: "widgetWithAllAttributes")
        let newStyle = Style(backgroundColor: nil, cornerRadius: .init(radius: -9999.0))
        let newId = "newID"
        let newFlex = Flex()
        let newAccessiblity = Accessibility(accessibilityLabel: "new label", accessible: false)
        
        // when
        text.style = newStyle
        text.id = newId
        text.flex = newFlex
        text.accessibility = newAccessiblity
        
        // then
        XCTAssert(
            text.style == newStyle &&
            text.id == newId &&
            text.flex == newFlex &&
            text.accessibility == newAccessiblity
        )
    }
}
