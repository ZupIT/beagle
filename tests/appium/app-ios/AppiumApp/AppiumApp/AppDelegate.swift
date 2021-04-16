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

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?
    
    
    func application(_ app: UIApplication, open url: URL,
                     options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        
        BeagleConfig.config()
        
        // extracts the suffix of the link "appiumapp:"
        let deepLinkValue = url.absoluteString
        var bffUrl: String = ""
        if let range = deepLinkValue.range(of: "appiumapp://") {
            bffUrl = String(deepLinkValue[range.upperBound...])
        }
        
        // fixes http pattern
        bffUrl = bffUrl.replacingOccurrences(of: "http//", with: "http://")
        bffUrl = bffUrl.replacingOccurrences(of: "https//", with: "https://")
        
        let deepLinkController = DeepLinkViewController()
        deepLinkController.bffUrl = bffUrl
        let screen = UINavigationController(rootViewController: deepLinkController)
        window = UIWindow(frame: UIScreen.main.bounds)
        window?.rootViewController = screen
        window?.makeKeyAndVisible()
        
        return true
    }
    
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        BeagleConfig.config()
                
        let screen = UINavigationController(rootViewController: MainViewController())
        window = UIWindow(frame: UIScreen.main.bounds)
        window?.rootViewController = screen
        window?.makeKeyAndVisible()
        
        return true
    }
    
}
