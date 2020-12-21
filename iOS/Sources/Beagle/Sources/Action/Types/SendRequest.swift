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

public struct SendRequest: Action, AutoInitiableAndDecodable {
    
    public enum HTTPMethod: String, Codable {
        case get = "GET"
        case post = "POST"
        case put = "PUT"
        case patch = "PATCH"
        case delete = "DELETE"
    }
    
    public let url: Expression<String>
    public let method: Expression<SendRequest.HTTPMethod>?
    public let data: DynamicObject?
    public let headers: Expression<[String: String]>?
    public let onSuccess: [Action]?
    public let onError: [Action]?
    public var onFinish: [Action]?
    public let analytics: ActionAnalyticsConfig?
    
// sourcery:inline:auto:SendRequest.Init
    public init(
        url: Expression<String>,
        method: Expression<SendRequest.HTTPMethod>? = nil,
        data: DynamicObject? = nil,
        headers: Expression<[String: String]>? = nil,
        onSuccess: [Action]? = nil,
        onError: [Action]? = nil,
        onFinish: [Action]? = nil,
        analytics: ActionAnalyticsConfig? = nil
    ) {
        self.url = url
        self.method = method
        self.data = data
        self.headers = headers
        self.onSuccess = onSuccess
        self.onError = onError
        self.onFinish = onFinish
        self.analytics = analytics
    }
// sourcery:end
    
    @available(*, deprecated, message: "Since version 1.3, we allow expressions in the parameters method and headers, please consider using the new method for initialization instead.")
    public init(
        url: Expression<String>,
        method: SendRequest.HTTPMethod? = nil,
        data: DynamicObject? = nil,
        headers: [String: String]? = nil,
        onSuccess: [Action]? = nil,
        onError: [Action]? = nil,
        onFinish: [Action]? = nil,
        analytics: ActionAnalyticsConfig? = nil
    ) {
        self.url = url
        self.method = .value(method ?? .get)
        self.data = data
        self.headers = .value(headers ?? ["": ""])
        self.onSuccess = onSuccess
        self.onError = onError
        self.onFinish = onFinish
        self.analytics = analytics
    }
}
