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

final class OperationLogicEvaluationTests: OperationEvaluationTests {
    
    func testEvaluateCondition() {
        // Given
        let name = BeagleSchema.Operation.Name.condition
        let contexts = [Context(id: "context", value: .bool(true))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        
        let simpleOperations =
        [
            [.value(.literal(.bool(true))), .value(.literal(.int(1))), .value(.literal(.int(0)))],
            [.value(.literal(.bool(false))), .value(.literal(.string("yes"))), .value(.literal(.string("no")))],
            [.value(.binding(bindings[0])), .value(.literal(.bool(true))), .value(.literal(.bool(false)))]
        ].map { Operation(name: name, parameters: $0) }
        
        let complexOperations =
        [
            [.operation(simpleOperations[2]), .value(.literal(.double(1.1))), .value(.literal(.double(0.0)))]
        ].map { Operation(name: name, parameters: $0) }
        
        let failingOperations =
        [
            [.value(.literal(.int(1))), .value(.literal(.int(0)))],
            [.value(.literal(.int(1))), .value(.literal(.int(1))), .value(.literal(.int(0)))],
            [.value(.literal(.bool(true))), .value(.literal(.int(1))), .value(.literal(.double(0.0)))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        let comparableResults: [DynamicObject] =
        [
            .int(1),
            .string("no"),
            .bool(true),
            .double(1.1),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperations(simpleOperations + complexOperations + failingOperations,
                           contexts: contexts,
                           comparableResults: comparableResults)
    }
    
    func testEvaluateNot() {
        // Given
        let name = BeagleSchema.Operation.Name.not
        let contexts = [Context(id: "context", value: .bool(true))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        
        let simpleOperations =
        [
            [.value(.literal(.bool(true)))],
            [.value(.literal(.bool(false)))],
            [.value(.binding(bindings[0]))]
        ].map { Operation(name: name, parameters: $0) }
        
        let complexOperations =
        [
            [.operation(simpleOperations[2])]
        ].map { Operation(name: name, parameters: $0) }
        
        let failingOperations =
        [
            [.value(.literal(.int(1)))],
            [.value(.literal(.int(1))), .value(.literal(.int(1)))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        let comparableResults: [DynamicObject] =
        [
            .bool(false),
            .bool(true),
            .bool(false),
            .bool(true),
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperations(simpleOperations + complexOperations + failingOperations,
                           contexts: contexts,
                           comparableResults: comparableResults)
    }
    
    func testEvaluateAnd() {
        // Given
        let results: [DynamicObject] =
        [
            .bool(true),
            .bool(false),
            .bool(false),
            .bool(true),
            .bool(false),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.and, comparableResults: results)
    }
    
    func testEvaluateOr() {
        // Given
        let results: [DynamicObject] =
        [
            .bool(true),
            .bool(false),
            .bool(true),
            .bool(true),
            .bool(true),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.or, comparableResults: results)
    }
    
    private func evaluateOperation(_ name: BeagleSchema.Operation.Name, comparableResults: [DynamicObject]) {
        // Given
        let contexts = [Context(id: "context1", value: .bool(false))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        
        let simpleOperations =
        [
            [.value(.literal(.bool(true))), .value(.literal(.bool(true)))],
            [.value(.literal(.bool(false))), .value(.literal(.bool(false)))],
            [.value(.literal(.bool(true))), .value(.binding(bindings[0]))]
        ].map { Operation(name: name, parameters: $0) }
        
        let complexOperations =
        [
            [.value(.literal(.bool(true))), .operation(simpleOperations[0])],
            [.operation(simpleOperations[0]), .operation(simpleOperations[1]), .operation(simpleOperations[2])]
        ].map { Operation(name: name, parameters: $0) }
        
        let failingOperations =
        [
            [.value(.literal(.int(1))), .value(.literal(.double(1.5)))],
            [.value(.literal(.int(1))), .value(.literal(.string("1")))],
            [.value(.literal(.int(1))), .value(.literal(.string("true")))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        // When/Then
        evaluateOperations(simpleOperations + complexOperations + failingOperations,
                           contexts: contexts,
                           comparableResults: comparableResults)
    }
}
