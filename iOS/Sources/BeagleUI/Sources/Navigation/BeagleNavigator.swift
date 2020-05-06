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
    
    private let dependencies: Dependencies
    
    init(dependencies: Dependencies) {
        self.dependencies = dependencies
    }
    
    // MARK: - Navigate
    
    func navigate(action: Navigate, context: BeagleContext, animated: Bool = false) {
        dependencies.logger.log(Log.navigation(.didReceiveAction(action)))
        let source = context.screenController
        switch action {
        case .openDeepLink(let deepLink):
            if let component = deepLink.component {
                openDeepLink(component: component, source: source, data: deepLink.data, animated: animated)
            } else {
                openDeepLink(path: deepLink.path, source: source, data: deepLink.data, animated: animated)
            }

        case .swapScreen(let screen):
            swapTo(viewController(screen: screen), context: context, animated: animated)
            
        case .swapView(let newPath):
            swapTo(viewController(newPath: newPath), context: context, animated: animated)

        case .addScreen(let screen):
            add(viewController(screen: screen), context: context, animated: animated)
            
        case .addView(let newPath):
            add(viewController(newPath: newPath), context: context, animated: animated)
            
        case .presentScreen(let screen):
            present(viewController(screen: screen), context: context, animated: animated)
            
        case .presentView(let newPath):
            present(viewController(newPath: newPath), context: context, animated: animated)

        case .finishView:
            finishView(source: source, animated: animated)

        case .popView:
            popView(source: source, animated: animated)

        case .popToView(let path):
            popToView(identifier: path, source: source, animated: animated)
        }
    }
    
    // MARK: - Navigation Methods
        
    private func openDeepLink(path: String, source: UIViewController, data: [String: String]?, animated: Bool) {
        guard let deepLinkHandler = dependencies.deepLinkHandler else { return }

        do {
            let viewController = try deepLinkHandler.getNativeScreen(with: path, data: data)
            source.navigationController?.pushViewController(viewController, animated: animated)
        } catch {
            dependencies.logger.log(Log.navigation(.didNotFindDeepLinkScreen(path: path)))
            return
        }
    }

    private func openDeepLink(component: ServerDrivenComponent, source: UIViewController, data: [String: String]?, animated: Bool) {
        let viewController = Beagle.screen(.declarative(Screen(child: component)))
        let navigationToPresent = dependencies.navigationControllerType.init()
        navigationToPresent.viewControllers = [viewController]
        source.navigationController?.present(navigationToPresent, animated: animated, completion: nil)
    }
    
    private func finishView(source: UIViewController, animated: Bool) {
        source.dismiss(animated: animated)
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
    
    private func swapTo(_ viewController: UIViewController, context: BeagleContext, animated: Bool) {
        context.screenController.navigationController?.setViewControllers([viewController], animated: animated)
    }

    private func add(_ viewController: UIViewController, context: BeagleContext, animated: Bool) {
        context.screenController.navigationController?.pushViewController(viewController, animated: animated)
    }

    private func present(_ viewController: UIViewController, context: BeagleContext, animated: Bool) {
        let navigationToPresent = dependencies.navigationControllerType.init()
        navigationToPresent.viewControllers = [viewController]
        context.screenController.present(navigationToPresent, animated: animated)
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
