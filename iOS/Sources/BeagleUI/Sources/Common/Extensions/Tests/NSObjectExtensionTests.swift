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
import UIKit
@testable import BeagleUI

final class NSObjectExtensionTests: XCTestCase {
    
    func test_className_shouldReturnValidName() {
        // Given
        let expectedName = "MockTableViewClass"
        
        // When
        let sut = MockTableViewClass.className
        
        // Then
        XCTAssertEqual(expectedName, sut, "The names should be as expected.")
    }
    
}

private class MockTableViewClass: UITableView {}
