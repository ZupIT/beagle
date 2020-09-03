//
//  BeagleConfig.swift
//  AutomatedTests
//
//  Created by Lucas Sousa Silva on 07/07/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Beagle
import Foundation

class BeagleConfig {
    
    static func config() {
        let dependencies = BeagleDependencies()
        dependencies.urlBuilder = UrlBuilder(
            baseUrl: URL(string: "http://localhost:8080/")
        )
        dependencies.navigation.registerNavigationController(builder: CustomBeagleNavigationController.init, forId: "CustomBeagleNavigation")
        Beagle.dependencies = dependencies
    }
}
