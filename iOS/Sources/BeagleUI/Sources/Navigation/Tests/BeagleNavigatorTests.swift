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

final class BeagleNavigatorTests: XCTestCase {

    func test_open_valid_ExternalURL() {

        // Given
        let opener = URLOpenerDumb()
        let action = Navigate.openExternalURL("https://localhost:8080")
        let context = BeagleScreenViewController(component: ComponentDummy())
        let sut = BeagleNavigator(dependencies: NavigatorDependencies(opener: opener))

        // When
        sut.navigate(action: action, context: context)

        // Then
        XCTAssert(opener.hasInvokedTryToOpen == true)
    }
    
    func test_openDeepLink_shouldNotPushANativeScreenToNavigationWhenDeepLinkHandlerItsNotSet() {
        // Given
        let sut = BeagleNavigator(dependencies: NavigatorDependencies())
        let action = Navigate.openNativeRoute("https://example.com/screen.json")
        let context = BeagleScreenViewController(component: ComponentDummy())
        let navigation = BeagleNavigationController(rootViewController: context)
        
        // When
        sut.navigate(action: action, context: context)
        
        //Then
        XCTAssert(navigation.viewControllers.count == 1)
    }

    func test_swapView_shouldReplaceApplicationStackWithRemoteScreen() {

        // Given
        let windowMock = WindowMock()
        let windowManager = WindowManagerDumb(window: windowMock)
        let dependencies = NavigatorDependencies(windowManager: windowManager)
        let sut = BeagleNavigator(dependencies: dependencies)
        let context = BeagleScreenViewController(component: ComponentDummy())

        let resetRemote = Navigate.resetApplication(.remote("https://example.com/screen.json"))

        // When
        sut.navigate(action: resetRemote, context: context)

        // Then
        XCTAssert(windowMock.hasInvokedReplaceRootViewController == true)
    }

    func test_swapView_shouldReplaceApplicationStackWithDeclarativeScreen() {

        // Given
        let windowMock = WindowMock()
        let windowManager = WindowManagerDumb(window: windowMock)
        let dependencies = NavigatorDependencies(windowManager: windowManager)
        let sut = BeagleNavigator(dependencies: dependencies)
        let context = BeagleScreenViewController(component: ComponentDummy())

        let resetDeclarative = Navigate.resetApplication(.declarative(Screen(child: Text("Declarative"))))

        // When
        sut.navigate(action: resetDeclarative, context: context)

        // Then
        XCTAssert(windowMock.hasInvokedReplaceRootViewController == true)
    }

    func test_swapView_shouldReplaceNavigationStack() {
        let swapRemote = Navigate.resetStack(.remote("https://example.com/screen.json"))
        let swapDeclarative = Navigate.resetStack(.declarative(Screen(child: Text("Declarative"))))
        
        swapViewTest(swapRemote)
        swapViewTest(swapDeclarative)
    }
    
    private func swapViewTest(_ navigate: Navigate) {
        let sut = BeagleNavigator(dependencies: NavigatorDependencies())
        let firstViewController = BeagleScreenViewController(component: Text("First"))
        let secondViewController = BeagleScreenViewController(component: Text("Second"))
        let navigation = BeagleNavigationController()
        navigation.viewControllers = [firstViewController, secondViewController]
        
        sut.navigate(action: navigate, context: secondViewController)
        
        XCTAssertEqual(1, navigation.viewControllers.count)
        XCTAssert(navigation.viewControllers.last is BeagleScreenViewController)
    }

    func test_addView_shouldPushScreenInNavigation() {
        let addViewRemote = Navigate.pushView(.remote("https://example.com/screen.json"))
        let addViewDeclarative = Navigate.pushView(.declarative(Screen(child: Text("Declarative"))))
        
        addViewTest(addViewRemote)
        addViewTest(addViewDeclarative)
    }
    
    private func addViewTest(_ navigate: Navigate) {
        let sut = BeagleNavigator(dependencies: NavigatorDependencies())
        let firstViewController = BeagleScreenViewController(component: Text("First"))
        let navigation = BeagleNavigationController(rootViewController: firstViewController)
        
        sut.navigate(action: navigate, context: firstViewController)
        
        XCTAssertEqual(2, navigation.viewControllers.count)
        XCTAssert(navigation.viewControllers.last is BeagleScreenViewController)
    }

    func test_popStack_shouldDismissNavigation() {
        // Given
        let sut = BeagleNavigator(dependencies: NavigatorDependencies())
        let action = Navigate.popStack
        let navigationSpy = UINavigationControllerSpy(
            viewModel: .init(screenType: .declarative(ComponentDummy().toScreen()))
        )

        // When
        sut.navigate(action: action, context: navigationSpy)

        // Then
        XCTAssert(navigationSpy.dismissViewControllerCalled)
    }

    func test_popView_shouldPopNavigationScreen() {
        // Given
        let sut = BeagleNavigator(dependencies: NavigatorDependencies())
        let action = Navigate.popView
        let firstViewController = BeagleScreenViewController(component: Text("First"))
        let secondViewController = BeagleScreenViewController(component: Text("Second"))
        let thirdViewController = BeagleScreenViewController(component: Text("Third"))
        let navigation = BeagleNavigationController()
        navigation.viewControllers = [firstViewController, secondViewController, thirdViewController]

        // When
        sut.navigate(action: action, context: thirdViewController)

        // Then
        XCTAssert(navigation.viewControllers.count == 2)
    }

    func test_popToView_shouldNotNavigateWhenScreenIsNotFound() {
        // Given
        let screenURL1 = "https://example.com/screen1.json"
        let screenURL2 = "https://example.com/screen2.json"
        let screenURL3 = "https://example.com/screen3.json"
        let sut = BeagleNavigator(dependencies: NavigatorDependencies())
        let component = SimpleComponent()
        let action = Navigate.popToView(screenURL1)
        let vc1 = beagleViewController(screen: .declarative(component.content.toScreen()))
        let vc2 = beagleViewController(screen: .remote(.init(url: screenURL2)))
        let vc3 = beagleViewController(screen: .remote(.init(url: screenURL3)))
        let vc4 = UIViewController()
        let navigation = BeagleNavigationController()
        navigation.viewControllers = [vc1, vc2, vc3, vc4]

        // When
        sut.navigate(action: action, context: vc3)

        // Then
        XCTAssert(navigation.viewControllers.count == 4)
        XCTAssert(navigation.viewControllers.last == vc4)
    }

    func test_popToView_shouldRemoveFromStackScreensAfterTargetScreen() {
        // Given
        let screenURL1 = "https://example.com/screen1.json"
        let screenURL2 = "https://example.com/screen2.json"
        let screenURL3 = "https://example.com/screen3.json"
        let sut = BeagleNavigator(dependencies: NavigatorDependencies())
        let action = Navigate.popToView(screenURL2)
        let vc1 = beagleViewController(screen: .remote(.init(url: screenURL1)))
        let vc2 = beagleViewController(screen: .remote(.init(url: screenURL2)))
        let vc3 = beagleViewController(screen: .remote(.init(url: screenURL3)))
        let vc4 = UIViewController()
        let navigation = BeagleNavigationController()
        navigation.viewControllers = [vc1, vc2, vc3, vc4]

        // When
        sut.navigate(action: action, context: vc3)

        // Then
        XCTAssert(navigation.viewControllers.count == 2)
        XCTAssert(navigation.viewControllers.last == vc2)
    }
    
    func test_popToView_absoluteURL() {
        let dependecies = BeagleDependencies()
        dependecies.urlBuilder.baseUrl = URL(string: "https://server.com/path/")
        let sut = BeagleNavigator(dependencies: dependecies)
        let screen = beagleViewController(screen: .remote(.init(url: "/screen")))

        let navigation = BeagleNavigationController()
        let stack = [screen, BeagleScreenViewController(component: ComponentDummy()), BeagleScreenViewController(component: ComponentDummy())]
        navigation.viewControllers = stack
        
        sut.navigate(
            action: Navigate.popToView("https://server.com/path/screen"),
            context: screen
        )
        XCTAssert(navigation.viewControllers.last == screen)
        
        navigation.viewControllers = stack
        sut.navigate(
            action: Navigate.popToView("/screen"),
            context: BeagleContextDummy(viewController: stack[2])
        )
        XCTAssert(navigation.viewControllers.last == screen)
    }
    
    func test_popToView_byIdentifier() {
        // Given
        let sut = BeagleNavigator(dependencies: NavigatorDependencies())
        let vc1 = beagleViewController(screen: .declarative(Screen(identifier: "1", child: Text("Screen 1"))))
        let vc2 = beagleViewController(screen: .declarative(Screen(identifier: "2", child: Text("Screen 2"))))
        let vc3 = UIViewController()
        let vc4 = beagleViewController(screen: .declarative(Screen(identifier: "4", child: Text("Screen 4"))))
        let action = Navigate.popToView("2")
        
        let context = BeagleContextDummy(viewController: vc4)
        let navigation = UINavigationController()
        navigation.viewControllers = [vc1, vc2, vc3, vc4]
        
        // When
        sut.navigate(action: action, context: context)
        
        // Then
        XCTAssert(navigation.viewControllers.count == 2)
        XCTAssert(navigation.viewControllers.last == vc2)
    }

    func test_pushStack_shouldPresentTheScreen() {
        let presentViewRemote = Navigate.pushStack(.remote("https://example.com/screen.json"))
        let presentViewDeclarative = Navigate.pushStack(.declarative(Screen(child: Text("Declarative"))))
        
        pushStackTest(presentViewRemote)
        pushStackTest(presentViewDeclarative)
    }
    
    private func pushStackTest(_ navigate: Navigate) {
        let sut = BeagleNavigator(dependencies: NavigatorDependencies())
        let navigationSpy = UINavigationControllerSpy(
            viewModel: .init(screenType: .declarative(ComponentDummy().toScreen()))
        )
        
        sut.navigate(action: navigate, context: navigationSpy)
        
        XCTAssert(navigationSpy.presentViewControllerCalled)
    }
    
    func test_openDeepLink_shouldPushANativeScreenWithData() {
        // Given
        let deepLinkSpy = DeepLinkHandlerSpy()
        let dependencies = NavigatorDependencies(deepLinkHandler: deepLinkSpy)
        let sut = BeagleNavigator(dependencies: dependencies)
        
        let data = ["uma": "uma", "dois": "duas"]
        let path = "https://example.com/screen.json"
        let action = Navigate.openNativeRoute(path, data: data)
        let firstViewController = BeagleScreenViewController(component: Text("First"))
        let navigation = BeagleNavigationController(rootViewController: firstViewController)
        
        // When
        sut.navigate(action: action, context: firstViewController)
        
        //Then
        XCTAssertEqual(2, navigation.viewControllers.count)
        XCTAssertEqual(data, deepLinkSpy.calledData)
        XCTAssertEqual(path, deepLinkSpy.calledPath)
    }

    private func beagleViewController(screen: ScreenType) -> BeagleScreenViewController {
        return BeagleScreenViewController(viewModel: .init(
            screenType: screen,
            dependencies: BeagleScreenDependencies()
        ))
    }
}

class DeepLinkHandlerSpy: DeepLinkScreenManaging {
    var calledPath: String?
    var calledData: [String: String]?
    
    func getNativeScreen(with path: String, data: [String: String]?) throws -> UIViewController {
        calledData = data
        calledPath = path
        return UIViewController()
    }
}

class FormManagerDummy: FormManaging {
    func register(form: Form, formView: UIView, submitView: UIView, validatorHandler: ValidatorProvider?) { }
}

class LazyLoadManagerDummy: LazyLoadManaging {
    func lazyLoad(url: String, initialState: UIView) {}
}

class ActionManagerDummy: ActionManaging {
    func register(events: [Event], inView view: UIView) {}
    func doAction(_ action: Action, sender: Any) {}
    func doAnalyticsAction(_ action: AnalyticsClick, sender: Any) {}
}

class BeagleContextDummy: BeagleContext {
    var formManager: FormManaging
    
    var lazyLoadManager: LazyLoadManaging
    
    var actionManager: ActionManaging
    
    let viewController: BeagleScreenViewController
    
    init() {
        self.viewController = BeagleScreenViewControllerDummy(viewModel: .init(screenType: .declarative(ComponentDummy().toScreen())))
        self.formManager = FormManagerDummy()
        self.lazyLoadManager = LazyLoadManagerDummy()
        self.actionManager = ActionManagerDummy()
    }
    
    init(viewController: BeagleScreenViewController) {
        self.viewController = viewController
        self.formManager = FormManagerDummy()
        self.lazyLoadManager = LazyLoadManagerDummy()
        self.actionManager = ActionManagerDummy()
    }
    
    var screenController: BeagleScreenViewController { return viewController }
    
    func doAnalyticsAction(_ action: AnalyticsClick, sender: Any) {}
    func register(form: Form, formView: UIView, submitView: UIView, validatorHandler validator: ValidatorProvider?) {}
    func register(formSubmitEnabledWidget: Widget?, formSubmitDisabledWidget: Widget?) {}
    func lazyLoad(url: String, initialState: UIView) {}
    func doAction(_ action: Action, sender: Any) {}
    func applyLayout() {}
    func register(events: [Event], inView view: UIView) { }
}

struct NavigatorDependencies: BeagleNavigator.Dependencies {
    var deepLinkHandler: DeepLinkScreenManaging?
    var urlBuilder: UrlBuilderProtocol = UrlBuilder()
    var logger: BeagleLoggerType = BeagleLoggerDumb()
    var windowManager: WindowManager = WindowManagerDumb()
    var opener: URLOpener = URLOpenerDumb()
    var navigationControllerType = BeagleNavigationController.self

    init(
        deepLinkHandler: DeepLinkScreenManaging? = nil,
        urlBuilder: UrlBuilderProtocol = UrlBuilder(),
        logger: BeagleLoggerType = BeagleLoggerDumb(),
        windowManager: WindowManager = WindowManagerDumb(),
        opener: URLOpener = URLOpenerDumb()
    ) {
        self.deepLinkHandler = deepLinkHandler
        self.urlBuilder = urlBuilder
        self.logger = logger
        self.windowManager = windowManager
        self.opener = opener
    }
}
