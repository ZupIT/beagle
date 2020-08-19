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
    
    func test_RawRepresentable() {
        // Given
        let path1 = "client"
        let path2 = "client2.name"
        let path3 = "client_[2].matrix[1][1]"
        let path4 = "[2]"
        let path5 = "[2][2]"
        
        let path6 = ""
        let path7 = "2client.phones[0]"
        let path8 = "client.[2]"
        let path9 = "client[2].[2]"
        let path10 = "client[a]"
        
        // When
        // Then
        XCTAssertNotNil(Path(rawValue: path1))
        XCTAssertNotNil(Path(rawValue: path2))
        XCTAssertNotNil(Path(rawValue: path3))
        XCTAssertNotNil(Path(rawValue: path4))
        XCTAssertNotNil(Path(rawValue: path5))
        
        XCTAssertNil(Path(rawValue: path6))
        XCTAssertNil(Path(rawValue: path7))
        XCTAssertNil(Path(rawValue: path8))
        XCTAssertNil(Path(rawValue: path9))
        XCTAssertNil(Path(rawValue: path10))
    }
    
}
