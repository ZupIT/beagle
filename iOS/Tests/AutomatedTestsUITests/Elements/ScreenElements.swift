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

    //Button Screen
    case BUTTON_SCREEN_HEADER = "Beagle Button"
    case BUTTON_DEFAULT = "Button"
    case BUTTON_WITH_STYLE = "Button with style"
    case BUTTON_WITH_APPEARANCE = "Button with Appearance"
    case BUTTON_WITH_APPEARANCE_AND_STYLE = "Button with Appearance and style"
    case ACTION_CLICK_HEADER = "Action Click"
    case ACTION_CLICK_TEXT = "You clicked right"

    //Image Screen
    case IMAGE_SCREEN_HEADER = "Beagle Image"
    case IMAGE_TEXT_1 = "Image"
    case IMAGE_TEXT_2 = "Image with contentMode = FIT_XY"
    case IMAGE_TEXT_3 = "Image with contentMode = FIT_CENTER"
    case IMAGE_TEXT_4 = "Image with contentMode = CENTER_CROP"
    case IMAGE_TEXT_5 = "Image with contentMode = CENTER"

    //PageView Screen
    case PAGEVIEW_SCREEN_HEADER = "Beagle PageView"
    case PAGE_1_TEXT = "Page 1"
    case PAGE_2_TEXT = "Page 2"
    case PAGE_3_TEXT = "Page 3"

    //ListView Screen
    case LISTVIEW_SCREEN_HEADER = "Beagle ListView"
    case STATIC_LISTVIEW_TEXT_1 = "Static VERTICAL ListView"
    case STATIC_LISTVIEW_TEXT_2 = "Static HORIZONTAL ListView"
    case DYNAMIC_LISTVIEW_TEXT_1 = "Dynamic VERTICAL ListView"
    case DYNAMIC_LISTVIEW_TEXT_2 = "Dynamic HORIZONTAL ListView"

    //Touchable Screen
    case TOUCHABLE_SCREEN_HEADER = "Beagle Touchable"
    case TOUCHABLE_TEXT_1 = "Text with Touchable"
    case TOUCHABLE_TEXT_2 = "Click here!"
    case TOUCHABLE_TEXT_3 = "Image with Touchable"
    case TOUCHABLE_TEXT_4 = "NetworkImage with Touchable"
    
    //Simpleform Screen
    case SIMPLE_FORM_SCREEN_HEADER = "SimpleForm"
    case ZIP_FIELD = "ZIP"
    case STREET_FIELD = "Street"
    case NUMBER_FIELD = "Number"
    case NEIGHBORHOOD_FIELD = "Neighborhood"
    case CITY_FIELD = "City"
    case STATE_FIELD = "State"
    case COMPLEMENT_FIELD = "Complement"
    case SEND_BUTTON = "Enviar"
    
    //Confirm Popup
    case OK_BUTTON = "Ok"
    case CANCEL_BUTTON = "Cancel"
    
    //Conditional Action
    case CONDITIONAL_ACTION_SCREEN_HEADER = "Conditional Screen"

    //Send Request Buttons Screen
    case SEND_REQUEST_SCREEN_TITLE = "Send Request Screen"

    //Navigate Actions
    case NAVIGATION_SCREEN_TITLE = "Navigation Screen"
    case SAMPLE_NAVIGATION_SCREEN_TITLE = "Sample Screen"
    case RESET_NAVIGATION_SCREEN_TITLE = "Reset Screen"

    //Add Children
    case ADD_CHILDREN_HEADER = "Add Children"
    case TEXT_FIXED = "I'm the single text on screen"
    case TEXT_ADDED = "New text added"
    case CONTAINER_ID = "containerId"
    case ADD_CHILDREN_BUTTON_DEFAULT = "DEFAULT"
    case ADD_CHILDREN_BUTTON_PREPEND = "PREPEND"
    case ADD_CHILDREN_BUTTON_APPEND = "APPEND"
    case ADD_CHILDREN_BUTTON_REPLACE = "REPLACE"
    case ADD_CHILDREN_BUTTON_PREPEND_COMPONENT_NULL = "PREPEND COMPONENT NULL"
    case ADD_CHILDREN_BUTTON_APPEND_COMPONENT_NULL = "APPEND COMPONENT NULL"
    case ADD_CHILDREN_BUTTON_REPLACE_COMPONENT_NULL = "REPLACE COMPONENT NULL"

    //Container Screen
    case CONTAINER_SCREEN_TITLE = "Container Screen"

    //Text Input
    case TEXT_INPUT_SCREEN_TITLE = "Beagle Text Input"

    //Text
    case TEXT_SCREEN_TITLE = "TextScreen"

    //Alert Screen
    case ALERT_SCREEN_TITLE = "Alert Screen"

    var element: XCUIElement {
        switch self {
        case .MAIN_HEADER, .BUTTON_SCREEN_HEADER, .ACTION_CLICK_HEADER, .ACTION_CLICK_TEXT, .IMAGE_SCREEN_HEADER, .IMAGE_TEXT_1, .IMAGE_TEXT_2, .IMAGE_TEXT_3, .IMAGE_TEXT_4, .IMAGE_TEXT_5, .PAGEVIEW_SCREEN_HEADER, .PAGE_1_TEXT, .PAGE_2_TEXT, .PAGE_3_TEXT, .LISTVIEW_SCREEN_HEADER, .STATIC_LISTVIEW_TEXT_1, .STATIC_LISTVIEW_TEXT_2, .DYNAMIC_LISTVIEW_TEXT_1, .DYNAMIC_LISTVIEW_TEXT_2, .TOUCHABLE_SCREEN_HEADER, .TOUCHABLE_TEXT_1, .TOUCHABLE_TEXT_2, .TOUCHABLE_TEXT_3, .TOUCHABLE_TEXT_4, .SIMPLE_FORM_SCREEN_HEADER, .NAVIGATION_SCREEN_TITLE, .SAMPLE_NAVIGATION_SCREEN_TITLE, .RESET_NAVIGATION_SCREEN_TITLE, .SEND_REQUEST_SCREEN_TITLE, .ADD_CHILDREN_HEADER, .TEXT_FIXED, .TEXT_ADDED, .CONTAINER_ID, .CONTAINER_SCREEN_TITLE, .TEXT_INPUT_SCREEN_TITLE, .TEXT_SCREEN_TITLE, .ALERT_SCREEN_TITLE, .CONDITIONAL_ACTION_SCREEN_HEADER:
            return XCUIApplication().staticTexts[self.rawValue]

        case .BUTTON_DEFAULT, .BUTTON_WITH_STYLE, .BUTTON_WITH_APPEARANCE, .BUTTON_WITH_APPEARANCE_AND_STYLE, .OK_BUTTON, .CANCEL_BUTTON, .SEND_BUTTON, .ADD_CHILDREN_BUTTON_DEFAULT, .ADD_CHILDREN_BUTTON_PREPEND, .ADD_CHILDREN_BUTTON_APPEND, .ADD_CHILDREN_BUTTON_REPLACE, .ADD_CHILDREN_BUTTON_APPEND_COMPONENT_NULL, .ADD_CHILDREN_BUTTON_PREPEND_COMPONENT_NULL, .ADD_CHILDREN_BUTTON_REPLACE_COMPONENT_NULL:
            return XCUIApplication().buttons[self.rawValue]
            
        case .ZIP_FIELD, .STREET_FIELD, .NUMBER_FIELD, .NEIGHBORHOOD_FIELD, .CITY_FIELD, .STATE_FIELD, .COMPLEMENT_FIELD:
            return XCUIApplication().textFields[self.rawValue]
        }
    }
}
