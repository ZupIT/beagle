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

final class CacheManagerDefaultTests: XCTestCase {
    
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
    
    func testValidCache() {
        //Given
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        let reference = CacheReference(identifier: "", data: jsonData, hash: "")
        
        //When
        let isValid = sut.isValid(reference: reference)
        
        //Then
        XCTAssertTrue(isValid)
    }
    
    func testMaxAgeDefaultExpired() {
        //Given
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 1))
        let reference = CacheReference(identifier: "", data: jsonData, hash: "")
        let timeOutComponent = expectation(description: "timeOutComponent")
        
        //When
        delay(seconds: 1) {
            //Then
            let isValid = sut.isValid(reference: reference)
            XCTAssertFalse(isValid)
            timeOutComponent.fulfill()
        }
        
        waitForExpectations(timeout: 2, handler: nil)
    }
    
    func testMaxAgeFromServer() {
        //Given
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 0))
        let cacheReference = CacheReference(identifier: defaultURL, data: jsonData, hash: defaultHash, maxAge: 5)
        
        //When
        let isValid = sut.isValid(reference: cacheReference)
        
        //Then
        XCTAssertTrue(isValid)
    }
    
    func testMaxAgeFromServerExpired() {
        //Given
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 1))
        let cacheReference = CacheReference(identifier: defaultURL, data: jsonData, hash: defaultHash, maxAge: 2)
        let timeOutComponent = expectation(description: "timeOutComponent")
        
        //When
        delay(seconds: 3) {
            //Then
            let isValid = sut.isValid(reference: cacheReference)
            XCTAssertFalse(isValid)
            timeOutComponent.fulfill()
        }
        
        waitForExpectations(timeout: 5, handler: nil)
    }
    
    func testSecondRecordDeletionFromMemory() {
        //Given
        let memoryCapacity = 2
        let diskCapacity = 1
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: memoryCapacity, diskMaximumCapacity: diskCapacity, cacheMaxAge: 10))
        let cacheReference1 = CacheReference(identifier: url1, data: jsonData, hash: defaultHash)
        let cacheReference2 = CacheReference(identifier: url2, data: jsonData, hash: defaultHash)
        let cacheReference3 = CacheReference(identifier: url3, data: jsonData, hash: defaultHash)
        
        //When
        sut.addToCache(cacheReference1)
        sut.addToCache(cacheReference2)
        sut.addToCache(cacheReference3)
        
        //Then
        XCTAssertNil(sut.getReference(identifiedBy: url1))
        XCTAssertNotNil(sut.getReference(identifiedBy: url2))
        XCTAssertNotNil(sut.getReference(identifiedBy: url3))
    }
    
    func testFirstRecordDeletionFromMemory() {
        //Given
        let memoryCapacity = 2
        let diskCapacity = 0
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: memoryCapacity, diskMaximumCapacity: diskCapacity, cacheMaxAge: 10))
        let cacheReference1 = CacheReference(identifier: url1, data: jsonData, hash: defaultHash)
        let cacheReference2 = CacheReference(identifier: url2, data: jsonData, hash: defaultHash)
        let cacheReference3 = CacheReference(identifier: url3, data: jsonData, hash: defaultHash)
        
        //When
        sut.clear()
        sut.addToCache(cacheReference1)
        sut.addToCache(cacheReference2)
        _ = sut.getReference(identifiedBy: url1)
        sut.addToCache(cacheReference3)
        
        //Then
        XCTAssertNil(sut.getReference(identifiedBy: url2))
        XCTAssertNotNil(sut.getReference(identifiedBy: url1))
        XCTAssertNotNil(sut.getReference(identifiedBy: url3))
    }
    
    func testSecondRecordDeletionFromDisk() {
        //Given
        let memoryCapacity = 1
        let diskCapacity = 2
        struct CacheManagerDependenciesLocal: CacheManagerDefault.Dependencies {
            var logger: BeagleLoggerType = BeagleLoggerDefault()
            var cacheDiskManager: CacheDiskManagerProtocol = DefaultCacheDiskManager(dependencies: CacheDiskManagerDependencies())
            var decoder: ComponentDecoding = ComponentDecoder()
        }
        let manager = CacheManagerDefault(dependencies: CacheManagerDependenciesLocal(), config: .init(memoryMaximumCapacity: memoryCapacity, diskMaximumCapacity: diskCapacity, cacheMaxAge: 10))
        let cacheReference1 = CacheReference(identifier: url1, data: jsonData, hash: defaultHash)
        let cacheReference2 = CacheReference(identifier: url2, data: jsonData, hash: defaultHash)
        let cacheReference3 = CacheReference(identifier: url3, data: jsonData, hash: defaultHash)
        
        //When
        manager.clear()
        manager.addToCache(cacheReference1)
        manager.addToCache(cacheReference2)
        _ = manager.getReference(identifiedBy: url1)
        manager.addToCache(cacheReference3)
        
        //Then
        XCTAssertNil(manager.getReference(identifiedBy: url2))
        XCTAssertNotNil(manager.getReference(identifiedBy: url1))
        XCTAssertNotNil(manager.getReference(identifiedBy: url3))
    }
    
    func testFirstRecordDeletionFromDisk() {
        //Given
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
        
        //When
        sut.addToCache(cacheReference1)
        sut.addToCache(cacheReference2)
        sut.addToCache(cacheReference3)
        
        //Then
        XCTAssertNil(sut.getReference(identifiedBy: url1))
        XCTAssertNotNil(sut.getReference(identifiedBy: url2))
        XCTAssertNotNil(sut.getReference(identifiedBy: url3))
    }
    
    func testGetExistingReference() {
        //Given
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        
        //When
        addDefaultComponent(manager: sut)
        
        //Then
        XCTAssertNotNil(getDefaultReference(manager: sut))
    }
    
    func testGetInexistentReference() {
        //Given
        let sut = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: .init(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        
        //When
        sut.clear()
        
        //Then
        XCTAssertNil(getDefaultReference(manager: sut))
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

extension XCTestCase {
    /// Improves readability when implementing a `Delay`.
    /// - Parameter seconds: time to trigger the  completion, by default is 1 second.
    func delay(seconds: Int, _ completionHadler: @escaping () -> Void) {
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + .seconds(seconds)) {
            completionHadler()
        }
    }
}
