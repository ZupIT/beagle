//
//  SPMTestUITests.swift
//  SPMTestUITests
//
//  Created by Daniel Tes on 13/04/21.
//

import XCTest

class SPMTestUITests: XCTestCase {

    func testExample() throws {
        let app = XCUIApplication()
        app.launch()

        let label = app.textViews["textId"].staticTexts.firstMatch
        XCTAssertEqual(label.label, "Beagle SPM library integration")
    }

}
