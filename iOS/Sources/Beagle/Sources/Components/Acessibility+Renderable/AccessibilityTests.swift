//
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

@testable import Beagle
import BeagleSchema

class AccessibilityTest: XCTestCase {
    
    private lazy var configurator: ViewConfiguratorProtocol = ViewConfigurator(view: testView)
    private lazy var testView = UIView()
    private let label = "test label"
    
    func testIfAttributesWereAppliedToNavigationItem() {
        //given
        let accessibility = Accessibility(accessibilityLabel: label, accessible: true)
        let navigationItem = UINavigationItem()

        //when
        ViewConfigurator.applyAccessibility(accessibility, to: navigationItem)

        //then
        XCTAssert(navigationItem.accessibilityLabel == label)
        XCTAssert(navigationItem.isAccessibilityElement)
    }

    func testIfAttributesWereAppliedToBarButtonItem() {
        //given
        let accessibility = Accessibility(accessibilityLabel: label, accessible: true)
        let barButtonItem = UIBarButtonItem()

        //when
        ViewConfigurator.applyAccessibility(accessibility, to: barButtonItem)

        //then
        XCTAssert(barButtonItem.accessibilityLabel == label)
        XCTAssert(barButtonItem.isAccessibilityElement)
    }
    
    func testIfAttributesWereAppliedToView() {
        //given
        let accessibility = Accessibility(accessibilityLabel: label, accessible: true)
        
        //when
        configurator.setup(accessibility: accessibility)
        
        //then
        XCTAssert(testView.accessibilityLabel == label)
        XCTAssert(testView.isAccessibilityElement)
    }
    
    func testIfViewIsNotAccessible() {
        //given
        let accessibility = Accessibility(accessible: false)
        
        //when
        configurator.setup(accessibility: accessibility)
        
        //then
        XCTAssert(testView.isAccessibilityElement == false)
    }
}
