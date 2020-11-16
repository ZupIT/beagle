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

import Foundation

class CucumberishInitializer: NSObject {
    @objc class func CucumberishSwiftInit() {
        //A closure that will be executed only before executing any of your features
        beforeStart { () -> Void in
            let stepsDefinitions: [CucumberStepsDefinition] =
                [
                    ButtonSteps(),
                    TabViewSteps(),
                    ImageSteps(),
                    PageViewSteps(),
                    SimpleFormSteps(),
                    ScrollViewSteps(),
                    SendRequestActionSteps(),
                    NavigateSteps(),
                    ContainerSteps(),
                    AddChildrenSteps(),
                    TextInputSteps(),
                    TextSteps(),
                    AlertSteps(),
                    ConditionalActionSteps(),
                    SetContextSteps()
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
