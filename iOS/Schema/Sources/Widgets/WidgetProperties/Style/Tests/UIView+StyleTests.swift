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
@testable import BeagleSchema

final class UIViewStyleTests: XCTestCase {
    
    func test_invalidHexColor() {
        let color = UIColor(hex: "")
        var r, g, b, a: CGFloat
        (r, g, b, a) = (1, 1, 1, 0)
        color.getRed(&r, green: &g, blue: &b, alpha: &a)
        
        XCTAssertEqual(r, 0)
        XCTAssertEqual(g, 0)
        XCTAssertEqual(b, 0)
        XCTAssertEqual(a, 1)
    }
    
    func test_24BitHex() {
        let color = UIColor(hex: "AABBCC")
        var r, g, b, a: CGFloat
        (r, g, b, a) = (0, 0, 0, 0)
        color.getRed(&r, green: &g, blue: &b, alpha: &a)

        XCTAssertEqual(r, 0xAA / 255)
        XCTAssertEqual(g, 0xBB / 255)
        XCTAssertEqual(b, 0xCC / 255)
        XCTAssertEqual(a, 1)
    }
    
    func test_32BitHex() {
        let color = UIColor(hex: "#75AABBCC")
        var r, g, b, a: CGFloat
        (r, g, b, a) = (0, 0, 0, 0)
        color.getRed(&r, green: &g, blue: &b, alpha: &a)

        XCTAssertEqual(r, 0xAA / 255)
        XCTAssertEqual(g, 0xBB / 255)
        XCTAssertEqual(b, 0xCC / 255)
        XCTAssertEqual(a, 0x75 / 255)
    }
}
