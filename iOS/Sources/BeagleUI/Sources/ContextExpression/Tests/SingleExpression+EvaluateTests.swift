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
@testable import BeagleUI
import XCTest
import SnapshotTesting

final class SingleExpressionTests: XCTestCase {
    
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

    func test_evaluation() {
        // Given
        let sut = [
            SingleExpression(nodes: [.property("client"), .property("name")]),
            SingleExpression(nodes: [.property("client"), .property("name"), .property("first")]),
            SingleExpression(nodes: [.property("client"), .property("unknown")]),
            SingleExpression(nodes: [.property("client"), .property("phones"), .arrayItem(0)]),
            SingleExpression(nodes: [.property("client"), .property("matrix"), .arrayItem(0), .arrayItem(0)]),
            SingleExpression(nodes: [.property("client"), .property("matrix"), .arrayItem(1), .arrayItem(1)]),
            SingleExpression(nodes: [.property("client"), .property("matrix"), .arrayItem(2), .arrayItem(2)]),
            SingleExpression(nodes: [.property("client"), .property("matrix"), .arrayItem(3), .arrayItem(3)])
        ]
        let context = model

        // When
        let result = sut.map { $0.evaluate(model: context) }
        // Then
        assertSnapshot(matching: result, as: .dump)
    }
    
}
