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

@testable import Beagle
import XCTest

final class NSErrorExtensionTests: XCTestCase {
    
    func test_initNSErrorWithDescription() {
        // Given
        let description = "Some description"
        
        // When
        let error = NSError(domain: "NSErrorExtensionTests", code: -1, description: description)
        
        // Then
        XCTAssertEqual(error.localizedDescription, description, "The descriptions are different, when they should be the same.")
    }
    
}
