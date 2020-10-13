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
    static let baseURL = "http://localhost:8080/"
    
    // MARK: - Image
    static let networkImageBeagle = "https://www.petlove.com.br/images/breeds/193436/profile/original/beagle-p.jpg?1532538271"
    
    // MARK: - Endpoint
    static let lazyComponentEndpoint = "lazycomponent"
    static let pageViewEndpoint = "pageview"
    static let tabBarEndpoint = "tabbar"
    static let listViewEndpoint = "listview"
    static let formEndpoint = "form"
    static let customComponentEndpoint = "customComponent"
    static let componentsEndpoint = "/components"
    static let screenDeeplinkEndpoint = "screen-deep-link"
    static let navigateStep1Endpoint = "navigateScreenStep1"
    static let navigateStep2Endpoint = "navigateScreenStep2"
    static let globalContextEndpoint = "globalContext"
    static let beagleView = "beagleView"
    static let textFormEndpoint = "https://t001-2751a.firebaseapp.com/action/shownativedialog.json"
    static let textLazyComponentEndpoint = "https://run.mocky.io/v3/bd78fba6-da3d-4cb3-8807-85923366fe13"
    static let webViewEndpoint = "webViewComponent"
    static let componentInterationEndpoint = "componentInteractionText"
    static let conditionActionEndpoint = "conditionActionText"
    static let simpleFormEndpoint = "simpleFormComponent"
    static let imageEndpoint = "image"

    // MARK: - URL
    static let webViewURL = "https://maps.google.com/"
    
    // MARK: - Style
    static let tabViewStyle = "DesignSystem.TabView.Style"
    static let formSubmitStyle = "DesignSystem.Form.Submit"
    static let blackButtonStyle = "DesignSystem.Button.Style"
    static let navigationBarDefaultStyle = "DesignSystem.Navigationbar.Style.Default"
    static let navigationBarGreenStyle = "DesignSystem.Navigationbar.Style.Green"
    static let buttonWithAppearanceStyle = "DesignSystem.Stylish.ButtonAndAppearance"
    static let buttonStyle = "DesignSystem.Stylish.Button"
    static let textActionClickStyle = "DesignSystem.Text.Action.Click"
    static let textImageSyle = "DesignSystem.Text.Image"
    static let textHelloWorldStyle = "DesignSystem.Text.helloWord"
    static let textInputStyle = "DesignSystem.TextInput.Style"
    static let textInputBFFStyle = "DesignSystem.TextInput.Style.Bff"
    static let buttonContextStyle = "DesignSystem.Button.Context"
    
    // MARK: - Hex Colors
    static let blueButton = "#0f4c81"
    static let salmonButton = "#ed6663"
    static let lightOrangeButton = "#ffa372"
    static let brownButton = "#b7472a"
    static let greenWaterButton = "#2a7886"
    static let redButton = "#c81912"
}

extension UIColor {
    static var demoGreen = #colorLiteral(red: 0.34, green: 0.62, blue: 0.16, alpha: 1)
    
    static var demoGray = #colorLiteral(red: 0.5723067522, green: 0.5723067522, blue: 0.5723067522, alpha: 1)
    
    static var demoDarkGray = #colorLiteral(red: 0.2605186105, green: 0.2605186105, blue: 0.2605186105, alpha: 1)
}
