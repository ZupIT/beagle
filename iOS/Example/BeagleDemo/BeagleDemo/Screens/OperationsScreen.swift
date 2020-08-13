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

import Foundation
import Beagle
import BeagleSchema
import UIKit

let operationsMenuScreen: Screen = {
    return Screen(navigationBar: NavigationBar(title: "Context Operations", showBackButton: true)) {
        Container {
            Button(
                text: "Operations",
                onPress: [Navigate.pushView(.declarative(operationsScreen))]
            )
            Button(
                text: "Operations Sample",
                onPress: [Navigate.pushView(.declarative(operationsWebSampleScreen))]
            )
        }
    }
}()

let operationsScreen: Screen = {
    return Screen(navigationBar: NavigationBar(title: "Operations", showBackButton: true)) {
        Container(context: Context(id: "context1", value: 2)) {
            Container(context: Context(id: "context2", value: 3)) {
                TextInput(
                    onChange: [
                        SetContext(
                            contextId: "context1",
                            value: "@{onChange.value}"
                        )
                    ]
                )
                Text("@{sum(2, 3)}")
                Text("@{sum(context1, 4)}")
                Text("@{sum(context1, context2)}")
                Button(
                    text: "update",
                    onPress: [
                        SetContext(
                            contextId: "context1",
                            value: 3
                        )
                    ]
                )
            }
        }
    }
}()

let operationsWebSampleScreen: Screen = {
    return Screen(navigationBar: NavigationBar(title: "Operations Web Sample", showBackButton: true)) {
        
        Container(context: Context(id: "counter", value: 0)) {
            Text("@{counter}")
            Button(
                text: "increment",
                onPress: [
                    SetContext(
                        contextId: "counter",
                        value: "@{sum(counter, 1)}"
                    )
                ]
            )
            Button(
                text: "decrement",
                onPress: [
                    SetContext(
                        contextId: "counter",
                        value: "@{subtract(counter, 1)}"
                    )
                ]
            )
            Text("The following square will be red when \"counter + 2\" is below 5 and green when it's above.")
            BoxComponent(
                backgroundColor: "@{condition(lt(sum(counter, 2), 5), '#FF0000', '#00FF00')}",
                widgetProperties: .init(style: Style(size: Size().width(100).height(100)))
            )
        }
        
    }
}()

struct BoxComponent: Widget {
    let backgroundColor: Expression<String>?
    var widgetProperties: WidgetProperties
    
    func toView(renderer: BeagleRenderer) -> UIView {
        let view = UILabel()
        view.text = "box"
        renderer.observe(backgroundColor, andUpdate: \.backgroundColor, in: view) {
            if let colorHex = $0 {
                return UIColor(hex: colorHex)
            } else {
                return .black
            }
        }
        return view
    }
}

// TODO: duplicated code inside Beagle
extension UIColor {
    
    /// Create a color from hex String.
    /// Format:  #RRGGBB[AA] or #RGB[A]
    convenience init?(hex: String) {
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
