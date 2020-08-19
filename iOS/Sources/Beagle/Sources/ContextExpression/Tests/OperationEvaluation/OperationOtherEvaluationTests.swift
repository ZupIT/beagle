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
final class OperationOtherEvaluationTests: XCTestCase {

    func testEvaluateIsNull() {
        // Given
        let view = UIView()
        let context = Context(id: "context", value: .array([.int(1), .int(2), .int(3)]))
        let binding = Binding(context: context.id, path: Path(nodes: []))
        let insert = Operation(name: .insert, parameters: [])
        let name = BeagleSchema.Operation.Name.isNull
        
        let isNull1 = Operation(name: name, parameters: [.value(.literal(.null))])
        let isNull2 = Operation(name: name, parameters: [.value(.literal(.int(1)))])
        let isNull3 = Operation(name: name, parameters: [.value(.binding(binding))])
        let isNull4 = Operation(name: name, parameters: [.operation(isNull1)])
        let isNull5 = Operation(name: name, parameters: [.operation(insert)])
        let isNull6 = Operation(name: name, parameters: [.value(.literal(.null)), .value(.literal(.null))])
        let isNull7 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context)
        
        let result1 = isNull1.evaluate(in: view)
        let result2 = isNull2.evaluate(in: view)
        let result3 = isNull3.evaluate(in: view)
        let result4 = isNull4.evaluate(in: view)
        let result5 = isNull5.evaluate(in: view)
        let result6 = isNull6.evaluate(in: view)
        let result7 = isNull7.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.bool(true))
        XCTAssertEqual(result2, DynamicObject.bool(false))
        XCTAssertEqual(result3, DynamicObject.bool(false))
        XCTAssertEqual(result4, DynamicObject.bool(false))
        XCTAssertEqual(result5, DynamicObject.bool(true))
        XCTAssertEqual(result6, DynamicObject.empty)
        XCTAssertEqual(result7, DynamicObject.empty)
    }
    
    func testEvaluateIsEmpty() {
        // Given
        let view = UIView()
        let context1 = Context(id: "context1", value: .array([.int(1)]))
        let binding1 = Binding(context: context1.id, path: Path(nodes: []))
        let context2 = Context(id: "context2", value: .dictionary(["one": .int(1), "two": .int(2), "three": .int(3)]))
        let binding2 = Binding(context: context2.id, path: Path(nodes: []))
        let removeIndex = Operation(name: .removeIndex, parameters: [.value(.binding(binding1))])
        let name = BeagleSchema.Operation.Name.isEmpty
        
        let isEmpty1 = Operation(name: name, parameters: [.value(.literal(.string("")))])
        let isEmpty2 = Operation(name: name, parameters: [.value(.binding(binding1))])
        let isEmpty3 = Operation(name: name, parameters: [.value(.binding(binding2))])
        let isEmpty4 = Operation(name: name, parameters: [.operation(removeIndex)])
        let isEmpty5 = Operation(name: name, parameters: [.value(.literal(.int(1)))])
        let isEmpty6 = Operation(name: name, parameters: [.value(.literal(.string(""))), .value(.literal(.string("")))])
        let isEmpty7 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context1)
        view.setContext(context2)
        
        let result1 = isEmpty1.evaluate(in: view)
        let result2 = isEmpty2.evaluate(in: view)
        let result3 = isEmpty3.evaluate(in: view)
        let result4 = isEmpty4.evaluate(in: view)
        let result5 = isEmpty5.evaluate(in: view)
        let result6 = isEmpty6.evaluate(in: view)
        let result7 = isEmpty7.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.bool(true))
        XCTAssertEqual(result2, DynamicObject.bool(false))
        XCTAssertEqual(result3, DynamicObject.bool(false))
        XCTAssertEqual(result4, DynamicObject.bool(true))
        XCTAssertEqual(result5, DynamicObject.empty)
        XCTAssertEqual(result6, DynamicObject.empty)
        XCTAssertEqual(result7, DynamicObject.empty)
    }
    
    func testEvaluateLength() {
        // Given
        let view = UIView()
        let context1 = Context(id: "context1", value: .array([.int(1)]))
        let binding1 = Binding(context: context1.id, path: Path(nodes: []))
        let context2 = Context(id: "context2", value: .dictionary(["one": .int(1), "two": .int(2), "three": .int(3)]))
        let binding2 = Binding(context: context2.id, path: Path(nodes: []))
        let removeIndex = Operation(name: .removeIndex, parameters: [.value(.binding(binding1))])
        let name = BeagleSchema.Operation.Name.length
        
        let length1 = Operation(name: name, parameters: [.value(.literal(.string("string")))])
        let length2 = Operation(name: name, parameters: [.value(.binding(binding1))])
        let length3 = Operation(name: name, parameters: [.value(.binding(binding2))])
        let length4 = Operation(name: name, parameters: [.operation(removeIndex)])
        let length5 = Operation(name: name, parameters: [.value(.literal(.int(1)))])
        let length6 = Operation(name: name, parameters: [.value(.literal(.string(""))), .value(.literal(.string("")))])
        let length7 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context1)
        view.setContext(context2)
        
        let result1 = length1.evaluate(in: view)
        let result2 = length2.evaluate(in: view)
        let result3 = length3.evaluate(in: view)
        let result4 = length4.evaluate(in: view)
        let result5 = length5.evaluate(in: view)
        let result6 = length6.evaluate(in: view)
        let result7 = length7.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.int(6))
        XCTAssertEqual(result2, DynamicObject.int(1))
        XCTAssertEqual(result3, DynamicObject.int(3))
        XCTAssertEqual(result4, DynamicObject.int(0))
        XCTAssertEqual(result5, DynamicObject.empty)
        XCTAssertEqual(result6, DynamicObject.empty)
        XCTAssertEqual(result7, DynamicObject.empty)
    }

}
