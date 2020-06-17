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

import UIKit

public struct SendRequest: RawAction, AutoInitiable {
    
    public enum HTTPMethod: String {
        case get = "GET"
        case post = "POST"
        case put = "PUT"
        case patch = "PATCH"
        case delete = "DELETE"
    }
    
    public let url: String
    public let method: SendRequest.HTTPMethod?
    public let data: DynamicObject?
    public let headers: [String: String]?
    public let onSuccess: [RawAction]?
    public let onError: [RawAction]?
    public let onFinish: [RawAction]?
    
// sourcery:inline:auto:SendRequest.Init
    public init(
        url: String,
        method: SendRequest.HTTPMethod? = nil,
        data: DynamicObject? = nil,
        headers: [String: String]? = nil,
        onSuccess: [RawAction]? = nil,
        onError: [RawAction]? = nil,
        onFinish: [RawAction]? = nil
    ) {
        self.url = url
        self.method = method
        self.data = data
        self.headers = headers
        self.onSuccess = onSuccess
        self.onError = onError
        self.onFinish = onFinish
    }
// sourcery:end
}

extension SendRequest.HTTPMethod: Decodable {}

extension SendRequest: Decodable {
    
    enum CodingKeys: String, CodingKey {
        case url
        case method
        case data
        case headers
        case onSuccess
        case onError
        case onFinish
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.url = try container.decode(String.self, forKey: .url)
        self.method = try container.decode(SendRequest.HTTPMethod.self, forKey: .method)
        self.data = try container.decode(DynamicObject.self, forKey: .data)
        self.headers = try container.decode([String: String].self, forKey: .headers)
        self.onSuccess = try container.decode(forKey: .onSuccess)
        self.onError = try container.decode(forKey: .onError)
        self.onFinish = try container.decode(forKey: .onFinish)
    }
}
