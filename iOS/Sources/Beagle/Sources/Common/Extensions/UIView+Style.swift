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

import UIKit

extension UIColor {
    
    /// Create a color from hex String.
    /// Format:  #RRGGBB[AA] or #RGB[A]
    public convenience init?(hex: String) {
        guard hex.range(of: "^#[0-9A-F]{3,8}$", options: [.regularExpression, .caseInsensitive]) != nil else {
            return nil
        }
        var int = UInt64()
        let hexDigits = String(hex.suffix(from: hex.index(after: hex.startIndex)))
        Scanner(string: hexDigits).scanHexInt64(&int)
        let r, g, b, a: UInt64
        switch hexDigits.count {
        case 3: // Short RGB (12-bit)
            r = UIColor.expand(shortColor: int >> 8)
            g = UIColor.expand(shortColor: int >> 4 & 0x0F)
            b = UIColor.expand(shortColor: int & 0x0F)
            a = 255
        case 4: // Short RGBA (16-bit)
            r = UIColor.expand(shortColor: int >> 12)
            g = UIColor.expand(shortColor: int >> 8 & 0x0F)
            b = UIColor.expand(shortColor: int >> 4 & 0x0F)
            a = UIColor.expand(shortColor: int & 0x0F)
        case 6: // RGB (24-bit)
            (r, g, b, a) = (int >> 16, int >> 8 & 0xFF, int & 0xFF, 255)
        case 8: // RGBA (32-bit)
            (r, g, b, a) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            return nil
        }
        self.init(
            red: CGFloat(r) / 255,
            green: CGFloat(g) / 255,
            blue: CGFloat(b) / 255,
            alpha: CGFloat(a) / 255
        )
    }
    
    private static func expand(shortColor: UInt64) -> UInt64 {
        return shortColor | (shortColor << 4)
    }
    
}
