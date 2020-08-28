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
import BeagleSchema

final class BeagleNavigatorTests: XCTestCase {

    func test_open_valid_ExternalURL() {

        // Given
        let opener = URLOpenerDumb()
        let action = Navigate.openExternalURL("https://localhost:8080")
        let dependencies = BeagleScreenDependencies(opener: opener)
        let controller = BeagleControllerStub(dependencies: dependencies)
        let sut = BeagleNavigator()

        // When
        sut.navigate(action: action, controller: controller)

        // Then
        XCTAssert(opener.hasInvokedTryToOpen == true)
    }
    
    func test_openDeepLink_shouldNotPushANativeScreenToNavigationWhenDeepLinkHandlerItsNotSet() {
        // Given
        let sut = BeagleNavigator()
        let action = Navigate.openNativeRoute(.init(route: "https://example.com/screen.json"))
        let controller = BeagleControllerStub()
        let navigation = BeagleNavigationController(rootViewController: controller)
        
        // When
        sut.navigate(action: action, controller: controller)
        
        //Then
        XCTAssert(navigation.viewControllers.count == 1)
    }

    func test_swapView_shouldReplaceApplicationStackWithRemoteScreen() {

        // Given
        let windowMock = WindowMock()
        let windowManager = WindowManagerDumb(window: windowMock)
        let repository = RepositoryStub(componentResult: .success(ComponentDummy()))
        let dependencies = BeagleScreenDependencies(repository: repository, windowManager: windowManager)
        
        let sut = BeagleNavigator()
        let controller = BeagleControllerStub(dependencies: dependencies)

        let resetRemote = Navigate.resetApplication(.remote(.init(url:"https://example.com/screen.json")))

        // When
        sut.navigate(action: resetRemote, controller: controller)

        // Then
        XCTAssert(windowMock.hasInvokedReplaceRootViewController == true)
    }

    func test_swapView_shouldReplaceApplicationStackWithDeclarativeScreen() {

        // Given
        let windowMock = WindowMock()
        let windowManager = WindowManagerDumb(window: windowMock)
        let dependencies = BeagleScreenDependencies(windowManager: windowManager)
        let sut = BeagleNavigator()
        let controller = BeagleControllerStub(dependencies: dependencies)

        let resetDeclarative = Navigate.resetApplication(.declarative(Screen(child: Text("Declarative"))))

        // When
        sut.navigate(action: resetDeclarative, controller: controller)

        // Then
        XCTAssert(windowMock.hasInvokedReplaceRootViewController == true)
    }

    func test_swapView_shouldReplaceNavigationStack() {
        let swapRemote = Navigate.resetStack(.remote(.init(url: "https://example.com/screen.json")))
        let swapDeclarative = Navigate.resetStack(.declarative(Screen(child: Text("Declarative"))))
        
        swapViewTest(swapRemote)
        swapViewTest(swapDeclarative)
    }
    
    private func swapViewTest(_ navigate: Navigate) {
        let sut = BeagleNavigator()
        let firstViewController = UIViewController()
        
        let repository = RepositoryStub(componentResult: .success(ComponentDummy()))
        let dependencies = BeagleScreenDependencies(repository: repository)
        let secondViewController = BeagleControllerStub(dependencies: dependencies)
        let navigation = BeagleNavigationController()
        navigation.viewControllers = [firstViewController, secondViewController]
        
        sut.navigate(action: navigate, controller: secondViewController)
        
        XCTAssertEqual(1, navigation.viewControllers.count)
        XCTAssert(navigation.viewControllers.last is BeagleController)
    }

    func test_addView_shouldPushScreenInNavigation() {
        let addViewRemote = Navigate.pushView(.remote(.init(url: "https://example.com/screen.json")))
        let addViewDeclarative = Navigate.pushView(.declarative(Screen(child: Text("Declarative"))))
        
        addViewTest(addViewRemote)
        addViewTest(addViewDeclarative)
    }
    
    private func addViewTest(_ navigate: Navigate) {
        let sut = BeagleNavigator()
        let repository = RepositoryStub(componentResult: .success(ComponentDummy()))
        let dependencies = BeagleScreenDependencies(repository: repository)
        let firstViewController = BeagleControllerStub(dependencies: dependencies)
        let navigation = BeagleNavigationController(rootViewController: firstViewController)
        
        sut.navigate(action: navigate, controller: firstViewController)
        
        XCTAssertEqual(2, navigation.viewControllers.count)
        XCTAssert(navigation.viewControllers.last is BeagleController)
    }

    func test_popStack_shouldDismissNavigation() {
        // Given
        let sut = BeagleNavigator()
        let action = Navigate.popStack
        let navigationSpy = BeagleControllerNavigationSpy()

        // When
        sut.navigate(action: action, controller: navigationSpy)

        // Then
        XCTAssert(navigationSpy.dismissViewControllerCalled)
    }

    func test_popView_shouldPopNavigationScreen() {
        // Given
        let sut = BeagleNavigator()
        let action = Navigate.popView
        let firstViewController = BeagleControllerStub()
        let secondViewController = UIViewController()
        let thirdViewController = BeagleControllerStub()
        let navigation = BeagleNavigationController()
        navigation.viewControllers = [firstViewController, secondViewController, thirdViewController]

        // When
        sut.navigate(action: action, controller: thirdViewController)

        // Then
        XCTAssert(navigation.viewControllers.count == 2)
    }

    func test_popToView_shouldNotNavigateWhenScreenIsNotFound() {
        
        // Given
        let sut = BeagleNavigator()
        let action = Navigate.popToView("screenURL1")
        let dependencies = BeagleScreenDependencies(urlBuilder: UrlBuilder())
        let vc1 = BeagleControllerStub(dependencies: dependencies)
        let vc2 = BeagleControllerStub(dependencies: dependencies)
        let vc3 = BeagleControllerStub(dependencies: dependencies)
        let vc4 = UIViewController()
        let navigation = BeagleNavigationController()
        navigation.viewControllers = [vc1, vc2, vc3, vc4]

        // When
        sut.navigate(action: action, controller: vc2)

        // Then
        XCTAssertEqual(navigation.viewControllers.count, 4)
        XCTAssertEqual(navigation.viewControllers.last, vc4)
    }

    func test_popToView_shouldRemoveFromStackScreensAfterTargetScreen() {
        // Given
        let screenURL1 = "https://example.com/screen1.json"
        let screenURL2 = "https://example.com/screen2.json"
        let screenURL3 = "https://example.com/screen3.json"
        let sut = BeagleNavigator()
        let action = Navigate.popToView(screenURL2)
        let dependencies = BeagleScreenDependencies(urlBuilder: UrlBuilder())
        let vc1 = BeagleControllerStub(.remote(.init(url: screenURL1)), dependencies: dependencies)
        let vc2 = BeagleControllerStub(.remote(.init(url: screenURL2)), dependencies: dependencies)
        let vc3 = BeagleControllerStub(.remote(.init(url: screenURL3)), dependencies: dependencies)
        let vc4 = UIViewController()
        let navigation = BeagleNavigationController()
        navigation.viewControllers = [vc1, vc2, vc3, vc4]

        // When
        sut.navigate(action: action, controller: vc3)

        // Then
        XCTAssertEqual(navigation.viewControllers.count, 2)
        XCTAssertEqual(navigation.viewControllers.last, vc2)
    }
    
    func test_popToView_absoluteURL() {
        let dependencies = BeagleDependencies()
        dependencies.urlBuilder.baseUrl = URL(string: "https://server.com/path/")
        let sut = BeagleNavigator()
        
        let target = BeagleControllerStub(.remote(.init(url: "/screen")), dependencies: dependencies)
        let declarative = BeagleControllerStub(.declarative(Screen(child: ComponentDummy())), dependencies: dependencies)
        let remote = BeagleControllerStub(.remote(.init(url: "remote")), dependencies: dependencies)
        let current = BeagleControllerStub(.declarativeText("{}"), dependencies: dependencies)

        let navigation = UINavigationController()
        let stack = [target, declarative, remote, current]
        navigation.viewControllers = stack
        
        sut.navigate(
            action: Navigate.popToView("https://server.com/path/screen"),
            controller: current
        )
        XCTAssertEqual(navigation.viewControllers.last, target)
        
        navigation.viewControllers = stack
        sut.navigate(
            action: Navigate.popToView("/screen"),
            controller: current
        )
        XCTAssertEqual(navigation.viewControllers.last, target)
    }
    
    func test_popToView_byIdentifier() {
        // Given
        let sut = BeagleNavigator()
        let vc1 = BeagleControllerStub(.declarative(Screen(id: "1", child: Text("Screen 1"))))
        let vc2 = BeagleControllerStub(.declarative(Screen(id: "2", child: Text("Screen 2"))))
        let vc3 = UIViewController()
        let vc4 = BeagleControllerStub(.declarative(Screen(id: "4", child: Text("Screen 4"))))
        let action = Navigate.popToView("2")
        
        let navigation = UINavigationController()
        navigation.viewControllers = [vc1, vc2, vc3, vc4]
        
        // When
        sut.navigate(action: action, controller: vc4)
        
        // Then
        XCTAssert(navigation.viewControllers.count == 2)
        XCTAssert(navigation.viewControllers.last == vc2)
    }

    func test_pushStack_shouldPresentTheScreen() {
        let presentViewRemote = Navigate.pushStack(.remote(.init(url: "https://example.com/screen.json")))
        let presentViewDeclarative = Navigate.pushStack(.declarative(Screen(child: Text("Declarative"))))
        
        pushStackTest(presentViewRemote)
        pushStackTest(presentViewDeclarative)
    }
    
    private func pushStackTest(_ navigate: Navigate) {
        let sut = BeagleNavigator()
        let repository = RepositoryStub(componentResult: .success(ComponentDummy()))
        let dependencies = BeagleScreenDependencies(repository: repository)
        let navigationSpy = BeagleControllerNavigationSpy(dependencies: dependencies)
        
        sut.navigate(action: navigate, controller: navigationSpy)
        
        XCTAssertNotNil(navigationSpy.viewControllerToPresent)
    }
    
    func test_openDeepLink_shouldPushANativeScreenWithData() {
        // Given
        let deepLinkSpy = DeepLinkHandlerSpy()
        let sut = BeagleNavigator()
        
        let data = ["uma": "uma", "dois": "duas"]
        let path = "https://example.com/screen.json"
        let action = Navigate.openNativeRoute(.init(route: path, data: data))
        let firstViewController = BeagleControllerStub()
        firstViewController.dependencies = BeagleScreenDependencies(deepLinkHandler: deepLinkSpy)
        let navigation = BeagleNavigationController(rootViewController: firstViewController)
        
        // When
        sut.navigate(action: action, controller: firstViewController)
        
        //Then
        XCTAssertEqual(2, navigation.viewControllers.count)
        XCTAssertEqual(data, deepLinkSpy.calledData)
        XCTAssertEqual(path, deepLinkSpy.calledPath)
    }
    
    func testRegisterNavigationController() {
        // Given
        let controllerId = "customId"
        
        // When
        dependencies.navigation.registerNavigationController(builder: BeagleNavigationStub.init, forId: controllerId)
        
        // Then
        XCTAssertTrue(dependencies.navigation.navigationController(forId: controllerId) is BeagleNavigationStub)
    }

    func testDefaultNavigationWithCustom() {
        // Given
        let defaultNavigation = BeagleNavigationStub()
        dependencies.navigation.registerDefaultNavigationController(builder: { defaultNavigation })

        // When
        let result = dependencies.navigation.navigationController(forId: nil)

        // Then
        XCTAssertTrue(result === defaultNavigation)
    }

    func testDefaultNavigationWithDeprecated() {
        // Given
        let dependencies = BeagleDependencies()
        dependencies.navigationControllerType = BeagleNavigationStub.self
        Beagle.dependencies = dependencies

        // When
        let result = dependencies.navigation.navigationController(forId: nil)

        // Then
        XCTAssertTrue(result is BeagleNavigationStub)
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

class BeagleControllerNavigationSpy: BeagleControllerStub {
    private(set) var viewControllerToPresent: UIViewController?
    private(set) var dismissViewControllerCalled = false
    
    override func present(_ viewControllerToPresent: UIViewController, animated flag: Bool, completion: (() -> Void)? = nil) {
        self.viewControllerToPresent = viewControllerToPresent
        super.present(viewControllerToPresent, animated: flag, completion: completion)
    }
    
    override func dismiss(animated flag: Bool, completion: (() -> Void)? = nil) {
        dismissViewControllerCalled = true
        super.dismiss(animated: flag, completion: completion)
    }
}
