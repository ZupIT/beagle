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
import Beagle
@testable import BeagleDemo

class CacheDiskManagerTests: XCTestCase {
    // swiftlint:disable force_unwrapping
    private let jsonData = """
    {
      "_beagleType_": "beagle:component:text",
      "text": "cache",
      "style": {
        "backgroundColor": "#4000FFFF"
      }
    }
    """.data(using: .utf8)!
    // swiftlint:enable force_unwrapping
    
    func testGetReference() {
        //Given
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let hash = "1"
        let identifier = "id"
        let reference = CacheReference(identifier: identifier, data: jsonData, hash: hash, timeOfCreation: generateTimeOfCreation())
        
        //When
        sut.clear()
        sut.update(reference)
        let result = sut.getReference(for: identifier)
        
        //Then
        XCTAssertEqual(result?.data, jsonData, "Got wrong data from cache.")
        XCTAssertEqual(result?.hash, hash, "Got wrong data from cache.")
        XCTAssertEqual(result?.identifier, identifier, "Got wrong data from cache.")
    }
    
    func testGetNilReference() {
        //Given //When
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        sut.clear()
        sut.saveChanges()
        let identifier = "id"
        
        //Then
        XCTAssertNil(sut.getReference(for: identifier))
    }
    
    func testClear() {
        //Given
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let identifier = "id"
        let hash = "1"
        let reference = CacheReference(identifier: identifier, data: jsonData, hash: hash)
        
        //When
        sut.update(reference)
        sut.saveChanges()
        sut.clear()
        
        //Then
        XCTAssertNil(sut.getReference(for: identifier))
    }
    
    func testRemoveLastUsed() {
        //Given
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let identifier1 = "id1"
        let hash1 = "1"
        let identifier2 = "id2"
        let hash2 = "2"
        let reference1 = CacheReference(identifier: identifier1, data: jsonData, hash: hash1)
        let reference2 = CacheReference(identifier: identifier2, data: jsonData, hash: hash2)
        
        //When
        sut.clear()
        sut.update(reference1)
        sut.update(reference2)
        sut.removeLastUsed()
        sut.saveChanges()
        
        //Then
        
       XCTAssertNil(sut.getReference(for: identifier1))
    }
    
    func testRemoveLastUsedUpdated() {
        //Given
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let identifier1 = "id1"
        let hash1 = "1"
        let identifier2 = "id2"
        let hash2 = "2"
        let hash3 = "3"
        let reference1 = CacheReference(identifier: identifier1, data: jsonData, hash: hash1)
        let reference2 = CacheReference(identifier: identifier2, data: jsonData, hash: hash2)
        let reference3 = CacheReference(identifier: identifier1, data: jsonData, hash: hash3)
        
        //When
        sut.clear()
        sut.update(reference1)
        sut.update(reference2)
        sut.update(reference3)
        sut.removeLastUsed()
        sut.saveChanges()
        
        //Then
        XCTAssertNil(sut.getReference(for: identifier2))
    }
    
    func testInsertNewValue() {
        //Given
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let identifier = "id"
        let hash = "1"
        let reference = CacheReference(identifier: identifier, data: jsonData, hash: hash, timeOfCreation: generateTimeOfCreation())
        
        //When
        sut.update(reference)
        let result = sut.getReference(for: identifier)
        
        //Then
        XCTAssertEqual(result?.data, jsonData, "Got wrong data from cache.")
        XCTAssertEqual(result?.hash, hash, "Got wrong data from cache.")
        XCTAssertEqual(result?.identifier, identifier, "Got wrong data from cache.")
    }
    
    func testUpdateValue() {
        //Given
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let identifier = "id"
        let hash = "1"
        let hash2 = "2"
        let reference = CacheReference(identifier: identifier, data: jsonData, hash: hash, timeOfCreation: generateTimeOfCreation())
        let reference1 = CacheReference(identifier: identifier, data: jsonData, hash: hash2, timeOfCreation: generateTimeOfCreation())
        
        //When
        sut.update(reference)
        sut.update(reference1)
        let result = sut.getReference(for: identifier)
        
        //Then
        XCTAssertEqual(result?.data, jsonData, "Got wrong data from cache.")
        XCTAssertEqual(result?.hash, hash2, "Got wrong data from cache.")
        XCTAssertEqual(result?.identifier, identifier, "Got wrong data from cache.")
    }
    
    func testNumberOfRegistersNoRegisters() {
        //Given
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        
        //When
        sut.clear()
        
        //Then
        XCTAssertEqual(sut.numberOfReferences(), 0, "Counted references wrong")
    }
    
    func testNumberOfRegistersWithRegisters() {
        //Given
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let identifier1 = "id1"
        let hash1 = "1"
        let identifier2 = "id2"
        let hash2 = "2"
        let reference1 = CacheReference(identifier: identifier1, data: jsonData, hash: hash1)
        let reference2 = CacheReference(identifier: identifier2, data: jsonData, hash: hash2)
        
        //When
        sut.clear()
        sut.update(reference1)
        sut.update(reference2)
        
        //Then
        XCTAssertEqual(sut.numberOfReferences(), 2, "Counted references wrong")
    }
    
    private func generateTimeOfCreation() -> Date {
        let stringDate = "2020-01-01"
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        // swiftlint:disable force_unwrapping
        return dateFormatter.date(from: stringDate)!
        // swiftlint:enable force_unwrapping
    }
}

struct CacheDiskManagerDependencies: DependencyLogger {
    var logger: BeagleLoggerType = BeagleLoggerDefault()
}
