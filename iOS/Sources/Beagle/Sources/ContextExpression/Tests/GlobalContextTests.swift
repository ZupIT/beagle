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
    
    func testGetContext() {
        let globalId = "global"
        
        let view1 = UIView()
        let view2 = UIView()
        
        let context1 = Context(id: globalId, value: "Fist value")
        let context2 = Context(id: globalId, value: "Second value")
        
        XCTAssertNil(view1.contextMap)
        view1.setContext(context1)
        assertSnapshot(matching: view1.contextMap, as: .dump)
        XCTAssertNotNil(view1.contextMap)
        
        if let view1ContextMapValue = view1.getContext(with: globalId)?.value,
            let globalContextMapValue = GlobalContext.global.getContext(with: globalId)?.value {
            XCTAssertTrue(view1ContextMapValue == globalContextMapValue)
        } else {
            XCTFail("Could not get Global context correctly.")
        }
        
        XCTAssertNil(view2.contextMap)
        view2.setContext(context2)
        assertSnapshot(matching: view2.contextMap, as: .dump)
        XCTAssertNotNil(view2.contextMap)
        
        if let view2ContextMapValue = view2.getContext(with: globalId)?.value,
            let globalContextMapValue = GlobalContext.global.getContext(with: globalId)?.value {
            XCTAssertTrue(view2ContextMapValue == globalContextMapValue)
        } else {
            XCTFail("Could not get Global context correctly.")
        }
        
        XCTAssertNil(GlobalContext.global.getContext(with: "unknown"))
    }
    
    func testSetContext() {
        let globalId = "global"
        
        let view1 = UIView()
        let view2 = UIView()
        
        let context1 = Context(id: globalId, value: "Fist value")
        let context2 = Context(id: globalId, value: "Second value")
        
        GlobalContext.global.setContext(context1)
        
        XCTAssertTrue(view1.getContext(with: globalId)?.value == context1)
        XCTAssertTrue(view2.getContext(with: globalId)?.value == context1)
        
        GlobalContext.global.setContext(context2)
        
        XCTAssertTrue(view1.getContext(with: globalId)?.value == context2)
        XCTAssertTrue(view2.getContext(with: globalId)?.value == context2)
    }
    
}
