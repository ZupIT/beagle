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
import Schema
@testable import BeagleUI
// swiftlint:disable force_unwrapping

class SafeAreaTests: XCTestCase {

    func testMethodNamedAll() {
        // given
        let all = SafeArea.all
        
        // then
        XCTAssertTrue(all.top! && all.leading! && all.trailing! && all.bottom!)
    }

    func testMethodNamedNone() {
        // given
        let none = SafeArea.none
        
        // then
        XCTAssertFalse(none.top! && none.leading! && none.trailing! && none.bottom!)
    }
}
