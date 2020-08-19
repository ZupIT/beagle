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
final class OperationLogicEvaluationTests: XCTestCase {
    
    func testEvaluateCondition() {
        // Given
        let view = UIView()
        let context = Context(id: "context", value: .bool(true))
        let binding = Binding(context: context.id, path: Path(nodes: []))
        let name = BeagleSchema.Operation.Name.condition
        
        let condition1 = Operation(name: name, parameters: [.value(.literal(.bool(true))), .value(.literal(.int(1))), .value(.literal(.int(0)))])
        let condition2 = Operation(name: name, parameters: [.value(.literal(.bool(false))),
                                                            .value(.literal(.string("yes"))),
                                                            .value(.literal(.string("no")))])
        let condition3 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.bool(true))), .value(.literal(.bool(false)))])
        let condition4 = Operation(name: name, parameters: [.operation(condition3), .value(.literal(.double(1.1))), .value(.literal(.double(0.0)))])
        let condition5 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.int(0)))])
        let condition6 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.int(1))), .value(.literal(.int(0)))])
        let condition7 = Operation(name: name, parameters: [.value(.literal(.bool(true))), .value(.literal(.int(1))), .value(.literal(.double(0.0)))])
        let condition8 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context)
        
        let result1 = condition1.evaluate(in: view)
        let result2 = condition2.evaluate(in: view)
        let result3 = condition3.evaluate(in: view)
        let result4 = condition4.evaluate(in: view)
        let result5 = condition5.evaluate(in: view)
        let result6 = condition6.evaluate(in: view)
        let result7 = condition7.evaluate(in: view)
        let result8 = condition8.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.int(1))
        XCTAssertEqual(result2, DynamicObject.string("no"))
        XCTAssertEqual(result3, DynamicObject.bool(true))
        XCTAssertEqual(result4, DynamicObject.double(1.1))
        XCTAssertEqual(result5, DynamicObject.empty)
        XCTAssertEqual(result6, DynamicObject.empty)
        XCTAssertEqual(result7, DynamicObject.empty)
        XCTAssertEqual(result8, DynamicObject.empty)
    }
    
    func testEvaluateNot() {
        // Given
        let view = UIView()
        let context = Context(id: "context", value: .bool(true))
        let binding = Binding(context: context.id, path: Path(nodes: []))
        let name = BeagleSchema.Operation.Name.not
        
        let not1 = Operation(name: name, parameters: [.value(.literal(.bool(true)))])
        let not2 = Operation(name: name, parameters: [.value(.literal(.bool(false)))])
        let not3 = Operation(name: name, parameters: [.value(.binding(binding))])
        let not4 = Operation(name: name, parameters: [.operation(not3)])
        let not5 = Operation(name: name, parameters: [.value(.literal(.int(1)))])
        let not6 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.int(1)))])
        let not7 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context)
        
        let result1 = not1.evaluate(in: view)
        let result2 = not2.evaluate(in: view)
        let result3 = not3.evaluate(in: view)
        let result4 = not4.evaluate(in: view)
        let result5 = not5.evaluate(in: view)
        let result6 = not6.evaluate(in: view)
        let result7 = not7.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.bool(false))
        XCTAssertEqual(result2, DynamicObject.bool(true))
        XCTAssertEqual(result3, DynamicObject.bool(false))
        XCTAssertEqual(result4, DynamicObject.bool(true))
        XCTAssertEqual(result5, DynamicObject.empty)
        XCTAssertEqual(result6, DynamicObject.empty)
        XCTAssertEqual(result7, DynamicObject.empty)
    }
    
    func testEvaluateAnd() {
        // Given
        // When
        evaluateOperation(.and) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.bool(true))
            XCTAssertEqual(array[1], DynamicObject.bool(false))
            XCTAssertEqual(array[2], DynamicObject.bool(false))
            XCTAssertEqual(array[3], DynamicObject.bool(true))
            XCTAssertEqual(array[4], DynamicObject.bool(false))
            XCTAssertEqual(array[5], DynamicObject.empty)
            XCTAssertEqual(array[6], DynamicObject.empty)
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
        }
    }
    
    func testEvaluateOr() {
        // Given
        // When
        evaluateOperation(.or) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.bool(true))
            XCTAssertEqual(array[1], DynamicObject.bool(false))
            XCTAssertEqual(array[2], DynamicObject.bool(true))
            XCTAssertEqual(array[3], DynamicObject.bool(true))
            XCTAssertEqual(array[4], DynamicObject.bool(true))
            XCTAssertEqual(array[5], DynamicObject.empty)
            XCTAssertEqual(array[6], DynamicObject.empty)
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
        }
    }
    
    private func evaluateOperation(_ name: BeagleSchema.Operation.Name, completion: ([DynamicObject]) -> Void) {
        // Given
        var array: [DynamicObject] = []
        let view = UIView()
        let context1 = Context(id: "context1", value: .bool(false))
        let binding1 = Binding(context: context1.id, path: Path(nodes: []))
        
        let operation1 = Operation(name: name, parameters: [.value(.literal(.bool(true))), .value(.literal(.bool(true)))])
        let operation2 = Operation(name: name, parameters: [.value(.literal(.bool(false))), .value(.literal(.bool(false)))])
        let operation3 = Operation(name: name, parameters: [.value(.literal(.bool(true))), .value(.binding(binding1))])
        let operation4 = Operation(name: name, parameters: [.value(.literal(.bool(true))), .operation(operation1)])
        let operation5 = Operation(name: name, parameters: [.operation(operation1), .operation(operation2), .operation(operation3)])
        let operation6 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.double(1.5)))])
        let operation7 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.string("1")))])
        let operation8 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.string("true")))])
        let operation9 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context1)
        
        array.append(operation1.evaluate(in: view))
        array.append(operation2.evaluate(in: view))
        array.append(operation3.evaluate(in: view))
        array.append(operation4.evaluate(in: view))
        array.append(operation5.evaluate(in: view))
        array.append(operation6.evaluate(in: view))
        array.append(operation7.evaluate(in: view))
        array.append(operation8.evaluate(in: view))
        array.append(operation9.evaluate(in: view))
        
        // Then
        completion(array)
    }
}
