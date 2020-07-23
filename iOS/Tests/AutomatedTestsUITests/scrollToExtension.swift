//
//  scrollToExtension.swift
//  AutomatedTestsUITests
//
//  Created by Debliane Sousa on 7/23/20.
//  Copyright © 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA. All rights reserved.
//

import Foundation

extension XCUIElement {

func scrollToElement(element: XCUIElement) {
    while !element.visible() {
        swipeUp()
    }
  }
    
    func visible() -> Bool {
        guard self.exists && !self.frame.isEmpty else { return false }
        return XCUIApplication().windows.element(boundBy: 0).frame.contains(self.frame)
    }

}
