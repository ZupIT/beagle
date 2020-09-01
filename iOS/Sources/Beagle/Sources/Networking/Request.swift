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

public struct Request {
    public let url: URL
    public let type: RequestType
    public let additionalData: RemoteScreenAdditionalData?

    public init(
        url: URL,
        type: RequestType,
        additionalData: RemoteScreenAdditionalData?
    ) {
        self.url = url
        self.type = type
        self.additionalData = additionalData
    }

    public enum RequestType {
        case fetchComponent
        case submitForm(FormData)
        case fetchImage
        case rawRequest(RequestData)
    }

    public struct FormData {
        public let method: FormRemoteAction.Method
        public let values: [String: String]

        public init(
            method: FormRemoteAction.Method,
            values: [String: String]
        ) {
            self.method = method
            self.values = values
        }
    }
    
    public struct RequestData {
        public let method: String?
        public let headers: [String: String]?
        public let body: Any?
        
        public init(
            method: String? = "GET",
            headers: [String: String]? = [:],
            body: Any? = nil
        ) {
            self.method = method
            self.headers = headers
            self.body = body
        }
    }

    public enum Error: Swift.Error {
        case networkError(NetworkError)
        case decoding(Swift.Error)
        case loadFromTextError
        case urlBuilderError
    }
}
