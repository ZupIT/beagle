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

final class CustomOperationsTests: OperationEvaluationTests {
        
    func testCustomOperation() {
        // Given
        let customOperation = Operation(name: .custom("isValidCPF"), parameters: [.value(.literal(.string("")))])
        
        dependencies.customOperationsProvider.register(operation: customOperation, handler: { parameters -> DynamicObject in
            if let intParameters = parameters.first as? Int {
                let stringParameters = String(intParameters)
                return .bool(stringParameters.isValidCPF)
            } else if let stringParameters = parameters.first as? String {
                return .bool(stringParameters.isValidCPF)
            }
            return .bool(false)
        })
        
        let comparableResults: [DynamicObject] = [true, true, true, true, false, false, false, false]

        // When
        evaluateCustomOperation(.custom("isValidCPF")) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testReplacingSumOperation() {
        // Given
        let customSumOperation = Operation(name: .sum, parameters: [.value(.literal(.string("")))])
        
        let comparableResults: [DynamicObject] = [10, 11, 7, 14]
        
        dependencies.customOperationsProvider.register(operation: customSumOperation, handler: { parameters -> DynamicObject in
            if let integerParameters = parameters as? [Int] {
                return .int(integerParameters.reduce(0, +))
            }
            return nil
        })

        // When
        evaluateSumOperation { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }

    private func evaluateCustomOperation(_ name: Operation.Name, completion: ([DynamicObject]) -> Void) {
        // Given
        let contexts = [Context(id: "context1", value: "50573178577")]
        let binding = contexts[0].id
        
        let simpleOperations = ["42249625000", "82887113593", "59843571860", "\(binding)"].toOperations(name: name)
        
        let failingOperations = ["00000000000", "1111", "1234567890", "999999999"].toOperations(name: name)
        
        let operations = simpleOperations + failingOperations
        
        // When/Then
        evaluateOperations(operations, contexts: contexts, completion: completion)
    }
    
    private func evaluateSumOperation(completion: ([DynamicObject]) -> Void) {
        // Given
        let contexts = [Context(id: "context1", value: 3), Context(id: "context2", value: 10)]
        let bindings = contexts.map { $0.id }
        
        let simpleOperations = ["6, 4", "5, 6", "4, \(bindings[0])", "4, \(bindings[1])"].toOperations(name: .sum)
                
        // When/Then
        evaluateOperations(simpleOperations, contexts: contexts, completion: completion)
    }
}

// MARK: - Helpers

private extension Collection where Element == Int {
    var digitCPF: Int {
        var number = count + 2
        let digit = 11 - reduce(into: 0) {
            number -= 1
            $0 += $1 * number
        } % 11
        return digit > 9 ? 0 : digit
    }
}

private extension StringProtocol {
    var isValidCPF: Bool {
        let numbers = compactMap(\.wholeNumberValue)
        guard numbers.count == 11 && Set(numbers).count != 1 else { return false }
        return numbers.prefix(9).digitCPF == numbers[9] &&
               numbers.prefix(10).digitCPF == numbers[10]

    }
}
