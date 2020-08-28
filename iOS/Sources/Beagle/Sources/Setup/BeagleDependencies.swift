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

public protocol BeagleDependenciesProtocol: BeagleSchema.Dependencies,
    DependencyAnalyticsExecutor,
    DependencyUrlBuilder,
    DependencyNetworkClient,
    DependencyDeepLinkScreenManaging,
    DependencyLocalFormHandler,
    DependencyNavigationController,
    DependencyNavigation,
    DependencyViewConfigurator,
    DependencyStyleViewConfigurator,
    DependencyTheme,
    DependencyValidatorProvider,
    DependencyPreFetching,
    DependencyAppBundle,
    DependencyRepository,
    DependencyLogger,
    DependencyWindowManager,
    DependencyURLOpener,
    DependencyCacheManager,
    DependencyFormDataStoreHandler,
    DependencyRenderer,
    DependencyGlobalContext,
    DependencyLoggingCondition {
}

open class BeagleDependencies: BeagleDependenciesProtocol {

    public var urlBuilder: UrlBuilderProtocol
    public var networkClient: NetworkClient
    public var appBundle: Bundle
    public var theme: Theme
    public var validatorProvider: ValidatorProvider?
    public var deepLinkHandler: DeepLinkScreenManaging?
    public var localFormHandler: LocalFormHandler?
    public var repository: Repository
    public var analytics: Analytics?
    public var navigation: BeagleNavigation
    public var preFetchHelper: BeaglePrefetchHelping
    public var cacheManager: CacheManagerProtocol?
    public var formDataStoreHandler: FormDataStoreHandling
    public var windowManager: WindowManager
    public var opener: URLOpener
    public var globalContext: GlobalContext
    public var isLoggingEnabled: Bool
    
    public var logger: BeagleLoggerType {
        didSet {
            logger = BeagleLoggerProxy(logger: logger, dependencies: self)
        }
    }
    
    // MARK: BeagleSchema

    public var decoder: ComponentDecoding
    public var schemaLogger: SchemaLogger? { return logger }

    // MARK: Builders

    public var renderer: (BeagleController) -> BeagleRenderer = {
        return BeagleRenderer(controller: $0)
    }

    public var style: (UIView) -> StyleViewConfiguratorProtocol = {
        return StyleViewConfigurator(view: $0)
    }

    public var viewConfigurator: (UIView) -> ViewConfiguratorProtocol = {
        return ViewConfigurator(view: $0)
    }

    @available(*, deprecated, message: "use functionality from BeagleNavigation to customize BeagleNavigationController type")
    public var navigationControllerType: BeagleNavigationController.Type = BeagleNavigationController.self

    private let resolver: InnerDependenciesResolver

    public init() {
        let resolver = InnerDependenciesResolver()
        self.resolver = resolver

        self.urlBuilder = UrlBuilder()
        self.preFetchHelper = BeaglePreFetchHelper(dependencies: resolver)
        self.localFormHandler = nil
        self.appBundle = Bundle.main
        self.theme = AppTheme(styles: [:])
        self.isLoggingEnabled = true
        self.logger = BeagleLoggerProxy(logger: BeagleLoggerDefault(), dependencies: resolver)

        self.decoder = BeagleSchema.dependencies.decoder
        self.formDataStoreHandler = FormDataStoreHandler()
        self.windowManager = WindowManagerDefault()
        self.navigation = BeagleNavigator()
        self.globalContext = DefaultGlobalContext()
        
        self.networkClient = NetworkClientDefault(dependencies: resolver)
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
    DependencyDeepLinkScreenManaging,
    DependencyRepository,
    DependencyWindowManager,
    DependencyURLOpener,
    DependencyLoggingCondition,
    BeagleSchema.DependencyLogger {
        
    var container: () -> BeagleDependenciesProtocol = {
        fatalError("You should set this closure to get the dependencies container")
    }

    var decoder: ComponentDecoding { return container().decoder }
    var schemaLogger: SchemaLogger? { return container().logger }
    var urlBuilder: UrlBuilderProtocol { return container().urlBuilder }
    var networkClient: NetworkClient { return container().networkClient }
    var navigation: BeagleNavigation { return container().navigation }
    var deepLinkHandler: DeepLinkScreenManaging? { return container().deepLinkHandler }
    var localFormHandler: LocalFormHandler? { return container().localFormHandler }
    var logger: BeagleLoggerType { return container().logger }
    var cacheManager: CacheManagerProtocol? { return container().cacheManager }
    var repository: Repository { return container().repository }
    var windowManager: WindowManager { return container().windowManager }
    var opener: URLOpener { return container().opener }
    var isLoggingEnabled: Bool { return container().isLoggingEnabled }
}
