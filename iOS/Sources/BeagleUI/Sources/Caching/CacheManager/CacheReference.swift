/*
* Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at https://mozilla.org/MPL/2.0/.
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
