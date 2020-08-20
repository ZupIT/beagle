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
    
    func testValidSingleExpressionRawRepresentable() {
        // Given
        [
            "@{client}",
            "@{client2.name}",
            "@{client_[2].matrix[1][1]}",
            "@{10}",
            "@{3.5}",
            "@{true}",
            "@{'name'}",
            "@{null}",
            "@{sum(1, counter)}",
            "@{condition(lt(1, counter), sum(counter, 2), subtract(counter, 2))}"
        ]
        
        // When
        .map {
            SingleExpression(rawValue: $0)
        }
            
        // Then
        .forEach {
            XCTAssertNotNil($0)
        }
    }
    
    func testInvalidSingleExpressionRawRepresentable() {
        // Given
        [
            "@{2client.phones[0]}",
            "@{}",
            "@{[2]}",
            "@{client.[2]}",
            "@{[2][2]}",
            "@{client[2].[2]}",
            "@{client[a]}",
            "sum(1, 2)}",
            "@{sum(1,2)",
            "@{test()}"
        ]
        
        // When
        .map {
            SingleExpression(rawValue: $0)
        }
            
        // Then
        .forEach {
            XCTAssertNil($0)
        }
    }
    
    func testValidMultipleExpressionRawRepresentable() {
        // Given
        [
            "name: @{client.name}",
            "name: @{client.name.first}, phone: @{client.phones[0]}",
            "@{client.phones[0]}@{client.phones[1]}",
            "name@name\\@name@@{client}",
            "@{client}",
            "\\\\@{client}",
            "Operation: @{sum(1, counter)} and @{condition(lt(1, counter), sum(counter, 2), subtract(counter, 2))}}"
        ]
        
        // When
        .map {
            MultipleExpression(rawValue: $0)
        }
            
        // Then
        .forEach {
            XCTAssertNotNil($0)
        }
    }
    
    func testInvalidMultipleExpressionRawRepresentable() {
        // Given
        [
            "\\@{client}",
            "\\\\\\@{client}",
            "@{@{client.phones[1]}}",
            "name",
            "Operation: @{sum(1, counter and @{condition(lt(1, counter), sum(counter, 2), subtract(counter, 2))}}",
            "Operation: @{sum(1, counter) and @{sum(2, counter)"
        ]
        
        // When
        .map {
            MultipleExpression(rawValue: $0)
        }
            
        // Then
        .forEach {
            XCTAssertNil($0)
        }
    }
    
}
