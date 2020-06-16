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

public protocol DependencyNavigationController {
    var navigationControllerType: BeagleNavigationController.Type { get }
}

public protocol BeagleNavigation {
    var defaultAnimation: BeagleNavigatorAnimation? { get set }
    
    func navigate(action: Navigate, controller: BeagleController, animated: Bool)
}

public protocol DependencyNavigation {
    var navigation: BeagleNavigation { get }
}

class BeagleNavigator: BeagleNavigation {
    
    var defaultAnimation: BeagleNavigatorAnimation?
    
    // MARK: - Navigate
    
    func navigate(action: Navigate, controller: BeagleController, animated: Bool = false) {
        controller.dependencies.logger.log(Log.navigation(.didReceiveAction(action)))
        switch action {
        case let .openExternalURL(url):
            openExternalURL(path: url, controller: controller)
        case let .openNativeRoute(route, data, shouldResetApplication):
            openNativeRoute(path: route, controller: controller, data: data, resetApplication: shouldResetApplication, animated: animated)
        case let .resetApplication(route):
            resetApplication(with: route, controller: controller, animated: animated)
        case let .resetStack(route):
            resetStack(with: route, controller: controller, animated: animated)
        case let .pushView(route):
            pushView(with: route, controller: controller, animated: animated)
        case .popView:
            popView(controller: controller, animated: animated)
        case let .popToView(route):
            popToView(identifier: route, controller: controller, animated: animated)
        case let .pushStack(route):
            pushStack(with: route, controller: controller, animated: animated)
        case .popStack:
            popStack(controller: controller, animated: animated)
        }
    }
    
    // MARK: - Navigate Handle
    
    private func openExternalURL(path: String, controller: BeagleController) {
        controller.dependencies.opener.tryToOpen(path: path)
    }
    
    private func openNativeRoute(path: String, controller: BeagleController, data: [String: String]?, resetApplication: Bool, animated: Bool) {
        
        do {
            guard let deepLinkHandler = controller.dependencies.deepLinkHandler else { return }
            let viewController = try deepLinkHandler.getNativeScreen(with: path, data: data)
            
            if let transition = defaultAnimation?.getTransition(.push) {
                controller.navigationController?.view.layer.add(transition, forKey: nil)
            }
            
            if resetApplication {
                controller.dependencies.windowManager.window?.replace(rootViewController: viewController, animated: animated, completion: nil)
            } else {
                controller.navigationController?.pushViewController(viewController, animated: animated)
            }
        } catch {
            controller.dependencies.logger.log(Log.navigation(.didNotFindDeepLinkScreen(path: path)))
            return
        }
    }
    
    private func resetApplication(with type: Route, controller: BeagleController, animated: Bool) {
        controller.dependencies.windowManager.window?.replace(rootViewController: viewControllerToPresent(type), animated: animated, completion: nil)
    }
    
    private func resetStack(with type: Route, controller: BeagleController, animated: Bool) {
        controller.navigationController?.setViewControllers([viewControllerToPresent(type)], animated: animated)
    }
    
    private func pushView(with type: Route, controller: BeagleController, animated: Bool) {
        if let transition = defaultAnimation?.getTransition(.push) {
            controller.navigationController?.view.layer.add(transition, forKey: nil)
        }
        controller.navigationController?.pushViewController(viewControllerToPresent(type), animated: animated)
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
    
    private func pushStack(with type: Route, controller: BeagleController, animated: Bool) {
        let navigationToPresent = controller.dependencies.navigationControllerType.init()
        switch type {
        case let .remote(route, _, fallback):
            navigationToPresent.viewControllers = [viewController(route: route, fallback: fallback)]
        case let .declarative(screen):
            navigationToPresent.viewControllers = [viewController(screen: screen)]
        }
        
        if #available(iOS 13.0, *) {
            navigationToPresent.modalPresentationStyle = defaultAnimation?.modalPresentationStyle ?? .automatic
        } else {
            navigationToPresent.modalPresentationStyle = defaultAnimation?.modalPresentationStyle ?? .fullScreen
        }
        
        if let defaultAnimation = defaultAnimation {
            navigationToPresent.modalTransitionStyle = defaultAnimation.modalTransitionStyle
        }
        
        controller.present(navigationToPresent, animated: animated)
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
