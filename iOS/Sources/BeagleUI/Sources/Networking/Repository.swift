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

    private let cacheHashHeader = "beagle-hash"
    private let serviceMaxCacheAge = "cache-control"
    
    // MARK: Initialization
    
    public init(dependencies: Dependencies) {
        self.dependencies = dependencies
    }
    
    // MARK: Public Methods
    
    @discardableResult
    public func fetchComponent(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        completion: @escaping (Result<ServerDrivenComponent, Request.Error>) -> Void
    ) -> RequestToken? {
        let cache = dependencies.cacheManager?.getReference(identifiedBy: url)

        if let cache = cache, dependencies.cacheManager?.isValid(reference: cache) == true {
            completion(decodeComponent(from: cache.data))
            return nil
        }

        var newData = additionalData
        appendCacheHeaders(cache, to: &newData)
    
        guard let request = handleUrlBuilderRequest(url: url, type: .fetchComponent, additionalData: newData) else {
            completion(.failure(.urlBuilderError))
            return nil
        }

        return dependencies.networkClient.executeRequest(request) { [weak self] result in
            guard let self = self else { return }

            let mapped = result
                .flatMapError { .failure(.networkError($0)) }
                .flatMap { self.handleFetchComponent($0, cachedComponent: cache?.data, url: url) }

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
            saveCacheIfPossible(url: url, response: response)
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

    // MARK: Cache

    private func saveCacheIfPossible(url: String, response: NetworkResponse) {
        guard
            let manager = dependencies.cacheManager,
            let http = response.response as? HTTPURLResponse,
            let hash = http.allHeaderFields[cacheHashHeader] as? String
        else {
            return
        }

        let maxAge = cacheMaxAge(httpHeaders: http.allHeaderFields)
        manager.addToCache(
            CacheReference(identifier: url, data: response.data, hash: hash, maxAge: maxAge)
        )
    }

    private func cacheMaxAge(httpHeaders: [AnyHashable: Any]) -> Int? {
        guard let specifiedAge = httpHeaders[serviceMaxCacheAge] as? String else {
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

    private func appendCacheHeaders(_ cache: CacheReference?, to data: inout RemoteScreenAdditionalData?) {
        if let cache = cache, dependencies.cacheManager?.isValid(reference: cache) != true {
            data?.headers[cacheHashHeader] = cache.hash
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
