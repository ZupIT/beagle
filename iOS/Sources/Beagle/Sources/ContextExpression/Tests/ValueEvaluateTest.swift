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

final class ValueEvaluateTest: XCTestCase {
    
    let bindingModel: DynamicObject = [
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

    func testBindingEvaluation() {
        // Given
        let sut: [Binding] = [
            Binding(context: "context", path: Path(nodes: [.key("client"), .key("name")])),
            Binding(context: "context", path: Path(nodes: [.key("client"), .key("name"), .key("first")])),
            Binding(context: "context", path: Path(nodes: [.key("client"), .key("unknown")])),
            Binding(context: "context", path: Path(nodes: [.key("client"), .key("phones"), .index(0)])),
            Binding(context: "context", path: Path(nodes: [.key("client"), .key("matrix"), .index(0), .index(0)])),
            Binding(context: "context", path: Path(nodes: [.key("client"), .key("matrix"), .index(1), .index(1)])),
            Binding(context: "context", path: Path(nodes: [.key("client"), .key("matrix"), .index(2), .index(2)])),
            Binding(context: "context", path: Path(nodes: [.key("client"), .key("matrix"), .index(3), .index(3)]))
        ]
        let context = bindingModel

        // When
        let result = sut.map { $0.evaluate(model: context) }
        // Then
        assertSnapshot(matching: result, as: .dump)
    }
    
    func testLiteralEvaluation() {
        // Given
        let sut: [Literal] = [
            Literal.int(3),
            Literal.double(5.0),
            Literal.bool(true),
            Literal.string("name"),
            Literal.null
        ]
        
        // When
        let result = sut.map { $0.evaluate() }
        // Then
        assertSnapshot(matching: result, as: .dump)
    }
    
}
