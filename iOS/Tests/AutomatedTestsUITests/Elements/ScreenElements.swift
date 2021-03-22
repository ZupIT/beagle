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
import XCTest

enum ScreenElements: String {

    case MAIN_HEADER = "Automated Tests"

    //ListView Screen
    case LISTVIEW_SCREEN_HEADER = "Beagle ListView"
    case STATIC_LISTVIEW_TEXT_1 = "Static VERTICAL ListView"
    case STATIC_LISTVIEW_TEXT_2 = "Static HORIZONTAL ListView"
    case DYNAMIC_LISTVIEW_TEXT_1 = "Dynamic VERTICAL ListView"
    case DYNAMIC_LISTVIEW_TEXT_2 = "Dynamic HORIZONTAL ListView"
    
    //Navigate Actions
    case NAVIGATION_SCREEN_TITLE = "Navigation Screen"
    case SAMPLE_NAVIGATION_SCREEN_TITLE = "Sample Screen"
    case RESET_NAVIGATION_SCREEN_TITLE = "Reset Screen"

    var element: XCUIElement {
        switch self {
        case .MAIN_HEADER, .LISTVIEW_SCREEN_HEADER, .STATIC_LISTVIEW_TEXT_1, .STATIC_LISTVIEW_TEXT_2, .DYNAMIC_LISTVIEW_TEXT_1, .DYNAMIC_LISTVIEW_TEXT_2, .NAVIGATION_SCREEN_TITLE, .SAMPLE_NAVIGATION_SCREEN_TITLE, .RESET_NAVIGATION_SCREEN_TITLE:
            return XCUIApplication().staticTexts[self.rawValue]
        }
    }
}
