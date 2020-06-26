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
@testable import Beagle
import XCTest
import SnapshotTesting

final class UIViewContextTests: XCTestCase {

    func test_setContext() {
        let view = UIView()
        let context1 = Context(id: "contexta", value: ["a": "b"])
        
        let context2 = Context(id: "contextb", value: [1])
        let context3 = Context(id: "contextb", value: [nil])

        XCTAssertNil(view.contextMap)
        view.setContext(context1)
        assertSnapshot(matching: view.contextMap, as: .dump)
        
        view.setContext(context2)
        assertSnapshot(matching: view.contextMap, as: .dump)
        
        view.setContext(context3)
        assertSnapshot(matching: view.contextMap, as: .dump)
    }

}
