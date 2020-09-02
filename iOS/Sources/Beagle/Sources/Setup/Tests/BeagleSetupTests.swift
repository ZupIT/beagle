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

final class BeagleSetupTests: XCTestCase {
    // swiftlint:disable discouraged_direct_init

    override func setUp() {
        super.setUp()
        BeagleSchema.dependencies = DefaultDependencies()
    }

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
        dep.localFormHandler = LocalFormHandlerSpy()
        if let url = URL(string: "www.test.com") {
            dep.urlBuilder.baseUrl = url
        }
        dep.networkClient = NetworkClientDummy()
        dep.style = { _ in return StyleViewConfiguratorDummy() }
        dep.decoder = ComponentDecodingDummy()
        dep.cacheManager = nil
        dep.windowManager = WindowManagerDumb()
        dep.opener = URLOpenerDumb()
        dep.globalContext = GlobalContextDummy()

        assertSnapshot(matching: dep, as: .dump)
    }

    func test_whenChangingGlobalDependency_itShouldUpdateAllLibs() {
        // Given
        let old = Beagle.dependencies
        let new = BeagleDependencies()

        // When
        Beagle.dependencies = new

        // Then
        XCTAssert(BeagleSchema.dependencies as AnyObject === new)
        XCTAssert(BeagleSchema.dependencies.decoder as AnyObject === new.decoder as AnyObject)
        XCTAssert(BeagleSchema.dependencies.schemaLogger as AnyObject? === new.logger as AnyObject)

        // Teardown
        Beagle.dependencies = old
    }

    func test_ifChangingDependency_othersShouldUseNewInstance() {
        let dependencies = BeagleDependencies()

        let themeSpy = ThemeSpy()
        dependencies.theme = themeSpy

        let view = UIView()
        let styleId = "custom-style"

        dependencies.theme.applyStyle(for: view, withId: styleId)

        XCTAssertEqual(themeSpy.styledView, view)
        XCTAssertEqual(themeSpy.styleApplied, styleId)
    }
}

// MARK: - Testing Helpers

final class DeepLinkHandlerDummy: DeepLinkScreenManaging {
    func getNativeScreen(with path: String, data: [String: String]?) throws -> UIViewController {
        return UIViewController()
    }
}

final class FormDataStoreHandlerDummy: FormDataStoreHandling {
    func formManagerDidSubmitForm(group: String?) { }
    func save(data: [String: String], group: String) { }
    func read(group: String) -> [String: String]? { return nil }
}

final class ComponentDecodingDummy: ComponentDecoding {
    func register<T>(component type: T.Type) where T: RawComponent {}
    func register<A>(action type: A.Type) where A: RawAction {}
    func register<T>(component type: T.Type, named typeName: String) where T: BeagleSchema.RawComponent {}
    func register<A>(action type: A.Type, named typeName: String) where A: BeagleSchema.RawAction {}
    func componentType(forType type: String) -> Decodable.Type? { return nil }
    func actionType(forType type: String) -> Decodable.Type? { return nil }
    func decodeComponent(from data: Data) throws -> BeagleSchema.RawComponent { return ComponentDummy() }
    func decodeAction(from data: Data) throws -> RawAction { return ActionDummy() }
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
    func prefetchComponent(newPath: Route.NewPath) { }
}

struct ComponentDummy: ServerDrivenComponent, CustomStringConvertible {
    
    private let resultView: UIView?
    
    var description: String {
        return "ComponentDummy()"
    }
    
    init(resultView: UIView? = nil) {
        self.resultView = resultView
    }
    
    init(from decoder: Decoder) throws {
        self.resultView = nil
    }
    
    func toView(renderer: BeagleRenderer) -> UIView {
        return resultView ?? UIView()
    }
}

struct ActionDummy: Action, Equatable {
    func execute(controller: BeagleController, origin: UIView) {}
}

struct BeagleScreenDependencies: BeagleDependenciesProtocol {
    var isLoggingEnabled: Bool = true
    var analytics: Analytics?
    var repository: Repository = RepositoryStub()
    var theme: Theme = AppThemeDummy()
    var validatorProvider: ValidatorProvider?
    var preFetchHelper: BeaglePrefetchHelping = PreFetchHelperDummy()
    var appBundle: Bundle = Bundle(for: ImageTests.self)
    var cacheManager: CacheManagerProtocol?
    var decoder: ComponentDecoding = ComponentDecodingDummy()
    var logger: BeagleLoggerType = BeagleLoggerDumb()
    var formDataStoreHandler: FormDataStoreHandling = FormDataStoreHandlerDummy()
    var navigationControllerType = BeagleNavigationController.self
    var schemaLogger: SchemaLogger?
    var urlBuilder: UrlBuilderProtocol = UrlBuilder()
    var networkClient: NetworkClient = NetworkClientDummy()
    var deepLinkHandler: DeepLinkScreenManaging?
    var localFormHandler: LocalFormHandler?
    var navigation: BeagleNavigation = BeagleNavigationDummy()
    var windowManager: WindowManager = WindowManagerDumb()
    var opener: URLOpener = URLOpenerDumb()
    var globalContext: GlobalContext = GlobalContextDummy()

    var renderer: (BeagleController) -> BeagleRenderer = {
        return BeagleRenderer(controller: $0)
    }
    var viewConfigurator: (UIView) -> ViewConfiguratorProtocol = {
        return ViewConfigurator(view: $0)
    }
    var style: (UIView) -> StyleViewConfiguratorProtocol = { _ in
        return StyleViewConfiguratorDummy()
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

class BeagleNavigationDummy: BeagleNavigation {
    var defaultAnimation: BeagleNavigatorAnimation?
    
    func navigate(action: Navigate, controller: BeagleController, animated: Bool) {
        // Intentionally unimplemented...
    }

    func registerDefaultNavigationController(builder: @escaping NavigationBuilder) {
        // Intentionally unimplemented...
    }

    func registerNavigationController(builder: @escaping NavigationBuilder, forId controllerId: String) {
        // Intentionally unimplemented...
    }

    func navigationController(forId controllerId: String?) -> BeagleNavigationController {
        return .init()
    }
}

class GlobalContextDummy: GlobalContext {
    let globalId: String = ""
    let context: Observable<Context> = Observable(value: .init(id: "", value: .empty))
    
    func isGlobal(id: String?) -> Bool { true }
    func setValue(_ value: DynamicObject) {}
}
