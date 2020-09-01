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

@available(*, deprecated, message: "use functionality from BeagleNavigation to customize BeagleNavigationController type")
public protocol DependencyNavigationController {
    var navigationControllerType: BeagleNavigationController.Type { get }
}

public protocol BeagleNavigation {
    var defaultAnimation: BeagleNavigatorAnimation? { get set }
    
    func navigate(action: Navigate, controller: BeagleController, animated: Bool)

    typealias NavigationBuilder = () -> BeagleNavigationController

    /// Register the default `BeagleNavigationController` to be used when creating a new navigation flow.
    /// - Parameter builder: will be called when a `BeagleNavigationController` custom type needs to be used.
    func registerDefaultNavigationController(builder: @escaping NavigationBuilder)

    /// Register a `BeagleNavigationController` to be used when creating a new navigation flow with the associated `controllerId`.
    /// - Parameters:
    ///   - builder: will be called when a `BeagleNavigationController` custom type needs to be used.
    ///   - controllerId: the cross platform id that identifies the controller in the BFF.
    func registerNavigationController(builder: @escaping NavigationBuilder, forId controllerId: String)

    /// You can use this to the right `BeagleNavigationController` instance depending on the `controllerId` used
    /// - Parameter controllerId: if nil, will return the default navigation controller type
    func navigationController(forId controllerId: String?) -> BeagleNavigationController
}

public protocol DependencyNavigation {
    var navigation: BeagleNavigation { get }
}

class BeagleNavigator: BeagleNavigation {

    var defaultAnimation: BeagleNavigatorAnimation?
    
    private var builders: [String: NavigationBuilder] = [:]
    private var defaultBuilder: NavigationBuilder?
    
    // MARK: - Public Methods

    // MARK: Navigate
    
    func navigate(action: Navigate, controller: BeagleController, animated: Bool = false) {
        controller.dependencies.logger.log(Log.navigation(.didReceiveAction(action)))
        switch action {
        case let .openExternalURL(url):
            openExternalURL(path: url, controller: controller)
        case let .openNativeRoute(nativeRoute):
            openNativeRoute(controller: controller, animated: animated, nativeRoute: nativeRoute)
        case let .resetApplication(route):
            navigate(route: route, origin: controller, animated: animated, transition: resetApplication(origin:destination:animated:))
        case let .resetStack(route):
            navigate(route: route, origin: controller, animated: animated, transition: resetStack(origin:destination:animated:))
        case let .pushView(route):
            navigate(route: route, origin: controller, animated: animated, transition: pushView(origin:destination:animated:))
        case .popView:
            popView(controller: controller, animated: animated)
        case let .popToView(route):
            popToView(identifier: route, controller: controller, animated: animated)
        case let .pushStack(route, controllerId):
            navigate(route: route,
                     origin: controller,
                     animated: animated) { [weak self] origin, destination, animated in
                self?.pushStack(origin: origin, destination: destination, controllerId: controllerId, animated: animated)
            }
        case .popStack:
            popStack(controller: controller, animated: animated)
        }
    }
    
    // MARK: - Register

    func registerDefaultNavigationController(builder: @escaping NavigationBuilder) {
        defaultBuilder = builder
    }

    func registerNavigationController(builder: @escaping NavigationBuilder, forId controllerId: String) {
        builders[controllerId] = builder
    }

    func navigationController(forId controllerId: String? = nil) -> BeagleNavigationController {
        if let id = controllerId, let builder = builders[id] {
            return builder()
        } else {
            if let providedBuilder = defaultBuilder {
                return providedBuilder()
            } else {
                return dependencies.navigationControllerType.init()
            }
        }
    }
    
    // MARK: - Private Methods

    // MARK: Navigate Handle

    private typealias Transition = (BeagleController, UIViewController, Bool) -> Void
    
    private func navigate(route: Route, origin: BeagleController, animated: Bool, transition: @escaping Transition) {
        viewController(
            route: route,
            origin: origin,
            retry: { [weak origin] in
                guard let origin = origin else { return }
                self.navigate(route: route, origin: origin, animated: animated, transition: transition)
            },
            success: {
                transition(origin, $0, animated)
            }
        )
    }
    
    private func openExternalURL(path: String, controller: BeagleController) {
        controller.dependencies.opener.tryToOpen(path: path)
    }
    
    private func openNativeRoute(controller: BeagleController, animated: Bool, nativeRoute: Navigate.OpenNativeRoute) {
        do {
            guard let deepLinkHandler = controller.dependencies.deepLinkHandler else { return }
            let viewController = try deepLinkHandler.getNativeScreen(with: nativeRoute.route, data: nativeRoute.data)
            
            if let transition = defaultAnimation?.getTransition(.push) {
                controller.navigationController?.view.layer.add(transition, forKey: nil)
            }
            
            if nativeRoute.shouldResetApplication {
                controller.dependencies.windowManager.window?.replace(rootViewController: viewController, animated: animated, completion: nil)
            } else {
                controller.navigationController?.pushViewController(viewController, animated: animated)
            }
        } catch {
            controller.dependencies.logger.log(Log.navigation(.didNotFindDeepLinkScreen(path: nativeRoute.route)))
            return
        }
    }
    
    private func resetApplication(origin: BeagleController, destination: UIViewController, animated: Bool) {
        origin.dependencies.windowManager.window?.replace(rootViewController: destination, animated: animated, completion: nil)
    }
    
    private func resetStack(origin: BeagleController, destination: UIViewController, animated: Bool) {
        origin.navigationController?.setViewControllers([destination], animated: animated)
    }
    
    private func pushView(origin: BeagleController, destination: UIViewController, animated: Bool) {
        if let transition = defaultAnimation?.getTransition(.push) {
            origin.navigationController?.view.layer.add(transition, forKey: nil)
        }
        origin.navigationController?.pushViewController(destination, animated: animated)
    }
    
    private func popView(controller: BeagleController, animated: Bool) {
        if controller.navigationController?.viewControllers.count == 1 {
            controller.dependencies.logger.log(Log.navigation(.errorTryingToPopScreenOnNavigatorWithJustOneScreen))
        }
        if let transition = defaultAnimation?.getTransition(.pop) {
            controller.navigationController?.view.layer.add(transition, forKey: nil)
        }
        controller.navigationController?.popViewController(animated: animated)
    }
    
    private func popToView(identifier: String, controller: BeagleController, animated: Bool) {
        guard let viewControllers = controller.navigationController?.viewControllers else {
            assertionFailure("Trying to pop when there is nothing to pop"); return
        }

        let last = viewControllers.last {
            isViewController($0, identifiedBy: identifier)
        }

        guard let target = last else {
            controller.dependencies.logger.log(Log.navigation(.cantPopToAlreadyCurrentScreen(identifier: identifier)))
            return
        }
        if let transition = defaultAnimation?.getTransition(.pop) {
            controller.navigationController?.view.layer.add(transition, forKey: nil)
        }
        controller.navigationController?.popToViewController(target, animated: animated)
    }
    
    private func pushStack(origin: BeagleController, destination: UIViewController, controllerId: String? = nil, animated: Bool) {
        let navigationToPresent = navigationController(forId: controllerId)
        navigationToPresent.viewControllers = [destination]
        
        if #available(iOS 13.0, *) {
            navigationToPresent.modalPresentationStyle = defaultAnimation?.modalPresentationStyle ?? .automatic
        } else {
            navigationToPresent.modalPresentationStyle = defaultAnimation?.modalPresentationStyle ?? .fullScreen
        }
        
        if let defaultAnimation = defaultAnimation {
            navigationToPresent.modalTransitionStyle = defaultAnimation.modalTransitionStyle
        }
        
        origin.present(navigationToPresent, animated: animated)
    }
    
    private func popStack(controller: UIViewController, animated: Bool) {
        controller.dismiss(animated: animated)
    }
    
    // MARK: Utils
    
    private func isViewController(
        _ viewController: UIViewController,
        identifiedBy identifier: String
    ) -> Bool {
        guard let controller = viewController as? BeagleController else {
            return false
        }
        switch controller.screenType {
        case .remote(let remote):
            let urlBuilder = controller.dependencies.urlBuilder
            let expectedUrl = absoluteURL(for: identifier, builder: urlBuilder)
            let screenUrl = absoluteURL(for: remote.url, builder: urlBuilder)
            return screenUrl == expectedUrl
        case .declarative(let screen):
            return screen.id == identifier
        case .declarativeText:
            return controller.screen?.id == identifier
        }
    }
    
    private func absoluteURL(for path: String, builder: UrlBuilderProtocol) -> String? {
        return builder.build(path: path)?.absoluteString
    }
    
    private func viewController(
        route: Route,
        origin: BeagleController,
        retry: @escaping BeagleRetry,
        success: @escaping (BeagleScreenViewController) -> Void
    ) {
        switch route {
        case .remote(let newPath):
            remote(path: newPath, origin: origin, retry: retry, success: success)
        case .declarative(let screen):
            success(BeagleScreenViewController(viewModel: .init(
                screenType: .declarative(screen)
            )))
        }
    }
    
    @discardableResult
    private func remote(
        path: Route.NewPath,
        origin: BeagleController,
        retry: @escaping BeagleRetry,
        success: @escaping (BeagleScreenViewController) -> Void
    ) -> RequestToken? {
        
        origin.serverDrivenState = .started
        let remote = ScreenType.Remote(url: path.url, fallback: path.fallback, additionalData: nil)
        
        return BeagleScreenViewController.remote(remote, dependencies: origin.dependencies) {
            [weak origin] result in guard let origin = origin else { return }
            origin.serverDrivenState = .finished
            switch result {
            case .success(let viewController):
                origin.serverDrivenState = .success
                success(viewController)
            case .failure(let error):
                origin.serverDrivenState = .error(.remoteScreen(error), retry)
            }
        }
    }
}
