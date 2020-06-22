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
@testable import BeagleUI
import BeagleSchema

final class FormSubmitTests: XCTestCase {
    
    lazy var controller = BeagleControllerStub()
    lazy var renderer = BeagleRenderer(controller: controller)
    
    func test_toView_shouldReturnTheExpectedView() {
        // Given
        let formSubmit = FormSubmit(child: ComponentDummy())
                
        // When
        let view = renderer.render(formSubmit)
        
        // Then
        XCTAssertTrue(view.subviews.first?.beagleFormElement is FormSubmit)
    }

    func testRenderView() {
        // Given
        let formSubmit = FormSubmit(child: Button(text: "Button"))

        // When
        let view = renderer.render(formSubmit)
        view.frame = CGRect(origin: .zero, size: CGSize(width: 100, height: 100))
        view.style.applyLayout()

        // Then
        assertSnapshotImage(view, size: .inferFromFrame)
    }
}
