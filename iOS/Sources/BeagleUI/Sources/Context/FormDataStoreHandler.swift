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

public protocol FormDataStoreHandling {
    func save(key: String, value: String)
    func read(key: String) -> String?
}

internal class FormDataStoreHandler: FormDataStoreHandling {
    
    private(set) var dataStore: [String: String] = [:]
    
    // MARK: - Dependency
    
    private let dependency: DependencyCacheManager
    
    init(dependency: DependencyCacheManager) {
        self.dependency = dependency
    }
    
    func save(key: String, value: String) {
        if let data = value.data(using: .utf8) {
            let cacheReference = CacheReference(identifier: key, data: data, hash: "")
            dependency.cacheManager?.addToCache(cacheReference)
        }
    }
    
    func read(key: String) -> String? {
        guard
            let cacheReference = dependency.cacheManager?.getReference(identifiedBy: key),
            let value = String(data: cacheReference.data, encoding: .utf8)
        else { return nil }
        return value
    }
}
