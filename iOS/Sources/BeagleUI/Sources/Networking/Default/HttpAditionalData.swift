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

/// You can pass this to a Remote Beagle Screen to pass additional http data on requests
/// triggered by Beagle.
public struct HttpAdditionalData: RemoteScreenAdditionalData {

    public let httpData: HttpData?
    public var headers: [String: String]

    public struct HttpData {
        public let method: Method
        public let body: Data

        public init(method: Method, body: Data) {
            self.method = method
            self.body = body
        }
    }

    public enum Method: String {
        case POST, PUT
    }

    public init(
        httpData: HttpData?,
        headers: [String: String] = [:]
    ) {
        self.httpData = httpData
        self.headers = headers
    }
}
