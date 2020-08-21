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

final class OperationNumberEvaluationTests: OperationEvaluationTests {

    func testEvaluateSum() {
        // Given
        let comparableResults: [DynamicObject] = [10, 10.5, 6, 6.5, 14, 13.3, 27.5, nil, nil, nil, nil]
        
        // When
        evaluateOperation(.sum) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateSubtract() {
        // Given
        let comparableResults: [DynamicObject] = [2, -1.5, 2, 1.5, 2, 4.3, -1.5, nil, nil, nil, nil]
        
        // When
        evaluateOperation(.subtract) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateMultiply() {
        // Given
        let comparableResults: [DynamicObject] = [24, 27.0, 8, 10.0, 96, 75.6, 7290.0, nil, nil, nil, nil]
        
        // When
        evaluateOperation(.multiply) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateDivide() {
        // Given
        let comparableResults: [DynamicObject] = [1, 0.75, 2, 1.6, 4, 3.733333333333333, 0.625, nil, nil, nil, nil]
        
        // When
        evaluateOperation(.divide) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    private func evaluateOperation(_ name: Operation.Name, completion: ([DynamicObject]) -> Void) {
        // Given
        let contexts = [Context(id: "context1", value: 2), Context(id: "context2", value: 2.5)]
        let bindings = contexts.map { $0.id }
        
        let simpleOperations = ["6, 4", "4.5, 6.0", "4, \(bindings[0])", "4.0, \(bindings[1])"].toOperations(name: name)
        
        let complexOperations = [
            "4, \(simpleOperations[0].rawValue)",
            "2.8, \(simpleOperations[1].rawValue)",
            "\(simpleOperations[1].rawValue), \(simpleOperations[3].rawValue), \(simpleOperations[1].rawValue)"
        ].toOperations(name: name)
        
        let failingOperations = ["1, 1.5", "1, '1'", "1, true", ""].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        // When/Then
        evaluateOperations(operations, contexts: contexts, completion: completion)
    }
}
