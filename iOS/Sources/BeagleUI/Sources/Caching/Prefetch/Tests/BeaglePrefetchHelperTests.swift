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
@testable import BeagleUI
import SnapshotTesting
import BeagleSchema

final class BeaglePrefetchHelperTests: XCTestCase {

    struct Dependencies: DependencyRepository, DependencyCacheManager {
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
        guard let remoteComponent = decodeComponent(from: jsonData) else {
            XCTFail("Could not decode component.")
            return
        }
        let cacheManager = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        let dependencies = Dependencies(cacheManager: cacheManager, repository: RepositoryStub(componentResult: .success(remoteComponent)))
        let sut = BeaglePreFetchHelper(dependencies: dependencies)
        let url = "url-test"

        sut.prefetchComponent(newPath: .init(route: url, shouldPrefetch: true))
        let reference = CacheReference(identifier: url, data: jsonData, hash: "123")
        cacheManager.addToCache(reference)
        
        let result = dependencies.cacheManager?.getReference(identifiedBy: url)
        XCTAssert(result?.data == jsonData, "Retrived wrong component.")
    }
    
    func testPrefetchTheSameScreenTwice() {
        guard let remoteComponent = decodeComponent(from: jsonData) else {
            XCTFail("Could not decode component.")
            return
        }
        let cacheManager = CacheManagerDefault(dependencies: CacheManagerDependencies(), config: CacheManagerDefault.Config(memoryMaximumCapacity: 2, diskMaximumCapacity: 2, cacheMaxAge: 10))
        let dependencies = Dependencies(cacheManager: cacheManager, repository: RepositoryStub(componentResult: .success(remoteComponent)))
        let sut = BeaglePreFetchHelper(dependencies: dependencies)
        let url = "url-test"
        
        let reference = CacheReference(identifier: url, data: jsonData, hash: "123")
        cacheManager.addToCache(reference)

        sut.prefetchComponent(newPath: .init(route: url, shouldPrefetch: true))
        let result1 = dependencies.cacheManager?.getReference(identifiedBy: url)
        sut.prefetchComponent(newPath: .init(route: url, shouldPrefetch: true))
        let result2 = dependencies.cacheManager?.getReference(identifiedBy: url)
        
        XCTAssert(result1?.data == jsonData, "Retrived wrong component.")
        XCTAssert(result2?.data == jsonData, "Retrived wrong component.")
    }

    func testNavigationIsPrefetchable() {
        let path = "path"
        let data = ["data": "value"]
        let container = Container(children: [])

        let actions: [Navigate] = [
            Navigate.openExternalURL("http://localhost"),
            Navigate.openNativeRoute(path, data: nil),
            Navigate.openNativeRoute(path, data: data),

            Navigate.resetApplication(.declarative(Screen(child: container))),
            Navigate.resetApplication(.remote(path, shouldPrefetch: true)),
            Navigate.resetApplication(.remote(path, shouldPrefetch: false)),

            Navigate.resetStack(.declarative(Screen(child: container))),
            Navigate.resetStack(.remote(path, shouldPrefetch: true)),
            Navigate.resetStack(.remote(path, shouldPrefetch: false)),

            Navigate.pushStack(.declarative(Screen(child: container))),
            Navigate.pushStack(.remote(path, shouldPrefetch: true)),
            Navigate.pushStack(.remote(path, shouldPrefetch: false)),

            Navigate.pushView(.declarative(Screen(child: container))),
            Navigate.pushView(.remote(path, shouldPrefetch: true)),
            Navigate.pushView(.remote(path, shouldPrefetch: false)),

            Navigate.popStack,
            Navigate.popView,
            Navigate.popToView(path)
        ]

        let bools = actions.map { $0.newPath }

        let result: String = zip(actions, bools).reduce("") { partial, zip in
            "\(partial)  \(zip.0)  -->  \(descriptionWithoutOptional(zip.1)) \n\n"
        }
        assertSnapshot(matching: result, as: .description)
    }
    
    private func decodeComponent(from data: Data) -> BeagleUI.ServerDrivenComponent? {
        do {
            let component = try decoder.decodeComponent(from: data)
            return component as? BeagleUI.ServerDrivenComponent
        } catch {
            return nil
        }
    }
}
