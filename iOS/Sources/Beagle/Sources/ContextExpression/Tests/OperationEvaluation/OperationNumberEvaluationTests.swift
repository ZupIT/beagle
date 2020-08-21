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
        let comparableResults: [DynamicObject] =
        [
            .int(10),
            .double(10.5),
            .int(6),
            .double(6.5),
            .int(14),
            .double(13.3),
            .double(27.5),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When
        evaluateOperation(.sum) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateSubtract() {
        // Given
        let comparableResults: [DynamicObject] =
        [
            .int(2),
            .double(-1.5),
            .int(2),
            .double(1.5),
            .int(2),
            .double(4.3),
            .double(-1.5),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When
        evaluateOperation(.subtract) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateMultiply() {
        // Given
        let comparableResults: [DynamicObject] =
        [
            .int(24),
            .double(27.0),
            .int(8),
            .double(10.0),
            .int(96),
            .double(75.6),
            .double(7290.0),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When
        evaluateOperation(.multiply) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testEvaluateDivide() {
        // Given
        let comparableResults: [DynamicObject] =
        [
            .int(1),
            .double(0.75),
            .int(2),
            .double(1.6),
            .int(4),
            .double(3.733333333333333),
            .double(0.625),
            .empty,
            .empty,
            .empty,
            .empty
        ]
        
        // When
        evaluateOperation(.divide) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    private func evaluateOperation(_ name: BeagleSchema.Operation.Name, completion: ([DynamicObject]) -> Void) {
        // Given
        let contexts = [Context(id: "context1", value: .int(2)), Context(id: "context2", value: .double(2.5))]
        let bindings = contexts.map { $0.id }
        
        let simpleOperations =
        [
            "6, 4",
            "4.5, 6.0",
            "4, \(bindings[0])",
            "4.0, \(bindings[1])"
        ].toOperations(name: name)
        
        let complexOperations =
        [
            "4, \(simpleOperations[0].rawValue)",
            "2.8, \(simpleOperations[1].rawValue)",
            "\(simpleOperations[1].rawValue), \(simpleOperations[3].rawValue), \(simpleOperations[1].rawValue)"
        ].toOperations(name: name)
        
        let failingOperations =
        [
            "1, 1.5",
            "1, '1'",
            "1, true",
            ""
        ].toOperations(name: name)
        
        let operations = simpleOperations + complexOperations + failingOperations
        
        // When/Then
        evaluateOperations(operations, contexts: contexts, completion: completion)
    }
}
