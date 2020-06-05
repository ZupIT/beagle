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
@testable import Schema

class FlexTests: XCTestCase {

    func testEqualityOfUnityValue() {
        // given
        let unitValue1 = UnitValue.createMock()
        let unitValue2 = UnitValue.createMock()
        
        //then
        XCTAssertTrue(unitValue1 == unitValue2)
    }
    
    func testEqualitlyOfEdgeValue() {
        // given
        let edgeValue1 = EdgeValue.createMock()
        let edgeValue2 = EdgeValue.createMock()
        
        //then
        XCTAssertTrue(edgeValue1 == edgeValue2)
    }
    
    func testEqualityOfSize() {
        let size1 = Size.createMock()
        let size2 = Size.createMock()
        
        //then
        XCTAssertTrue(size1 == size2)
    }
    
    func testEqualityOfFlex() {
        // given
        let flex1 = Flex.createMock()
        let flex2 = Flex.createMock()
        
        // then
        XCTAssertTrue(flex1 == flex2)
    }
    
    func testDifferenceOfUnityValue() {
        // given
        let unitValue1 = UnitValue.createMock()
        let unitValue2 = UnitValue(floatLiteral: -999.9)
        
        //then
        XCTAssertFalse(unitValue1 == unitValue2)
    }
    
    func testDifferenceOfEdgeValue() {
        // given
        let edgeValue1 = EdgeValue.createMock()
        let edgeValue2 = EdgeValue(left: .none)
        
        //then
        XCTAssertFalse(edgeValue1 == edgeValue2)
    }
    
    func testDifferenceOfSize() {
        let size1 = Size.createMock()
        let size2 = Size(aspectRatio: -9999.99)
        
        //then
        XCTAssertFalse(size1 == size2)
    }
    
    func testDifferenceOfFlex() {
        // given
        let flex1 = Flex.createMock()
        let flex2 = Flex(position: EdgeValue.createMock())
        
        // then
        XCTAssertFalse(flex1 == flex2)
    }
}
