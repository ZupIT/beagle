//
//  ScreenElements.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/20/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

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
    
    //TabView Screen
    case TABVIEW_SCREEN_HEADER = "Beagle Tab View"
    case TAB_1 = "Tab 1"
    case TAB_1_TEXT = "Welcome to Tab 1"
    case TAB_1_TEXT_2 = "This is Tab1's second text"
    case TAB_2 = "Tab 2"
    case TAB_2_TEXT = "Welcome to Tab 2"
    case TAB_2_TEXT_2 = "This is Tab2's second text"
    case TAB_3 = "Tab 3"
    case TAB_3_TEXT = "Welcome to Tab 3"
    case TAB_3_TEXT_2 = "This is Tab3's second text"
    case TAB_4 = "Tab 4"
    case TAB_4_TEXT = "Welcome to Tab 4"
    case TAB_4_TEXT_2 = "This is Tab4's second text"
    
    //PageView Screen
    case PAGEVIEW_SCREEN_HEADER = "Beagle PageView"
    case PAGE_1_TEXT = "Page 1"
    case PAGE_2_TEXT = "Page 2"
    case PAGE_3_TEXT = "Page 3"
    
    //ScrollView Screen
    case SCROLLVIEW_SCREEN_HEADER = "Beagle ScrollView"
    case SCROLLVIEW_TEXT_1 = "Vertical ScrollView"
    case SCROLLVIEW_TEXT_2 = "Horizontal ScrollView with scrollBars"
    
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
    case SIMPLE_FORM_SCREEN_HEADER = "Beagle Context"
    case SIMPLE_FORM_TITLE = "Fill the form"
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
    
    //Navigate Actions
    case NAVIGATION_SCREEN_TITLE = "Navigation Screen"
    case SAMPLE_NAVIGATION_SCREEN_TITLE = "Sample Screen"
    case RESET_NAVIGATION_SCREEN_TITLE = "Reset Screen"
    
    var element: XCUIElement {
        switch self {
        case .MAIN_HEADER, .BUTTON_SCREEN_HEADER, .ACTION_CLICK_HEADER, .ACTION_CLICK_TEXT, .IMAGE_SCREEN_HEADER, .IMAGE_TEXT_1, .IMAGE_TEXT_2, .IMAGE_TEXT_3, .IMAGE_TEXT_4, .IMAGE_TEXT_5, .TABVIEW_SCREEN_HEADER, .TAB_1, .TAB_2, .TAB_3, .TAB_4, .TAB_1_TEXT, .TAB_2_TEXT, .TAB_3_TEXT, .TAB_4_TEXT, .TAB_1_TEXT_2, .TAB_2_TEXT_2, .TAB_3_TEXT_2, .TAB_4_TEXT_2, .PAGEVIEW_SCREEN_HEADER, .PAGE_1_TEXT, .PAGE_2_TEXT, .PAGE_3_TEXT, .SCROLLVIEW_SCREEN_HEADER, .SCROLLVIEW_TEXT_1, .SCROLLVIEW_TEXT_2, .LISTVIEW_SCREEN_HEADER, .STATIC_LISTVIEW_TEXT_1, .STATIC_LISTVIEW_TEXT_2, .DYNAMIC_LISTVIEW_TEXT_1, .DYNAMIC_LISTVIEW_TEXT_2, .TOUCHABLE_SCREEN_HEADER, .TOUCHABLE_TEXT_1, .TOUCHABLE_TEXT_2, .TOUCHABLE_TEXT_3, .TOUCHABLE_TEXT_4, .SIMPLE_FORM_SCREEN_HEADER, .SIMPLE_FORM_TITLE, .NAVIGATION_SCREEN_TITLE, .SAMPLE_NAVIGATION_SCREEN_TITLE, .RESET_NAVIGATION_SCREEN_TITLE:
            return XCUIApplication().staticTexts[self.rawValue]
            
        case .BUTTON_DEFAULT, .BUTTON_WITH_STYLE, .BUTTON_WITH_APPEARANCE, .BUTTON_WITH_APPEARANCE_AND_STYLE, .OK_BUTTON, .CANCEL_BUTTON, .SEND_BUTTON:
            return XCUIApplication().buttons[self.rawValue]
            
        case .ZIP_FIELD, .STREET_FIELD, .NUMBER_FIELD, .NEIGHBORHOOD_FIELD, .CITY_FIELD, .STATE_FIELD, .COMPLEMENT_FIELD:
            return XCUIApplication().textFields[self.rawValue]
        }
    }
}
