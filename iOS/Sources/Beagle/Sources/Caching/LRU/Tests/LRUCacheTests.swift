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

class LRUCacheTests: XCTestCase {
    
    lazy var sut = CacheLRU<String, String>(capacity: 2)
    let key1 = "key1"
    let key2 = "key2"
    let key3 = "key3"
    let val1 = "val1"
    let val2 = "val2"
    let val3 = "val3"
    
    func testLRUInsert() {
        sut.setValue(val1, for: key1)
        
        XCTAssert(sut.count == 1)
        XCTAssert(sut.getValue(for: key1) == val1)
    }
    
    func testLRUClear() {
        sut.setValue(val1, for: key1)
        sut.setValue(val2, for: key2)
        
        sut.clear()
        
        XCTAssert(sut.count == 0)
        XCTAssert(sut.getValue(for: key1) == nil)
        XCTAssert(sut.getValue(for: key2) == nil)
    }
    
    func testLRUDump() {
        sut.setValue(val1, for: key1)
        sut.setValue(val2, for: key2)
        sut.setValue(val3, for: key3)
        
        XCTAssert(sut.count == 2)
        XCTAssert(sut.getValue(for: key1) == nil)
        XCTAssert(sut.getValue(for: key2) == val2)
        XCTAssert(sut.getValue(for: key3) == val3)
    }
    
    func testUpdateValue() {
        let val11 = "val1.1"
        sut.setValue(val1, for: key1)
        sut.setValue(val2, for: key2)
        
        sut.setValue(val11, for: key1)
        
        XCTAssert(sut.count == 2)
        XCTAssert(sut.getValue(for: key1) == val11)
        XCTAssert(sut.getValue(for: key2) == val2)
    }
    
    func testRefreshElementUsage() {
        let val11 = "val1.1"
        sut.setValue(val1, for: key1)
        sut.setValue(val2, for: key2)
        
        sut.setValue(val11, for: key1)
        sut.setValue(val3, for: key3)
        
        XCTAssert(sut.count == 2)
        XCTAssert(sut.getValue(for: key1) == val11)
        XCTAssert(sut.getValue(for: key2) == nil)
        XCTAssert(sut.getValue(for: key3) == val3)
    }
}
