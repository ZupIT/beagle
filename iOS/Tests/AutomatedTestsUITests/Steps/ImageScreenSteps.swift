//
//  ImageScreenSteps.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/21/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation
import XCTest

class ImageScreenSteps: NSObject {
    
    func ImageScreenSteps() {
    
        let screen = ScreenRobot()
                
        MatchAll("^App is running$") { (args, userInfo) -> Void in
            XCTAssertTrue(ScreenElements.MAIN_HEADER.element.exists)
        }
        
        Given("^Given the app will load http://localhost:8080/image$") { (args, userInfo) -> Void in
            XCTAssertTrue(ScreenElements.MAIN_HEADER.element.exists)
        }

    }
}
