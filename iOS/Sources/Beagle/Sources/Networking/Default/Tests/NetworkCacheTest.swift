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

import XCTest
@testable import Beagle
import SnapshotTesting

// swiftlint:disable force_unwrapping
final class NetworkCacheTests: XCTestCase {

    private lazy var subject = NetworkCache(dependencies: dependencies)

    private lazy var dependencies = BeagleScreenDependencies(cacheManager: cacheSpy)
    private let cacheSpy = CacheManagerSpy()

    private let id = "id"
    private let data = "Cache Test".data(using: .utf8)!
    private lazy var reference = CacheReference(identifier: id, data: data, hash: id)

    func test_withCacheControlHeader_itShouldCacheTheResponse() {
        // Given
        let (hash, maxAge) = ("123", 15)
        let headers = [
            "Beagle-Hash": hash,
            "Cache-Control": "max-age=\(maxAge)"
        ]
        let urlResponse = HTTPURLResponse(url: URL(string: id)!, statusCode: 200, httpVersion: nil, headerFields: headers)!

        // When
        subject.saveCacheIfPossible(
            url: id,
            response: .init(data: data, response: urlResponse)
        )

        // Then
        let cache = cacheSpy.references.first!.cache
        XCTAssertEqual(cache.identifier, id)
        XCTAssertEqual(cache.data, data)
        XCTAssertEqual(cache.hash, hash)
        XCTAssertEqual(cache.maxAge, maxAge)
    }

    func test_whenNoCacheData() {
        // When
        let cache = subject.checkCache(identifiedBy: id, additionalData: nil)

        // Then
        XCTAssert(cache == .dataNotCached)
    }

    func test_whenValidCache() {
        // Given
        cacheSpy.references.append(.init(cache: reference, isValid: true))

        // When
        let cache = subject.checkCache(identifiedBy: id, additionalData: nil)

        // Then
        XCTAssert(cache == .validCachedData(data))
    }

    func test_cacheDisabled() {
        // Given
        dependencies.cacheManager = nil

        // When
        let cache = subject.checkCache(identifiedBy: "id", additionalData: nil)

        // Then
        XCTAssert(cache == .disabled)
    }

    func test_invalidCachedData() {
        // Given
        cacheSpy.references.append(.init(cache: reference, isValid: false))

        // When
        let cache = subject.checkCache(identifiedBy: id, additionalData: nil)

        // Then
        XCTAssert(cache == .invalidCachedData(data: data, additional: nil))
    }

    func test_invalidCachedDataWithHash() {
        // Given
        cacheSpy.references.append(.init(cache: reference, isValid: false))

        // When
        let cache = subject.checkCache(identifiedBy: id, additionalData: .Http(httpData: nil))

        // Then
        let expected: NetworkCache.CacheCheck = .invalidCachedData(
            data: data,
            additional: .Http(httpData: nil, headers: [subject.cacheHashHeader: id])
        )

        XCTAssert(cache == expected)
    }
}

extension NetworkCache.CacheCheck {

    public static func == (lhs: NetworkCache.CacheCheck, rhs: NetworkCache.CacheCheck) -> Bool {
        switch (lhs, rhs) {
        case let (.validCachedData(data1), .validCachedData(data2)):
            return data1 == data2

        case (.dataNotCached, .dataNotCached),
             (.disabled, .disabled):
            return true

        case let (
            .invalidCachedData(data: data1, additional: add1),
            .invalidCachedData(data: data2, additional: add2)
        ):
            return data1 == data2 && add1?.headers == add2?.headers

        default:
            return false
        }
    }
}
