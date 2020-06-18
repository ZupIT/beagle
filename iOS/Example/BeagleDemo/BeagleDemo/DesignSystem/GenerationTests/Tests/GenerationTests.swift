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
@testable import BeagleDemo
@testable import BeagleSchema
@testable import BeagleUI

class GenerationTests: XCTestCase {

    override func setUp() {
        super.setUp()
        let dependencies = BeagleDependencies()
        dependencies.decoder = ComponentDecoder()
        Beagle.dependencies = dependencies
        registerDummyComponents()
    }
    
    func testDecodingOfTextContainer() {
        
        let component: TextContainer? = try? componentFromJsonFile(fileName: "TextContainer", decoder: Beagle.dependencies.decoder)
        
        XCTAssertNotNil(component)
        
    }
    
    private func registerDummyComponents() {
        Beagle.registerCustomComponent("TextContainer", componentType: TextContainer.self)
        Beagle.registerCustomComponent("SingleTextContainer", componentType: TextComponentsDefault.self)
        Beagle.registerCustomComponent("CustomActionableContainer", componentType: TextComponentsDefault.self)
        Beagle.registerCustomComponent("TextContainerWithAction", componentType: TextComponentsDefault.self)
        Beagle.registerCustomComponent("SingleCustomActionableContainer", componentType: TextComponentsDefault.self)
        
        Beagle.registerCustomComponent("TextComponentHeaderDefault", componentType: TextComponentHeaderDefault.self)
        Beagle.registerCustomComponent("TextComponentsDefault", componentType: TextComponentsDefault.self)
        Beagle.registerCustomAction("SpecificActionFromContainerDefault", actionType: SpecificActionFromContainerDefault.self)
    }
}
