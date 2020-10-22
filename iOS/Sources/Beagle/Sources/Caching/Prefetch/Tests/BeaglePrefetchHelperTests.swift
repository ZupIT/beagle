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

import XCTest
@testable import Beagle
import SnapshotTesting
import BeagleSchema

final class BeaglePrefetchHelperTests: XCTestCase {

    struct Dependencies: BeaglePreFetchHelper.Dependencies {
        var logger: BeagleLoggerType
        var cacheManager: CacheManagerProtocol?
        let repository: Repository
    }
    
    private let decoder = ComponentDecoder()
    private let jsonData = """
    {
      "_beagleComponent_": "beagle:text",
      "text": "cache",
      "style": {
        "backgroundColor": "#4000FFFF"
      }
    }
    """.data(using: .utf8) ?? Data()
    
    func testPrefetchAndDequeue() {
        //Given
        guard let remoteComponent = decodeComponent(from: jsonData) else {
            XCTFail("Could not decode component.")
            return
        }
        let cacheManager = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        let dependencies = Dependencies(logger: BeagleLoggerDumb(), cacheManager: cacheManager, repository: RepositoryStub(componentResult: .success(remoteComponent)))
        let sut = BeaglePreFetchHelper(dependencies: dependencies)
        let url = "url-test"
        let reference = CacheReference(identifier: url, data: jsonData, hash: "123")
        
        //When
        sut.prefetchComponent(newPath: .init(url: url, shouldPrefetch: true))
        cacheManager.addToCache(reference)
        let result = dependencies.cacheManager?.getReference(identifiedBy: url)

        //Then
        XCTAssertEqual(result?.data, jsonData, "Retrieved wrong component.")
    }
    
    func testPrefetchTheSameScreenTwice() {
        //Given
        guard let remoteComponent = decodeComponent(from: jsonData) else {
            XCTFail("Could not decode component.")
            return
        }
        let cacheManager = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        let dependencies = Dependencies(logger: BeagleLoggerDumb(), cacheManager: cacheManager, repository: RepositoryStub(componentResult: .success(remoteComponent)))
        let sut = BeaglePreFetchHelper(dependencies: dependencies)
        let url = "url-test"
        let reference = CacheReference(identifier: url, data: jsonData, hash: "123")
        
        //When
        cacheManager.addToCache(reference)
        sut.prefetchComponent(newPath: .init(url: url, shouldPrefetch: true))
        let result1 = dependencies.cacheManager?.getReference(identifiedBy: url)
        sut.prefetchComponent(newPath: .init(url: url, shouldPrefetch: true))
        let result2 = dependencies.cacheManager?.getReference(identifiedBy: url)
        
        //Then
        XCTAssertEqual(result1?.data, jsonData, "Retrieved wrong component.")
        XCTAssertEqual(result2?.data, jsonData, "Retrieved wrong component.")
    }

    func testNavigationIsPrefetchable() {
        //Given //When
        let path = "path"
        let data = ["data": "value"]
        let container = Container(children: [])

        let actions: [Navigate] = [
            Navigate.openExternalURL("http://localhost"),
            Navigate.openNativeRoute(.init(route: path)),
            Navigate.openNativeRoute(.init(route: path, data: data)),

            Navigate.resetApplication(.declarative(Screen(child: container))),
            Navigate.resetApplication(.remote(.init(url: path, shouldPrefetch: true))),
            Navigate.resetApplication(.remote(.init(url: path, shouldPrefetch: false))),

            Navigate.resetStack(.declarative(Screen(child: container))),
            Navigate.resetStack(.remote(.init(url: path, shouldPrefetch: true))),
            Navigate.resetStack(.remote(.init(url: path, shouldPrefetch: false))),

            Navigate.pushStack(.declarative(Screen(child: container))),
            Navigate.pushStack(.remote(.init(url: path, shouldPrefetch: true))),
            Navigate.pushStack(.remote(.init(url: path, shouldPrefetch: false))),
            
            Navigate.pushStack(.declarative(Screen(child: container)), controllerId: "customId"),
            Navigate.pushStack(.remote(.init(url: path, shouldPrefetch: true)), controllerId: "customId"),
            Navigate.pushStack(.remote(.init(url: path, shouldPrefetch: false)), controllerId: "customId"),

            Navigate.pushView(.declarative(Screen(child: container))),
            Navigate.pushView(.remote(.init(url: path, shouldPrefetch: true))),
            Navigate.pushView(.remote(.init(url: path, shouldPrefetch: false))),

            Navigate.popStack,
            Navigate.popView,
            Navigate.popToView(path)
        ]
        let bools = actions.map { $0.newPath }
        let result: String = zip(actions, bools).reduce("") { partial, zip in
            "\(partial)  \(zip.0)  -->  \(descriptionWithoutOptional(zip.1)) \n\n"
        }
        
        //Then
        assertSnapshot(matching: result, as: .description)
    }
    
    func testNavigateWithContextShouldNotPrefetch() {
        // Given
        let cacheManager = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        let dependencies = Dependencies(logger: BeagleLoggerDumb(), cacheManager: cacheManager, repository: RepositoryStub(componentResult: .success(ComponentDummy())))
        let sut = BeaglePreFetchHelper(dependencies: dependencies)

        // When
        sut.prefetchComponent(newPath: .init(url: "@{url}", shouldPrefetch: true))
        
        // Then
        XCTAssertNil(cacheManager.getReference(identifiedBy: "@{url}"))
    }

    private func decodeComponent(from data: Data) -> ServerDrivenComponent? {
        do {
            let component = try decoder.decodeComponent(from: data)
            return component as? ServerDrivenComponent
        } catch {
            return nil
        }
    }
}
