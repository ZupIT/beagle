// THIS IS A GENERATED FILE. DO NOT EDIT THIS
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

public struct SendRequest: RawAction, AutoDecodable {

    public var url: Expression<String>
    public var method: Expression<HTTPMethod>?
    public var data: DynamicObject?
    public var headers: Expression<[String : String]>?
    public var onSuccess: [RawAction]?
    public var onError: [RawAction]?
    public var onFinish: [RawAction]?

    public init(
        url: Expression<String>,
        method: Expression<HTTPMethod>? = nil,
        data: DynamicObject? = nil,
        headers: Expression<[String : String]>? = nil,
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
    
    public enum HTTPMethod: String, Decodable {
    
        case get = "GET"
        case post = "POST"
        case put = "PUT"
        case patch = "PATCH"
        case delete = "DELETE"
    
    }

}
