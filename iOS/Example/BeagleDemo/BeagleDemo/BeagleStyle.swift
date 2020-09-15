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
import Beagle
import BeagleSchema

struct AppTheme {
    
    static let theme = Beagle.AppTheme(styles: [
        .blackButtonStyle: Self.blackTextNormalStyle,
        .textHelloWorldStyle: Self.designSystemTextHelloWord,
        .textImageSyle: Self.designSystemTextImage,
        .textActionClickStyle: Self.designSystemTextActionClick,
        .buttonStyle: Self.designSystemStylishButton,
        .buttonWithAppearanceStyle: Self.designSystemStylishButtonAndAppearance,
        .formSubmitStyle: Self.formButton,
        .navigationBarGreenStyle: Self.designSystemStyleNavigationBar,
        .navigationBarDefaultStyle: Self.designSystemStyleNavigationBarDefault,
        .tabViewStyle: Self.tabView,
        .textInputStyle: Self.designSystemTextInput,
        .textInputBFFStyle: textInput,
        .buttonContextStyle: designSystemButtonScreenContext
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
            $0?.setTitleColor(.white, for: .normal)
            
            let layer: CAGradientLayer = CAGradientLayer()
            layer.frame.size = $0?.frame.size ?? CGSize(width: 0, height: 0)
            layer.frame.origin = CGPoint(x: 0, y: 0)

            layer.cornerRadius = CGFloat(10)
            // swiftlint:disable object_literal

            let color0 = UIColor(red: 255 / 255, green: 122 / 255, blue: 0 / 255, alpha: 1.0).cgColor
            let color1 = UIColor(red: 255 / 255, green: 176 / 255, blue: 0 / 255, alpha: 1.0).cgColor
            let color2 = UIColor(red: 250 / 255, green: 98 / 255, blue: 44 / 255, alpha: 1.0).cgColor
            layer.locations = [0.5, 1.0]
            layer.startPoint = CGPoint(x: 0.0, y: 0.5)
            layer.endPoint = CGPoint(x: 0.5, y: 0.5)
            layer.colors = [color2, color0, color1]

            $0?.layer.insertSublayer(layer, at: 0)

            $0?.alpha = $0?.isHighlighted ?? false ? 0.7 : 1
        }
    }
    
    static func tabView() -> (UIView?) -> Void {
        return BeagleStyle.tabBar(backgroundColor: .clear, indicatorColor: .demoGray, selectedTextColor: .demoGray, unselectedTextColor: .demoDarkGray, selectedIconColor: .demoGray, unselectedIconColor: .demoDarkGray)
    }
    
    static func textInput() -> (UITextField?) -> Void {
        return {
            $0?.borderStyle = UITextField.BorderStyle.roundedRect
        }
    }
  
    static func designSystemButtonScreenContext() -> (UIButton?) -> Void {
           return BeagleStyle.button(withTitleColor: .white)
               <> {
                   $0?.titleLabel |> BeagleStyle.label(withFont: .systemFont(ofSize: 16, weight: .semibold))
           }
       }
}
