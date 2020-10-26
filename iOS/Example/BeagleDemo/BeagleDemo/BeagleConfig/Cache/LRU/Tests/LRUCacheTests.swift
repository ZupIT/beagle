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
@testable import BeagleDemo

class LRUCacheTests: XCTestCase {
    
    lazy var sut = CacheLRU<String, String>(capacity: 2)
    let key1 = "key1"
    let key2 = "key2"
    let key3 = "key3"
    let val1 = "val1"
    let val2 = "val2"
    let val3 = "val3"
    
    func testLRUInsert() {
        //Given //When
        sut.setValue(val1, for: key1)
        
        //Then
        XCTAssertEqual(sut.count, 1)
        XCTAssertEqual(sut.getValue(for: key1), val1)
    }
    
    func testLRUClear() {
        //Given
        sut.setValue(val1, for: key1)
        sut.setValue(val2, for: key2)
        
        //When
        sut.clear()
        
        //Then
        XCTAssertEqual(sut.count, 0)
        XCTAssertNil(sut.getValue(for: key1))
        XCTAssertNil(sut.getValue(for: key2))
    }
    
    func testLRUDump() {
        //Given //When
        sut.setValue(val1, for: key1)
        sut.setValue(val2, for: key2)
        sut.setValue(val3, for: key3)
        
        //Then
        XCTAssertEqual(sut.count, 2)
        XCTAssertNil(sut.getValue(for: key1))
        XCTAssertEqual(sut.getValue(for: key2), val2)
        XCTAssertEqual(sut.getValue(for: key3), val3)
    }
    
    func testUpdateValue() {
        //Given //When
        let val11 = "val1.1"
        sut.setValue(val1, for: key1)
        sut.setValue(val2, for: key2)
        sut.setValue(val11, for: key1)
        
        //Then
        XCTAssertEqual(sut.count, 2)
        XCTAssertEqual(sut.getValue(for: key1), val11)
        XCTAssertEqual(sut.getValue(for: key2), val2)
    }
    
    func testRefreshElementUsage() {
        //Given //When
        let val11 = "val1.1"
        sut.setValue(val1, for: key1)
        sut.setValue(val2, for: key2)
        sut.setValue(val11, for: key1)
        sut.setValue(val3, for: key3)
        
        //Then
        XCTAssertEqual(sut.count, 2)
        XCTAssertEqual(sut.getValue(for: key1), val11)
        XCTAssertNil(sut.getValue(for: key2))
        XCTAssertEqual(sut.getValue(for: key3), val3)
    }
}
