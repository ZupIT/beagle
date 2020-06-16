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
import XCTest
@testable import BeagleUI
import BeagleSchema

final class SpacerTests: XCTestCase {
    
    func test_toView_shouldReturnTheExpectedView() {
        // Given
        let spacer = Spacer(1.0)
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        
        // When
        let view = renderer.render(spacer)
        view.backgroundColor = .blue
        
        // Then
        assertSnapshotImage(view, size: .custom(CGSize(width: 100, height: 100)))
    }
    
    func test_screenWithSpacedButtons() throws {
        let component: ScreenComponent = try componentFromJsonFile(fileName: "Spacer")
        let screen = Beagle.screen(.declarative(component.toScreen()))
        assertSnapshotImage(screen)
    }
}
