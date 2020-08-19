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

class OperationArrayEvaluationTests: XCTestCase {

    func testEvaluateInsert() {
        // Given
        let view = UIView()
        let context = Context(id: "context", value: .array([.int(1), .int(2), .int(3)]))
        let binding = Binding(context: context.id, path: Path(nodes: []))
        let name = BeagleSchema.Operation.Name.insert
        
        let insert1 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(3))), .value(.literal(.int(2)))])
        let insert2 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.double(3.0))), .value(.literal(.int(1)))])
        let insert3 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(0)))])
        let insert4 = Operation(name: name, parameters: [.operation(insert1), .value(.literal(.int(4)))])
        let insert5 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(3))), .value(.literal(.double(2.0)))])
        let insert6 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(3))), .value(.literal(.int(5)))])
        let insert7 = Operation(name: name, parameters: [.value(.literal(.string("array"))), .value(.literal(.string(""))), .value(.literal(.int(0)))])
        let insert8 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context)
        
        let result1 = insert1.evaluate(in: view)
        let result2 = insert2.evaluate(in: view)
        let result3 = insert3.evaluate(in: view)
        let result4 = insert4.evaluate(in: view)
        let result5 = insert5.evaluate(in: view)
        let result6 = insert6.evaluate(in: view)
        let result7 = insert7.evaluate(in: view)
        let result8 = insert8.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.array([.int(1), .int(2), .int(3), .int(3)]))
        XCTAssertEqual(result2, DynamicObject.array([.int(1), .double(3.0), .int(2), .int(3)]))
        XCTAssertEqual(result3, DynamicObject.array([.int(1), .int(2), .int(3), .int(0)]))
        XCTAssertEqual(result4, DynamicObject.array([.int(1), .int(2), .int(3), .int(3), .int(4)]))
        XCTAssertEqual(result5, DynamicObject.empty)
        XCTAssertEqual(result6, DynamicObject.empty)
        XCTAssertEqual(result7, DynamicObject.empty)
        XCTAssertEqual(result8, DynamicObject.empty)
    }
    
    func testEvaluateRemove() {
        // Given
        let view = UIView()
        let context = Context(id: "context", value: .array([.int(1), .int(2), .double(3.0), .double(3.0)]))
        let binding = Binding(context: context.id, path: Path(nodes: []))
        let name = BeagleSchema.Operation.Name.remove
        
        let remove1 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(2)))])
        let remove2 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.double(3.0)))])
        let remove3 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(4)))])
        let remove4 = Operation(name: name, parameters: [.operation(remove1), .value(.literal(.int(1)))])
        let remove5 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(2))), .value(.literal(.double(3.0)))])
        let remove6 = Operation(name: name, parameters: [.value(.literal(.string("array"))), .value(.literal(.string(""))), .value(.literal(.int(0)))])
        let remove7 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context)
        
        let result1 = remove1.evaluate(in: view)
        let result2 = remove2.evaluate(in: view)
        let result3 = remove3.evaluate(in: view)
        let result4 = remove4.evaluate(in: view)
        let result5 = remove5.evaluate(in: view)
        let result6 = remove6.evaluate(in: view)
        let result7 = remove7.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.array([.int(1), .double(3.0), .double(3.0)]))
        XCTAssertEqual(result2, DynamicObject.array([.int(1), .int(2)]))
        XCTAssertEqual(result3, DynamicObject.array([.int(1), .int(2), .double(3.0), .double(3.0)]))
        XCTAssertEqual(result4, DynamicObject.array([.double(3.0), .double(3.0)]))
        XCTAssertEqual(result5, DynamicObject.empty)
        XCTAssertEqual(result6, DynamicObject.empty)
        XCTAssertEqual(result7, DynamicObject.empty)
    }
    
    func testEvaluateRemoveIndex() {
        // Given
        let view = UIView()
        let context = Context(id: "context", value: .array([.int(1), .int(2), .double(3.0), .double(3.0)]))
        let binding = Binding(context: context.id, path: Path(nodes: []))
        let name = BeagleSchema.Operation.Name.removeIndex
        
        let removeIndex1 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(1)))])
        let removeIndex2 = Operation(name: name, parameters: [.value(.binding(binding))])
        let removeIndex3 = Operation(name: name, parameters: [.operation(removeIndex1), .value(.literal(.int(0)))])
        let removeIndex4 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(4)))])
        let removeIndex5 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.double(3.0)))])
        let removeIndex6 = Operation(name: name, parameters: [.value(.literal(.string("array"))), .value(.literal(.int(0)))])
        let removeIndex7 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context)
        
        let result1 = removeIndex1.evaluate(in: view)
        let result2 = removeIndex2.evaluate(in: view)
        let result3 = removeIndex3.evaluate(in: view)
        let result4 = removeIndex4.evaluate(in: view)
        let result5 = removeIndex5.evaluate(in: view)
        let result6 = removeIndex6.evaluate(in: view)
        let result7 = removeIndex7.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.array([.int(1), .double(3.0), .double(3.0)]))
        XCTAssertEqual(result2, DynamicObject.array([.int(1), .int(2), .double(3.0)]))
        XCTAssertEqual(result3, DynamicObject.array([.double(3.0), .double(3.0)]))
        XCTAssertEqual(result4, DynamicObject.empty)
        XCTAssertEqual(result5, DynamicObject.empty)
        XCTAssertEqual(result6, DynamicObject.empty)
        XCTAssertEqual(result7, DynamicObject.empty)
    }
    
    func testEvaluateIncludes() {
        // Given
        let view = UIView()
        let context = Context(id: "context", value: .array([.int(1), .int(2), .int(3)]))
        let binding = Binding(context: context.id, path: Path(nodes: []))
        let insert = Operation(name: .insert, parameters: [.value(.binding(binding)), .value(.literal(.int(4))), .value(.literal(.int(2)))])
        let name = BeagleSchema.Operation.Name.includes
        
        let includes1 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(3)))])
        let includes2 = Operation(name: name, parameters: [.value(.binding(binding)), .value(.literal(.int(4)))])
        let includes3 = Operation(name: name, parameters: [.operation(insert), .value(.literal(.int(4)))])
        let includes4 = Operation(name: name, parameters: [.value(.binding(binding))])
        let includes5 = Operation(name: name, parameters: [.value(.literal(.string("array"))), .value(.literal(.int(0)))])
        let includes6 = Operation(name: name, parameters: [])
        
        // When
        view.setContext(context)
        
        let result1 = includes1.evaluate(in: view)
        let result2 = includes2.evaluate(in: view)
        let result3 = includes3.evaluate(in: view)
        let result4 = includes4.evaluate(in: view)
        let result5 = includes5.evaluate(in: view)
        let result6 = includes6.evaluate(in: view)
        
        // Then
        XCTAssertEqual(result1, DynamicObject.bool(true))
        XCTAssertEqual(result2, DynamicObject.bool(false))
        XCTAssertEqual(result3, DynamicObject.bool(true))
        XCTAssertEqual(result4, DynamicObject.empty)
        XCTAssertEqual(result5, DynamicObject.empty)
        XCTAssertEqual(result6, DynamicObject.empty)
    }

}
