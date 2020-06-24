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

final class ExpressionTests: XCTestCase {
    
    let model: DynamicObject = [
        "client": [
            "name": [
                "first": "John",
                "last": "Doe"
            ],
            "phones": [
                "111-1111-1111",
                "222-2222-2222"
            ],
            "matrix": [
                [1, 2, 3],
                [4, 5.5, 6],
                [7, 8, true]
            ]
        ]
    ]

    func test_singleExpressionEvaluation() {
        // Given
        let sut: [SingleExpression] = [
            SingleExpression(context: "context", path: Path(nodes: [.key("client"), .key("name")])),
            SingleExpression(context: "context", path: Path(nodes: [.key("client"), .key("name"), .key("first")])),
            SingleExpression(context: "context", path: Path(nodes: [.key("client"), .key("unknown")])),
            SingleExpression(context: "context", path: Path(nodes: [.key("client"), .key("phones"), .index(0)])),
            SingleExpression(context: "context", path: Path(nodes: [.key("client"), .key("matrix"), .index(0), .index(0)])),
            SingleExpression(context: "context", path: Path(nodes: [.key("client"), .key("matrix"), .index(1), .index(1)])),
            SingleExpression(context: "context", path: Path(nodes: [.key("client"), .key("matrix"), .index(2), .index(2)])),
            SingleExpression(context: "context", path: Path(nodes: [.key("client"), .key("matrix"), .index(3), .index(3)]))
        ]
        let context = model

        // When
        let result = sut.map { $0.evaluate(model: context) }
        // Then
        assertSnapshot(matching: result, as: .dump)
    }
    
}
