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

public protocol Theme {
    func applyStyle<T: UIView>(for view: T, withId id: String)
}

public protocol DependencyTheme {
    var theme: Theme { get }
}

public protocol DependencyAppBundle {
    var appBundle: Bundle { get }
}

public struct AppTheme: Theme {
    let styles: [String: Any]
    
    public init(
        styles: [String: Any]
    ) {
        self.styles = styles
    }

    public func applyStyle<T: UIView>(for view: T, withId id: String) {
        if let styleFunc = styles[id] as? (() -> (T?) -> Void) {
            view |> styleFunc()
        }
    }
}

public struct BeagleStyle {
    public static func backgroundColor(withColor color: UIColor) -> (UIView?) -> Void {
        return { $0?.backgroundColor = color }
    }
    
    public static func text(font: UIFont, color: UIColor) -> (UITextView?) -> Void {
        return {
            $0?.font = font
            $0?.textColor = color
        }
    }

    public static func label(withFont font: UIFont) -> (UILabel?) -> Void {
        return { $0?.font = font }
    }

    public static func label(withTextColor color: UIColor) -> (UILabel?) -> Void {
        return { $0?.textColor = color }
    }

    public static func label(font: UIFont, color: UIColor) -> (UILabel?) -> Void {
        return label(withFont: font)
            <> label(withTextColor: color)
    }
    
    public static func button(withTitleColor color: UIColor) -> (UIButton?) -> Void {
        return { $0?.setTitleColor(color, for: .normal) }
    }
    
    public static func tabBar(backgroundColor: UIColor, indicatorColor: UIColor, selectedTextColor: UIColor? = nil, unselectedTextColor: UIColor? = nil, selectedIconColor: UIColor? = nil, unselectedIconColor: UIColor? = nil) -> (UIView?) -> Void {
        return {
            guard let tabBar = $0 as? TabBarUIComponent else { return }
            tabBar.backgroundColor = backgroundColor
            tabBar.indicatorView.backgroundColor = indicatorColor
            tabBar.tabItemViews.forEach { _, item in
                item.theme = TabBarTheme(
                    selectedTextColor: selectedTextColor,
                    unselectedTextColor: unselectedTextColor,
                    selectedIconColor: selectedIconColor,
                    unselectedIconColor: unselectedIconColor
                )
            }
            
        }
    }

}
