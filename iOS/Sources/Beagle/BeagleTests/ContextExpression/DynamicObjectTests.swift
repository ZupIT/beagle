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

final class DynamicObjectTests: XCTestCase {

    func testSetObjectWithVariousPaths() {
        // Given
        let object: DynamicObject = [
            "a": "default",
            "c": [1, 2],
            "d": [
                ["name": "John", "code": "123"],
                ["name": "Doe", "code": "321"]
            ]
        ]

        let paths: [Path] = [
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
        let updates = paths.map { path in
            object.set("UPDATED", with: path)
        }

        // Then
        zip(paths, updates).forEach {
            let result = Result(path: $0.rawValue, result: $1)
            assertSnapshot(matching: result, as: .json, named: $0.rawValue, testName: "path")
        }
    }
    
    func testInitFromEncodable() throws {
        struct User: Encodable {
            let id: UUID
            let name: String
        }
        
        // Given
        let user = try User(id: XCTUnwrap(UUID(uuidString: "ECCF622D-2F31-4C90-9150-0A01EFDCB4A0")), name: "John")

        // When
        let object = DynamicObject(user)

        // Then
        _assertInlineSnapshot(matching: object, as: .dump, with: """
        - [id: ECCF622D-2F31-4C90-9150-0A01EFDCB4A0, name: John]
        """)
    }

    fileprivate struct Result: Encodable {
        let path: String
        let result: DynamicObject
    }
}
