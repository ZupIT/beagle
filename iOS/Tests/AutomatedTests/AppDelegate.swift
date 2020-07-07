//
//  AppDelegate.swift
//  AutomatedTests
//
//  Created by Lucas Sousa Silva on 07/07/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import UIKit
import Beagle

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        BeagleConfig.config()
        let beagleScreen = Beagle.screen(.remote(.init(url: "/test")))
        window = UIWindow(frame: UIScreen.main.bounds)
        window?.rootViewController = beagleScreen
        window?.makeKeyAndVisible()

        return true
    }

}

