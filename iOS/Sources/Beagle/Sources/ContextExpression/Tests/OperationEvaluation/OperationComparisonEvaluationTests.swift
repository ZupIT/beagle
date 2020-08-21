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

final class OperationComparisonEvaluationTests: OperationEvaluationTests {
    
    func testEvaluateGt() {
        // Given
        let results: [DynamicObject] =
        [
            .bool(false),
            .bool(false),
            .bool(true),
            .bool(true),
            .bool(false),
            .bool(false),
            .bool(false),
            .empty,
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.gt, comparableResults: results)
    }
    
    func testEvaluateGte() {
        // Given
        let results: [DynamicObject] =
        [
            .bool(true),
            .bool(false),
            .bool(true),
            .bool(true),
            .bool(false),
            .bool(false),
            .bool(true),
            .empty,
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.gte, comparableResults: results)
    }
    
    func testEvaluateLt() {
        // Given
        let results: [DynamicObject] =
        [
            .bool(false),
            .bool(true),
            .bool(false),
            .bool(false),
            .bool(true),
            .bool(true),
            .bool(false),
            .empty,
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.lt, comparableResults: results)
    }
    
    func testEvaluateLte() {
        // Given
        let results: [DynamicObject] =
        [
            .bool(true),
            .bool(true),
            .bool(false),
            .bool(false),
            .bool(true),
            .bool(true),
            .bool(true),
            .empty,
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.lte, comparableResults: results)
    }
    
    func testEvaluateEq() {
        // Given
        let name = BeagleSchema.Operation.Name.eq
        let contexts = [Context(id: "context", value: .bool(true))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        
        let simpleOperations =
        [
            [.value(.literal(.bool(true))), .value(.literal(.bool(true)))],
            [.value(.literal(.string("no"))), .value(.literal(.string("no")))],
            [.value(.literal(.int(1))), .value(.literal(.int(1)))],
            [.value(.literal(.double(2.2))), .value(.literal(.double(2.2)))],
            [.value(.binding(bindings[0])), .value(.binding(bindings[0]))]
        ].map { Operation(name: name, parameters: $0) }
        
        let complexOperations =
        [
            [.operation(simpleOperations[2]), .operation(simpleOperations[2])]
        ].map { Operation(name: name, parameters: $0) }
        
        let failingOperations =
        [
            [.value(.literal(.int(1))), .value(.literal(.int(0)))],
            [.value(.literal(.double(2.2))), .value(.literal(.double(2.5)))],
            [.value(.literal(.bool(true))), .value(.literal(.int(1)))],
            [.value(.literal(.int(0))), .value(.literal(.int(0))), .value(.literal(.int(0)))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        let comparableResults: [DynamicObject] =
        [
            .bool(true),
            .bool(true),
            .bool(true),
            .bool(true),
            .bool(true),
            .bool(true),
            .bool(false),
            .bool(false),
            .bool(false),
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperations(simpleOperations + complexOperations + failingOperations,
                           contexts: contexts,
                           comparableResults: comparableResults)
    }
    
    private func evaluateOperation(_ name: BeagleSchema.Operation.Name, comparableResults: [DynamicObject]) {
        // Given
        let contexts = [Context(id: "context1", value: .int(2)),
                        Context(id: "context2", value: .double(2.5))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        
        let sums = [Operation(name: .sum, parameters: [.value(.literal(.int(10))), .value(.literal(.int(4)))]),
                    Operation(name: .sum, parameters: [.value(.literal(.double(12.5))), .value(.literal(.double(5.5)))])]
        let subtract = Operation(name: .subtract, parameters: [.value(.literal(.int(28))), .operation(sums[0])])
        
        let successfulOperations =
        [
            [.value(.literal(.int(6))), .value(.literal(.int(6)))],
            [.value(.literal(.double(4.5))), .value(.literal(.double(6.0)))],
            [.value(.literal(.int(4))), .value(.binding(bindings[0]))],
            [.value(.literal(.double(4.0))), .value(.binding(bindings[1]))],
            [.value(.literal(.int(4))), .operation(sums[0])],
            [.value(.literal(.double(2.8))), .operation(sums[1])],
            [.operation(sums[0]), .operation(subtract)]
        ].map { Operation(name: name, parameters: $0) }
        
        let failingOperations =
        [
            [.value(.literal(.int(6))), .value(.literal(.int(4))), .value(.literal(.int(4)))],
            [.value(.literal(.int(1))), .value(.literal(.double(1.5)))],
            [.value(.literal(.int(1))), .value(.literal(.string("1")))],
            [.value(.literal(.int(1))), .value(.literal(.string("true")))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        // When/Then
        evaluateOperations(successfulOperations + failingOperations,
                           contexts: contexts,
                           comparableResults: comparableResults)
    }
}
