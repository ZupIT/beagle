//
//  AutomatedTestsUITests.swift
//  AutomatedTestsUITests
//
//  Created by Lucas Sousa Silva on 07/07/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation

class CucumberishInitializer: NSObject {
    @objc class func CucumberishSwiftInit() {
        //A closure that will be executed only before executing any of your features
        beforeStart { () -> Void in
            let stepsDefinitions: [CucumberStepsDefinition] =
                [
                    ButtonScreenSteps(),
                    TabViewScreenSteps(),
                    ImageScreenSteps(),
                    PageViewScreenSteps(),
                    SimpleFormScreenSteps(),
                    NavigationActionSteps()
                ]
            for stepsDefinition in stepsDefinitions {
                stepsDefinition.loadSteps()
            }
        }
        
        let bundle = Bundle(for: CucumberishInitializer.self)

        let tags = getTags()
        Cucumberish.executeFeatures(inDirectory: "Features", from: bundle, includeTags: tags, excludeTags: nil)
    }
    
    class func waitForElementToAppear(_ element: XCUIElement) {
        let result = element.waitForExistence(timeout: 10)
        guard result else {
            XCTFail("Element does not appear")
            return
        }
    }

    fileprivate class func getTags() -> [String]? {
        var itemsTags: [String]?
        for i in ProcessInfo.processInfo.arguments {
            if i.hasPrefix("-Tags:") {
                let newItems = i.replacingOccurrences(of: "-Tags:", with: "")
                itemsTags = newItems.components(separatedBy: ",")
            }
        }
        return itemsTags
    }
    
}
