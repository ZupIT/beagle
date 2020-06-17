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
import BeagleSchema

final class ListViewInitTests: XCTestCase {

    private let listWithOneRow = ListView(rows: [
        Text("text")
    ])

    func test_initWithRowBuilder_shouldReturnExpectedInstance() {
        // Given / When
        let component = listWithOneRow
        // Then
        XCTAssert(component.rows.count == 1)
        XCTAssert(component.rows[safe: 0] is Text)
    }
    
    func test_initWithRowsBuilder_shouldReturnExpectedInstance() {
        // Given / When
        let component = ListView(rows: [
            Text("text"),
            Button(text: "text")
        ])

        // Then
        XCTAssert(component.rows.count == 2)
        XCTAssert(component.rows[safe: 0] is Text)
        XCTAssert(component.rows[safe: 1] is Button)
    }
    
    func test_toUIKit_shouldConvertDirectionProperly() {
        // Given
        let expectedConversions: [UICollectionView.ScrollDirection] = [.horizontal, .vertical]
        let directionsToConvert: [ListView.Direction] = [.horizontal, .vertical]
        
        // When
        let converted = directionsToConvert.map { $0.toUIKit() }
        
        // Then
        XCTAssert(expectedConversions == converted)
    }

}
