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

/// HTTP Method to indicate the desired action to be performed for a given resource
public enum HTTPMethod: String, Codable {
    /// The GET method requests a representation of the specified resource. Requests using GET should only retrieve data.
    case get = "GET"
    /// The POST method is used to submit an entity to the specified resource, often causing a change in state or side effects on the server.
    case post = "POST"
    /// The PUT method replaces all current representations of the target resource with the request payload.
    case put = "PUT"
    /// The DELETE method deletes the specified resource.
    case delete = "DELETE"
    /// The HEAD method asks for a response identical to that of a GET request, but without the response body.
    case head = "HEAD"
    /// The PATCH method is used to apply partial modifications to a resource.
    case patch = "PATCH"
    
    func toDeprecatedMethod() -> HttpAdditionalData.Method? {
        switch self {
        case .post: return .POST
        case .put: return .PUT
        case .get: return .GET
        case .delete: return .DELETE
        case .head: return .HEAD
        case .patch: return .PATCH
        }
    }
}

/// HttpAdditionalData can be used on Remote Beagle Screen to pass additional http data on requests
/// triggered by Beagle.
public struct HttpAdditionalData: RemoteScreenAdditionalData {

    public let httpData: HttpData?
    public var headers: [String: String]

    public struct HttpData {
        
        @available(*, deprecated, message: "It was deprecated in version 1.7.0 and will be removed in a future version. Use the httpMethod field instead.")
        public let method: Method
        
        public let httpMethod: HTTPMethod?
        public let body: Data

        public init(httpMethod: HTTPMethod? = .get, body: Data) {
            self.httpMethod = httpMethod
            self.body = body
            self.method = httpMethod?.toDeprecatedMethod() ?? .GET
        }
        
        @available(*, deprecated, message: "It was deprecated in version 1.7.0 and will be removed in a future version. Use the httpMethod field instead.")
        public init(method: Method, body: Data) {
            self.method = method
            self.body = body
            self.httpMethod = method.toMethod()
        }
    }

    public init(
        httpData: HttpData?,
        headers: [String: String] = [:]
    ) {
        self.httpData = httpData
        self.headers = headers
    }
}

extension HttpAdditionalData: Equatable, Decodable {
    
    @available(*, deprecated, message: "It was deprecated in version 1.7.0 and will be removed in a future version. Use the httpMethod field instead.")
    public enum Method: String, Codable {
        case POST, PUT, GET, DELETE, HEAD, PATCH
        
        func toMethod() -> HTTPMethod {
            switch self {
            case .POST: return .post
            case .PUT: return .put
            case .GET: return .get
            case .DELETE: return .delete
            case .HEAD: return .head
            case .PATCH: return .patch
            }
        }
    }
}

extension HttpAdditionalData.HttpData: Equatable, Decodable {
}
