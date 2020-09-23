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
import SnapshotTesting

class BeagleViewTests: XCTestCase {

    func testControllerChildren() {
        // Given
        let text = Text("I'm a text")
        let beagleView = BeagleView(text)
        let viewController = UIViewController()
        
        // Then
        XCTAssertTrue(viewController.children.isEmpty)
        
        // When
        viewController.view.addSubview(beagleView)
        beagleView.didMoveToWindow()
        
        // Then
        XCTAssertFalse(viewController.children.isEmpty)
    }
    
    func testViewLayout() {
        // Given
        let text = Text("I'm a text")
        let beagleView = BeagleView(text)
        let viewController = UIViewController()
        viewController.view.backgroundColor = .white
        
        // When
        viewController.view.addSubview(beagleView)
        beagleView.didMoveToWindow()
        beagleView.anchorTo(superview: viewController.view)
        
        // Then
        assertSnapshotImage(viewController, size: .custom(CGSize(width: 150, height: 125)))
    }
}
