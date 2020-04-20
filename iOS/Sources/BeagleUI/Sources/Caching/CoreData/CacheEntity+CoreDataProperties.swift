/*
* Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at https://mozilla.org/MPL/2.0/.
*/

import Foundation
import CoreData

public protocol CacheEntityProtocol {}

public class CacheEntity: NSManagedObject, CacheEntityProtocol {}

extension CacheEntity {

    @NSManaged public var id: String
    @NSManaged public var data: Data
    @NSManaged public var beagleHash: String
    @NSManaged public var timeOfCreation: Date
    @NSManaged public var maxAge: Int

    public func update(with reference: CacheReference) {
        id = reference.identifier
        data = reference.data
        beagleHash = reference.hash
        timeOfCreation = reference.timeOfCreation
        maxAge = reference.maxAge ?? 0
    }
}

extension CacheEntity {
    func mapToReference() -> CacheReference {
        return CacheReference(
            identifier: id,
            data: data,
            hash: beagleHash,
            maxAge: maxAge == 0 ? nil : maxAge,
            timeOfCreation: timeOfCreation
        )
    }
}
