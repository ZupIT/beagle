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
        case BUTTON_DEFAULT_TEXT = "Button"
        case BUTTON_WITH_STYLE_TEXT = "Button with style"
        case BUTTON_WITH_APPEARANCE_TEXT = "Button with Appearance"
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

    
    
    var element: XCUIElement {
        switch self {
        case .MAIN_HEADER, .BUTTON_SCREEN_HEADER, .ACTION_CLICK_HEADER, .ACTION_CLICK_TEXT_2, .IMAGE_SCREEN_HEADER, .IMAGE_TEXT_1, .IMAGE_TEXT_2, .IMAGE_TEXT_3, .IMAGE_TEXT_4, .IMAGE_TEXT_5, .TABVIEW_SCREEN_HEADER, .TAB_1, .TAB_2, .TAB_3, .TAB_4, .TAB_1_TEXT, .TAB_2_TEXT, .TAB_3_TEXT, .TAB_4_TEXT, .TAB_1_TEXT_2, .TAB_2_TEXT_2, .TAB_3_TEXT_2, .TAB_4_TEXT_2, .PAGEVIEW_SCREEN_HEADER, .PAGE_1_TEXT, .PAGE_2_TEXT, .PAGE_3_TEXT:
            return XCUIApplication().staticTexts[self.rawValue]

        case .BUTTON_DEFAULT_TEXT, .BUTTON_WITH_STYLE_TEXT, .BUTTON_WITH_APPEARANCE_TEXT:
            return XCUIApplication().buttons[self.rawValue]
        }
    }
}
