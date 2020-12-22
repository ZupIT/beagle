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
import SnapshotTesting
@testable import Beagle

class SimpleFormTests: XCTestCase {
    
    func testFormView() {
        //Given
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        let numberOfChilds = 3
        let simpleFormChilds = Array(repeating: ComponentDummy(), count: numberOfChilds)
        let simpleForm = SimpleForm(children: simpleFormChilds)
        
        // When
        let resultingView = renderer.render(simpleForm)
        
        //Then
        XCTAssertEqual(resultingView.subviews.count, numberOfChilds)
    }
    
    func test_whenDecodingJson_shouldReturnASimpleForm() throws {
        let component: SimpleForm = try componentFromJsonFile(fileName: "simpleFormComponent")
        assertSnapshot(matching: component, as: .dump)
    }
    
}
