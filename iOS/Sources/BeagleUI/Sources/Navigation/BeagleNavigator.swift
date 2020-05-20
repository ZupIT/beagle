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

public protocol BeagleNavigation {
    func navigate(action: Navigate, context: BeagleContext, animated: Bool)
}

public protocol DependencyNavigation {
    var navigation: BeagleNavigation { get }
}

class BeagleNavigator: BeagleNavigation {
    
    typealias Dependencies = DependencyDeepLinkScreenManaging
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
        case .openExternalURL(let path):
            openExternalURL(path: path, source: source)

        case .openNativeRoute(let deepLink):
            if let component = deepLink.component {
                openNativeRoute(component: component, source: source, data: deepLink.data, resetApplication: deepLink.shouldResetApplication, animated: animated)
            } else {
                openNativeRoute(path: deepLink.path, source: source, data: deepLink.data, resetApplication: deepLink.shouldResetApplication, animated: animated)
            }

        case .resetApplication(let type):
            swapWindowViewController(with: type, context: context, animated: animated)

        case .resetStack(let type):
            swapTo(with: type, context: context, animated: animated)

        case .pushView(let type):
            push(with: type, context: context, animated: animated)

        case .pushStack(let type):
            present(with: type, context: context, animated: animated)

        case .popStack:
            popStack(source: source, animated: animated)

        case .popView:
            popView(source: source, animated: animated)

        case .popToView(let path):
            popToView(identifier: path, source: source, animated: animated)
        }
    }
    
    // MARK: - Navigation Methods

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

    private func openNativeRoute(component: ServerDrivenComponent, source: UIViewController, data: [String: String]?, resetApplication: Bool, animated: Bool) {

        let viewController = Beagle.screen(.declarative(Screen(child: component)))
        let navigationToPresent = UINavigationController(rootViewController: viewController)

        if resetApplication {
            dependencies.windowManager.window?.replace(rootViewController: navigationToPresent, animated: animated, completion: nil)
        } else {
            source.navigationController?.present(navigationToPresent, animated: animated, completion: nil)
        }
    }
    
    private func popStack(source: UIViewController, animated: Bool) {
        source.navigationController?.dismiss(animated: animated)
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

    private func isViewController(
        _ viewController: UIViewController,
        identifiedBy identifier: String
    ) -> Bool {
        let screenController = viewController as? BeagleScreenViewController
        guard let screenType = screenController?.viewModel.screenType else {
            return false
        }
        switch screenType {
        case .remote(let remote):
            return absoluteURL(for: remote.url) == absoluteURL(for: identifier)
        case .declarative(let screen):
            return screen.identifier == identifier
        case .declarativeText:
            return screenController?.viewModel.screen?.identifier == identifier
        }
    }
    
    private func absoluteURL(for path: String) -> String? {
        return dependencies.urlBuilder.build(path: path)?.absoluteString
    }

    private func swapTo(with type: Navigate.ScreenType, context: BeagleContext, animated: Bool) {
        let viewControllerToPresent: UIViewController
        switch type {
        case .remote(let path): viewControllerToPresent = viewController(newPath: path)
        case .declarative(let screen): viewControllerToPresent = viewController(screen: screen)
        }
        context.screenController.navigationController?.setViewControllers([viewControllerToPresent], animated: animated)
    }
    private func swapTo(_ viewController: UIViewController, context: BeagleContext, animated: Bool) {
        context.screenController.navigationController?.setViewControllers([viewController], animated: animated)
    }

    private func swapWindowViewController(with type: Navigate.ScreenType, context: BeagleContext, animated: Bool) {
        let viewControllerToPresent: UIViewController
        switch type {
        case .remote(let path): viewControllerToPresent = viewController(newPath: path)
        case .declarative(let screen): viewControllerToPresent = viewController(screen: screen)
        }
        dependencies.windowManager.window?.replace(rootViewController: viewControllerToPresent, animated: animated, completion: nil)
    }

    private func swapWindowViewController(_ viewController: UIViewController, context: BeagleContext, animated: Bool) {
        dependencies.windowManager.window?.replace(rootViewController: viewController, animated: animated, completion: nil)
    }

    private func push(with type: Navigate.ScreenType, context: BeagleContext, animated: Bool) {
        let viewControllerToPresent: UIViewController
        switch type {
        case .remote(let path): viewControllerToPresent = viewController(newPath: path)
        case .declarative(let screen): viewControllerToPresent = viewController(screen: screen)
        }
        context.screenController.navigationController?.pushViewController(viewControllerToPresent, animated: animated)
    }

    private func push(_ viewController: UIViewController, context: BeagleContext, animated: Bool) {
        context.screenController.navigationController?.pushViewController(viewController, animated: animated)
    }

    private func present(with type: Navigate.ScreenType, context: BeagleContext, animated: Bool) {
        let viewControllerToPresent: UIViewController
        switch type {
        case .remote(let path): viewControllerToPresent = viewController(newPath: path)
        case .declarative(let screen): viewControllerToPresent = viewController(screen: screen)
        }
        context.screenController.navigationController?.present(viewControllerToPresent, animated: animated)
    }

    private func present(_ viewController: UIViewController, context: BeagleContext, animated: Bool) {
        context.screenController.navigationController?.present(viewController, animated: animated)
    }
    
    private func viewController(screen: Screen) -> UIViewController {
        return BeagleScreenViewController(viewModel: .init(
            screenType: .declarative(screen)
        ))
    }
    
    private func viewController(newPath: Navigate.NewPath) -> UIViewController {
        return BeagleScreenViewController(viewModel: .init(
            screenType: .remote(.init(
                url: newPath.path,
                fallback: newPath.fallback
            ))
        ))
    }
}
