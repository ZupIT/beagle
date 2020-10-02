//
//  TestUtils.swift
//  AutomatedTestsUITests
//
//  Created by Lucas Sousa Silva on 21/09/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation

class TestUtils {
    
    static func launchBeagleApplication(url: String) -> XCUIApplication {
        let application = XCUIApplication()
        application.launchEnvironment["InitialUrl"] = url
        application.launch()
        return application
    }
}
