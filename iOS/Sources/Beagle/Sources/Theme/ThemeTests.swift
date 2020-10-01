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
import BeagleSchema

final class ThemeTests: XCTestCase {
    
    func test_applyStyle() {
        // Given
        let view = UIView()
        let style: () -> (UIView?) -> Void = { { $0?.backgroundColor = .red } }
        let sut = AppTheme(styles: ["styleId": style])
        
        // When
        sut.applyStyle(for: view, withId: "styleId")
        
        // Then
        XCTAssertEqual(.red, view.backgroundColor)
    }
    
    func test_backgroundColor_shouldReturnAFunctionThatChangesBackgroundColor() {
        // Given
        let view = UIView()
        
        // When
        view |> BeagleStyle.backgroundColor(withColor: .red)
        
        // Then
        XCTAssertEqual(.red, view.backgroundColor)
    }
    
    func test_labelWithFont_shouldReturnAFunctionThatChangesFont() {
        // Given
        let view = UILabel()
        
        // When
        view |> BeagleStyle.label(withFont: .italicSystemFont(ofSize: 8))
        
        // Then
        XCTAssertEqual(.italicSystemFont(ofSize: 8), view.font)
    }
    
    func test_labelWithTextColor_shouldReturnAFunctionThatChangesTextColor() {
        // Given
        let view = UILabel()
        
        // When
        view |> BeagleStyle.label(withTextColor: .red)
        
        // Then
        XCTAssertEqual(.red, view.textColor)
    }
    
    func test_labelWithFontAndTextColor_shouldReturnAFunctionThatChangesFontAndTextColor() {
        // Given
        let view = UILabel()
        
        // When
        view |> BeagleStyle.label(font: .italicSystemFont(ofSize: 8), color: .red)
        
        // Then
        XCTAssertEqual(.italicSystemFont(ofSize: 8), view.font)
        XCTAssertEqual(.red, view.textColor)
    }
    
    func test_buttonWithTitleColor_shouldReturnAFunctionThatChangesButtonTitleColorForNormalState() {
        // Given
        let view = UIButton()
        
        // When
        view |> BeagleStyle.button(withTitleColor: .red)
        
        // Then
        XCTAssertEqual(.red, view.titleColor(for: .normal))
    }
    
    func test_textWithFontAndColor_shouldReturnAFunctionThatChangesFontAndColor() {
        // Given
        let font = UIFont.boldSystemFont(ofSize: 20)
        let color = UIColor.blue
        let view = UITextView()
        
        // When
        view |> BeagleStyle.text(font: font, color: color)
        
        // Then
        XCTAssertEqual(font, view.font)
        XCTAssertEqual(color, view.textColor)
    }
    
    func test_tabViewWithStyle_shouldReturnAFunctionThatChangesTabViewStyle() {
        // Given
        
        let controller = BeagleControllerStub()
        let renderer = BeagleRenderer(controller: controller)
        
        let backgroundColor: UIColor = .clear
        let indicatorColor: UIColor = .blue
        let tabItem = TabBarItem(title: "Tab")
        let tabBar = TabBarUIComponent(
            model: TabBarUIComponent.Model(tabIndex: 0, tabBarItems: [tabItem, tabItem], renderer: renderer)
        )
        
        // When
        tabBar |> BeagleStyle.tabBar(backgroundColor: backgroundColor, indicatorColor: indicatorColor)
        
        //Then
        XCTAssertEqual(backgroundColor, tabBar.backgroundColor)
        XCTAssertEqual(indicatorColor, tabBar.indicatorView.backgroundColor)
    }
}
