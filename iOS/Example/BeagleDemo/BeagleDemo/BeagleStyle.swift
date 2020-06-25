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
import BeagleSchema

struct AppTheme {
    
    static let theme = BeagleUI.AppTheme(styles: [
        .BUTTON_BLACK_TEXT_STYLE: Self.blackTextNormalStyle,
        .TEXT_HELLO_WORD_STYLE: Self.designSystemTextHelloWord,
        .TEXT_IMAGE_STYLE: Self.designSystemTextImage,
        .TEXT_ACTION_CLICK_STYLE: Self.designSystemTextActionClick,
        .TEXT_STYLISH_STYLE: Self.designSystemStylishButton,
        .BUTTON_WITH_APPEARANCE_STYLE: Self.designSystemStylishButtonAndAppearance,
        .FORM_SUBMIT_STYLE: Self.formButton,
        .NAVIGATION_BAR_GREEN_STYLE: Self.designSystemStyleNavigationBar,
        .NAVIGATION_BAR_DEFAULT_STYLE: Self.designSystemStyleNavigationBarDefault,
        .TEXT_INPUT_STYLE: Self.designSystemTextInput,
        .TAB_VIEW_STYLE: Self.tabView,
        .DESIGN_SYSTEM_STYLE_BUTTON_SCREEN_BUTTON: designSystemScreenButton,
        .STYLE_TAB_VIEW_BFF: tabViewBff

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
    
    static func designSystemTextInput() -> (UITextField?) -> Void {
        return {
            $0?.textColor = .black
            $0?.font = .boldSystemFont(ofSize: 30)
        }
    }

    static func designSystemStylishButton() -> (UIButton?) -> Void {
        return BeagleStyle.button(withTitleColor: .black)
            <> {
                $0?.titleLabel |> BeagleStyle.label(withFont: .systemFont(ofSize: 16, weight: .semibold))
        }
    }
    
    static func designSystemScreenButton() -> (UIButton?) -> Void {
        return BeagleStyle.button(withTitleColor: #colorLiteral(red: 1.0, green: 1.0, blue: 1.0, alpha: 1.0))
            <> {
                $0?.backgroundColor = #colorLiteral(red: 0.1098039216, green: 0.1098039216, blue: 0.1098039216, alpha: 1)
                $0?.titleLabel |> BeagleStyle.label(withFont: .systemFont(ofSize: 18))
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
            $0?.backgroundColor = ($0?.isEnabled ?? false) ? .demoGreen : .demoGray
            $0?.alpha = $0?.isHighlighted ?? false ? 0.7 : 1
        }
    }
    
    static func tabView() -> (UIView?) -> Void {
        return BeagleStyle.tabView(backgroundColor: .clear, indicatorColor: .demoGray, selectedTextColor: .demoGray, unselectedTextColor: .demoDarkGray, selectedIconColor: .demoGray, unselectedIconColor: .demoDarkGray)
    }

    static func tabViewBff() -> (UIView?) -> Void {
           return BeagleStyle.tabView(
            backgroundColor: #colorLiteral(red: 0.6901960784, green: 0.768627451, blue: 0.8705882353, alpha: 1),
            indicatorColor: #colorLiteral(red: 1.0, green: 1.0, blue: 1.0, alpha: 1.0),
            selectedTextColor: #colorLiteral(red: 0.2745098039, green: 0.5098039216, blue: 0.7058823529, alpha: 1),
            unselectedTextColor: #colorLiteral(red: 0.1098039216, green: 0.1098039216, blue: 0.1098039216, alpha: 1),
            selectedIconColor: #colorLiteral(red: 0.2745098039, green: 0.5098039216, blue: 0.7058823529, alpha: 1),
            unselectedIconColor: #colorLiteral(red: 0.1098039216, green: 0.1098039216, blue: 0.1098039216, alpha: 1))
       }
}
