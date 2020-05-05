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

final class BeagleSetupTests: XCTestCase {
    // swiftlint:disable discouraged_direct_init

    func testDefaultDependencies() {
        let dependencies = BeagleDependencies()
        dependencies.appBundle = Bundle()
        assertSnapshot(matching: dependencies, as: .dump)
    }

    func testChangedDependencies() {
        let dep = BeagleDependencies()
        dep.appBundle = Bundle()
        dep.deepLinkHandler = DeepLinkHandlerDummy()
        dep.theme = AppThemeDummy()
        dep.validatorProvider = ValidatorProviding()
        dep.customActionHandler = CustomActionHandlerDummy()
        if let url = URL(string: "www.test.com") {
            dep.urlBuilder.baseUrl = url
        }
        dep.networkClient = NetworkClientDummy()
        dep.flex = { _ in return FlexViewConfiguratorDummy() }
        dep.decoder = ComponentDecodingDummy()
        dep.cacheManager = nil
        dep.logger = BeagleLoggerDumb()

        assertSnapshot(matching: dep, as: .dump)
    }

    func test_ifChangingDependency_othersShouldUseNewInstance() {
        let dependencies = BeagleDependencies()

        let actionSpy = CustomActionHandlerSpy()
        dependencies.customActionHandler = actionSpy

        let dummyAction = CustomAction(name: "", data: [:])

        dependencies.actionExecutor.doAction(
            dummyAction,
            sender: self,
            context: BeagleContextDummy()
        )

        XCTAssert(actionSpy.actionsHandledCount == 1)
    }
}

// MARK: - Testing Helpers

final class DeepLinkHandlerDummy: DeepLinkScreenManaging {
    func getNativeScreen(with path: String, data: [String: String]?) throws -> UIViewController {
        return UIViewController()
    }
}

final class ComponentDecodingDummy: ComponentDecoding {
    func register<T>(_ type: T.Type, for typeName: String) where T: ServerDrivenComponent {}
    func decodableType(forType type: String) -> Decodable.Type? { return nil }
    func decodeComponent(from data: Data) throws -> ServerDrivenComponent { return ComponentDummy() }
    func decodeAction(from data: Data) throws -> Action { return ActionDummy() }
}

final class CacheManagerDummy: CacheManagerProtocol {
    func addToCache(_ reference: CacheReference) { }
    
    func getReference(identifiedBy id: String) -> CacheReference? {
        return nil
    }
    
    func isValid(reference: CacheReference) -> Bool {
        return true
    }
}

final class PreFetchHelperDummy: BeaglePrefetchHelping {
    func prefetchComponent(newPath: Navigate.NewPath) { }
}

struct ComponentDummy: ServerDrivenComponent, Equatable, CustomStringConvertible {
    
    private let uuid = UUID()
    
    var description: String {
        return "ComponentDummy()"
    }
    
    func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        return DummyView()
    }
}

final class DummyView: UIView {}

struct ActionDummy: Action, Equatable {}

struct BeagleScreenDependencies: BeagleScreenViewModel.Dependencies {
    
    var analytics: Analytics?
    var actionExecutor: ActionExecutor
    var flex: FlexViewConfiguratorProtocol
    var repository: Repository
    var theme: Theme
    var validatorProvider: ValidatorProvider?
    var preFetchHelper: BeaglePrefetchHelping
    var appBundle: Bundle
    var cacheManager: CacheManagerProtocol
    var decoder: ComponentDecoding
    var logger: BeagleLoggerType
    var navigationControllerType = BeagleNavigationController.self

    init(
        actionExecutor: ActionExecutor = ActionExecutorDummy(),
        flex: FlexViewConfiguratorProtocol = FlexViewConfiguratorDummy(),
        repository: Repository = RepositoryStub(),
        theme: Theme = AppThemeDummy(),
        validatorProvider: ValidatorProvider = ValidatorProviding(),
        preFetchHelper: BeaglePrefetchHelping = PreFetchHelperDummy(),
        appBundle: Bundle = Bundle(for: ImageTests.self),
        cacheManager: CacheManagerProtocol = CacheManagerDummy(),
        decoder: ComponentDecoding = ComponentDecodingDummy(),
        logger: BeagleLoggerType = BeagleLoggerDumb(),
        analytics: Analytics = AnalyticsExecutorSpy()
    ) {
        self.actionExecutor = actionExecutor
        self.flex = flex
        self.repository = repository
        self.theme = theme
        self.validatorProvider = validatorProvider
        self.preFetchHelper = preFetchHelper
        self.appBundle = appBundle
        self.cacheManager = cacheManager
        self.decoder = decoder
        self.logger = logger
        self.analytics = analytics
    }
}

class NetworkClientDummy: NetworkClient {
    func executeRequest(_ request: Request, completion: @escaping RequestCompletion) -> RequestToken? {
        return nil
    }
}

final class AppThemeDummy: Theme {
    func applyStyle<T>(for view: T, withId id: String) where T: UIView {

    }
}
