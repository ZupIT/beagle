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
@testable import BeagleUI

// swiftlint:disable force_unwrapping

final class MemoryCacheServiceTests: XCTestCase {

    // MARK: - Properties

    var sut = MemoryCacheService()

    // MARK: - Test Lifecycle

    override func tearDown() {
        super.tearDown()
        sut.clear()
    }

    // MARK: - Tests

    func testSaveAndLoad() {
        // Given
        let dataToSave = "value".data(using: .utf8)!
        let key = "Save-Memory-Tests"

        // When
        sut.save(data: dataToSave, key: key)
        let result = sut.loadData(from: key)

        // Then
        switch result {
        case .success(let data):
            XCTAssert(data == dataToSave)
        case .failure:
            XCTFail("should be able to load data")
        }
    }

    func testLoadingInvalidData() {
        // Given
        let key = "LoadFail-Memory-Tests"

        // When
        let result = sut.loadData(from: key)

        // Then
        switch result {
        case .success:
            XCTFail("should NOT be able to load unknown data")
        case .failure:
            XCTAssert(true)
        }
    }

    func testClear() {
        // Given
        let dataToSave = "value".data(using: .utf8)!
        let key = "Clear-Memory-Tests"
        sut.save(data: dataToSave, key: key)

        // When
        sut.clear()
        let result = sut.loadData(from: key)

        // Then
        switch result {
        case .success:
            XCTFail("should NOT be able to load cleared data")
        case .failure:
            XCTAssert(true)
        }
    }

}
