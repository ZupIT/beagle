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

class OperationStringEvaluationTests: XCTestCase {
    
    func testEvaluateConcat() {
        // Given
        let view = UIView()
        let context = Context(id: "context", value: .string("Lastname"))
        let binding = Binding(context: context.id, path: Path(nodes: []))
        let name = BeagleSchema.Operation.Name.concat
        
        let concat1 = Operation(name: name, parameters: [.value(.literal(.string("string"))), .value(.literal(.string("STRING")))])
        let concat2 = Operation(name: name, parameters: [.value(.literal(.string("Name"))), .value(.binding(binding))])
        let concat3 = Operation(name: name, parameters: [.value(.literal(.string("StRiNg"))), .operation(concat1)])
        let concat4 = Operation(name: name, parameters: [.operation(concat3), .value(.literal(.string(" "))), .value(.binding(binding))])
        let concat5 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.int(0)))])
        let concat6 = Operation(name: name, parameters: [.value(.literal(.string("1"))), .value(.literal(.int(1)))])
        let concat7 = Operation(name: name, parameters: [.value(.literal(.bool(true))), .value(.literal(.string("false")))])
        let concat8 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context)
        
        let result1 = concat1.evaluate(in: view)
        let result2 = concat2.evaluate(in: view)
        let result3 = concat3.evaluate(in: view)
        let result4 = concat4.evaluate(in: view)
        let result5 = concat5.evaluate(in: view)
        let result6 = concat6.evaluate(in: view)
        let result7 = concat7.evaluate(in: view)
        let result8 = concat8.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.string("stringSTRING"))
        XCTAssertEqual(result2, DynamicObject.string("NameLastname"))
        XCTAssertEqual(result3, DynamicObject.string("StRiNgstringSTRING"))
        XCTAssertEqual(result4, DynamicObject.string("StRiNgstringSTRING Lastname"))
        XCTAssertEqual(result5, DynamicObject.empty)
        XCTAssertEqual(result6, DynamicObject.empty)
        XCTAssertEqual(result7, DynamicObject.empty)
        XCTAssertEqual(result8, DynamicObject.empty)
    }
    
    func testEvaluateCapitalize() {
        // Given
        // When
        evaluateOperation(.capitalize) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.string("String"))
            XCTAssertEqual(array[1], DynamicObject.string("String"))
            XCTAssertEqual(array[2], DynamicObject.string("Name"))
            XCTAssertEqual(array[3], DynamicObject.string("Lastname"))
            XCTAssertEqual(array[4], DynamicObject.string("Name Lastname"))
            XCTAssertEqual(array[5], DynamicObject.empty)
            XCTAssertEqual(array[6], DynamicObject.empty)
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
        }
    }
    
    func testEvaluateUppercase() {
        // Given
        // When
        evaluateOperation(.uppercase) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.string("STRING"))
            XCTAssertEqual(array[1], DynamicObject.string("STRING"))
            XCTAssertEqual(array[2], DynamicObject.string("NAME"))
            XCTAssertEqual(array[3], DynamicObject.string("LASTNAME"))
            XCTAssertEqual(array[4], DynamicObject.string("NAME LASTNAME"))
            XCTAssertEqual(array[5], DynamicObject.empty)
            XCTAssertEqual(array[6], DynamicObject.empty)
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
        }
    }
    
    func testEvaluateLowercase() {
        // Given
        // When
        evaluateOperation(.lowercase) { array in
            // Then
            XCTAssertEqual(array[0], DynamicObject.string("string"))
            XCTAssertEqual(array[1], DynamicObject.string("string"))
            XCTAssertEqual(array[2], DynamicObject.string("name"))
            XCTAssertEqual(array[3], DynamicObject.string("lastname"))
            XCTAssertEqual(array[4], DynamicObject.string("name lastname"))
            XCTAssertEqual(array[5], DynamicObject.empty)
            XCTAssertEqual(array[6], DynamicObject.empty)
            XCTAssertEqual(array[7], DynamicObject.empty)
            XCTAssertEqual(array[8], DynamicObject.empty)
        }
    }
    
    func testEvaluateSubstr() {
        // Given
        let view = UIView()
        let context = Context(id: "context", value: .string("some long string"))
        let binding = Binding(context: context.id, path: Path(nodes: []))
        let name = BeagleSchema.Operation.Name.substr
        
        let substr1 = Operation(name: name, parameters: [.value(.literal(.string("string"))), .value(.literal(.int(0))), .value(.literal(.int(3)))])
        let substr2 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(0))), .value(.literal(.int(4)))])
        let substr3 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(5))), .value(.literal(.int(4)))])
        let substr4 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(5)))])
        let substr5 = Operation(name: name, parameters: [.operation(substr4), .value(.literal(.int(5)))])
        let substr6 = Operation(name: name, parameters: [.value(.literal(.string("str"))), .value(.literal(.int(0))), .value(.literal(.int(0)))])
        let substr7 = Operation(name: name, parameters: [.value(.literal(.string("str"))), .value(.literal(.int(-1))), .value(.literal(.int(1)))])
        let substr8 = Operation(name: name, parameters: [.value(.literal(.string("str"))), .value(.literal(.int(0))), .value(.literal(.int(5)))])
        let substr9 = Operation(name: name, parameters: [.value(.literal(.string("str"))), .value(.literal(.int(3))), .value(.literal(.int(2)))])
        let substr10 = Operation(name: name, parameters: [.value(.literal(.int(1))), .value(.literal(.int(0)))])
        let substr11 = Operation(name: name, parameters: [.value(.literal(.string("1"))), .value(.literal(.double(0.0)))])
        let substr12 = Operation(name: name, parameters: [.value(.literal(.string("1"))), .value(.literal(.int(0))), .value(.literal(.double(1.0)))])
        let substr13 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context)
        
        let result1 = substr1.evaluate(in: view)
        let result2 = substr2.evaluate(in: view)
        let result3 = substr3.evaluate(in: view)
        let result4 = substr4.evaluate(in: view)
        let result5 = substr5.evaluate(in: view)
        let result6 = substr6.evaluate(in: view)
        let result7 = substr7.evaluate(in: view)
        let result8 = substr8.evaluate(in: view)
        let result9 = substr9.evaluate(in: view)
        let result10 = substr10.evaluate(in: view)
        let result11 = substr11.evaluate(in: view)
        let result12 = substr12.evaluate(in: view)
        let result13 = substr13.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.string("str"))
        XCTAssertEqual(result2, DynamicObject.string("some"))
        XCTAssertEqual(result3, DynamicObject.string("long"))
        XCTAssertEqual(result4, DynamicObject.string("long string"))
        XCTAssertEqual(result5, DynamicObject.string("string"))
        XCTAssertEqual(result6, DynamicObject.string(""))
        XCTAssertEqual(result7, DynamicObject.empty)
        XCTAssertEqual(result8, DynamicObject.empty)
        XCTAssertEqual(result9, DynamicObject.empty)
        XCTAssertEqual(result10, DynamicObject.empty)
        XCTAssertEqual(result11, DynamicObject.empty)
        XCTAssertEqual(result12, DynamicObject.empty)
        XCTAssertEqual(result13, DynamicObject.empty)
    }
    
    private func evaluateOperation(_ name: BeagleSchema.Operation.Name, completion: ([DynamicObject]) -> Void) {
        // Given
        var array: [DynamicObject] = []
        let view = UIView()
        let context1 = Context(id: "context1", value: .string("name"))
        let binding1 = Binding(context: context1.id, path: Path(nodes: []))
        let context2 = Context(id: "context2", value: .string("Lastname"))
        let binding2 = Binding(context: context2.id, path: Path(nodes: []))
        let concat1 = Operation(name: .concat, parameters: [.value(.binding(binding1)), .value(.literal(.string(" "))), .value(.binding(binding2))])
        
        let operation1 = Operation(name: name, parameters: [.value(.literal(.string("string")))])
        let operation2 = Operation(name: name, parameters: [.value(.literal(.string("String")))])
        let operation3 = Operation(name: name, parameters: [.value(.binding(binding1))])
        let operation4 = Operation(name: name, parameters: [.value(.binding(binding2))])
        let operation5 = Operation(name: name, parameters: [.operation(concat1)])
        let operation6 = Operation(name: name, parameters: [.value(.literal(.int(1)))])
        let operation7 = Operation(name: name, parameters: [.value(.literal(.bool(true)))])
        let operation8 = Operation(name: name, parameters: [.value(.literal(.string("0"))), .value(.literal(.string("1")))])
        let operation9 = Operation(name: name, parameters: [])
        
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
        
        // Then
        completion(array)
    }
}
