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

import BeagleSchema
import XCTest
import SnapshotTesting

final class ExpressionTests: XCTestCase {
    
    func test_singleExpressionRawRepresentable() {
        // Given
        let singleExpression1 = "@{client}"
        let singleExpression2 = "@{client2.name}"
        let singleExpression3 = "@{client_[2].matrix[1][1]}"
        let singleExpression4 = "@{10}"
        let singleExpression5 = "@{3.5}"
        let singleExpression6 = "@{true}"
        let singleExpression7 = "@{'name'}"
        let singleExpression8 = "@{null}"
        let singleExpression9 = "@{sum(1, counter)}"
        let singleExpression10 = "@{condition(lt(1, counter), sum(counter, 2), subtract(counter, 2))}"
        
        let singleExpression11 = "@{2client.phones[0]}"
        let singleExpression12 = "@{}"
        let singleExpression13 = "@{[2]}"
        let singleExpression14 = "@{client.[2]}"
        let singleExpression15 = "@{[2][2]}"
        let singleExpression16 = "@{client[2].[2]}"
        let singleExpression17 = "@{client[a]}"
        let singleExpression18 = "sum(1, 2)}"
        let singleExpression19 = "@{sum(1,2)"
        let singleExpression20 = "@{test()}"
        
        // When
        // Then
        XCTAssertNotNil(SingleExpression(rawValue: singleExpression1))
        XCTAssertNotNil(SingleExpression(rawValue: singleExpression2))
        XCTAssertNotNil(SingleExpression(rawValue: singleExpression3))
        XCTAssertNotNil(SingleExpression(rawValue: singleExpression4))
        XCTAssertNotNil(SingleExpression(rawValue: singleExpression5))
        XCTAssertNotNil(SingleExpression(rawValue: singleExpression6))
        XCTAssertNotNil(SingleExpression(rawValue: singleExpression7))
        XCTAssertNotNil(SingleExpression(rawValue: singleExpression8))
        XCTAssertNotNil(SingleExpression(rawValue: singleExpression9))
        XCTAssertNotNil(SingleExpression(rawValue: singleExpression10))
        
        XCTAssertNil(SingleExpression(rawValue: singleExpression11))
        XCTAssertNil(SingleExpression(rawValue: singleExpression12))
        XCTAssertNil(SingleExpression(rawValue: singleExpression13))
        XCTAssertNil(SingleExpression(rawValue: singleExpression14))
        XCTAssertNil(SingleExpression(rawValue: singleExpression15))
        XCTAssertNil(SingleExpression(rawValue: singleExpression16))
        XCTAssertNil(SingleExpression(rawValue: singleExpression17))
        XCTAssertNil(SingleExpression(rawValue: singleExpression18))
        XCTAssertNil(SingleExpression(rawValue: singleExpression19))
        XCTAssertNil(SingleExpression(rawValue: singleExpression20))
    }
    
    func test_multipleExpressionRawRepresentable() {
        // Given
        let multipleExpression1 = "name: @{client.name}"
        let multipleExpression2 = "name: @{client.name.first}, phone: @{client.phones[0]}"
        let multipleExpression3 = "@{client.phones[0]}@{client.phones[1]}"
        let multipleExpression4 = "name@name\\@name@@{client}"
        let multipleExpression5 = "@{client}"
        let multipleExpression6 = "\\\\@{client}"
        let multipleExpression7 = "Operation: @{sum(1, counter)} and @{condition(lt(1, counter), sum(counter, 2), subtract(counter, 2))}}"
        
        let multipleExpression8 = "\\@{client}"
        let multipleExpression9 = "\\\\\\@{client}"
        let multipleExpression10 = "@{@{client.phones[1]}}"
        let multipleExpression11 = "name"
        let multipleExpression12 = "Operation: @{sum(1, counter and @{condition(lt(1, counter), sum(counter, 2), subtract(counter, 2))}}"
        let multipleExpression13 = "Operation: @{sum(1, counter) and @{sum(2, counter)"
        
        // When
        // Then
        XCTAssertNotNil(MultipleExpression(rawValue: multipleExpression1))
        XCTAssertNotNil(MultipleExpression(rawValue: multipleExpression2))
        XCTAssertNotNil(MultipleExpression(rawValue: multipleExpression3))
        XCTAssertNotNil(MultipleExpression(rawValue: multipleExpression4))
        XCTAssertNotNil(MultipleExpression(rawValue: multipleExpression5))
        XCTAssertNotNil(MultipleExpression(rawValue: multipleExpression6))
        XCTAssertNotNil(MultipleExpression(rawValue: multipleExpression7))
        
        XCTAssertNil(MultipleExpression(rawValue: multipleExpression8))
        XCTAssertNil(MultipleExpression(rawValue: multipleExpression9))
        XCTAssertNil(MultipleExpression(rawValue: multipleExpression10))
        XCTAssertNil(MultipleExpression(rawValue: multipleExpression11))
        XCTAssertNil(MultipleExpression(rawValue: multipleExpression12))
        XCTAssertNil(MultipleExpression(rawValue: multipleExpression13))
    }
    
}
