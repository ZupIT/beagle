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
    
    private let simple = "@{42}"
    
    func testValidSingleExpression() {
        // Given
        let data = [
            "@{client}",
            "@{client2.name}",
            "@{client_[2].matrix[1][1]}",
            "@{2client.phones[0]}",
            simple,
            "@{3.5}",
            "@{true}",
            "@{'name'}",
            "@{null}",
            "@{sum(1, counter)}"
        ]
        
        // When
        let result = data.compactMap {
            SingleExpression(rawValue: $0)
        }
        let rawValues = result.map(\.rawValue)
            
        // Then
        assertSnapshot(matching: result, as: .dump)
        XCTAssertEqual(rawValues, data)
    }
    
    func testInvalidSingleExpression() {
        // Given
        [
            "2",
            "@{}",
            "@{[2]}",
            "@{client.[2]}",
            "@{[2][2]}",
            "@{client[2].[2]}",
            "@{client[a]}",
            "sum(1, 2)}",
            "@{sum(1,2)",
            "@{test()}",
            "@{@{2}}"
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
    
    // MARK: Multiple Expressions
    
    func testValidMultipleExpressions() {
        // Given
        let data = [
            "name: \(simple), phone: \(simple)",
            "name@name\\@name@\(simple)",
            "\\\\@\(simple)",
            "Operation: @{condition(lt(sum(counter, 2), 5), '#FF0000', '#00FF00')}",
            "Operation1: @{sum(1, counter, null)} and @{sum(2, 'counter', 2count)}"
        ]
        
        // When
        let result = data.compactMap {
            MultipleExpression(rawValue: $0)
        }
        let rawValues = result.map(\.rawValue)
        
        // Then
        assertSnapshot(matching: result, as: .dump)
        _assertInlineSnapshot(matching: rawValues, as: .json, with: #"""
        [
          "name: @{42}, phone: @{42}",
          "name@name\\@name@@{42}",
          "\\@@{42}",
          "Operation: @{condition(lt(sum(counter, 2), 5), '#FF0000', '#00FF00')}",
          "Operation1: @{sum(1, counter, null)} and @{sum(2, 'counter', 2count)}"
        ]
        """#)
    }
    
    func testInvalidMultipleExpressions() {
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
    
    func testDictionarySnapShot() throws {
        guard let url = Bundle(for: ComponentDecoderTests.self).url(
            forResource: "testDictionarySnapShot",
            withExtension: ".json"
        ) else {
            throw ComponentFromJsonError.wrongUrlPath
        }

        let json = try Data(contentsOf: url)
        let decoder = JSONDecoder()
        let result = try? decoder.decode(DynamicObject.self, from: json)
        assertSnapshot(matching: result, as: .dump)
    }
    
    func testEquatable() {
        // Given
        let valueA = Expression.value(1)
        let valueB = Expression.value(1)
        let valueC = Expression.value(-1)
        let singleExpression = SingleExpression.value(
            .binding(.init(context: "ctx", path: .init(nodes: [])))
        )
        
        let expressionA = Expression<Int>.expression(.single(singleExpression))
        let expressionB = expressionA
        let expressionC = Expression<Int>.expression(
            .multiple(.init(nodes: [.expression(singleExpression)]))
        )
        
        // When / Then
        XCTAssertTrue(valueA == valueB)
        XCTAssertTrue(expressionA == expressionB)
        
        XCTAssertFalse(valueA == valueC)
        XCTAssertFalse(expressionA == expressionC)
        
        XCTAssertFalse(valueA == expressionA)
        XCTAssertFalse(expressionC == valueC)
    }
}
