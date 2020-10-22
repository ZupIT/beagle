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
import Beagle

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        let deepLinkHandler = DeeplinkScreenManager.shared
        deepLinkHandler[.lazyComponentEndpoint] = LazyComponentScreen.self
        deepLinkHandler[.pageViewEndpoint] = PageViewScreen.self
        deepLinkHandler[.tabBarEndpoint] = TabBarScreen.self
        deepLinkHandler[.formEndpoint] = FormScreen.self
        deepLinkHandler[.customComponentEndpoint] = CustomComponentScreen.self
        deepLinkHandler[.screenDeeplinkEndpoint] = ScreenDeepLink.self
        deepLinkHandler[.listViewEndpoint] = ListViewScreen.self
        deepLinkHandler[.webViewEndpoint] = WebViewScreen.self
        deepLinkHandler[.componentInterationEndpoint] = ComponentInteractionText.self
        deepLinkHandler[.conditionActionEndpoint] = ConditionActionText.self
        deepLinkHandler[.simpleFormEndpoint] = SimpleFormScreen.self
        deepLinkHandler[.navigateStep1Endpoint] = NavigateStep1Screen.self
        deepLinkHandler[.navigateStep2Endpoint] = NavigateStep2Screen.self
        deepLinkHandler[.imageEndpoint] = ImageScreen.self
        deepLinkHandler[.globalContextEndpoint] = GlobalContexScreen.self
        deepLinkHandler[.beagleView] = BeagleViewScreen.self

        let validator = ValidatorProviding()
        validator[FormScreen.textValidatorName] = FormScreen.textValidator
        
        let dependencies = BeagleDependencies()
        dependencies.theme = AppTheme.theme
        dependencies.urlBuilder = UrlBuilder(baseUrl: URL(string: .baseURL))
        dependencies.navigation.defaultAnimation = .init(pushTransition: .init(type: .fade, subtype: .fromRight, duration: 0.1), modalPresentationStyle: .formSheet)
        dependencies.deepLinkHandler = deepLinkHandler
        dependencies.validatorProvider = validator
        dependencies.analytics = AnalyticsMock()
        dependencies.isLoggingEnabled = true
        
        registerCustomComponents(in: dependencies)
        registerCustomControllers(in: dependencies)
        
        Beagle.dependencies = dependencies
        
        let mainScreenViewController = MainScreen().screenController()
        mainScreenViewController.title = "Beagle"
        
        let nativeViewController = NativeViewController()
        nativeViewController.title = "Native"
        
        let tabBarController = UITabBarController()
        tabBarController.viewControllers = [mainScreenViewController, UINavigationController(rootViewController: nativeViewController)]
        
        window?.rootViewController = tabBarController
        
        return true
    }
    
    private func registerCustomComponents(in dependencies: BeagleDependencies) {
        dependencies.decoder.register(component: DSCollection.self)
        dependencies.decoder.register(component: MyComponent.self)
        dependencies.decoder.register(action: CustomConsoleLogAction.self)
        dependencies.decoder.register(component: DemoTextField.self, named: "SampleTextField")
    }
    
    private func registerCustomControllers(in dependencies: BeagleDependencies) {
        dependencies.navigation.registerNavigationController(builder: CustomBeagleNavigationController.init, forId: "CustomBeagleNavigation")
        dependencies.navigation.registerNavigationController(builder: CustomPushStackNavigationController.init, forId: "PushStackNavigation")
    }
}
