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
@testable import BeagleUI

final class CacheManagerDefaultTests: XCTestCase {
    
    private let dependencies = BeagleScreenDependencies()
    private let jsonData = """
    {
      "_beagleType_": "beagle:component:text",
      "text": "cache",
      "appearance": {
        "backgroundColor": "#4000FFFF"
      }
    }
    """.data(using: .utf8)!
    private let cacheHashHeader = "beagle-hash"
    private let serviceMaxCacheAge = "cache-control"
    private let defaultHash = "123"
    private let defaultURL = "urlTeste"

    func test_whenHaveDefaultValue_itShouldAffectIsValid() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        
        guard let reference = getDefaultReference(manager: sut) else {
            XCTFail("Could not retrive reference.")
            return
        }
        
        let isValid = sut.isValid(reference: reference)
        XCTAssert(isValid == true, "Should not need revalidation")
    }
    
    func testMaxAgeDefaultExpired() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 1))
        
        guard let reference = getDefaultReference(manager: sut) else {
            XCTFail("Could not retrive reference.")
            return
        }
        
        let timeOutComponent = expectation(description: "timeOutComponent")
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + .seconds(2)) {
            let isValid = sut.isValid(reference: reference)
            XCTAssert(isValid == false, "Should need revalidation")
            timeOutComponent.fulfill()
        }
        waitForExpectations(timeout: 13, handler: nil)
    }
    
    func testMaxAgeFromServer() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 0))
        let cacheReference = CacheReference(identifier: defaultURL, data: jsonData, hash: defaultHash, maxAge: 5)
        
        guard let reference = getDefaultReference(manager: sut) else {
            XCTFail("Could not retrive reference.")
            return
        }
        
        let isValid = sut.isValid(reference: reference)
        XCTAssert(isValid == true, "Should not need revalidation")
    }
    
    func testMaxAgeServerExpired() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 3))
        let cacheReference = CacheReference(identifier: defaultURL, data: jsonData, hash: defaultHash, maxAge: 5)
        
        guard let reference = getDefaultReference(manager: sut) else {
            XCTFail("Could not retrive reference.")
            return
        }
        
        let timeOutComponent = expectation(description: "timeOutComponent")
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + .seconds(1)) {
            let isValid = sut.isValid(reference: reference)
            XCTAssert(isValid == false, "Should need revalidation")
            timeOutComponent.fulfill()
        }
        waitForExpectations(timeout: 7, handler: nil)
    }
    
    func testMemoryLRU1() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 1, cacheMaxAge: 10))
        let url1 = "urlTeste1"
        let url2 = "urlTeste2"
        let url3 = "urlTeste3"
        let cacheReference1 = CacheReference(identifier: url1, data: jsonData, hash: defaultHash)
        let cacheReference2 = CacheReference(identifier: url2, data: jsonData, hash: defaultHash)
        let cacheReference3 = CacheReference(identifier: url3, data: jsonData, hash: defaultHash)
        sut.addToCache(cacheReference1)
        sut.addToCache(cacheReference2)
        sut.addToCache(cacheReference3)
        
        if let _ = sut.getReference(identifiedBy: url1) {
            XCTFail("Should not find the cached reference.")
        }
        guard let _ = sut.getReference(identifiedBy: url2),
            let _ = sut.getReference(identifiedBy: url3) else {
                XCTFail("Could not find the cached reference.")
                return
        }
    }
    
    func testMemoryLRU2() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 0, cacheMaxAge: 10))
        sut.clear()
        let url1 = "urlTeste1"
        let url2 = "urlTeste2"
        let url3 = "urlTeste3"
        let cacheReference1 = CacheReference(identifier: url1, data: jsonData, hash: defaultHash)
        let cacheReference2 = CacheReference(identifier: url2, data: jsonData, hash: defaultHash)
        let cacheReference3 = CacheReference(identifier: url3, data: jsonData, hash: defaultHash)
        sut.addToCache(cacheReference1)
        sut.addToCache(cacheReference2)
        let _ = sut.getReference(identifiedBy: url1)
        sut.addToCache(cacheReference3)
        
        if let _ = sut.getReference(identifiedBy: url2) {
            XCTFail("Should not find the cached reference.")
        }
        guard let _ = sut.getReference(identifiedBy: url1),
            let _ = sut.getReference(identifiedBy: url3) else {
                XCTFail("Could not find the cached reference.")
                return
        }
    }
    
    func testDiskLRU1() {
        struct CacheManagerDependenciesLocal: CacheManagerDefault.Dependencies {
            var logger: BeagleLoggerType = BeagleLogger()
            var cacheDiskManager: CacheDiskManagerProtocol = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
            var decoder: ComponentDecoding = ComponentDecoder()
        }
        let manager = CacheManagerDefault(dependencies: CacheManagerDependenciesLocal(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 1, diskMaximumCapacity: 2, cacheMaxAge: 10))
        let url1 = "urlTeste1"
        let url2 = "urlTeste2"
        let url3 = "urlTeste3"
        let cacheReference1 = CacheReference(identifier: url1, data: jsonData, hash: defaultHash)
        let cacheReference2 = CacheReference(identifier: url2, data: jsonData, hash: defaultHash)
        let _ = manager.getReference(identifiedBy: url1)
        let cacheReference3 = CacheReference(identifier: url3, data: jsonData, hash: defaultHash)
        manager.addToCache(cacheReference1)
        manager.addToCache(cacheReference2)
        let _ = manager.getReference(identifiedBy: url1)
        manager.addToCache(cacheReference3)
        
        if let _ = manager.getReference(identifiedBy: url2) {
            XCTFail("Should not find the cached reference.")
        }
        guard let _ = manager.getReference(identifiedBy: url1),
        let _ = manager.getReference(identifiedBy: url3) else {
            XCTFail("Could not find the cached reference.")
            return
        }
    }
    
    func testDiskLRU2() {
        struct CacheManagerDependenciesLocal: CacheManagerDefault.Dependencies {
            var logger: BeagleLoggerType = BeagleLogger()
            var cacheDiskManager: CacheDiskManagerProtocol = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
            var decoder: ComponentDecoding = ComponentDecoder()
        }
        let sut = CacheManagerDefault(dependencies: CacheManagerDependenciesLocal(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 0, diskMaximumCapacity: 2, cacheMaxAge: 10))
        let url1 = "urlTeste1"
        let url2 = "urlTeste2"
        let url3 = "urlTeste3"
        let cacheReference1 = CacheReference(identifier: url1, data: jsonData, hash: defaultHash)
        let cacheReference2 = CacheReference(identifier: url2, data: jsonData, hash: defaultHash)
        let cacheReference3 = CacheReference(identifier: url3, data: jsonData, hash: defaultHash)
        sut.addToCache(cacheReference1)
        sut.addToCache(cacheReference2)
        sut.addToCache(cacheReference3)
        
        if let _ = sut.getReference(identifiedBy: url1) {
            XCTFail("Should not find the cached reference.")
        }
        guard let _ = sut.getReference(identifiedBy: url2),
        let _ = sut.getReference(identifiedBy: url3) else {
            XCTFail("Could not find the cached reference.")
            return
        }
    }
    
    func testGetExistingReference() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        addDefaultComponent(manager: sut)
        
        guard let _ = getDefaultReference(manager: sut) else {
            XCTFail("Could not retrive reference.")
            return
        }
    }
    
    func testGetInexistentReference() {
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        sut.clear()
        if let _ = getDefaultReference(manager: sut) {
            XCTFail("Should not retrive reference.")
            return
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
    var logger: BeagleLoggerType = BeagleLogger()
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
