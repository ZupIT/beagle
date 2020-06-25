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
    
    let paths = [
        "client",
        "client2.name",
        "2client.phones[0]",
        "client_[2].matrix[1][1]",
        "[2]",
        "[2][2]",
        "",
        "client.[2]",
        "client[2].[2]",
        "client[a]"
    ]
    
    func test_RawRepresentable() {
        // Given
        let sut = paths
        // When
        let result1 = sut.map { Path(rawValue: $0) }
        let result2 = result1.map { $0?.rawValue }
        // Then
        assertSnapshot(matching: result1, as: .dump)
        assertSnapshot(matching: result2, as: .dump)
    }
    
}
