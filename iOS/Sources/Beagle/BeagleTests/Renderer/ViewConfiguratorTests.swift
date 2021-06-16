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

import XCTest
@testable import Beagle

class ViewConfiguratorTests: XCTestCase {
    
    func testSetupView() {
        // Given
        let view = UIView()
        let style = Style().borderColor("#000000").borderWidth(2).backgroundColor("#FFFFFF").cornerRadius(.init(radius: 4))
        let accessibility = Accessibility(accessibilityLabel: "accessibilityLabel", accessible: true)
        let component = Text("text", widgetProperties: .init(style: style, accessibility: accessibility))
        let viewConfigurator = ViewConfigurator(view: view)
        
        // When
        viewConfigurator.setupView(of: component)
        
        // Then
        // swiftlint:disable force_unwrapping
        XCTAssertEqual(view.backgroundColor, UIColor(hex: style.backgroundColor!))
        XCTAssertTrue(view.layer.masksToBounds)
        XCTAssertEqual(view.layer.cornerRadius, CGFloat(style.cornerRadius!.radius!))
        XCTAssertEqual(view.layer.borderWidth, CGFloat(style.borderWidth!))
        XCTAssertEqual(view.layer.borderColor, UIColor(hex: style.borderColor!)!.cgColor)
        
        XCTAssertEqual(view.accessibilityLabel, accessibility
                        .accessibilityLabel)
        XCTAssertTrue(view.isAccessibilityElement)
        // swiftlint:enable force_unwrapping
    }
}
