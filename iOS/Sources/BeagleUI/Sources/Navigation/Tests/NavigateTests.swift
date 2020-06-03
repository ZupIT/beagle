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
@testable import BeagleUI

final class NavigateTests: XCTestCase {

    func test_whenDecodingJson_thenItShouldReturnOpenExternalUrl() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "openexternalurl")
        assertSnapshot(matching: action, as: .dump)
    }
    
    func test_whenDecodingJson_thenItShouldReturnOpenNativeRoute() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "opennativeroute")
        assertSnapshot(matching: action, as: .dump)
    }
    
    func test_whenDecodingJson_thenItShouldReturnResetApplication() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "resetapplication")
        assertSnapshot(matching: action, as: .dump)
    }
    
    func test_whenDecodingJson_thenItShouldReturnResetStack() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "resetstack")
        assertSnapshot(matching: action, as: .dump)
    }
    
    func test_whenDecodingJson_thenItShouldReturnPushStack() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "pushstack")
        assertSnapshot(matching: action, as: .dump)
    }
    
    func test_whenDecodingJson_thenItShouldReturnPopStack() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "popstack")
        assertSnapshot(matching: action, as: .dump)
    }
    
    func test_whenDecodingJson_thenItShouldReturnPushView() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "pushview")
        assertSnapshot(matching: action, as: .dump)
    }
    
    func test_whenDecodingJson_thenItShouldReturnPopView() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "popview")
        assertSnapshot(matching: action, as: .dump)
    }
    
    func test_whenDecodingJson_thenItShouldReturnPopToView() throws {
        let action: Navigate = try actionFromJsonFile(fileName: "poptoview")
        assertSnapshot(matching: action, as: .dump)
    }
}
