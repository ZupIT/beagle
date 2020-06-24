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

final class ValidatorHandlerTests: XCTestCase {
    
    func test_getValidator_shouldReturnTheValidator() {
        // Given
        let sut = ValidatorProviding()
        let validatorName = "custom-validator"
        let validInput = "Valid Input"
        sut[validatorName] = {
            $0 as? String == validInput
        }
        
        // When
        let validator = sut.getValidator(name: validatorName)
        let validationResult = validator?.isValid(input: validInput)
        
        // Then
        XCTAssertNotNil(validator)
        XCTAssertNotNil(sut[validatorName])
        XCTAssertTrue(validationResult ?? false)
    }
    
    func test_removedValidator_shouldReturnNil() {
        // Given
        let sut = ValidatorProviding()
        let validatorName = "custom-validator"
        sut[validatorName] = nil
                
        // When
        let validator = sut.getValidator(name: validatorName)
        
        // Then
        XCTAssertNil(validator)
    }
    
}
