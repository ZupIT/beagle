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
import CoreData

/// This class is responsible to maintain and manage the cache.
public class CacheManagerDefault: CacheManagerProtocol {
    
    public typealias Dependencies =
        DependencyLogger

    let dependencies: Dependencies

    public let config: Config

    public struct Config {
        public let memoryMaximumCapacity: Int
        public let diskMaximumCapacity: Int
        public let cacheMaxAge: Int

        public init(
            memoryMaximumCapacity: Int = 15,
            diskMaximumCapacity: Int = 150,
            cacheMaxAge: Int = 300
        ) {
            self.memoryMaximumCapacity = memoryMaximumCapacity
            self.diskMaximumCapacity = diskMaximumCapacity
            self.cacheMaxAge = cacheMaxAge
        }
    }

    lazy private var memoryReferences = CacheLRU<String, CacheReference>(
        capacity: config.memoryMaximumCapacity
    )

    lazy var diskManager: CacheDiskManagerProtocol =
        DefaultCacheDiskManager(dependencies: dependencies)

    private let defaultMaxCacheAge = "maxValidAge"
    
    // MARK: Init
    
    public init(dependencies: Dependencies, config: Config = Config()) {
        self.config = config
        self.dependencies = dependencies
    }
    
    // MARK: Public

    public func addToCache(_ reference: CacheReference) {
        saveInMemory(reference: reference)
        saveInDisk(reference: reference)
    }
    
    public func getReference(identifiedBy id: String) -> CacheReference? {
        return memoryReferences.getValue(for: id)
            ?? diskManager.getReference(for: id)
    }
    
    public func clear() {
        memoryReferences.clear()
        diskManager.clear()
    }

    public func isValid(reference: CacheReference) -> Bool {
        let maxAge = reference.maxAge ?? config.cacheMaxAge
        let expirationDate = reference.timeOfCreation.addingTimeInterval(TimeInterval(maxAge))
        return expirationDate > Date()
    }

    // MARK: Privates

    private func saveInMemory(reference: CacheReference) {
        memoryReferences.setValue(reference, for: reference.identifier)
    }
    
    private func saveInDisk(reference: CacheReference) {
        diskManager.update(reference)

        if diskManager.numberOfReferences() > config.diskMaximumCapacity {
            diskManager.removeLastUsed()
        }

        diskManager.saveChanges()
    }
}
