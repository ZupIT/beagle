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

import Foundation

class NetworkCache {

    let cacheHashHeader = "beagle-hash"
    let serviceMaxCacheAge = "cache-control"

    typealias Dependencies = DependencyCacheManager

    private let dependencies: Dependencies

    init(dependencies: Dependencies) {
        self.dependencies = dependencies
    }

    enum CacheCheck {
        case validCache(Data)
        case notValidCache(data: Data?, additional: RemoteScreenAdditionalData?)

        var data: Data? {
            switch self {
            case .validCache(let data): return data
            case .notValidCache(data: let data, additional: _): return data
            }
        }
    }

    func checkCache(identifiedBy id: String, additionalData: inout RemoteScreenAdditionalData?) -> CacheCheck {
        let cache = dependencies.cacheManager?.getReference(identifiedBy: id)

        if let cache = cache, dependencies.cacheManager?.isValid(reference: cache) == true {
            return .validCache(cache.data)
        }

        appendHeaders(cache, to: &additionalData)
        return .notValidCache(data: cache?.data, additional: additionalData)
    }

    func saveCacheIfPossible(url: String, response: NetworkResponse) {
        guard
            let manager = dependencies.cacheManager,
            let http = response.response as? HTTPURLResponse,
            let hash = value(forHTTPHeaderField: cacheHashHeader, in: http)
        else {
            return
        }

        let maxAge = cacheMaxAge(httpResponse: http)
        manager.addToCache(
            CacheReference(identifier: url, data: response.data, hash: hash, maxAge: maxAge)
        )
    }

    private func appendHeaders(_ cache: CacheReference?, to data: inout RemoteScreenAdditionalData?) {
        if let cache = cache, dependencies.cacheManager?.isValid(reference: cache) != true {
            data?.headers[cacheHashHeader] = cache.hash
        }
    }

    private func cacheMaxAge(httpResponse: HTTPURLResponse) -> Int? {
        guard let specifiedAge = value(forHTTPHeaderField: serviceMaxCacheAge, in: httpResponse) else {
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

    private func value(forHTTPHeaderField header: String, in response: HTTPURLResponse) -> String? {
        let key = header.lowercased()
        let headers = response.allHeaderFields
        for entry in headers {
            if (entry.key as? String)?.lowercased() == key, let value = entry.value as? String {
                return value
            }
        }
        return nil
    }
}
