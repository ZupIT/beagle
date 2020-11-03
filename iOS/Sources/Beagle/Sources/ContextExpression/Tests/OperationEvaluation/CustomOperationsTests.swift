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
        dependencies.customOperationsProvider.register(operationId: "isValidCPF") { parameters in
            let anyParameters = parameters.map { $0.asAny() }
            if let intParameters = anyParameters.first as? Int {
                let stringParameters = String(intParameters)
                return .bool(stringParameters.isValidCPF)
            } else if let stringParameters = anyParameters.first as? String {
                return .bool(stringParameters.isValidCPF)
            }
            return nil
        }
        
        let comparableResults: [DynamicObject] = [true, true, true, true, false, false, false, false]

        // When
        evaluateCustomOperation(.custom("isValidCPF")) { evaluatedResults in
            // Then
            XCTAssertEqual(evaluatedResults, comparableResults)
        }
    }
    
    func testInvalidName() {
        // Given
        let view = UIView()
        let customSumOperation = Operation(name: .custom("sum???"), parameters: [.value(.literal(.int(2)))])
        let customEmptyOperation = Operation(name: .custom(""), parameters: [.value(.literal(.int(2)))])
        let customNumbersOperation = Operation(name: .custom("123"), parameters: [.value(.literal(.int(2)))])

        dependencies.customOperationsProvider.register(operationId: "sum???") { _ in
            return nil
        }
        
        dependencies.customOperationsProvider.register(operationId: "") { _ in
            return nil
        }
        
        dependencies.customOperationsProvider.register(operationId: "123") { _ in
            return nil
        }
        
        // When // Then
        XCTAssertNil(dependencies.customOperationsProvider.getOperationHandler(with: customSumOperation, in: view))
        XCTAssertNil(dependencies.customOperationsProvider.getOperationHandler(with: customEmptyOperation, in: view))
        XCTAssertNil(dependencies.customOperationsProvider.getOperationHandler(with: customNumbersOperation, in: view))
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
