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

extension String {
    // MARK: - BeagleConfig
    static let BASE_URL = "http://localhost:8080/"
    
    // MARK: - Image
    static let NETWORK_IMAGE_BEAGLE = "https://www.petlove.com.br/images/breeds/193436/profile/original/beagle-p.jpg?1532538271"
    
    // MARK: - Endpoint
    static let LAZY_COMPONENTS_ENDPOINT = "lazycomponent"
    static let PAGE_VIEW_ENDPOINT = "pageview"
    static let TAB_VIEW_ENDPOINT = "tabview"
    static let LIST_VIEW_ENDPOINT = "listview"
    static let FORM_ENDPOINT = "form"
    static let CUSTOM_COMPONENT_ENDPOINT = "customComponent"
    static let COMPONENTS_ENDPOINT = "/components"
    static let DEEPLINK_ENDPOINT = "screen-deep-link"
    static let NAVIGATE_ENDPOINT = "https://t001-2751a.firebaseapp.com/flow/step1.json"
    static let TEXT_FORM_ENDPOINT = "https://t001-2751a.firebaseapp.com/action/shownativedialog.json"
    static let TEXT_LAZY_COMPONENTS_ENDPOINT = "http://www.mocky.io/v2/5e4e91c02f00001f2016a8f2"
    static let WEB_VIEW_ENDPOINT = "webViewComponent"
    static let COMPONENT_INTERACTION_ENDPOINT = "componentInteractionText"
    
    // MARK: - URL
    static let WEB_VIEW_URL = "https://maps.google.com/"
    
    // MARK: - Style
    static let TAB_VIEW_STYLE = "DesignSystem.TabView.Style"
    static let FORM_SUBMIT_STYLE = "DesignSystem.Form.Submit"
    static let BUTTON_BLACK_TEXT_STYLE = "DesignSystem.Button.Style"
    static let NAVIGATION_BAR_DEFAULT_STYLE = "DesignSystem.Navigationbar.Style.Default"
    static let NAVIGATION_BAR_GREEN_STYLE = "DesignSystem.Navigationbar.Style.Green"
    static let BUTTON_WITH_APPEARANCE_STYLE = "DesignSystem.Stylish.ButtonAndAppearance"
    static let TEXT_STYLISH_STYLE = "DesignSystem.Stylish.Button"
    static let TEXT_ACTION_CLICK_STYLE = "DesignSystem.Text.Action.Click"
    static let TEXT_IMAGE_STYLE = "DesignSystem.Text.Image"
    static let TEXT_HELLO_WORD_STYLE = "DesignSystem.Text.helloWord"
    static let TEXT_INPUT_STYLE = "DesignSystem.TextInput.Style"
}

extension UIColor {
    class var demoGreen: UIColor {
        return UIColor(red: 0.34, green: 0.62, blue: 0.16, alpha: 1)
    }
    class var demoGray: UIColor {
        return UIColor(white: 0.5, alpha: 1)
    }
    class var demoDarkGray: UIColor {
        return UIColor(white: 0.2, alpha: 1)
    }
}
