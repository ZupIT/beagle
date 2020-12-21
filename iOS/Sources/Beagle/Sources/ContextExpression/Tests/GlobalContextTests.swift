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

import XCTest
@testable import Beagle

final class GlobalContextTests: XCTestCase {
    
    private let globalId = "global"
    
    private var view1 = UIView()
    private var view2 = UIView()
    
    private lazy var globalContext1 = Context(id: globalId, value: "First value")
    private lazy var globalContext2 = Context(id: globalId, value: "Second value")
    
    private lazy var globalContextWithDictionary = Context(id: globalId, value: .dictionary([globalId: "First value"]))
    
    func testGetContext() {
        // Given
        // view1
        let globalContext = dependencies.globalContext
        let globalContextValue1 = Context(id: globalId, value: globalContext.get(path: globalId))
        
        // When/Then
        view1.setContext(globalContextValue1)
        XCTAssertEqual(view1.getContext(with: globalId)?.value, globalContextValue1)
        
        view2.setContext(globalContext2)
        let globalContextValue = globalContext.get(path: nil)
        XCTAssertEqual(view1.getContext(with: globalId)?.value.value, globalContextValue)
    }
    
    func testSetContextInViewWithGlobalId() {
        // Given
        // view1, view2
        
        // When/Then
        XCTAssertTrue(view1.contextMap.isEmpty)
        view1.setContext(globalContext1)
        XCTAssertTrue(view1.contextMap.isEmpty)
        XCTAssertEqual(dependencies.globalContext.get(path: nil), globalContext1.value)
        
        XCTAssertTrue(view2.contextMap.isEmpty)
        view2.setContext(globalContext2)
        XCTAssertTrue(view2.contextMap.isEmpty)
        XCTAssertEqual(dependencies.globalContext.get(path: nil), globalContext2.value)
    }
    
    func testSetContext() {
        // Given
        let global: GlobalContext = DefaultGlobalContext()
        
        // When/Then
        global.set(["a": "value a"])
        XCTAssertEqual(global.get(), ["a": "value a"])
        
        global.set(value: "value b", path: "b.object")
        XCTAssertEqual(global.get(path: "b"), ["object": "value b"])
        
        global.set(value: "update a", path: "a")
        XCTAssertEqual(global.get(path: "a"), "update a")
    }
    
    func testClearContext() {
        // Given
        let global: GlobalContext = DefaultGlobalContext()
        global.set(["a": "value a", "b": "value b"])
        
        // When/Then
        global.clear(path: "a")
        XCTAssertEqual(global.get(), ["a": .empty, "b": "value b"])
        
        global.clear(path: nil)
        XCTAssertEqual(global.get(), .empty)
    }
}
