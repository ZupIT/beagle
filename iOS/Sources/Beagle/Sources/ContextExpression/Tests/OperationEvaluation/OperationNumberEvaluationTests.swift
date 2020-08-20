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

// swiftlint:disable multiline_literal_brackets
final class OperationNumberEvaluationTests: OperationEvaluationTests {

    func testEvaluateSum() {
        // Given
        let results: [DynamicObject] =
        [
            .int(10),
            .double(10.5),
            .int(6),
            .double(6.5),
            .int(14),
            .double(13.3),
            .double(27.5),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.sum, comparableResults: results)
    }
    
    func testEvaluateSubtract() {
        // Given
        let results: [DynamicObject] =
        [
            .int(2),
            .double(-1.5),
            .int(2),
            .double(1.5),
            .int(2),
            .double(4.3),
            .double(-1.5),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.subtract, comparableResults: results)
    }
    
    func testEvaluateMultiply() {
        // Given
        let results: [DynamicObject] =
        [
            .int(24),
            .double(27.0),
            .int(8),
            .double(10.0),
            .int(96),
            .double(75.6),
            .double(7290.0),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.multiply, comparableResults: results)
    }
    
    func testEvaluateDivide() {
        // Given
        let results: [DynamicObject] =
        [
            .int(1),
            .double(0.75),
            .int(2),
            .double(1.6),
            .int(4),
            .double(3.733333333333333),
            .double(0.625),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.divide, comparableResults: results)
    }
    
    private func evaluateOperation(_ name: BeagleSchema.Operation.Name, comparableResults: [DynamicObject]) {
        // Given
        let contexts = [Context(id: "context1", value: .int(2)), Context(id: "context2", value: .double(2.5))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        
        let simpleOperations =
        [
            [.value(.literal(.int(6))), .value(.literal(.int(4)))],
            [.value(.literal(.double(4.5))), .value(.literal(.double(6.0)))],
            [.value(.literal(.int(4))), .value(.binding(bindings[0]))],
            [.value(.literal(.double(4.0))), .value(.binding(bindings[1]))]
        ].map { Operation(name: name, parameters: $0) }
        
        let complexOperations =
        [
            [.value(.literal(.int(4))), .operation(simpleOperations[0])],
            [.value(.literal(.double(2.8))), .operation(simpleOperations[1])],
            [.operation(simpleOperations[1]), .operation(simpleOperations[3]), .operation(simpleOperations[1])]
        ].map { Operation(name: name, parameters: $0) }
        
        let failingOperations =
        [
            [.value(.literal(.int(1))), .value(.literal(.double(1.5)))],
            [.value(.literal(.int(1))), .value(.literal(.string("1")))],
            [.value(.literal(.int(1))), .value(.literal(.string("true")))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        // When/Then
        evaluateOperations((simpleOperations + complexOperations + failingOperations),
                           contexts: contexts,
                           comparableResults: comparableResults)
        
    }
}
