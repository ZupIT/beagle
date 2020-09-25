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

final class GlobalContextTests: XCTestCase {
    
    private let globalId = "global"
    
    private var view1 = UIView()
    private var view2 = UIView()
    
    private lazy var globalContext1 = Context(id: globalId, value: "First value")
    private lazy var globalContext2 = Context(id: globalId, value: "Second value")
    
    private lazy var globalContextWithDictionary = Context(id: globalId, value: .dictionary([globalId: "Dictionary value"]))
    
    func testGetContext() {
        let globalContext = dependencies.globalContext
        
        view1.setContext(globalContextWithDictionary)
        var globalContextValue = globalContext.get(path: globalId)
        
        XCTAssertEqual(view1.getContext(with: globalId)?.value.value, globalContextWithDictionary.value)
        
        view2.setContext(globalContext2)
        globalContextValue = globalContext.get(path: nil)
        
        XCTAssertEqual(view1.getContext(with: globalId)?.value.value, globalContextValue)
    }
    
    func testSetContextInViewWithGlobalId() {
        XCTAssertTrue(view1.contextMap.isEmpty)
        XCTAssertTrue(view2.contextMap.isEmpty)
        
        view1.setContext(globalContext1)
        view2.setContext(globalContext2)
        
        XCTAssertTrue(view2.contextMap.isEmpty)
        XCTAssertTrue(view1.contextMap.isEmpty)
    }
    
    func testSetContext() {
        let globalContext = dependencies.globalContext
        
        globalContext.set(value: globalContext1.value, path: globalId)
        view1.setContext(globalContext1)
        XCTAssertEqual(view1.getContext(with: globalId)?.value, globalContext1)
    
        globalContext.set(value: globalContext2.value, path: nil)
        XCTAssertEqual(view1.getContext(with: globalId)?.value, globalContext2)
    }
    
    func testClearContext() {
        let globalContext = dependencies.globalContext
        
        globalContext.set(value: globalContext1.value, path: nil)
        XCTAssertEqual(view1.getContext(with: globalId)?.value, globalContext1)
        
        globalContext.clear(path: nil)
        XCTAssertEqual(view1.getContext(with: globalId)?.value.value, DynamicObject.empty)

    }
    
}
