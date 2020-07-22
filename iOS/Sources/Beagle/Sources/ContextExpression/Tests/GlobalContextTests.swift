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

class GlobalContextTests: XCTestCase {
    private static let globalId = "global"
    
    var view1 = UIView()
    var view2 = UIView()
    
    var globalContext1 = Context(id: globalId, value: "")
    var globalContext2 = Context(id: globalId, value: "")
    
    override func setUp() {
        super.setUp()
        
        GlobalContext.global.clear()
        
        view1 = UIView()
        view2 = UIView()
        
        globalContext1 = Context(id: Self.globalId, value: "Fist value")
        globalContext2 = Context(id: Self.globalId, value: "Second value")
    }
    
    func testClear() {
        GlobalContext.global.setContextValue(globalContext1.value)
        XCTAssertNotNil(GlobalContext.global.contextObservable)
        GlobalContext.global.clear()
        XCTAssertNil(GlobalContext.global.contextObservable)
    }
    
    func testGetContext() {
        view1.setContext(globalContext1)
        var globalContextValue = GlobalContext.global.getContext()?.value
        
        XCTAssertEqual(view1.getContext(with: Self.globalId)?.value, globalContextValue)
        XCTAssertEqual(view2.getContext(with: Self.globalId)?.value, globalContextValue)
        
        view2.setContext(globalContext2)
        globalContextValue = GlobalContext.global.getContext()?.value
        
        XCTAssertEqual(view1.getContext(with: Self.globalId)?.value, globalContextValue)
        XCTAssertEqual(view2.getContext(with: Self.globalId)?.value, globalContextValue)
    }
    
    func testSetContextInViewWithGlobalId() {
        XCTAssertNil(view1.contextMap)
        XCTAssertNil(view2.contextMap)
        
        view1.setContext(globalContext1)
        view2.setContext(globalContext2)
        
        XCTAssertNil(view2.contextMap)
        XCTAssertNil(view1.contextMap)
    }
    
    func testSetContext() {
        GlobalContext.global.setContextValue(globalContext1.value)
        
        XCTAssertEqual(view1.getContext(with: Self.globalId)?.value, globalContext1)
        XCTAssertEqual(view2.getContext(with: Self.globalId)?.value, globalContext1)
        
        GlobalContext.global.setContextValue(globalContext2.value)
        
        XCTAssertEqual(view1.getContext(with: Self.globalId)?.value, globalContext2)
        XCTAssertEqual(view2.getContext(with: Self.globalId)?.value, globalContext2)
    }
    
    func testGlobalContextObservableNotEmptyAfterSet() {
        XCTAssertNil(GlobalContext.global.contextObservable)
        GlobalContext.global.setContextValue(globalContext1.value)
        XCTAssertNotNil(GlobalContext.global.contextObservable)
    }
    
}
