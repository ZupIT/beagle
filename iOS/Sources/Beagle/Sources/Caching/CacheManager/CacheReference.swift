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

public struct CacheReference {
    public let identifier: String
    public let data: Data
    public let hash: String
    public let maxAge: Int?
    public let timeOfCreation: Date

    public init(identifier: String, data: Data, hash: String, maxAge: Int? = nil, timeOfCreation: Date = Date()) {
        self.identifier = identifier
        self.data = data
        self.hash = hash
        self.maxAge = maxAge
        self.timeOfCreation = timeOfCreation
    }
}
