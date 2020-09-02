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
        let name = Operation.Name.condition
        let contexts = [Context(id: "context", value: true)]
        let binding = contexts[0].id
        
        let simpleOperations = ["true, 1, 0", "false, 'yes', 'no'", "\(binding), true, false"].toOperations(name: name)
        
        let complexOperations = ["\(simpleOperations[2].rawValue), 1.1, 0.0"].toOperations(name: name)
        
        let failingOperations = ["1, 0", "1, 1, 0", "true, 1, 0.0", ""].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        let comparableResults: [DynamicObject] = [1, "no", true, 1.1, nil, nil, nil, nil]
        
        // When
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateNot() {
        // Given
        let name = Operation.Name.not
        let contexts = [Context(id: "context", value: true)]
        let binding = contexts[0].id
        
        let simpleOperations = ["true", "false", "\(binding)"].toOperations(name: name)
        
        let complexOperations = ["\(simpleOperations[2].rawValue)"].toOperations(name: name)
        
        let failingOperations = ["1", "1, 1", ""].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        let comparableResults: [DynamicObject] = [false, true, false, true, nil, nil, nil]
        
        // When
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateAnd() {
        // Given
        let comparableResults: [DynamicObject] = [true, false, false, true, false, nil, nil, nil, nil]
        
        // When
        evaluateOperation(.and) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateOr() {
        // Given
        let comparableResults: [DynamicObject] = [true, false, true, true, true, nil, nil, nil, nil]
        
        // When
        evaluateOperation(.or) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    private func evaluateOperation(_ name: Operation.Name, completion: ([DynamicObject]) -> Void) {
        // Given
        let contexts = [Context(id: "context1", value: false)]
        let binding = contexts[0].id
        
        let simpleOperations = ["true, true", "false, false", "true, \(binding)"].toOperations(name: name)
        
        let complexOperations = [
            "true, \(simpleOperations[0].rawValue)",
            "\(simpleOperations[0].rawValue), \(simpleOperations[1].rawValue), \(simpleOperations[2].rawValue)"
        ].toOperations(name: name)
        
        let failingOperations = ["0, 1.5", "0, '1'", "0, false", ""].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        // When/Then
        evaluateOperations(operations, contexts: contexts, completion: completion)
    }
}
