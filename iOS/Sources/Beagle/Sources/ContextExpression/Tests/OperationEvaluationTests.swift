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
import SnapshotTesting

class OperationNumberEvaluationTests: XCTestCase {
    
    private func evaluateOperation(_ name: BeagleSchema.Operation.Name, completion: ([DynamicObject]) -> Void) {
        // Given
        var array: [DynamicObject] = []
        let view = UIView()
        let context1 = Context(id: "context1", value: .int(2))
        let context2 = Context(id: "context2", value: .double(2.5))
        
        let operation1 = Operation(name: name, parameters: [.value(.literal(.int(6))), .value(.literal(.int(4)))])
        let operation2 = Operation(name: name, parameters: [.value(.literal(.double(4.5))), .value(.literal(.double(6.0)))])
        let operation3 = Operation(name: name, parameters: [.value(.literal(.int(4))), .value(.binding(Binding(context: "context1",
                                                                                                               path: Path(nodes: []))))])
        let operation4 = Operation(name: name, parameters: [.value(.literal(.double(4.0))), .value(.binding(Binding(context: "context2",
                                                                                                                    path: Path(nodes: []))))])
        let operation5 = Operation(name: name, parameters: [.value(.literal(.int(4))), .operation(operation1)])
        let operation6 = Operation(name: name, parameters: [.value(.literal(.double(2.8))), .operation(operation2)])
        let operation7 = Operation(name: name, parameters: [.operation(operation2), .operation(operation4), .operation(operation6)])
        let operation8 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.double(1.5)))])
        let operation9 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.string("'1'")))])
        let operation10 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.string("true")))])
        let operation11 = Operation(name: name, parameters: [])
        
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
        
        // Then
        completion(array)
    }

    func testEvaluateSum() {
        // Given
        // When
        evaluateOperation(.sum) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.int(10))
            XCTAssertEqual(array[1], DynamicObject.double(10.5))
            XCTAssertEqual(array[2], DynamicObject.int(6))
            XCTAssertEqual(array[3], DynamicObject.double(6.5))
            XCTAssertEqual(array[4], DynamicObject.int(14))
            XCTAssertEqual(array[5], DynamicObject.double(13.3))
            XCTAssertEqual(array[6], DynamicObject.double(30.3))
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
            XCTAssertEqual(array[9], DynamicObject.empty)
            XCTAssertEqual(array[10], DynamicObject.empty)
        }
    }
    
    func testEvaluateSubtract() {
        // Given
        // When
        evaluateOperation(.subtract) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.int(2))
            XCTAssertEqual(array[1], DynamicObject.double(-1.5))
            XCTAssertEqual(array[2], DynamicObject.int(2))
            XCTAssertEqual(array[3], DynamicObject.double(1.5))
            XCTAssertEqual(array[4], DynamicObject.int(2))
            XCTAssertEqual(array[5], DynamicObject.double(4.3))
            XCTAssertEqual(array[6], DynamicObject.double(-7.3))
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
            XCTAssertEqual(array[9], DynamicObject.empty)
            XCTAssertEqual(array[10], DynamicObject.empty)
        }
    }
    
    func testEvaluateMultiply() {
        // Given
        // When
        evaluateOperation(.multiply) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.int(24))
            XCTAssertEqual(array[1], DynamicObject.double(27.0))
            XCTAssertEqual(array[2], DynamicObject.int(8))
            XCTAssertEqual(array[3], DynamicObject.double(10.0))
            XCTAssertEqual(array[4], DynamicObject.int(96))
            XCTAssertEqual(array[5], DynamicObject.double(75.6))
            XCTAssertEqual(array[6], DynamicObject.double(20412.0))
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
            XCTAssertEqual(array[9], DynamicObject.empty)
            XCTAssertEqual(array[10], DynamicObject.empty)
        }
    }
    
    func testEvaluateDivide() {
        // Given
        // When
        evaluateOperation(.divide) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.int(1))
            XCTAssertEqual(array[1], DynamicObject.double(0.75))
            XCTAssertEqual(array[2], DynamicObject.int(2))
            XCTAssertEqual(array[3], DynamicObject.double(1.6))
            XCTAssertEqual(array[4], DynamicObject.int(4))
            XCTAssertEqual(array[5], DynamicObject.double(3.733333333333333))
            XCTAssertEqual(array[6], DynamicObject.double(0.12555803571428573))
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
            XCTAssertEqual(array[9], DynamicObject.empty)
            XCTAssertEqual(array[10], DynamicObject.empty)
        }
    }
    
}
