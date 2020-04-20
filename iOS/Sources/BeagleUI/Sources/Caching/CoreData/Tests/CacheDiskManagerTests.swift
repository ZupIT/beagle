//
/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

import XCTest
@testable import BeagleUI

class CacheDiskManagerTests: XCTestCase {
    private let jsonData = """
    {
      "_beagleType_": "beagle:component:text",
      "text": "cache",
      "appearance": {
        "backgroundColor": "#4000FFFF"
      }
    }
    """.data(using: .utf8)!
    
    func testGetReference() {
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        sut.clear()
        let hash = "1"
        let identifier = "id"
        
        let reference = CacheReference(identifier: identifier, data: jsonData, hash: hash)
        sut.update(reference)
        
        guard let result = sut.getReference(for: identifier) else {
            XCTFail("Could not retrive data.")
            return
        }
        XCTAssert(result.data == jsonData, "Got wrong data from cache.")
        XCTAssert(result.hash == hash, "Got wrong data from cache.")
        XCTAssert(result.identifier == identifier, "Got wrong data from cache.")
    }
    
    func testGetNilRefereence() {
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        sut.clear()
        sut.saveChanges()
        let identifier = "id"
        
        if let _ = sut.getReference(for: identifier) {
            XCTFail("Should not retrive data.")
        }
    }
    
    func testClear() {
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let identifier = "id"
        let hash = "1"
        
        let reference = CacheReference(identifier: identifier, data: jsonData, hash: hash)
        sut.update(reference)
        sut.saveChanges()
        sut.clear()

        if let _ = sut.getReference(for: identifier) {
            XCTFail("Should not retrive data.")
        }
    }
    
    func testRemoveLastUsed() {
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let identifier1 = "id1"
        let hash1 = "1"
        let identifier2 = "id2"
        let hash2 = "2"
        
        let reference1 = CacheReference(identifier: identifier1, data: jsonData, hash: hash1)
        let reference2 = CacheReference(identifier: identifier2, data: jsonData, hash: hash2)
        sut.update(reference1)
        sut.update(reference2)
        
        sut.removeLastUsed()
        sut.saveChanges()
        
        if let _ = sut.getReference(for: identifier1) {
            XCTFail("Should not retrive data.")
        }
    }
    
    func testRemoveLastUsedUpdated() {
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        sut.clear()
        let identifier1 = "id1"
        let hash1 = "1"
        let identifier2 = "id2"
        let hash2 = "2"
        let hash3 = "3"
        
        let reference1 = CacheReference(identifier: identifier1, data: jsonData, hash: hash1)
        let reference2 = CacheReference(identifier: identifier2, data: jsonData, hash: hash2)
        sut.update(reference1)
        sut.update(reference2)
        let reference3 = CacheReference(identifier: identifier1, data: jsonData, hash: hash3)
        sut.update(reference3)
        sut.removeLastUsed()
        sut.saveChanges()
        
        if let _ = sut.getReference(for: identifier2) {
            XCTFail("Should not retrive data.")
        }
    }
    
    func testInsertNewValue() {
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let identifier = "id"
        let hash = "1"
        
        let reference = CacheReference(identifier: identifier, data: jsonData, hash: hash)
        sut.update(reference)
        
        guard let result = sut.getReference(for: identifier) else {
            XCTFail("Could not retrive data.")
            return
        }
        XCTAssert(result.data == jsonData, "Got wrong data from cache.")
        XCTAssert(result.hash == hash, "Got wrong data from cache.")
        XCTAssert(result.identifier == identifier, "Got wrong data from cache.")
    }
    
    func testUpdateValue() {
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let identifier = "id"
        let hash = "1"
        let hash2 = "2"
        
        let reference = CacheReference(identifier: identifier, data: jsonData, hash: hash)
        sut.update(reference)
        let reference1 = CacheReference(identifier: identifier, data: jsonData, hash: hash2)
        sut.update(reference1)
        
        
        guard let result = sut.getReference(for: identifier) else {
            XCTFail("Could not retrive data.")
            return
        }
        XCTAssert(result.data == jsonData, "Got wrong data from cache.")
        XCTAssert(result.hash == hash2, "Got wrong data from cache.")
        XCTAssert(result.identifier == identifier, "Got wrong data from cache.")
    }
    
    func testNumberOfRegistersNoRegisters() {
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        sut.clear()
        XCTAssert(sut.numberOfReferences() == 0, "Counted references wrong")
    }
    
    func testNumberOfRegistersWithRegisters() {
        let sut = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
        let identifier1 = "id1"
        let hash1 = "1"
        let identifier2 = "id2"
        let hash2 = "2"
        sut.clear()
        
        let reference1 = CacheReference(identifier: identifier1, data: jsonData, hash: hash1)
        let reference2 = CacheReference(identifier: identifier2, data: jsonData, hash: hash2)
        sut.update(reference1)
        sut.update(reference2)
        
        XCTAssert(sut.numberOfReferences() == 2, "Counted references wrong")
    }
}

struct CacheDiskManagerDependencies: DefaultCacheDiskManager.Dependencies {
    var logger: BeagleLoggerType = BeagleLogger()
}
