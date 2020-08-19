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
import SnapshotTesting
@testable import Beagle

class UrlBuilderTests: XCTestCase {
    // swiftlint:disable force_unwrapping

   func testUrlBuilderCases() throws {
        let jsonData = try jsonFromFile(fileName: "UrlBuilderTestSpec").data(using: .utf8)!
        let dataSet = try JSONDecoder().decode([UrlBuilderTestHelper].self, from: jsonData)
        XCTAssertFalse(dataSet.isEmpty)
        
        dataSet.forEach { data in
            let base = URL(string: data.base ?? "")
            let builder = UrlBuilder(baseUrl: base)
            let actualResult = builder.build(path: data.path)

            XCTAssertEqual(actualResult?.absoluteString, data.result)
        }
    }
}

// MARK: - Testing Helper
private struct UrlBuilderTestHelper: Decodable {
    var base: String? = ""
    var path: String
    var result: String? = ""
}
