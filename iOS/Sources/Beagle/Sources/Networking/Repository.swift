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
        completion: @escaping (Result<ServerDrivenComponent, Request.Error>) -> Void
    ) -> RequestToken?

    @discardableResult
    func submitForm(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        data: Request.FormData,
        completion: @escaping (Result<RawAction, Request.Error>) -> Void
    ) -> RequestToken?

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

public final class RepositoryDefault: Repository {
    
    // MARK: Dependencies

    public typealias Dependencies =
        BeagleSchema.DependencyDecoder
        & DependencyNetworkClient
        & DependencyCacheManager
        & DependencyUrlBuilder
        & DependencyLogger

    let dependencies: Dependencies

    private var networkCache: NetworkCache
    
    // MARK: Initialization
    
    public init(dependencies: Dependencies ) {
        self.dependencies = dependencies
        self.networkCache = NetworkCache(dependencies: dependencies)
    }
    
    // MARK: Public Methods
    
    @discardableResult
    public func fetchComponent(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        completion: @escaping (Result<ServerDrivenComponent, Request.Error>) -> Void
    ) -> RequestToken? {
        var newData = additionalData

        let cache = networkCache.checkCache(identifiedBy: url, additionalData: &newData)
        if case .validCache(let data) = cache {
            completion(decodeComponent(from: data))
            return nil
        }
    
        guard let request = handleUrlBuilderRequest(url: url, type: .fetchComponent, additionalData: newData) else {
            completion(.failure(.urlBuilderError))
            return nil
        }

        return dependencies.networkClient.executeRequest(request) { [weak self] result in
            guard let self = self else { return }

            let mapped = result
                .flatMapError { .failure(.networkError($0)) }
                .flatMap { self.handleFetchComponent($0, cachedComponent: cache.data, url: url) }

            DispatchQueue.main.async { completion(mapped) }
        }
    }

    @discardableResult
    public func submitForm(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        data: Request.FormData,
        completion: @escaping (Result<RawAction, Request.Error>) -> Void
    ) -> RequestToken? {
        
        guard let request = handleUrlBuilderRequest(url: url, type: .submitForm(data), additionalData: additionalData)
            else {
            completion(.failure(.urlBuilderError))
            return nil
        }

        return dependencies.networkClient.executeRequest(request) { [weak self] result in
            guard let self = self else { return }

            let mapped = result
                .flatMapError { .failure(.networkError($0)) }
                .flatMap { self.handleForm($0.data) }

            DispatchQueue.main.async { completion(mapped) }
        }
    }

    @discardableResult
    public func fetchImage(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        completion: @escaping (Result<Data, Request.Error>) -> Void
    ) -> RequestToken? {
        
        guard let request = handleUrlBuilderRequest(url: url, type: .fetchImage, additionalData: additionalData) else {
            completion(.failure(.urlBuilderError))
            return nil
        }
        
        return dependencies.networkClient.executeRequest(request) { result in
            let mapped = result
                .flatMapError { .failure(Request.Error.networkError($0)) }
                .map { $0.data }

            DispatchQueue.main.async { completion(mapped) }
        }
    }
    
    // MARK: Private Methods
    
    private func handleFetchComponent(
        _ response: NetworkResponse,
        cachedComponent: Data?,
        url: String
    ) -> Result<ServerDrivenComponent, Request.Error> {
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
    private func decodeComponent(from data: Data) -> Result<ServerDrivenComponent, Request.Error> {
        do {
            guard let component = try dependencies.decoder.decodeComponent(from: data) as? ServerDrivenComponent else {
                return .failure(.loadFromTextError)
            }
            return .success(component)
        } catch {
            return .failure(.decoding(error))
        }
    }

    private func handleForm(_ data: Data) -> Result<RawAction, Request.Error> {
        do {
            let action = try dependencies.decoder.decodeAction(from: data)
            return .success(action)
        } catch {
            return .failure(.decoding(error))
        }
    }
    
    private func handleUrlBuilderRequest(url: String, type: Request.RequestType, additionalData: RemoteScreenAdditionalData?) -> Request? {
        guard let builderUrl = dependencies.urlBuilder.build(path: url) else {
            dependencies.logger.log(Log.network(.couldNotBuildUrl(url: url)))
            return nil
        }
        
        return Request(url: builderUrl, type: type, additionalData: additionalData)
    }
}
