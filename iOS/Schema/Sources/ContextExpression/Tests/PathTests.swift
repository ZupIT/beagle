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

import BeagleSchema
import XCTest
import SnapshotTesting

final class PathTests: XCTestCase {
    
    func testValidPaths() {
        // Given
        let data = [
            "client",
            "client2.name",
            "client_[2].matrix[1][1]",
            "2client.2phones[0]",
            "_2client._2phones_[0]",
            "[2]",
            "[2][2]",
            "2",
            "_"
        ]
        
        // When
        let result = data.compactMap {
            Path(rawValue: $0)
        }
        let rawValues = result.map(\.rawValue)
        
        // Then
        XCTAssertEqual(rawValues, data)
    }
    
    func testInvalidPaths() {
        // Given
        [
            "",
            "client.[2]",
            "client[2].[2]",
            "client[a]",
            "client[2.2]"
        ]
        
        // When
        .map {
            Path(rawValue: $0)
        }
        
        // Then
        .forEach {
            XCTAssertNil($0)
        }
    }
    
}
