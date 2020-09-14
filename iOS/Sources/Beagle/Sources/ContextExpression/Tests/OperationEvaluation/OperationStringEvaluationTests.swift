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
        let name = Operation.Name.concat
        let contexts = [Context(id: "context", value: "Lastname")]
        let binding = contexts[0].id
        
        let simpleOperations = ["'string', 'STRING'", "'Name', \(binding)"].toOperations(name: name)
         
        let complexOperations = [
            "'StRiNg', \(simpleOperations[0].rawValue)",
            "\(simpleOperations[0].rawValue), ' ', \(binding)"
        ].toOperations(name: name)
         
        let failingOperations = ["1, 0", "'1', 1", "true, 'false'", ""].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        let comparableResults: [DynamicObject] = [
            "stringSTRING",
            "NameLastname",
            "StRiNgstringSTRING",
            "stringSTRING Lastname",
            nil, nil, nil, nil
        ]
        
        // When
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateCapitalize() {
        // Given
        let comparableResults: [DynamicObject] = ["String", "String", "Name", "Lastname", "Name Lastname", nil, nil, nil, nil]
        
        // When
        evaluateOperation(.capitalize) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateUppercase() {
        // Given
        let comparableResults: [DynamicObject] = ["STRING", "STRING", "NAME", "LASTNAME", "NAME LASTNAME", nil, nil, nil, nil]
        
        // When
        evaluateOperation(.uppercase) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateLowercase() {
        // Given
        let comparableResults: [DynamicObject] = ["string", "string", "name", "lastname", "name lastname", nil, nil, nil, nil]
        
        // When
        evaluateOperation(.lowercase) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateSubstr() {
        // Given
        let name = Operation.Name.substr
        let contexts = [Context(id: "context", value: "some long string")]
        let binding = contexts[0].id
        
        let simpleOperations = [
            "'str', 0, 0",
            "'string', 0, 3",
            "\(binding), 0, 4",
            "\(binding), 5, 4",
            "\(binding), 5"
        ].toOperations(name: name)
         
        let complexOperations = ["\(simpleOperations[4].rawValue), 5"].toOperations(name: name)
         
        let failingOperations = [
            "'str', -1, 1",
            "'str', 0, 5",
            "'str', 3, 2",
            "1, 0",
            "'1', 0.0",
            "'1', 0, 1.0",
            ""
        ].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        let comparableResults: [DynamicObject] = ["", "str", "some", "long", "long string", "string", nil, nil, nil, nil, nil, nil, nil]
        
        // When
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    private func evaluateOperation(_ name: Operation.Name, completion: ([DynamicObject]) -> Void) {
        // Given
        let contexts = [Context(id: "context1", value: "name"), Context(id: "context2", value: "Lastname")]
        let bindings = contexts.map { $0.id }
        guard let concat = "\(bindings[0]), ' ', \(bindings[1])".toOperation(name: .concat) else {
            XCTFail("Failed to get operation")
            return
        }
        
        let successfulOperations = [
            "'string'",
            "'String'",
            "\(bindings[0])",
            "\(bindings[1])",
            "\(concat.rawValue)"
        ].toOperations(name: name)
        
        let failingOperations = ["1", "true", "'0', '1'", ""].toOperations(name: name)
        
        let operations = successfulOperations + failingOperations
        
        // When/Then
        evaluateOperations(operations, contexts: contexts, completion: completion)
    }
}
