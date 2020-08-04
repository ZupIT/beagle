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
import BeagleSchema
@testable import Beagle

final class SubmitFormTest: XCTestCase {
    
    func test_ifSubmitFormIsTriggered() {
        // Given
        let controller = BeagleControllerNavigationSpy()
        let renderer = BeagleRenderer(controller: controller)
        let submitFormAction = SubmitForm()
        let simpleForm = SimpleForm(
            onSubmit: [Alert(message: "Hello Beagle!")],
            children: [Button(text: "Test", onPress: [submitFormAction])]
        )
        
        // When
        let resultingView = renderer.render(simpleForm)
        submitFormAction.execute(controller: controller, origin: resultingView)

        // Then
        XCTAssertTrue(controller.viewControllerToPresent is UIAlertController)
    }
    
}
