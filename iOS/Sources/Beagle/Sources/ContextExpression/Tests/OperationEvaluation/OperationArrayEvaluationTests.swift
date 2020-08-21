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

final class OperationArrayEvaluationTests: OperationEvaluationTests {

    func testEvaluateInsert() {
        // Given
        let name = BeagleSchema.Operation.Name.insert
        let contexts = [Context(id: "context", value: .array([.int(1), .int(2), .int(3)]))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        
        let simpleOperations =
        [
            [.value(.binding(bindings[0])), .value(.literal(.int(3))), .value(.literal(.int(2)))],
            [.value(.binding(bindings[0])), .value(.literal(.double(3.0))), .value(.literal(.int(1)))],
            [.value(.binding(bindings[0])), .value(.literal(.int(0)))]
        ].map { Operation(name: name, parameters: $0) }
         
        let complexOperations =
        [
            [.operation(simpleOperations[0]), .value(.literal(.int(4)))]
        ].map { Operation(name: name, parameters: $0) }
         
        let failingOperations =
        [
            [.value(.binding(bindings[0])), .value(.literal(.int(3))), .value(.literal(.double(2.0)))],
            [.value(.binding(bindings[0])), .value(.literal(.int(3))), .value(.literal(.int(5)))],
            [.value(.literal(.string("array"))), .value(.literal(.string(""))), .value(.literal(.int(0)))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        let comparableResults: [DynamicObject] =
        [
            .array([.int(1), .int(2), .int(3), .int(3)]),
            .array([.int(1), .double(3.0), .int(2), .int(3)]),
            .array([.int(1), .int(2), .int(3), .int(0)]),
            .array([.int(1), .int(2), .int(3), .int(3), .int(4)]),
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
    
    func testEvaluateRemove() {
        // Given
        let name = BeagleSchema.Operation.Name.remove
        let contexts = [Context(id: "context", value: .array([.int(1), .int(2), .double(3.0), .double(3.0)]))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        
        let simpleOperations =
        [
            [.value(.binding(bindings[0])), .value(.literal(.int(2)))],
            [.value(.binding(bindings[0])), .value(.literal(.double(3.0)))],
            [.value(.binding(bindings[0])), .value(.literal(.int(4)))]
        ].map { Operation(name: name, parameters: $0) }
         
        let complexOperations =
        [
            [.operation(simpleOperations[0]), .value(.literal(.int(1)))]
        ].map { Operation(name: name, parameters: $0) }
         
        let failingOperations =
        [
            [.value(.binding(bindings[0])), .value(.literal(.int(2))), .value(.literal(.double(3.0)))],
            [.value(.literal(.string("array"))), .value(.literal(.string(""))), .value(.literal(.int(0)))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        let comparableResults: [DynamicObject] =
        [
            .array([.int(1), .double(3.0), .double(3.0)]),
            .array([.int(1), .int(2)]),
            .array([.int(1), .int(2), .double(3.0), .double(3.0)]),
            .array([.double(3.0), .double(3.0)]),
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperations(simpleOperations + complexOperations + failingOperations,
                           contexts: contexts,
                           comparableResults: comparableResults)
    }
    
    func testEvaluateRemoveIndex() {
        // Given
        let name = BeagleSchema.Operation.Name.removeIndex
        let contexts = [Context(id: "context", value: .array([.int(1), .int(2), .double(3.0), .double(3.0)]))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        
        let simpleOperations =
        [
            [.value(.binding(bindings[0])), .value(.literal(.int(1)))],
            [.value(.binding(bindings[0]))]
        ].map { Operation(name: name, parameters: $0) }
         
        let complexOperations =
        [
            [.operation(simpleOperations[0]), .value(.literal(.int(0)))]
        ].map { Operation(name: name, parameters: $0) }
         
        let failingOperations =
        [
            [.value(.binding(bindings[0])), .value(.literal(.int(4)))],
            [.value(.binding(bindings[0])), .value(.literal(.double(3.0)))],
            [.value(.literal(.string("array"))), .value(.literal(.int(0)))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        let comparableResults: [DynamicObject] =
        [
            .array([.int(1), .double(3.0), .double(3.0)]),
            .array([.int(1), .int(2), .double(3.0)]),
            .array([.double(3.0), .double(3.0)]),
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
    
    func testEvaluateIncludes() {
        // Given
        let name = BeagleSchema.Operation.Name.includes
        let contexts = [Context(id: "context", value: .array([.int(1), .int(2), .int(3)]))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        let insert = Operation(name: .insert, parameters: [.value(.binding(bindings[0])), .value(.literal(.int(4))), .value(.literal(.int(2)))])
        
        let successfulOperations =
        [
            [.value(.binding(bindings[0])), .value(.literal(.int(3)))],
            [.value(.binding(bindings[0])), .value(.literal(.int(4)))],
            [.operation(insert), .value(.literal(.int(4)))]
        ].map { Operation(name: name, parameters: $0) }
         
        let failingOperations =
        [
            [.value(.binding(bindings[0]))],
            [.value(.literal(.string("array"))), .value(.literal(.int(0)))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        let comparableResults: [DynamicObject] =
        [
            .bool(true),
            .bool(false),
            .bool(true),
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperations(successfulOperations + failingOperations,
                           contexts: contexts,
                           comparableResults: comparableResults)
        
    }

}
