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
import BeagleUI

struct Style {
    
    static let theme = AppTheme(styles: [
        .BUTTON_BLACK_TEXT_STYLE: Style.blackTextNormalStyle,
        .TEXT_HELLO_WORD_STYLE: Style.designSystemTextHelloWord,
        .TEXT_IMAGE_STYLE: Style.designSystemTextImage,
        .TEXT_ACTION_CLICK_STYLE: Style.designSystemTextActionClick,
        .TEXT_STYLISH_STYLE: Style.designSystemStylishButton,
        .BUTTON_WITH_APPEARANCE_STYLE: Style.designSystemStylishButtonAndAppearance,
        .FORM_SUBMIT_STYLE: Style.formButton,
        .NAVIGATION_BAR_GREEN_STYLE: Style.designSystemStyleNavigationBar,
        .NAVIGATION_BAR_DEFAULT_STYLE: Style.designSystemStyleNavigationBarDefault,
        .TAB_VIEW_STYLE: Style.tabView
    ])
    
    static func blackTextNormalStyle() -> (UITextView?) -> Void {
        return BeagleStyle.text(font: .systemFont(ofSize: 16), color: .black)
    }
    
    static func designSystemTextHelloWord() -> (UITextView?) -> Void {
        return BeagleStyle.text(font: .boldSystemFont(ofSize: 18), color: .darkGray)
    }
    
    static func designSystemTextImage() -> (UITextView?) -> Void {
        return BeagleStyle.text(font: .boldSystemFont(ofSize: 12), color: .black)
    }
    
    static func designSystemTextActionClick() -> (UITextView?) -> Void {
        return BeagleStyle.text(font: .boldSystemFont(ofSize: 40), color: .black)
    }
    
    static func designSystemStylishButton() -> (UIButton?) -> Void {
        return BeagleStyle.button(withTitleColor: .black)
            <> {
                $0?.titleLabel |> BeagleStyle.label(withFont: .systemFont(ofSize: 16, weight: .semibold))
        }
    }
    
    static func designSystemStylishButtonAndAppearance() -> (UIButton?) -> Void {
        return BeagleStyle.button(withTitleColor: .white)
            <> {
                $0?.titleLabel |> BeagleStyle.label(withFont: .systemFont(ofSize: 16, weight: .semibold))
        }
    }
    
    static func designSystemStyleNavigationBar() -> (UINavigationBar?) -> Void {
        return {
            $0?.barTintColor = .green
            $0?.isTranslucent = false
        }
    }
    
    static func designSystemStyleNavigationBarDefault() -> (UINavigationBar?) -> Void {
        return {
            $0?.barTintColor = nil
            $0?.isTranslucent = true
        }
    }

    static func formButton() -> (UIButton?) -> Void {
        return {
            $0?.layer.cornerRadius = 4
            $0?.setTitleColor(.white, for: .normal)
            $0?.backgroundColor = ($0?.isEnabled ?? false) ? #colorLiteral(red: 0.3411764706, green: 0.6235294118, blue: 0.168627451, alpha: 1) : #colorLiteral(red: 0.5019607843, green: 0.5019607843, blue: 0.5019607843, alpha: 1)
            $0?.alpha = $0?.isHighlighted ?? false ? 0.7 : 1
        }
    }
    
    static func tabView() -> (UIView?) -> Void {
        let orange = #colorLiteral(red: 0.5019607843, green: 0.5019607843, blue: 0.5019607843, alpha: 1)
        let gray = #colorLiteral(red: 0.2274509804, green: 0.2078431373, blue: 0.2078431373, alpha: 1)
        return BeagleStyle.tabView(backgroundColor: .clear, indicatorColor: orange, selectedTextColor: orange, unselectedTextColor: gray, selectedIconColor: orange, unselectedIconColor: gray)
    }
}
