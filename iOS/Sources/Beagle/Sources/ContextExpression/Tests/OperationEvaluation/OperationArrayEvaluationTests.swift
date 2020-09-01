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
        let name = Operation.Name.insert
        let contexts = [Context(id: "context", value: [1, 2, 3])]
        let binding = contexts[0].id
        
        let simpleOperations = ["\(binding), 3, 2", "\(binding), 3.0, 1", "\(binding), 0"].toOperations(name: name)
         
        let complexOperations = ["\(simpleOperations[0].rawValue), 4"].toOperations(name: name)
         
        let failingOperations = ["\(binding), 3, 2.0", "\(binding), 3, 5", "'array', '', 0", ""].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        let comparableResults: [DynamicObject] = [[1, 2, 3, 3], [1, 3.0, 2, 3], [1, 2, 3, 0], [1, 2, 3, 3, 4], nil, nil, nil, nil]
        
        // When/
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateRemove() {
        // Given
        let name = Operation.Name.remove
        let contexts = [Context(id: "context", value: [1, 2, 3.0, 3.0])]
        let binding = contexts[0].id
        
        let simpleOperations = ["\(binding), 2", "\(binding), 3.0", "\(binding), 4"].toOperations(name: name)
         
        let complexOperations = ["\(simpleOperations[0].rawValue), 1"].toOperations(name: name)
         
        let failingOperations = ["\(binding), 2, 3.0", "'array', '', 0", ""].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        let comparableResults: [DynamicObject] = [[1, 3.0, 3.0], [1, 2], [1, 2, 3.0, 3.0], [3.0, 3.0], nil, nil, nil]
        
        // When
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateRemoveIndex() {
        // Given
        let name = Operation.Name.removeIndex
        let contexts = [Context(id: "context", value: [1, 2, 3.0, 3.0])]
        let binding = contexts[0].id
        
        let simpleOperations = ["\(binding), 1", "\(binding)"].toOperations(name: name)
         
        let complexOperations = ["\(simpleOperations[0].rawValue), 0"].toOperations(name: name)
         
        let failingOperations = ["\(binding), 4", "\(binding), 3.0", "'array', 0", ""].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        let comparableResults: [DynamicObject] = [[1, 3.0, 3.0], [1, 2, 3.0], [3.0, 3.0], nil, nil, nil, nil]
        
        // When
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateContains() {
        // Given
        let name = Operation.Name.contains
        let contexts = [Context(id: "context", value: [1, 2, 3])]
        let binding = contexts[0].id
        guard let insert = "\(binding), 4, 2".toOperation(name: .insert) else {
            XCTFail("Failed to get operation")
            return
        }
        
        let successfulOperations = ["\(binding), 3", "\(binding), 4", "\(insert.rawValue), 4"].toOperations(name: name)
         
        let failingOperations = ["\(binding)", "'array', 0", ""].toOperations(name: name)
        
        let operations = successfulOperations + failingOperations
        
        let comparableResults: [DynamicObject] = [true, false, true, nil, nil, nil]
        
        // When
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
}
