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

final class NetworkCacheTests: XCTestCase {

    private lazy var subject = NetworkCache(dependencies: dependencies)

    private lazy var dependencies = BeagleScreenDependencies(cacheManager: cacheSpy)
    private lazy var cacheSpy = CacheManagerSpy()

    // swiftlint:disable force_unwrapping
    func test_withCacheControlHeader_itShouldCacheTheResponse() {
        // Given
        let (url, hash, maxAge) = ("url", "123", 15)
        let data = "Cache Test".data(using: .utf8)!
        let headers = [
            "Beagle-Hash": hash,
            "Cache-Control": "max-age=\(maxAge)"
        ]
        let urlResponse = HTTPURLResponse(url: URL(string: url)!, statusCode: 200, httpVersion: nil, headerFields: headers)!

        // When
        subject.saveCacheIfPossible(
            url: url,
            response: .init(data: data, response: urlResponse)
        )

        // Then
        let cache = cacheSpy.references.first!.cache
        XCTAssertEqual(cache.identifier, url)
        XCTAssertEqual(cache.data, data)
        XCTAssertEqual(cache.hash, hash)
        XCTAssertEqual(cache.maxAge, maxAge)
    }

    func test_whenNoCacheData() {
        // Given
        let id = "id"
        var data: RemoteScreenAdditionalData?

        // When
        let cache = subject.checkCache(identifiedBy: id, additionalData: &data)

        // Then
        XCTAssert(cache == .notValidCache(data: nil, additional: nil))
    }
}

extension NetworkCache.CacheCheck: AutoEquatable {
    public static func == (lhs: NetworkCache.CacheCheck, rhs: NetworkCache.CacheCheck) -> Bool {
        switch (lhs, rhs) {
        case (.validCache(let data1), .validCache(let data2)):
            return data1 == data2
        case (.notValidCache, .notValidCache):
            return true
        default:
            return false
        }
    }
}
