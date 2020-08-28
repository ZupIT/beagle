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
        let name = Operation.Name.isNull
        let contexts = [Context(id: "context", value: [1, 2, 3])]
        let binding = contexts[0].id
        guard let insert = "".toOperation(name: .insert) else {
            XCTFail("Failed to get operation")
            return
        }
        
        let simpleOperations = ["null", "1", "\(binding)"].toOperations(name: name)
         
        let complexOperations = ["\(simpleOperations[0].rawValue)", "\(insert.rawValue)"].toOperations(name: name)
         
        let failingOperations = ["null, null", ""].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        let comparableResults: [DynamicObject] = [true, false, false, false, true, nil, nil]
        
        // When
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateIsEmpty() {
        // Given
        let comparableResults: [DynamicObject] = [true, false, false, true, nil, nil, nil]
        
        // When
        evaluateOperation(.isEmpty) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateLength() {
        // Given
        let comparableResults: [DynamicObject] = [0, 1, 3, 0, nil, nil, nil]
        
        // When
        evaluateOperation(.length) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    private func evaluateOperation(_ name: Operation.Name, completion: ([DynamicObject]) -> Void) {
        // Given
        // swiftlint:disable multiline_literal_brackets
        let contexts = [Context(id: "context1", value: [1]),
                        Context(id: "context2", value: ["one": 1, "two": 2, "three": 3])]
        // swiftlint:enable multiline_literal_brackets
        let bindings = contexts.map { $0.id }
        guard let removeIndex = "\(bindings[0])".toOperation(name: .removeIndex) else {
            XCTFail("Failed to get operation")
            return
        }
        
        let successfulOperations = ["''", "\(bindings[0])", "\(bindings[1])", "\(removeIndex.rawValue)"].toOperations(name: name)
         
        let failingOperations = ["0", "'string', 'string'", ""].toOperations(name: name)
        
        let operations = successfulOperations + failingOperations
        
        // When/Then
        evaluateOperations(operations, contexts: contexts, completion: completion)
    }
}
