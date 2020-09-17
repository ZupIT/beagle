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

final class ConditionTests: XCTestCase {
    
    private func conditionTest(_ condition: Bool) {
        // Given
        let onTrue = ActionSpy()
        let onFalse = ActionSpy()
        
        let conditionAction = Condition(
            condition: .value(condition),
            onTrue: [onTrue],
            onFalse: [onFalse]
        )
        
        let view = UIView()
        let controller = BeagleControllerNavigationSpy()

        // When
        conditionAction.execute(controller: controller, origin: view)

        // Then
        XCTAssertEqual(onTrue.executionCount, condition ? 1 : 0)
        XCTAssertEqual(onFalse.executionCount, condition ? 0 : 1)
    }
    
    func testConditionTrue() {
        conditionTest(true)
    }
    
    func testConditionFalse() {
        conditionTest(false)
    }
    
    func testOnTrueOrOnFalseMissing() {
        // Given
        let onTrue = ActionSpy()
        let onFalse = ActionSpy()
        
        let conditionTrue = Condition(
            condition: true,
            onFalse: [onFalse]
        )
        let conditionFalse = Condition(
            condition: false,
            onTrue: [onTrue]
        )
        
        let view = UIView()
        let controller = BeagleControllerNavigationSpy()

        // When
        conditionTrue.execute(controller: controller, origin: view)
        conditionFalse.execute(controller: controller, origin: view)

        // Then
        XCTAssertEqual(onTrue.executionCount, 0)
        XCTAssertEqual(onFalse.executionCount, 0)
    }
    
    func testConditionInvalid() {
        // Given
        let onTrue = ActionSpy()
        let onFalse = ActionSpy()
        
        let conditionTrue = Condition(
            condition: "@{'any string that is not a valid expression to be evaluated'}",
            onTrue: [onTrue],
            onFalse: [onFalse]
        )
        
        let view = UIView()
        let controller = BeagleControllerNavigationSpy()

        // When
        conditionTrue.execute(controller: controller, origin: view)

        // Then
        XCTAssertEqual(onTrue.executionCount, 0)
        XCTAssertEqual(onFalse.executionCount, 0)
    }

}
