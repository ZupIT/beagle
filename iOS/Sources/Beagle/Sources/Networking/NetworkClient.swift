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

public protocol NetworkClient {
    typealias Error = NetworkError
    typealias NetworkResult = Result<NetworkResponse, NetworkError>
    typealias RequestCompletion = (NetworkResult) -> Void

    @discardableResult
    func executeRequest(
        _ request: Request,
        completion: @escaping RequestCompletion
    ) -> RequestToken?
}

public struct NetworkError: Error, AutoInitiable {
    public let error: Error
    public let data: Data?
    public let request: URLRequest
    public let response: URLResponse?

// sourcery:inline:auto:NetworkError.Init
    public init(
        error: Error,
        data: Data? = nil,
        request: URLRequest,
        response: URLResponse? = nil
    ) {
        self.error = error
        self.data = data
        self.request = request
        self.response = response
    }
// sourcery:end
}

public struct NetworkResponse {
    public let data: Data
    public let response: URLResponse

    public init(data: Data, response: URLResponse) {
        self.data = data
        self.response = response
    }
}

/// Token reference to cancel a request
public protocol RequestToken {
    func cancel()
}

public protocol DependencyNetworkClient {
    var networkClient: NetworkClient { get }
}
