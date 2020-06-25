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

final class DynamicObjectTests: XCTestCase {

    func test_setObjectWithPath() {
        var object: DynamicObject = [
            "a": "value a",
            "c": ["1", "2"]
        ]
        let paths: [Path] = [
            Path(nodes: [.key("a")]),
            Path(nodes: [.key("b")]),
            Path(nodes: [.key("c"), .index(0)]),
            Path(nodes: [.key("c"), .index(4)]),
            Path(nodes: []),
            Path(nodes: [.index(4)])
        ]
        let value: DynamicObject = "update"
        
        paths.forEach {
            object.set(value, forPath: $0)
            assertSnapshot(matching: object, as: .dump)
        }
    }
    
}
