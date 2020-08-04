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
        deepLinkHandler[.LAZY_COMPONENTS_ENDPOINT] = LazyComponentScreen.self
        deepLinkHandler[.PAGE_VIEW_ENDPOINT] = PageViewScreen.self
        deepLinkHandler[.TAB_VIEW_ENDPOINT] = TabViewScreen.self
        deepLinkHandler[.FORM_ENDPOINT] = FormScreen.self
        deepLinkHandler[.CUSTOM_COMPONENT_ENDPOINT] = CustomComponentScreen.self
        deepLinkHandler[.DEEPLINK_ENDPOINT] = ScreenDeepLink.self
        deepLinkHandler[.LIST_VIEW_ENDPOINT] = ListViewScreen.self
        deepLinkHandler[.WEB_VIEW_ENDPOINT] = WebViewScreen.self
        deepLinkHandler[.COMPONENT_INTERACTION_ENDPOINT] = ComponentInteractionText.self
        deepLinkHandler[.SIMPLE_FORM_ENDPOINT] = SimpleFormScreen.self

        let validator = ValidatorProviding()
        validator[FormScreen.textValidatorName] = FormScreen.textValidator
        
        let dependencies = BeagleDependencies()
        dependencies.theme = AppTheme.theme
        dependencies.urlBuilder = UrlBuilder(baseUrl: URL(string: .BASE_URL))
        dependencies.navigation.defaultAnimation = .init(pushTransition: .init(type: .fade, subtype: .fromRight, duration: 0.1), modalPresentationStyle: .formSheet)
        dependencies.deepLinkHandler = deepLinkHandler
        dependencies.validatorProvider = validator
        dependencies.analytics = AnalyticsMock()
        dependencies.isLoggingEnabled = true
        dependencies.navigationControllerType = CustomBeagleNavigationController.self
        
        registerCustomComponents(in: dependencies)
        
        Beagle.dependencies = dependencies
        
        let rootViewController = MainScreen().screenController()
        window?.rootViewController = rootViewController
        
        return true
    }
    
    private func registerCustomComponents(in dependencies: BeagleDependencies) {
        dependencies.decoder.register(component: DSCollection.self)
        dependencies.decoder.register(component: MyComponent.self)
        dependencies.decoder.register(action: CustomConsoleLogAction.self)
        dependencies.decoder.register(component: DemoTextField.self, named: "SampleTextField")
    }
}
