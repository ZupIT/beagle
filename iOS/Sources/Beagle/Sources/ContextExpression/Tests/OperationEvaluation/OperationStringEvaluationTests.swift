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

final class OperationStringEvaluationTests: OperationEvaluationTests {
    
    func testEvaluateConcat() {
        // Given
        let name = BeagleSchema.Operation.Name.concat
        let contexts = [Context(id: "context", value: .string("Lastname"))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        
        let simpleOperations =
         [
            [.value(.literal(.string("string"))), .value(.literal(.string("STRING")))],
            [.value(.literal(.string("Name"))), .value(.binding(bindings[0]))]
         ].map { Operation(name: name, parameters: $0) }
         
         let complexOperations =
         [
            [.value(.literal(.string("StRiNg"))), .operation(simpleOperations[0])],
            [.operation(simpleOperations[0]), .value(.literal(.string(" "))), .value(.binding(bindings[0]))]
         ].map { Operation(name: name, parameters: $0) }
         
         let failingOperations =
         [
            [.value(.literal(.int(1))), .value(.literal(.int(0)))],
            [.value(.literal(.string("1"))), .value(.literal(.int(1)))],
            [.value(.literal(.bool(true))), .value(.literal(.string("false")))],
            []
         ].map { Operation(name: name, parameters: $0) }
        
        let comparableResults: [DynamicObject] =
        [
            .string("stringSTRING"),
            .string("NameLastname"),
            .string("StRiNgstringSTRING"),
            .string("stringSTRING Lastname"),
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
    
    func testEvaluateCapitalize() {
        // Given
        let results: [DynamicObject] =
        [
            .string("String"),
            .string("String"),
            .string("Name"),
            .string("Lastname"),
            .string("Name Lastname"),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.capitalize, comparableResults: results)
    }
    
    func testEvaluateUppercase() {
        // Given
        let results: [DynamicObject] =
        [
            .string("STRING"),
            .string("STRING"),
            .string("NAME"),
            .string("LASTNAME"),
            .string("NAME LASTNAME"),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.uppercase, comparableResults: results)
    }
    
    func testEvaluateLowercase() {
        // Given
        let results: [DynamicObject] =
        [
            .string("string"),
            .string("string"),
            .string("name"),
            .string("lastname"),
            .string("name lastname"),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When/Then
        evaluateOperation(.lowercase, comparableResults: results)
    }
    
    func testEvaluateSubstr() {
        // Given
        let name = BeagleSchema.Operation.Name.substr
        let contexts = [Context(id: "context", value: .string("some long string"))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        
        let simpleOperations =
         [
            [.value(.literal(.string("string"))), .value(.literal(.int(0))), .value(.literal(.int(3)))],
            [.value(.binding(bindings[0])), .value(.literal(.int(0))), .value(.literal(.int(4)))],
            [.value(.binding(bindings[0])), .value(.literal(.int(5))), .value(.literal(.int(4)))],
            [.value(.binding(bindings[0])), .value(.literal(.int(5)))]
         ].map { Operation(name: name, parameters: $0) }
         
         let complexOperations =
         [
            [.operation(simpleOperations[3]), .value(.literal(.int(5)))]
         ].map { Operation(name: name, parameters: $0) }
         
         let failingOperations =
         [
            [.value(.literal(.string("str"))), .value(.literal(.int(0))), .value(.literal(.int(0)))],
            [.value(.literal(.string("str"))), .value(.literal(.int(-1))), .value(.literal(.int(1)))],
            [.value(.literal(.string("str"))), .value(.literal(.int(0))), .value(.literal(.int(5)))],
            [.value(.literal(.string("str"))), .value(.literal(.int(3))), .value(.literal(.int(2)))],
            [.value(.literal(.int(1))), .value(.literal(.int(0)))],
            [.value(.literal(.string("1"))), .value(.literal(.double(0.0)))],
            [.value(.literal(.string("1"))), .value(.literal(.int(0))), .value(.literal(.double(1.0)))],
            []
         ].map { Operation(name: name, parameters: $0) }
        
        let comparableResults: [DynamicObject] =
        [
            .string("str"),
            .string("some"),
            .string("long"),
            .string("long string"),
            .string("string"),
            .string(""),
            .empty,
            .empty,
            .empty,
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
    
    private func evaluateOperation(_ name: BeagleSchema.Operation.Name, comparableResults: [DynamicObject]) {
        // Given
        let contexts = [Context(id: "context1", value: .string("name")), Context(id: "context2", value: .string("Lastname"))]
        let bindings = contexts.map { Binding(context: $0.id, path: Path(nodes: [])) }
        let concat = Operation(name: .concat, parameters: [.value(.binding(bindings[0])), .value(.literal(.string(" "))), .value(.binding(bindings[1]))])
        
        let successfulOperations =
        [
            [.value(.literal(.string("string")))],
            [.value(.literal(.string("String")))],
            [.value(.binding(bindings[0]))],
            [.value(.binding(bindings[1]))],
            [.operation(concat)]
        ].map { Operation(name: name, parameters: $0) }
        
        let failingOperations =
        [
            [.value(.literal(.int(1)))],
            [.value(.literal(.bool(true)))],
            [.value(.literal(.string("0"))), .value(.literal(.string("1")))],
            []
        ].map { Operation(name: name, parameters: $0) }
        
        // When/Then
        evaluateOperations(successfulOperations + failingOperations,
                           contexts: contexts,
                           comparableResults: comparableResults)
    }
}
