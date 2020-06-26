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
import BeagleSchema

final class CacheManagerDefaultTests: XCTestCase {
    
    private let dependencies = BeagleScreenDependencies()
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
    private let cacheHashHeader = "beagle-hash"
    private let serviceMaxCacheAge = "cache-control"
    private let defaultHash = "123"
    private let defaultURL = "urlTeste"
    private let url1 = "urlTeste1"
    private let url2 = "urlTeste2"
    private let url3 = "urlTeste3"

    func test_whenHaveDefaultValue_itShouldAffectIsValid() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        let reference = CacheReference(identifier: "", data: jsonData, hash: "")
        
        let isValid = sut.isValid(reference: reference)
        XCTAssert(isValid, "Should not need revalidation")
    }
    
    func testMaxAgeDefaultExpired() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 1))
        let reference = CacheReference(identifier: "", data: jsonData, hash: "")
        let timeOutComponent = expectation(description: "timeOutComponent")
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + .seconds(1)) {
            let isValid = sut.isValid(reference: reference)
            timeOutComponent.fulfill()
            XCTAssert(isValid == false, "Should need revalidation")
        }
        waitForExpectations(timeout: 5, handler: nil)
    }
    
    func testMaxAgeFromServer() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 0))
        let cacheReference = CacheReference(identifier: defaultURL, data: jsonData, hash: defaultHash, maxAge: 5)
        
        let isValid = sut.isValid(reference: cacheReference)
        XCTAssert(isValid, "Should not need revalidation")
    }
    
    func testMaxAgeFromServerExpired() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 1))
        let cacheReference = CacheReference(identifier: defaultURL, data: jsonData, hash: defaultHash, maxAge: 2)
        
        let timeOutComponent = expectation(description: "timeOutComponent")
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + .seconds(3)) {
            let isValid = sut.isValid(reference: cacheReference)
            XCTAssert(isValid == false, "Should need revalidation")
            timeOutComponent.fulfill()
        }
        waitForExpectations(timeout: 5, handler: nil)
    }
    
    func testMemoryLRU_deletingSecondRecord() {
        let memoryCapacity = 2
        let diskCapacity = 1
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: memoryCapacity, diskMaximumCapacity: diskCapacity, cacheMaxAge: 10))
        let cacheReference1 = CacheReference(identifier: url1, data: jsonData, hash: defaultHash)
        let cacheReference2 = CacheReference(identifier: url2, data: jsonData, hash: defaultHash)
        let cacheReference3 = CacheReference(identifier: url3, data: jsonData, hash: defaultHash)
        sut.addToCache(cacheReference1)
        sut.addToCache(cacheReference2)
        sut.addToCache(cacheReference3)
        
        if sut.getReference(identifiedBy: url1) != nil {
            XCTFail("Should not find the cached reference.")
        }
        if sut.getReference(identifiedBy: url2) == nil ||
            sut.getReference(identifiedBy: url3) == nil {
                XCTFail("Could not find the cached reference.")
        }
    }
    
    func testMemoryLRU_deletingFirstRecord() {
        let memoryCapacity = 2
        let diskCapacity = 0
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: memoryCapacity, diskMaximumCapacity: diskCapacity, cacheMaxAge: 10))
        sut.clear()
        let cacheReference1 = CacheReference(identifier: url1, data: jsonData, hash: defaultHash)
        let cacheReference2 = CacheReference(identifier: url2, data: jsonData, hash: defaultHash)
        let cacheReference3 = CacheReference(identifier: url3, data: jsonData, hash: defaultHash)
        sut.addToCache(cacheReference1)
        sut.addToCache(cacheReference2)
        _ = sut.getReference(identifiedBy: url1)
        sut.addToCache(cacheReference3)
        
        if sut.getReference(identifiedBy: url2) != nil {
            XCTFail("Should not find the cached reference.")
        }
        if sut.getReference(identifiedBy: url1) == nil ||
            sut.getReference(identifiedBy: url3) == nil {
                XCTFail("Could not find the cached reference.")
        }
    }
    
    func testDiskLRU_deletingSecondRecord() {
        let memoryCapacity = 1
        let diskCapacity = 2
        struct CacheManagerDependenciesLocal: CacheManagerDefault.Dependencies {
            var logger: BeagleLoggerType = BeagleLoggerDefault()
            var cacheDiskManager: CacheDiskManagerProtocol = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
            var decoder: ComponentDecoding = ComponentDecoder()
        }
        let manager = CacheManagerDefault(dependencies: CacheManagerDependenciesLocal(), config: .init(memoryMaximumCapacity: memoryCapacity, diskMaximumCapacity: diskCapacity, cacheMaxAge: 10))
        manager.clear()
        let cacheReference1 = CacheReference(identifier: url1, data: jsonData, hash: defaultHash)
        let cacheReference2 = CacheReference(identifier: url2, data: jsonData, hash: defaultHash)
        let cacheReference3 = CacheReference(identifier: url3, data: jsonData, hash: defaultHash)
        manager.addToCache(cacheReference1)
        manager.addToCache(cacheReference2)
        _ = manager.getReference(identifiedBy: url1)
        manager.addToCache(cacheReference3)
        
        if manager.getReference(identifiedBy: url2) != nil {
            XCTFail("Should not find the cached reference.")
        }
        if manager.getReference(identifiedBy: url1) == nil ||
        manager.getReference(identifiedBy: url3) == nil {
            XCTFail("Could not find the cached reference.")
        }
    }
    
    func testDiskLRU_deletingFirstRecord() {
        let memoryCapacity = 0
        let diskCapacity = 2
        struct CacheManagerDependenciesLocal: CacheManagerDefault.Dependencies {
            var logger: BeagleLoggerType = BeagleLoggerDefault()
            var cacheDiskManager: CacheDiskManagerProtocol = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
            var decoder: ComponentDecoding = ComponentDecoder()
        }
        let sut = CacheManagerDefault(dependencies: CacheManagerDependenciesLocal(), config: .init(memoryMaximumCapacity: memoryCapacity, diskMaximumCapacity: diskCapacity, cacheMaxAge: 10))
        let cacheReference1 = CacheReference(identifier: url1, data: jsonData, hash: defaultHash)
        let cacheReference2 = CacheReference(identifier: url2, data: jsonData, hash: defaultHash)
        let cacheReference3 = CacheReference(identifier: url3, data: jsonData, hash: defaultHash)
        sut.addToCache(cacheReference1)
        sut.addToCache(cacheReference2)
        sut.addToCache(cacheReference3)
        
        if sut.getReference(identifiedBy: url1) != nil {
            XCTFail("Should not find the cached reference.")
        }
        if sut.getReference(identifiedBy: url2) == nil ||
        sut.getReference(identifiedBy: url3) == nil {
            XCTFail("Could not find the cached reference.")
        }
    }
    
    func testGetExistingReference() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        addDefaultComponent(manager: sut)
        
        if getDefaultReference(manager: sut) == nil {
            XCTFail("Could not retrieve reference.")
        }
    }
    
    func testGetInexistentReference() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        sut.clear()
        if getDefaultReference(manager: sut) != nil {
            XCTFail("Should not retrieve reference.")
        }
    }
    
    private func addDefaultComponent(manager: CacheManagerDefault) {
        let cacheReference = CacheReference(identifier: defaultURL, data: jsonData, hash: defaultHash)
        manager.addToCache(cacheReference)
    }
    
    private func getDefaultReference(manager: CacheManagerDefault) -> CacheReference? {
        return manager.getReference(identifiedBy: defaultURL)
    }
}

struct CacheManagerDependencies: CacheManagerDefault.Dependencies {
    var logger: BeagleLoggerType = BeagleLoggerDefault()
}

struct CacheDiskManagerDummy: CacheDiskManagerProtocol {
    func removeLastUsed() { }
    func saveChanges() { }
    func update(_ reference: CacheReference) { }
    func getReference(for key: String) -> CacheReference? {
        return nil
    }
    func numberOfReferences() -> Int {
        return 0
    }
    func clear() { }
}
