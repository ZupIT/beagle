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
import CoreData
import Beagle

public protocol CacheEntityProtocol {}

public class CacheEntity: NSManagedObject, CacheEntityProtocol {}

extension CacheEntity {

    @NSManaged public var id: String
    @NSManaged public var data: Data
    @NSManaged public var beagleHash: String
    @NSManaged public var timeOfCreation: Date

    public func update(with reference: CacheReference) {
        id = reference.identifier
        data = reference.data
        beagleHash = reference.hash
        timeOfCreation = Date()
    }
}

extension CacheEntity {
    func mapToReference() -> CacheReference {
        return CacheReference(
            identifier: id,
            data: data,
            hash: beagleHash,
            maxAge: 0,
            timeOfCreation: timeOfCreation
        )
    }
}
