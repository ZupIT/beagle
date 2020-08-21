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
@testable import Beagle

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
        
        XCTAssertNotNil(component?.childrenOfTextContainer.count == 1)
        XCTAssertNotNil(component?.headerOfTextContainer)
        XCTAssertNotNil(component?.actions.count == 3)
    }
    
    func testDecodingOfSingleTextContainerWithoutActions() {
        let component: SingleTextContainer? = try? componentFromJsonFile(fileName: "SingleTextContainerWithoutActions", decoder: Beagle.dependencies.decoder)
        
        XCTAssertNotNil(component?.firstTextContainer)
        XCTAssertNotNil(component?.secondTextContainer)
        XCTAssertNotNil(component?.child)
        XCTAssertNil(component?.actions)
    }
    
    func testDecodingOfSingleTextContainer() {
        let component: SingleTextContainer? = try? componentFromJsonFile(fileName: "SingleTextContainer", decoder: Beagle.dependencies.decoder)
        
        XCTAssertNotNil(component?.firstTextContainer)
        XCTAssertNotNil(component?.secondTextContainer)
        XCTAssertNotNil(component?.child)
        XCTAssertNotNil(component?.actions?.count == 1)
    }
    
    func testDecodingOfSingleCustomActionableContainer() {
        let component: SingleCustomActionableContainer? = try? componentFromJsonFile(fileName: "SingleCustomActionableContainer", decoder: Beagle.dependencies.decoder)
        
        XCTAssertNotNil(component?.action)
        XCTAssertNotNil(component?.child)
    }
    
    func testDecodingOfCustomActionableContainer() {
        let component: CustomActionableContainer? = try? componentFromJsonFile(fileName: "CustomActionableContainer", decoder: Beagle.dependencies.decoder)
        
        XCTAssertNotNil(component?.child.count == 2)
        XCTAssertNotNil(component?.verySpecificAction)
    }
    
    func testDecodingOfTextContainerWithAction() {
        let component: TextContainerWithAction? = try? componentFromJsonFile(fileName: "TextContainerWithAction", decoder: Beagle.dependencies.decoder)
        
        XCTAssertNotNil(component?.childrenOfTextContainer)
        XCTAssertNotNil(component?.action)
        XCTAssertNotNil(component?.secondAction)
    }
    
    func testDecodingOfTextContainerMissingAction() {
        let component: TextContainerWithAction? = try? componentFromJsonFile(fileName: "TextContainerMissingSecondAction", decoder: Beagle.dependencies.decoder)
        
        XCTAssertNotNil(component?.childrenOfTextContainer)
        XCTAssertNotNil(component?.action)
        XCTAssertNil(component?.secondAction)
    }
    
    private func registerDummyComponents() {
        dependencies.decoder.register(component: TextContainer.self)
        dependencies.decoder.register(component: SingleTextContainer.self)
        dependencies.decoder.register(component: CustomActionableContainer.self)
        dependencies.decoder.register(component: TextContainerWithAction.self)
        dependencies.decoder.register(component: SingleCustomActionableContainer.self)
        dependencies.decoder.register(component: TextComponentHeaderDefault.self)
        dependencies.decoder.register(component: TextComponentsDefault.self)
        dependencies.decoder.register(action: ActionDummyDefault.self)
    }
}
