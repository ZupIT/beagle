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

class FormDataStoreHandlerTests: XCTestCase {
    
    func test_saveMustSaveKeyAndValue() {
        //Given
        let sut = FormDataStoreHandler()
        let group = "group"
        let formData = ["age": "21", "name": "yan dias"]
        
        //When
        sut.save(data: formData, group: group)
        let readValue = sut.read(group: group)

        //Then
        assertSnapshot(matching: readValue, as: .dump)
    }

    func test_saveMustUpdateValue() {
        //Given
        let sut = FormDataStoreHandler()
        let group = "group"
        let formData = ["age": "21", "name": "yan dias"]
        let newFormData = ["age": "25", "zip": "32332"]
        //When
        sut.save(data: formData, group: group)
        sut.save(data: newFormData, group: group)
        let readValue = sut.read(group: group)

        //Then
        assertSnapshot(matching: readValue, as: .dump)
    }
    
    func test_formManagerDidSubmitForm_shouldClearFormData() {
        //Given
        let sut = FormDataStoreHandler()
        let group = "group"
        let formData = ["age": "21", "name": "yan dias"]
        
        //When
        sut.save(data: formData, group: group)
        sut.formManagerDidSubmitForm(group: group)
        let readValue = sut.read(group: group)

        //Then
        XCTAssert(readValue == nil)
    }
}
