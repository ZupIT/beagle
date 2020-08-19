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

// swiftlint:disable multiline_literal_brackets
final class OperationComparisonEvaluationTests: XCTestCase {
    
    func testEvaluateGt() {
        // Given
        // When
        evaluateOperation(.gt) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.bool(false))
            XCTAssertEqual(array[1], DynamicObject.bool(false))
            XCTAssertEqual(array[2], DynamicObject.bool(true))
            XCTAssertEqual(array[3], DynamicObject.bool(true))
            XCTAssertEqual(array[4], DynamicObject.bool(false))
            XCTAssertEqual(array[5], DynamicObject.bool(false))
            XCTAssertEqual(array[6], DynamicObject.bool(false))
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
            XCTAssertEqual(array[9], DynamicObject.empty)
            XCTAssertEqual(array[10], DynamicObject.empty)
            XCTAssertEqual(array[11], DynamicObject.empty)
        }
    }
    
    func testEvaluateGte() {
        // Given
        // When
        evaluateOperation(.gte) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.bool(true))
            XCTAssertEqual(array[1], DynamicObject.bool(false))
            XCTAssertEqual(array[2], DynamicObject.bool(true))
            XCTAssertEqual(array[3], DynamicObject.bool(true))
            XCTAssertEqual(array[4], DynamicObject.bool(false))
            XCTAssertEqual(array[5], DynamicObject.bool(false))
            XCTAssertEqual(array[6], DynamicObject.bool(true))
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
            XCTAssertEqual(array[9], DynamicObject.empty)
            XCTAssertEqual(array[10], DynamicObject.empty)
            XCTAssertEqual(array[11], DynamicObject.empty)
        }
    }
    
    func testEvaluateLt() {
        // Given
        // When
        evaluateOperation(.lt) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.bool(false))
            XCTAssertEqual(array[1], DynamicObject.bool(true))
            XCTAssertEqual(array[2], DynamicObject.bool(false))
            XCTAssertEqual(array[3], DynamicObject.bool(false))
            XCTAssertEqual(array[4], DynamicObject.bool(true))
            XCTAssertEqual(array[5], DynamicObject.bool(true))
            XCTAssertEqual(array[6], DynamicObject.bool(false))
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
            XCTAssertEqual(array[9], DynamicObject.empty)
            XCTAssertEqual(array[10], DynamicObject.empty)
            XCTAssertEqual(array[11], DynamicObject.empty)
        }
    }
    
    func testEvaluateLte() {
        // Given
        // When
        evaluateOperation(.lte) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.bool(true))
            XCTAssertEqual(array[1], DynamicObject.bool(true))
            XCTAssertEqual(array[2], DynamicObject.bool(false))
            XCTAssertEqual(array[3], DynamicObject.bool(false))
            XCTAssertEqual(array[4], DynamicObject.bool(true))
            XCTAssertEqual(array[5], DynamicObject.bool(true))
            XCTAssertEqual(array[6], DynamicObject.bool(true))
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
            XCTAssertEqual(array[9], DynamicObject.empty)
            XCTAssertEqual(array[10], DynamicObject.empty)
            XCTAssertEqual(array[11], DynamicObject.empty)
        }
    }
    
    func testEvaluateEq() {
        // Given
        let view = UIView()
        let context = Context(id: "context", value: .bool(true))
        let binding = Binding(context: context.id, path: Path(nodes: []))
        let name = BeagleSchema.Operation.Name.eq
        
        let eq1 = Operation(name: name, parameters: [.value(.literal(.bool(true))), .value(.literal(.bool(true)))])
        let eq2 = Operation(name: name, parameters: [.value(.literal(.string("no"))), .value(.literal(.string("no")))])
        let eq3 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.int(1)))])
        let eq4 = Operation(name: name, parameters: [.value(.literal(.double(2.2))), .value(.literal(.double(2.2)))])
        let eq5 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.binding(binding))])
        let eq6 = Operation(name: name, parameters: [.operation(eq3), .operation(eq3)])
        let eq7 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.int(0)))])
        let eq8 = Operation(name: name, parameters: [.value(.literal(.double(2.2))), .value(.literal(.double(2.5)))])
        let eq9 = Operation(name: name, parameters: [.value(.literal(.bool(true))), .value(.literal(.int(1)))])
        let eq10 = Operation(name: name, parameters: [.value(.literal(.int(0))), .value(.literal(.int(0))), .value(.literal(.int(0)))])
        let eq11 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context)
        
        let result1 = eq1.evaluate(in: view)
        let result2 = eq2.evaluate(in: view)
        let result3 = eq3.evaluate(in: view)
        let result4 = eq4.evaluate(in: view)
        let result5 = eq5.evaluate(in: view)
        let result6 = eq6.evaluate(in: view)
        let result7 = eq7.evaluate(in: view)
        let result8 = eq8.evaluate(in: view)
        let result9 = eq9.evaluate(in: view)
        let result10 = eq10.evaluate(in: view)
        let result11 = eq11.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.bool(true))
        XCTAssertEqual(result2, DynamicObject.bool(true))
        XCTAssertEqual(result3, DynamicObject.bool(true))
        XCTAssertEqual(result4, DynamicObject.bool(true))
        XCTAssertEqual(result5, DynamicObject.bool(true))
        XCTAssertEqual(result6, DynamicObject.bool(true))
        XCTAssertEqual(result7, DynamicObject.bool(false))
        XCTAssertEqual(result8, DynamicObject.bool(false))
        XCTAssertEqual(result9, DynamicObject.bool(false))
        XCTAssertEqual(result10, DynamicObject.empty)
        XCTAssertEqual(result11, DynamicObject.empty)
    }
    
    private func evaluateOperation(_ name: BeagleSchema.Operation.Name, completion: ([DynamicObject]) -> Void) {
        // Given
        var array: [DynamicObject] = []
        let view = UIView()
        let context1 = Context(id: "context1", value: .int(2))
        let binding1 = Binding(context: context1.id, path: Path(nodes: []))
        let context2 = Context(id: "context2", value: .double(2.5))
        let binding2 = Binding(context: context2.id, path: Path(nodes: []))
        let sum1 = Operation(name: .sum, parameters: [.value(.literal(.int(10))), .value(.literal(.int(4)))])
        let sum2 = Operation(name: .sum, parameters: [.value(.literal(.double(12.5))), .value(.literal(.double(5.5)))])
        let sum3 = Operation(name: .subtract, parameters: [.value(.literal(.int(28))), .operation(sum1)])
        
        let operation1 = Operation(name: name, parameters: [.value(.literal(.int(6))), .value(.literal(.int(6)))])
        let operation2 = Operation(name: name, parameters: [.value(.literal(.double(4.5))), .value(.literal(.double(6.0)))])
        let operation3 = Operation(name: name, parameters: [.value(.literal(.int(4))), .value(.binding(binding1))])
        let operation4 = Operation(name: name, parameters: [.value(.literal(.double(4.0))), .value(.binding(binding2))])
        let operation5 = Operation(name: name, parameters: [.value(.literal(.int(4))), .operation(sum1)])
        let operation6 = Operation(name: name, parameters: [.value(.literal(.double(2.8))), .operation(sum2)])
        let operation7 = Operation(name: name, parameters: [.operation(sum1), .operation(sum3)])
        let operation8 = Operation(name: name, parameters: [.value(.literal(.int(6))), .value(.literal(.int(4))), .value(.literal(.int(4)))])
        let operation9 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.double(1.5)))])
        let operation10 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.string("1")))])
        let operation11 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.string("true")))])
        let operation12 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context1)
        view.setContext(context2)
        
        array.append(operation1.evaluate(in: view))
        array.append(operation2.evaluate(in: view))
        array.append(operation3.evaluate(in: view))
        array.append(operation4.evaluate(in: view))
        array.append(operation5.evaluate(in: view))
        array.append(operation6.evaluate(in: view))
        array.append(operation7.evaluate(in: view))
        array.append(operation8.evaluate(in: view))
        array.append(operation9.evaluate(in: view))
        array.append(operation10.evaluate(in: view))
        array.append(operation11.evaluate(in: view))
        array.append(operation12.evaluate(in: view))
        
        // Then
        completion(array)
    }
}
