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

public protocol Repository {

    @discardableResult
    func fetchComponent(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        useCache: Bool,
        completion: @escaping (Result<ServerDrivenComponent, Request.Error>) -> Void
    ) -> RequestToken?

    @discardableResult
    func submitForm(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        data: Request.FormData,
        completion: @escaping (Result<RawAction, Request.Error>) -> Void
    ) -> RequestToken?

    @available(*, deprecated, message: "It was deprecated in version 1.3 and will be removed in a future version. Please use fetchImage from ImageDownloader instead.")
    @discardableResult
    func fetchImage(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        completion: @escaping (Result<Data, Request.Error>) -> Void
    ) -> RequestToken?
}

public protocol DependencyRepository {
    var repository: Repository { get }
}

// MARK: - Default

public struct RepositoryDefault: Repository {
    
    // MARK: Dependencies

    public typealias Dependencies =
        BeagleSchema.DependencyDecoder
        & DependencyNetworkClient
        & DependencyCacheManager
        & DependencyUrlBuilder
        & DependencyLogger

    let dependencies: Dependencies

    private var networkCache: NetworkCache
    private let dispatcher: RequestDispatcher
    
    // MARK: Initialization
    
    public init(dependencies: Dependencies) {
        self.dependencies = dependencies
        self.networkCache = NetworkCache(dependencies: dependencies)
        self.dispatcher = RequestDispatcher(dependencies: dependencies)
    }
    
    // MARK: Public Methods

    public typealias Result<Success> = Swift.Result<Success, Request.Error>

    @discardableResult
    public func fetchComponent(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        useCache: Bool = true,
        completion: @escaping (Result<ServerDrivenComponent>) -> Void
    ) -> RequestToken? {
        let cache = networkCache.checkCache(identifiedBy: url, additionalData: additionalData)
        if useCache, case .validCachedData(let data) = cache {
            DispatchQueue.main.async { completion(self.decodeComponent(from: data)) }
            return nil
        }

        let additional = cache.additional ?? additionalData
        return dispatcher.dispatchRequest(path: url, type: .fetchComponent, additionalData: additional) {  result in
            let mapped = result
                .flatMap { self.handleFetchComponent($0, cachedComponent: cache.data, url: url) }

            DispatchQueue.main.async { completion(mapped) }
        }
    }

    @discardableResult
    public func submitForm(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        data: Request.FormData,
        completion: @escaping (Result<RawAction>) -> Void
    ) -> RequestToken? {
        return dispatcher.dispatchRequest(path: url, type: .submitForm(data), additionalData: additionalData) {  result in
            let mapped = result
                .flatMap { self.handleForm($0.data) }

            DispatchQueue.main.async { completion(mapped) }
        }
    }

    @available(*, deprecated, message: "It was deprecated in version 1.3 and will be removed in a future version. Please use fetchImage from ImageDownloader instead.")
    @discardableResult
    public func fetchImage(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        completion: @escaping (Result<Data>) -> Void
    ) -> RequestToken? {
        return Beagle.dependencies.imageDownloader.fetchImage(url: url, additionalData: additionalData, completion: completion)
    }
    
    // MARK: Private Methods
    
    private func handleFetchComponent(
        _ response: NetworkResponse,
        cachedComponent: Data?,
        url: String
    ) -> Result<ServerDrivenComponent> {
        if
            let cached = cachedComponent,
            let http = response.response as? HTTPURLResponse,
            http.statusCode == 304
        {
            return decodeComponent(from: cached)
        }

        let decoded = decodeComponent(from: response.data)
        if case .success = decoded {
            networkCache.saveCacheIfPossible(url: url, response: response)
        }
        return decoded
    }

    //TODO: change loadFromTextError inside guard let to give a more proper error
    private func decodeComponent(from data: Data) -> Result<ServerDrivenComponent> {
        do {
            guard let component = try dependencies.decoder.decodeComponent(from: data) as? ServerDrivenComponent else {
                return .failure(.loadFromTextError)
            }
            return .success(component)
        } catch {
            return .failure(.decoding(error))
        }
    }

    private func handleForm(_ data: Data) -> Result<RawAction> {
        do {
            let action = try dependencies.decoder.decodeAction(from: data)
            return .success(action)
        } catch {
            return .failure(.decoding(error))
        }
    }
}
