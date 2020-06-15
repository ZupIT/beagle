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

import UIKit
import BeagleSchema

public protocol BeagleDependenciesProtocol: DependencyActionExecutor,
    DependencyAnalyticsExecutor,
    DependencyUrlBuilder,
    BeagleSchema.Dependencies,
    DependencyNetworkClient,
    DependencyDeepLinkScreenManaging,
    DependencyCustomActionHandler,
    DependencyNavigationController,
    DependencyNavigation,
    DependencyViewConfigurator,
    DependencyFlexConfigurator,
    RenderableDependencies,
    DependencyWindowManager,
    DependencyURLOpener,
    DependencyCacheManager,
    DependencyFormDataStoreHandler,
    DependencyRenderer {
}

open class BeagleDependencies: BeagleDependenciesProtocol {

    public var urlBuilder: UrlBuilderProtocol
    public var networkClient: NetworkClient
    public var appBundle: Bundle
    public var theme: Theme
    public var validatorProvider: ValidatorProvider?
    public var deepLinkHandler: DeepLinkScreenManaging?
    public var customActionHandler: CustomActionHandler?
    public var actionExecutor: ActionExecutor
    public var repository: Repository
    public var analytics: Analytics?
    public var navigationControllerType: BeagleNavigationController.Type
    public var navigation: BeagleNavigation
    public var preFetchHelper: BeaglePrefetchHelping
    public var cacheManager: CacheManagerProtocol?
    public var formDataStoreHandler: FormDataStoreHandling
    public var logger: BeagleLoggerType
    public var windowManager: WindowManager
    public var opener: URLOpener

    // MARK: BeagleSchema

    public var decoder: ComponentDecoding
    public var schemaLogger: SchemaLogger? { return logger }

    // MARK: Builders

    public var renderer: (BeagleContext, RenderableDependencies) -> BeagleRenderer = {
        return BeagleRenderer(context: $0, dependencies: $1)
    }

    public var flex: (UIView) -> FlexViewConfiguratorProtocol = {
        return FlexViewConfigurator(view: $0)
    }

    public var viewConfigurator: (UIView) -> ViewConfiguratorProtocol = {
        return ViewConfigurator(view: $0)
    }

    private let resolver: InnerDependenciesResolver

    public init() {
        let resolver = InnerDependenciesResolver()
        self.resolver = resolver

        self.urlBuilder = UrlBuilder()
        self.preFetchHelper = BeaglePreFetchHelper(dependencies: resolver)
        self.customActionHandler = nil
        self.appBundle = Bundle.main
        self.theme = AppTheme(styles: [:])
        self.navigationControllerType = BeagleNavigationController.self
        self.logger = BeagleLogger()
        self.decoder = BeagleSchema.DefaultDependencies().decoder
        self.formDataStoreHandler = FormDataStoreHandler()
        self.windowManager = WindowManagerDefault()
        
        self.networkClient = NetworkClientDefault(dependencies: resolver)
        self.navigation = BeagleNavigator(dependencies: resolver)
        self.actionExecutor = ActionExecuting(dependencies: resolver)
        self.repository = RepositoryDefault(dependencies: resolver)
        self.cacheManager = CacheManagerDefault(dependencies: resolver)
        self.opener = URLOpenerDefault(dependencies: resolver)

        self.resolver.container = { [unowned self] in self }
    }
}

// MARK: Resolver

/// This class helps solving the problem of using the same dependency container to resolve
/// dependencies within dependencies.
/// The problem happened because we needed to pass `self` as dependency before `init` has concluded.
/// - Example: see where `resolver` is being used in the `BeagleDependencies` `init`.
private class InnerDependenciesResolver: RepositoryDefault.Dependencies,
    ActionExecuting.Dependencies,
    DependencyNavigationController,
    DependencyDeepLinkScreenManaging,
    DependencyRepository,
    DependencyWindowManager,
    DependencyURLOpener,
    BeagleSchema.DependencyLogger {
    
    var container: () -> BeagleDependenciesProtocol = {
        fatalError("You should set this closure to get the dependencies container")
    }

    var decoder: ComponentDecoding { return container().decoder }
    var schemaLogger: SchemaLogger? { return container().logger }

    var urlBuilder: UrlBuilderProtocol { return container().urlBuilder }
    var networkClient: NetworkClient { return container().networkClient }
    var navigationControllerType: BeagleNavigationController.Type { return container().navigationControllerType }
    var navigation: BeagleNavigation { return container().navigation }
    var deepLinkHandler: DeepLinkScreenManaging? { return container().deepLinkHandler }
    var customActionHandler: CustomActionHandler? { return container().customActionHandler }
    var logger: BeagleLoggerType { return container().logger }
    var cacheManager: CacheManagerProtocol? { return container().cacheManager }
    var repository: Repository { return container().repository }
    var windowManager: WindowManager { return container().windowManager }
    var opener: URLOpener { return container().opener }
}
