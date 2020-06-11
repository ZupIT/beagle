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
import BeagleUI

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

        let validator = ValidatorProviding()
        validator[FormScreen.textValidatorName] = FormScreen.textValidator
        
        let dependencies = BeagleDependencies()
        dependencies.theme = AppTheme.theme
        dependencies.urlBuilder = UrlBuilder(baseUrl: URL(string: .BASE_URL))
        dependencies.deepLinkHandler = deepLinkHandler
        dependencies.validatorProvider = validator
        dependencies.analytics = AnalyticsMock()
        Beagle.dependencies = dependencies
        
        registerCustomComponents()
        
        let rootViewController = MainScreen().screenController()
        window?.rootViewController = rootViewController
        
        return true
    }
    
    private func registerCustomComponents() {
        Beagle.registerCustomComponent("DSCollection", componentType: DSCollection.self)
        Beagle.registerCustomComponent("SampleTextField", componentType: DemoTextField.self)
    }
}
