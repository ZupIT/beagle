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
import BeagleSchema

public protocol DependencyPreFetching {
    var preFetchHelper: BeaglePrefetchHelping { get }
}

public protocol BeaglePrefetchHelping {
    func prefetchComponent(newPath: Route.NewPath)
}

public class BeaglePreFetchHelper: BeaglePrefetchHelping {
    
    public typealias Dependencies = DependencyRepository
    let dependencies: Dependencies
    
    public init(dependencies: Dependencies) {
        self.dependencies = dependencies
    }
    
    public func prefetchComponent(newPath: Route.NewPath) {
        guard newPath.shouldPrefetch else { return }
        dependencies.repository.fetchComponent(url: newPath.url, additionalData: nil) { _ in
        }
    }
}
