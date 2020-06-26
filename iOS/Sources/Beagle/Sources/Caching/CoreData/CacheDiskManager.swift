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
import UIKit

public protocol CacheDiskManagerProtocol {
    func update(_ reference: CacheReference)
    func getReference(for key: String) -> CacheReference?
    func numberOfReferences() -> Int
    func removeLastUsed()
    func clear()
    
    /// call this when there is no more batch changes to perform
    func saveChanges()
}

public class DefaultCacheDiskManager: CacheDiskManagerProtocol {
    // disabling lint because it's CoreData 👌
    // swiftlint:disable force_unwrapping
    // swiftlint:disable force_cast

    public typealias Dependencies =
        DependencyLogger
    
    let dependencies: Dependencies

    lazy var persistentContainer: NSPersistentContainer = {
        let url = Bundle(for: BeagleDependencies.self)
            .url(forResource: "CoreCache", withExtension: "momd")!
        let object = NSManagedObjectModel(contentsOf: url)!
        return NSPersistentContainer(name: "CoreCache", managedObjectModel: object)
    }()

    private var context: NSManagedObjectContext {
        return persistentContainer.viewContext
    }
    
    lazy private var fetchRequest: NSFetchRequest<CacheEntity> = {
        return NSFetchRequest<CacheEntity>(entityName: "CacheEntity")
    }()
    
    // MARK: Init
    
    public init(dependencies: Dependencies) {
        self.dependencies = dependencies

        self.persistentContainer.loadPersistentStores() { _, error in
            if let error = error?.localizedDescription {
                self.dependencies.logger.log(Log.cache(.loadPersistentStores(description: error)))
                assertionFailure("ERROR: Beagle's DiskManager (CoreData) is not working")
            }
        }
    }
    
    // MARK: CacheDiskManagerProtocol

    public func update(_ reference: CacheReference) {
        if let old = getEntity(for: reference.identifier) {
            old.update(with: reference)
        } else {
            addNew(reference)
        }
    }

    public func numberOfReferences() -> Int {
        let request = NSFetchRequest<NSNumber>(entityName: "CacheEntity")
        request.resultType = .countResultType
        do {
            let results = try context.fetch(request)
            return (results.first?.intValue) ?? 0
        } catch {
            dependencies.logger.log(Log.cache(.clear(description: error.localizedDescription)))
            return 0
        }
    }

    public func getReference(for key: String) -> CacheReference? {
        let entity = getEntity(for: key)
        entity?.timeOfCreation = Date()
        try? context.save()
        return entity?.mapToReference()
    }
    
    public func removeLastUsed() {
        let request = fetchRequest
        request.sortDescriptors = [NSSortDescriptor(keyPath: \CacheEntity.timeOfCreation, ascending: true)]
        
        do {
            let result = try context.fetch(request)
            result.first.map { context.delete($0) }
        } catch {
            dependencies.logger.log(Log.cache(.removeData(description: error.localizedDescription)))
            return
        }
    }

    public func saveChanges() {
        guard context.hasChanges else { return }

        do {
            try context.save()
        } catch {
            dependencies.logger.log(Log.cache(.saveContext(description: error.localizedDescription)))
        }
    }
    
    public func clear() {
        let fetchRequest: NSFetchRequest<NSFetchRequestResult> = self.fetchRequest as! NSFetchRequest<NSFetchRequestResult>
        let deleteRequest = NSBatchDeleteRequest(fetchRequest: fetchRequest)

        do {
            try context.execute(deleteRequest)
        } catch {
            dependencies.logger.log(Log.cache(.clear(description: error.localizedDescription)))
        }
    }
    
    // MARK: Privates

    private func addNew(_ reference: CacheReference) {
        if let descriptor = NSEntityDescription.entity(forEntityName: "CacheEntity", in: context),
            let entity = NSManagedObject(entity: descriptor, insertInto: context) as? CacheEntity {
            entity.update(with: reference)
        }
    }

    private func getEntity(for key: String) -> CacheEntity? {
        let request = fetchRequest.copy() as! NSFetchRequest<CacheEntity>
        request.predicate = NSPredicate(format: "id == %@", key)

        do {
            let result = try context.fetch(request)
            return result.first
        } catch {
            dependencies.logger.log(Log.cache(.fetchData(description: error.localizedDescription)))
            return nil
        }
    }
}
