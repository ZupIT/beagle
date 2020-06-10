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
@testable import BeagleUI
import BeagleSchema

final class UnitValueExtensionTests: XCTestCase {
    
    func test_UnitValueExpressibleByIntegerLiteral() {
        let sut: UnitValue = 10
        assertSnapshot(matching: sut, as: .dump)
    }
    
    func test_UnitValueExpressibleByFloatLiteral() {
        let sut: UnitValue = 10.5
        assertSnapshot(matching: sut, as: .dump)
    }
    
    func test_UnitValueWithPercentageOperatorAndIntegerValue() {
        let sut: UnitValue = 10%
        assertSnapshot(matching: sut, as: .dump)
    }
    
    func test_UnitValueWithPercentageOperatorAndFloatValue() {
        let sut: UnitValue = 10.5%
        assertSnapshot(matching: sut, as: .dump)
    }
}
