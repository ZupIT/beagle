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

public protocol DependencyNavigationController {
    var navigationControllerType: BeagleNavigationController.Type { get }
}

public protocol BeagleNavigation {
    func navigate(action: Navigate, context: BeagleContext, animated: Bool)
}

public protocol DependencyNavigation {
    var navigation: BeagleNavigation { get }
}

class BeagleNavigator: BeagleNavigation {
    
    typealias Dependencies = DependencyNavigationController
        & DependencyDeepLinkScreenManaging
        & DependencyUrlBuilder
        & DependencyLogger
        & DependencyWindowManager
        & DependencyURLOpener
    
    private let dependencies: Dependencies
    
    init(dependencies: Dependencies) {
        self.dependencies = dependencies
    }
    
    // MARK: - Navigate
    
    func navigate(action: Navigate, context: BeagleContext, animated: Bool = false) {
        dependencies.logger.log(Log.navigation(.didReceiveAction(action)))
        let source = context.screenController
        switch action {
        case let .openExternalURL(url):
            openExternalURL(path: url, source: source)
        case let .openNativeRoute(route, data, shouldResetApplication):
            openNativeRoute(path: route, source: source, data: data, resetApplication: shouldResetApplication, animated: animated)
        case let .resetApplication(route):
            resetApplication(with: route, context: context, animated: animated)
        case let .resetStack(route):
            resetStack(with: route, context: context, animated: animated)
        case let .pushView(route):
            pushView(with: route, context: context, animated: animated)
        case .popView:
            popView(source: source, animated: animated)
        case let .popToView(route):
            popToView(identifier: route, source: source, animated: animated)
        case let .pushStack(route):
            pushStack(with: route, context: context, animated: animated)
        case .popStack:
            popStack(source: source, animated: animated)
        }        
    }
    
    // MARK: - Navigate Handle

    private func openExternalURL(path: String, source: UIViewController) {
        dependencies.opener.tryToOpen(path: path)
    }
        
    private func openNativeRoute(path: String, source: UIViewController, data: [String: String]?, resetApplication: Bool, animated: Bool) {

        do {
            guard let deepLinkHandler = dependencies.deepLinkHandler else { return }
            let viewController = try deepLinkHandler.getNativeScreen(with: path, data: data)

            if resetApplication {
                dependencies.windowManager.window?.replace(rootViewController: viewController, animated: animated, completion: nil)
            } else {
                source.navigationController?.pushViewController(viewController, animated: animated)
            }
        } catch {
            dependencies.logger.log(Log.navigation(.didNotFindDeepLinkScreen(path: path)))
            return
        }
    }
    
    private func resetApplication(with type: Route, context: BeagleContext, animated: Bool) {
        dependencies.windowManager.window?.replace(rootViewController: viewControllerToPresent(type), animated: animated, completion: nil)
    }
    
    private func resetStack(with type: Route, context: BeagleContext, animated: Bool) {
        context.screenController.navigationController?.setViewControllers([viewControllerToPresent(type)], animated: animated)
    }
    
    private func pushView(with type: Route, context: BeagleContext, animated: Bool) {
        context.screenController.navigationController?.pushViewController(viewControllerToPresent(type), animated: animated)
    }
    
    private func popView(source: UIViewController, animated: Bool) {
        if source.navigationController?.viewControllers.count == 1 {
            dependencies.logger.log(Log.navigation(.errorTryingToPopScreenOnNavigatorWithJustOneScreen))
        }
        source.navigationController?.popViewController(animated: animated)
    }
    
    private func popToView(identifier: String, source: UIViewController, animated: Bool) {
        guard let viewControllers = source.navigationController?.viewControllers else {
            assertionFailure("Trying to pop when there is nothing to pop"); return
        }

        let last = viewControllers.last {
            isViewController($0, identifiedBy: identifier)
        }

        guard let target = last else {
            dependencies.logger.log(Log.navigation(.cantPopToAlreadyCurrentScreen(identifier: identifier)))
            return
        }

        source.navigationController?.popToViewController(target, animated: animated)
    }

    private func pushStack(with type: Route, context: BeagleContext, animated: Bool) {
        let navigationToPresent = dependencies.navigationControllerType.init()
        switch type {
        case let .remote(route, _, fallback):
            navigationToPresent.viewControllers = [viewController(route: route, fallback: fallback)]
        case let .declarative(screen):
            navigationToPresent.viewControllers = [viewController(screen: screen)]
        }
        context.screenController.present(navigationToPresent, animated: animated)
    }
    
    private func popStack(source: UIViewController, animated: Bool) {
        source.dismiss(animated: animated)
    }
    
    // MARK: Utils
    
    private func isViewController(
        _ viewController: UIViewController,
        identifiedBy identifier: String
    ) -> Bool {
        let screenController = viewController as? BeagleScreenViewController
        guard let screenType = screenController?.screenType else {
            return false
        }
        switch screenType {
        case .remote(let remote):
            return absoluteURL(for: remote.url) == absoluteURL(for: identifier)
        case .declarative(let screen):
            return screen.identifier == identifier
        case .declarativeText:
            return screenController?.screen?.identifier == identifier
        }
    }
    
    private func absoluteURL(for path: String) -> String? {
        return dependencies.urlBuilder.build(path: path)?.absoluteString
    }
    
    private func viewControllerToPresent(_ type: Route) -> UIViewController {
        let viewControllerToPresent: UIViewController
        switch type {
        case let .remote(route, _, fallback): viewControllerToPresent = viewController(route: route, fallback: fallback)
        case .declarative(let screen): viewControllerToPresent = viewController(screen: screen)
        }
        return viewControllerToPresent
    }
    
    private func viewController(screen: Screen) -> UIViewController {
        return BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(screen)
        ))
    }
    
    private func viewController(route: String, fallback: Screen?) -> UIViewController {
        return BeagleScreenViewController(viewModel: .init(
            screenType: .remote(.init(
                url: route,
                fallback: fallback
            ))
        ))
    }
}
