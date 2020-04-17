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

public final class MemoryCacheService {

    private let memory = NSCache<NSString, NSData>()

    public enum Error: Swift.Error {
        case couldNotLoadData
    }

    // MARK: - Saving

    public func save(data: Data, key: String) {
        memory.setObject(data as NSData, forKey: key as NSString)
    }

    // MARK: - Data Loading

    public func loadData(
        from key: String
    ) -> Result<Data, Error> {
        guard let data = memory.object(forKey: key as NSString) else {
            return .failure(.couldNotLoadData)
        }

        return .success(data as Data)
    }

    // MARK: - Cleaning Caches

    public func clear() {
        memory.removeAllObjects()
    }
}
