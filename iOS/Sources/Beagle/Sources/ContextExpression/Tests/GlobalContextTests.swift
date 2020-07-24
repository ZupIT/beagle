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
    
    private lazy var globalContext1 = Context(id: globalId, value: "Fist value")
    private lazy var globalContext2 = Context(id: globalId, value: "Second value")
    
    func testGetContext() {
        let globalContext = dependencies.globalContext
        
        view1.setContext(globalContext1)
        var globalContextValue = globalContext.context.value
        
        XCTAssertEqual(view1.getContext(with: globalId)?.value, globalContextValue)
        XCTAssertEqual(view2.getContext(with: globalId)?.value, globalContextValue)
        
        view2.setContext(globalContext2)
        globalContextValue = globalContext.context.value
        
        XCTAssertEqual(view1.getContext(with: globalId)?.value, globalContextValue)
        XCTAssertEqual(view2.getContext(with: globalId)?.value, globalContextValue)
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
        let globalContext = dependencies.globalContext
        
        globalContext.setValue(globalContext1.value)
        
        XCTAssertEqual(view1.getContext(with: globalId)?.value, globalContext1)
        XCTAssertEqual(view2.getContext(with: globalId)?.value, globalContext1)
        
        globalContext.setValue(globalContext2.value)
        
        XCTAssertEqual(view1.getContext(with: globalId)?.value, globalContext2)
        XCTAssertEqual(view2.getContext(with: globalId)?.value, globalContext2)
    }
    
}
