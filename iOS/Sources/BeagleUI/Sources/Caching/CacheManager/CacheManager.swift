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

public protocol DependencyCacheManager {
    var cacheManager: CacheManagerProtocol { get }
}

public protocol CacheManagerProtocol {
    func saveComponent(_ component: ServerDrivenComponent, forPath path: String)
    func dequeueComponent(path: String) -> ServerDrivenComponent?
}

/// This class is responsible to maintain and manage the cache.
final public class CacheManager: CacheManagerProtocol {
    
    private let maximumScreensCapacity: Int
    lazy private var components = CacheLRU<String, ServerDrivenComponent>(capacity: maximumScreensCapacity)
    
    // MARK: Lifecycle

    /// Initialize the CacheManager
    /// - Parameter maximumScreensCapacity: The maximum number of registers that can be save simultaneously in the cache.
    init(maximumScreensCapacity: Int) {
        self.maximumScreensCapacity = maximumScreensCapacity
    }
    
    // MARK: Private

    /// Saves a component assosiated with a key in the cache.
    /// - Parameters:
    ///   - component: Component to be saved
    ///   - path: Key to be assosiated with the component
    public func saveComponent(_ component: ServerDrivenComponent, forPath path: String) {
        components.setValue(component, for: path)
    }

    /// Get a component from the cache, 'nil' if not found.
    /// - Parameter path: The search key for the component
    public func dequeueComponent(path: String) -> ServerDrivenComponent? {
        return components.getValue(for: path)
    }
}
