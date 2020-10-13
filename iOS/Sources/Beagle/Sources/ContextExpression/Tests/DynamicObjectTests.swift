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

import BeagleSchema
@testable import Beagle
import XCTest
import SnapshotTesting

final class DynamicObjectTests: XCTestCase {

    func testSetObjectWithVariousPaths() {
        // Given
        let object: DynamicObject = [
            "a": "default",
            "c": [1, 2],
            "d": [["name": "John", "code": "123"], ["name": "Doe", "code": "321"]]
        ]

        let result: [Result] = [
            "a",
            "b",
            "c[0]",
            "c[4]",
            "",
            "[4]",
            "d[0].name",
            "d[1].code",
            "d[2].name"
        ]
        .compactMap { Path(rawValue: $0) }

        // When
        .map {
            Result(input: $0.rawValue, output: object.set("UPDATED", with: $0))
        }

        // Then
        assertSnapshot(matching: result, as: .json)
    }

    fileprivate struct Result: Encodable {
        let input: String
        let output: DynamicObject
    }
}
