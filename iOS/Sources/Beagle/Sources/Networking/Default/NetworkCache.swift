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

import Foundation

class NetworkCache {

    let cacheHashHeader = "beagle-hash"
    let serviceMaxCacheAge = "cache-control"

    typealias Dependencies = DependencyCacheManager

    private let dependencies: Dependencies

    init(dependencies: Dependencies) {
        self.dependencies = dependencies
    }

    func checkCache(
        identifiedBy id: String,
        additionalData: RemoteScreenAdditionalData?
    ) -> CacheCheck {
        guard let manager = dependencies.cacheManager else { return .disabled }

        guard let cache = manager.getReference(identifiedBy: id) else {
            return .dataNotCached
        }

        if manager.isValid(reference: cache) {
            return .validCachedData(cache.data)
        }

        var newData = additionalData
        newData?.headers[cacheHashHeader] = cache.hash
        return .invalidCachedData(data: cache.data, additional: newData)
    }

    enum CacheCheck {
        case disabled
        case dataNotCached
        case validCachedData(Data)
        case invalidCachedData(data: Data, additional: RemoteScreenAdditionalData?)

        var data: Data? {
            switch self {
            case .disabled, .dataNotCached: return nil
            case .validCachedData(let data), .invalidCachedData(data: let data, additional: _): return data
            }
        }

        var additional: RemoteScreenAdditionalData? {
            switch self {
            case .invalidCachedData(data: _, additional: let additional): return additional
            default: return nil
            }
        }
    }

    func saveCacheIfPossible(url: String, response: NetworkResponse) {
        guard
            let manager = dependencies.cacheManager,
            let http = response.response as? HTTPURLResponse,
            let meta = getMetaData(from: http.allHeaderFields)
        else {
            return
        }

        manager.addToCache(
            CacheReference(identifier: url, data: response.data, hash: meta.hash, maxAge: meta.maxAge)
        )
    }

    func getMetaData(from headers: [AnyHashable: Any]) -> MetaData? {
        guard let hash = value(forHeader: cacheHashHeader, in: headers) else {
            return nil
        }

        let maxAge = serverMaxAge(in: headers)
        return MetaData(hash: hash, maxAge: maxAge)
    }

    struct MetaData {
        let hash: String
        let maxAge: Int?
    }

    private func serverMaxAge(in headers: [AnyHashable: Any]) -> Int? {
        guard let specifiedAge = value(forHeader: serviceMaxCacheAge, in: headers) else {
            return nil
        }

        // TODO: see if we need to work with other "cache-control" formats, like:
        // Cache-Control: private, max-age=0, no-cache
        let values = specifiedAge.split(separator: "=")
        if let maxAgeValue = values.last, let int = Int(String(maxAgeValue)) {
            return int
        } else {
            return nil
        }
    }

    private func value(forHeader header: String, in headers: [AnyHashable: Any]) -> String? {
        let key = header.lowercased()
        for entry in headers {
            if (entry.key as? String)?.lowercased() == key {
                return entry.value as? String
            }
        }
        return nil
    }
}
