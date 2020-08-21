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

final class OperationOtherEvaluationTests: OperationEvaluationTests {

    func testEvaluateIsNull() {
        // Given
        let name = BeagleSchema.Operation.Name.isNull
        let contexts = [Context(id: "context", value: .array([.int(1), .int(2), .int(3)]))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        let insert = Operation(name: .insert, parameters: [])
        
        let simpleOperations =
        [
            [.value(.literal(.null))],
            [.value(.literal(.int(1)))],
            [.value(.binding(bindings[0]))]
        ].map { Operation(name: name, parameters: $0) }
         
        let complexOperations =
        [
            [.operation(simpleOperations[0])],
            [.operation(insert)]
        ].map { Operation(name: name, parameters: $0) }
         
        let failingOperations =
        [
            [.value(.literal(.null)), .value(.literal(.null))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        let comparableResults: [DynamicObject] =
        [
            .bool(true),
            .bool(false),
            .bool(false),
            .bool(false),
            .bool(true),
            .empty,
            .empty
        ]
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        // When/Then
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            for (evaluated, comparable) in zip(evaluatedResults, comparableResults) {
                XCTAssertEqual(evaluated, comparable)
            }
        }
    }
    
    func testEvaluateIsEmpty() {
        // Given
        let comparableResults: [DynamicObject] =
        [
            .bool(true),
            .bool(false),
            .bool(false),
            .bool(true),
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.isEmpty, comparableResults: comparableResults)
    }
    
    func testEvaluateLength() {
        // Given
        let comparableResults: [DynamicObject] =
        [
            .int(0),
            .int(1),
            .int(3),
            .int(0),
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.length, comparableResults: comparableResults)
    }
    
    private func evaluateOperation(_ name: BeagleSchema.Operation.Name, comparableResults: [DynamicObject]) {
        // Given
        // swiftlint:disable multiline_literal_brackets
        let contexts = [Context(id: "context1", value: .array([.int(1)])),
                        Context(id: "context2", value: .dictionary(["one": .int(1), "two": .int(2), "three": .int(3)]))]
        // swiftlint:enable multiline_literal_brackets
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        let removeIndex = Operation(name: .removeIndex, parameters: [.value(.binding(bindings[0]))])
        
        let successfulOperations =
        [
            [.value(.literal(.string("")))],
            [.value(.binding(bindings[0]))],
            [.value(.binding(bindings[1]))],
            [.operation(removeIndex)]
        ].map { Operation(name: name, parameters: $0) }
         
        let failingOperations =
        [
            [.value(.literal(.int(0)))],
            [.value(.literal(.string("string"))), .value(.literal(.string("string")))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        let operations = successfulOperations + failingOperations
        
        // When/Then
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            for (evaluated, comparable) in zip(evaluatedResults, comparableResults) {
                XCTAssertEqual(evaluated, comparable)
            }
        }
    }

}
