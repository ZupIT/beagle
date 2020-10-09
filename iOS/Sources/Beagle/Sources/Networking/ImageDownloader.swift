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

public protocol ImageDownloader {
    @discardableResult
    func fetchImage(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        completion: @escaping (Result<Data, Request.Error>) -> Void
    ) -> RequestToken?
}

public protocol DependencyImageDownloader {
    var imageDownloader: ImageDownloader { get }
}

public struct ImageDownloaderDefault: ImageDownloader {
    
    public typealias Dependencies =
        DependencyNetworkClient
        & DependencyUrlBuilder
        & DependencyLogger

    let dependencies: Dependencies
    private let dispatcher: RequestDispatcher
    
    public init(dependencies: Dependencies) {
        self.dependencies = dependencies
        self.dispatcher = RequestDispatcher(dependencies: dependencies)
    }
    
    @discardableResult
    public func fetchImage(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        completion: @escaping (Result<Data, Request.Error>) -> Void
    ) -> RequestToken? {
        return dispatcher.dispatchRequest(path: url, type: .fetchImage, additionalData: additionalData) { result in
            let mapped = result
                .map { $0.data }
            
            DispatchQueue.main.async { completion(mapped) }
        }
    }
}
