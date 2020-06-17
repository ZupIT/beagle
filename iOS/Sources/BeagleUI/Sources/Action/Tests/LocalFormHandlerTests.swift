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

final class LocalFormHandlerTests: XCTestCase {
    
    func test_whenHandleFormLocalAction_shouldCallHandler() {
        // Given
        let actionName = "action-name"
        let action = FormLocalAction(name: actionName, data: [:])
        let sut = LocalFormHandling()
        var didHandleActioin = false
        sut[actionName] = { _, _, _ in
            didHandleActioin = true
        }
        
        // When
        sut.handle(action: action, controller: BeagleControllerStub()) { _ in }
        
        // Then
        XCTAssertTrue(didHandleActioin)
    }
}
