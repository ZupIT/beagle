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
import SnapshotTesting
import BeagleSchema

final class UnknownComponentTests: XCTestCase {
    
    func test_toView_shouldReturnTheExpectedView() {
        // Given
        let component = UnknownComponent(type: String(describing: ComponentDummy()))
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        
        // When
        let view = renderer.render(component)
        
        // Then
        XCTAssertTrue(view is UILabel)
        XCTAssertEqual((view as? UILabel)?.text, "Unknown Component of type:\n ComponentDummy()")
    }
}
