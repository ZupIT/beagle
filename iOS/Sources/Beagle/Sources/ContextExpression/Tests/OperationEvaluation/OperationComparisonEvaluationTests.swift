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
        let comparableResults: [DynamicObject] = [false, false, true, true, false, false, false, nil, nil, nil, nil, nil]
        
        // When
        evaluateOperation(.gt) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateGte() {
        // Given
        let comparableResults: [DynamicObject] = [true, false, true, true, false, false, true, nil, nil, nil, nil, nil]
        
        // When
        evaluateOperation(.gte) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateLt() {
        // Given
        let comparableResults: [DynamicObject] = [false, true, false, false, true, true, false, nil, nil, nil, nil, nil]
        
        // When
        evaluateOperation(.lt) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateLte() {
        // Given
        let comparableResults: [DynamicObject] = [true, true, false, false, true, true, true, nil, nil, nil, nil, nil]
        
        // When
        evaluateOperation(.lte) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateEq() {
        // Given
        let name = Operation.Name.eq
        let contexts = [Context(id: "context", value: true)]
        let binding = contexts[0].id
        
        let simpleOperations = ["true, true", "'no', 'no'", "1, 1", "2.2, 2.2", "\(binding), \(binding)"].toOperations(name: name)
        
        let complexOperations = ["\(simpleOperations[2].rawValue), \(simpleOperations[2].rawValue)"].toOperations(name: name)
        
        let failingOperations = ["1, 0", "2.2, 2.5", "true, 1", "0, 0, 0", ""].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        let comparableResults: [DynamicObject] = [true, true, true, true, true, true, false, false, false, nil, nil]
        
        // When
        evaluateOperations(operations, contexts: contexts) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    private func evaluateOperation(_ name: Operation.Name, completion: ([DynamicObject]) -> Void) {
        // Given
        let contexts = [Context(id: "context1", value: 2), Context(id: "context2", value: 2.5)]
        let bindings = contexts.map { $0.id }
        
        let sums = ["10, 4", "12.5, 5.5"].toOperations(name: .sum)
        guard let subtract = "28, \(sums[0].rawValue)".toOperation(name: .subtract) else {
            XCTFail("Failed to get operation")
            return
        }
        
        let successfulOperations = [
            "6, 6",
            "4.5, 6.0",
            "4, \(bindings[0])",
            "4.0, \(bindings[1])",
            "4, \(sums[0].rawValue)",
            "2.8, \(sums[1].rawValue)",
            "\(sums[0].rawValue), \(subtract.rawValue)"
        ].toOperations(name: name)
        
        let failingOperations = ["6, 4, 4", "1, 1.5", "1, '1'", "1, 'true'", ""].toOperations(name: name)
        
        let operations = successfulOperations + failingOperations
        
        // When/Then
        evaluateOperations(operations, contexts: contexts, completion: completion)
    }
}
