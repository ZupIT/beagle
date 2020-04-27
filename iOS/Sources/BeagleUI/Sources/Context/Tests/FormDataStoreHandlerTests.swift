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
import SnapshotTesting

class FormDataStoreHandlerTests: XCTestCase {
    
    func test_saveMustCallCacheManagerPassingRightIdentifierAndData() {
        //Given
        let cacheManagerStub = CacheManagerStub()
        let sut = FormDataStoreHandler(dependency: DependencyCacheManagerStub(cacheManager: cacheManagerStub))
        let parameters = (key: "key", value: "value")
        
        //When
        sut.save(key: parameters.key, value: parameters.value)
        guard
            let passedCacheReference = cacheManagerStub.passedReference,
            let passedValue = String(data: passedCacheReference.data, encoding: .utf8)
        else {
            XCTFail("By calling save, the data store must call cache manager passing a cache reference")
            return
        }
        
        //Then
        XCTAssert(cacheManagerStub.didCallAddToCache == true)
        XCTAssert(passedCacheReference.identifier == parameters.key)
        XCTAssert(passedValue == parameters.value)
    }
    
    func test_readMustCallCacheManagerWithRightKeyAndReceiveExpectedValue() {
        //Given
        let parameters = (key: "key", value: "value")
        // swiftlint:disable force_unwrapping
        let cacheReference = CacheReference(identifier: parameters.key, data: parameters.value.data(using: .utf8)!, hash: "")
        let cacheManagerStub = CacheManagerStub(cacheReference: cacheReference)
        let sut = FormDataStoreHandler(dependency: DependencyCacheManagerStub(cacheManager: cacheManagerStub))
        
        //When
        let value = sut.read(key: parameters.key)
        
        //Then
        XCTAssert(cacheManagerStub.didCallGetReference == true)
        XCTAssert(cacheManagerStub.passedId == parameters.key)
        XCTAssert(value == parameters.value)
    }
}

private class DependencyCacheManagerStub: DependencyCacheManager {
    var cacheManager: CacheManagerProtocol?
    
    init(cacheManager: CacheManagerProtocol) {
        self.cacheManager = cacheManager
    }
}

private class CacheManagerStub: CacheManagerProtocol {
    
    private(set) var didCallAddToCache = false
    private(set) var passedReference: CacheReference?
    
    func addToCache(_ reference: CacheReference) {
        didCallAddToCache = true
        passedReference = reference
    }
    
    private(set) var didCallGetReference = false
    private(set) var passedId: String = ""
    
    var cacheReference: CacheReference?
    
    func getReference(identifiedBy id: String) -> CacheReference? {
        didCallGetReference = true
        passedId = id
        return cacheReference
    }
    
    func isValid(reference: CacheReference) -> Bool { return true }
    
    init(cacheReference: CacheReference? = nil) {
        self.cacheReference = cacheReference
    }
}
