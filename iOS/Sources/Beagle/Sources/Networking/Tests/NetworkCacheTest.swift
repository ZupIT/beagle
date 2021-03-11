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

    private var (id, aHash, maxAge) = ("id", "123", 15)
    private let data = "Cache Test".data(using: .utf8)!
    private lazy var reference = CacheReference(identifier: id, data: data, hash: id)

    private lazy var completeHeaders = [
        "Beagle-Hash": aHash,
        "Cache-Control": "max-age=\(maxAge)"
    ]

    func testCacheControlHeaderShouldCacheTheResponse() {
        // Given
        let urlResponse = HTTPURLResponse(url: URL(string: id)!, statusCode: 200, httpVersion: nil, headerFields: completeHeaders)!

        // When
        subject.saveCacheIfPossible(
            url: id,
            response: .init(data: data, response: urlResponse)
        )

        // Then
        let cache = cacheSpy.references.first!.cache
        XCTAssert(cache.identifier == id)
        XCTAssert(cache.data == data)
        XCTAssert(cache.hash == aHash)
        XCTAssert(cache.maxAge == maxAge)
    }

    func testShouldFailMetaDataWithoutHash() {
        // Given
        let headers = [AnyHashable: Any]()

        // When
        let cache = subject.getMetaData(from: headers)

        // Then
        XCTAssert(cache == nil)
    }

    func testShouldHaveMetaData() {
        // When
        let cache = subject.getMetaData(from: completeHeaders)

        // Then
        XCTAssert(cache?.hash == aHash)
        XCTAssert(cache?.maxAge == maxAge)
    }

    func testMetaDataWithInvalidMaxAge() {
        // Given
        let invalidMaxAge = "notNumber"
        completeHeaders["Cache-Control"] = invalidMaxAge

        // When
        let cache = subject.getMetaData(from: completeHeaders)

        // Then
        XCTAssert(cache?.hash == aHash)
        XCTAssert(cache?.maxAge == nil)
    }

    func testWhenDataNotCached() {
        // When
        let cache = subject.checkCache(identifiedBy: id, additionalData: nil)

        // Then
        XCTAssert(cache == .dataNotCached)
    }

    func testWhenValidCachedData() {
        // Given
        cacheSpy.references.append(.init(cache: reference, isValid: true))

        // When
        let cache = subject.checkCache(identifiedBy: id, additionalData: nil)

        // Then
        XCTAssert(cache == .validCachedData(data))
    }

    func testCacheDisabled() {
        // Given
        dependencies.cacheManager = nil

        // When
        let cache = subject.checkCache(identifiedBy: "id", additionalData: nil)

        // Then
        XCTAssert(cache == .disabled)
    }

    func testInvalidCachedData() {
        // Given
        cacheSpy.references.append(.init(cache: reference, isValid: false))

        // When
        let cache = subject.checkCache(identifiedBy: id, additionalData: nil)

        // Then
        XCTAssert(cache == .invalidCachedData(data: data, additional: nil))
    }

    func testInvalidCachedDataWithHash() {
        // Given
        cacheSpy.references.append(.init(cache: reference, isValid: false))

        // When
        let cache = subject.checkCache(identifiedBy: id, additionalData: .Http(body: nil))

        // Then
        let expected: NetworkCache.CacheCheck = .invalidCachedData(
            data: data,
            additional: .Http(headers: [subject.cacheHashHeader: id], body: nil)
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
