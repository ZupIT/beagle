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
@testable import Beagle

final class UIViewStyleTests: XCTestCase {
    
    func test_invalidHexColor() {
        XCTAssertNil(UIColor(hex: "AABBCC"))
        XCTAssertNil(UIColor(hex: "#1"))
        XCTAssertNil(UIColor(hex: "#12"))
        XCTAssertNil(UIColor(hex: "#12345"))
        XCTAssertNil(UIColor(hex: "#1234567"))
        XCTAssertNil(UIColor(hex: "#XABBCCDD"))
        XCTAssertNil(UIColor(hex: "#AABBCCDDY"))
    }
    
    func test_shortRGB() {
        assert(hex: "#5AD", red: 0x55, green: 0xAA, blue: 0xDD, alpha: 255)
    }
    
    func test_shortRGBA() {
        assert(hex: "#368a", red: 0x33, green: 0x66, blue: 0x88, alpha: 0xAA)
    }
    
    func test_RGB() {
        assert(hex: "#ac0D42", red: 0xAC, green: 0xD, blue: 0x42, alpha: 255)
    }
    
    func test_RGBA() {
        assert(hex: "#31BF523C", red: 0x31, green: 0xBF, blue: 0x52, alpha: 0x3C)
    }
    
    private func assert(hex: String, red: Int, green: Int, blue: Int, alpha: Int) {
        let color = UIColor(hex: hex)
        var r, g, b, a: CGFloat
        (r, g, b, a) = (0, 0, 0, 0)
        color?.getRed(&r, green: &g, blue: &b, alpha: &a)

        XCTAssertEqual(r, CGFloat(red) / 255)
        XCTAssertEqual(g, CGFloat(green) / 255)
        XCTAssertEqual(b, CGFloat(blue) / 255)
        XCTAssertEqual(a, CGFloat(alpha) / 255)
    }
}
