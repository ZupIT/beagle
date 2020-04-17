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
import YogaKit
@testable import BeagleUI

final class YogaTranslatorTests: XCTestCase {
    
    // MARK: - Properties
    
    private lazy var yogaTranslator: YogaTranslator = YogaTranslating()

    // MARK: - Tests
    
    func test_translateWhenNoWrap_shouldReturnYGNoWrap() {
        // Given
        let expectedYogaWrap: YGWrap = .noWrap
        let flexWrap: Flex.Wrap = .noWrap
        // When
        let wrapTranslated = yogaTranslator.translate(flexWrap)
        // Then
        XCTAssertEqual(expectedYogaWrap, wrapTranslated, "Expected noWrap type of YGWrap, but got \(String(describing: type(of: wrapTranslated.self)))")
    }
    
    func test_translateWhenWrap_shouldReturnYGWrap() {
        // Given
        let expectedYogaWrap: YGWrap = .wrap
        let flexWrap: Flex.Wrap = .wrap
        // When
        let wrapTranslated = yogaTranslator.translate(flexWrap)
        // Then
        XCTAssertEqual(expectedYogaWrap, wrapTranslated, "Expected wrap type of YGWrap, but got \(String(describing: type(of: wrapTranslated.self)))")
    }
    
    func test_translateWhenWrapReverse_shouldReturnYGWrapReverse() {
        // Given
        let expectedYogaWrap: YGWrap = .wrapReverse
        let flexWrap: Flex.Wrap = .wrapReverse
        // When
        let wrapTranslated = yogaTranslator.translate(flexWrap)
        // Then
        XCTAssertEqual(expectedYogaWrap, wrapTranslated, "Expected wrapReverse type of YGWrap, but got \(String(describing: type(of: wrapTranslated.self)))")
    }
    
    // MARK: - Alignment Tests
    func test_translateWhenAutoAlignment_shouldReturnAutoYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .auto
        let flexAlignment: Flex.Alignment = .auto
        // When
        let alignTranslated = yogaTranslator.translate(flexAlignment)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignTranslated, "Expected auto type of YGAlign, but got \(String(describing: type(of: alignTranslated.self)))")
    }
    
    func test_translateWhenBaselineAlignment_shouldReturnBaselineYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .baseline
        let flexAlignment: Flex.Alignment = .baseline
        // When
        let alignTranslated = yogaTranslator.translate(flexAlignment)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignTranslated, "Expected baseline type of YGAlign, but got \(String(describing: type(of: alignTranslated.self)))")
    }
    
    func test_translateWhenCenterAlignment_shouldReturnCenterYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .center
        let flexAlignment: Flex.Alignment = .center
        // When
        let alignTranslated = yogaTranslator.translate(flexAlignment)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignTranslated, "Expected center type of YGAlign, but got \(String(describing: type(of: alignTranslated.self)))")
    }
    
    func test_translateWhenFlexStartAlignment_shouldReturnFlexStartYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .flexStart
        let flexAlignment: Flex.Alignment = .flexStart
        // When
        let alignTranslated = yogaTranslator.translate(flexAlignment)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignTranslated, "Expected flexStart type of YGAlign, but got \(String(describing: type(of: alignTranslated.self)))")
    }
    
    func test_translateWhenFlexEndAlignment_shouldReturnFlexEndYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .flexEnd
        let flexAlignment: Flex.Alignment = .flexEnd
        // When
        let alignTranslated = yogaTranslator.translate(flexAlignment)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignTranslated, "Expected flexEnd type of YGAlign, but got \(String(describing: type(of: alignTranslated.self)))")
    }
    
    func test_translateWhenSpaceAroundAlignment_shouldReturnSpaceAroundYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .spaceAround
        let flexAlignment: Flex.Alignment = .spaceAround
        // When
        let alignTranslated = yogaTranslator.translate(flexAlignment)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignTranslated, "Expected spaceAround type of YGAlign, but got \(String(describing: type(of: alignTranslated.self)))")
    }
    
    func test_translateWhenSpaceBetweenAlignment_shouldReturnSpaceBetweenYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .spaceBetween
        let flexAlignment: Flex.Alignment = .spaceBetween
        // When
        let alignTranslated = yogaTranslator.translate(flexAlignment)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignTranslated, "Expected spaceBetween type of YGAlign, but got \(String(describing: type(of: alignTranslated.self)))")
    }
    
    func test_translateWhenStretchAlignment_shouldReturnStretchYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .stretch
        let flexAlignment: Flex.Alignment = .stretch
        // When
        let alignTranslated = yogaTranslator.translate(flexAlignment)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignTranslated, "Expected stretch type of YGAlign, but got \(String(describing: type(of: alignTranslated.self)))")
    }
    
    // MARK: - Justify Content Tests
    func test_translateWhenCenterJustifyContent_shouldReturnCenterYGJustify() {
        // Given
        let expectedYogaJustify: YGJustify = .center
        let flexJustifyContent: Flex.JustifyContent = .center
        // When
        let justifyTranslated = yogaTranslator.translate(flexJustifyContent)
        // Then
        XCTAssertEqual(expectedYogaJustify, justifyTranslated, "Expected center type of YGJustify, but got \(String(describing: type(of: justifyTranslated.self)))")
    }
    
    func test_translateWhenFlexStartJustifyContent_shouldReturnFlexStartYGJustify() {
        // Given
        let expectedYogaJustify: YGJustify = .flexStart
        let flexJustifyContent: Flex.JustifyContent = .flexStart
        // When
        let justifyTranslated = yogaTranslator.translate(flexJustifyContent)
        // Then
        XCTAssertEqual(expectedYogaJustify, justifyTranslated, "Expected flexStart type of YGJustify, but got \(String(describing: type(of: justifyTranslated.self)))")
    }
    
    func test_translateWhenFlexEndJustifyContent_shouldReturnFlexEndYGJustify() {
        // Given
        let expectedYogaJustify: YGJustify = .flexEnd
        let flexJustifyContent: Flex.JustifyContent = .flexEnd
        // When
        let justifyTranslated = yogaTranslator.translate(flexJustifyContent)
        // Then
        XCTAssertEqual(expectedYogaJustify, justifyTranslated, "Expected flexEnd type of YGJustify, but got \(String(describing: type(of: justifyTranslated.self)))")
    }
    
    func test_translateWhenSpaceAroundJustifyContent_shouldReturnSpaceAroundYGJustify() {
        // Given
        let expectedYogaJustify: YGJustify = .spaceAround
        let flexJustifyContent: Flex.JustifyContent = .spaceAround
        // When
        let justifyTranslated = yogaTranslator.translate(flexJustifyContent)
        // Then
        XCTAssertEqual(expectedYogaJustify, justifyTranslated, "Expected spaceAround type of YGJustify, but got \(String(describing: type(of: justifyTranslated.self)))")
    }
    
    func test_translateWhenSpaceBetweenJustifyContent_shouldReturnSpaceBetweenYGJustify() {
        // Given
        let expectedYogaJustify: YGJustify = .spaceBetween
        let flexJustifyContent: Flex.JustifyContent = .spaceBetween
        // When
        let justifyTranslated = yogaTranslator.translate(flexJustifyContent)
        // Then
        XCTAssertEqual(expectedYogaJustify, justifyTranslated, "Expected spaceBetween type of YGJustify, but got \(String(describing: type(of: justifyTranslated.self)))")
    }
    
    func test_translateWhenSpaceEvenlyJustifyContent_shouldReturnSpaceEvenlyYGJustify() {
        // Given
        let expectedYogaJustify: YGJustify = .spaceEvenly
        let flexJustifyContent: Flex.JustifyContent = .spaceEvenly
        // When
        let justifyTranslated = yogaTranslator.translate(flexJustifyContent)
        // Then
        XCTAssertEqual(expectedYogaJustify, justifyTranslated, "Expected spaceEvenly type of YGJustify, but got \(String(describing: type(of: justifyTranslated.self)))")
    }

    // MARK: - FlexDirection Tests
    func test_translateWhenRowFlexDirection_shouldReturnRowYGFlexDirection() {
        // Given
        let expectedYogaFlexDirection: YGFlexDirection = .row
        let flexDirection: Flex.FlexDirection = .row
        // When
        let flexDirectionTranslated = yogaTranslator.translate(flexDirection)
        // Then
        XCTAssertEqual(expectedYogaFlexDirection, flexDirectionTranslated, "Expected row type of YGFlexDirection, but got \(String(describing: type(of: flexDirectionTranslated.self)))")
    }
    
    func test_translateWhenRowReverseFlexDirection_shouldReturnRowReverseYGFlexDirection() {
        // Given
        let expectedYogaFlexDirection: YGFlexDirection = .rowReverse
        let flexDirection: Flex.FlexDirection = .rowReverse
        // When
        let flexDirectionTranslated = yogaTranslator.translate(flexDirection)
        // Then
        XCTAssertEqual(expectedYogaFlexDirection, flexDirectionTranslated, "Expected rowReverse type of YGFlexDirection, but got \(String(describing: type(of: flexDirectionTranslated.self)))")
    }
    
    func test_translateWhenColumnFlexDirection_shouldReturnColumnYGFlexDirection() {
        // Given
        let expectedYogaFlexDirection: YGFlexDirection = .column
        let flexDirection: Flex.FlexDirection = .column
        // When
        let flexDirectionTranslated = yogaTranslator.translate(flexDirection)
        // Then
        XCTAssertEqual(expectedYogaFlexDirection, flexDirectionTranslated, "Expected column type of YGFlexDirection, but got \(String(describing: type(of: flexDirectionTranslated.self)))")
    }
    
    func test_translateWhenColumnReverseFlexDirection_shouldReturnColumnReverseYGFlexDirection() {
        // Given
        let expectedYogaFlexDirection: YGFlexDirection = .columnReverse
        let flexDirection: Flex.FlexDirection = .columnReverse
        // When
        let flexDirectionTranslated = yogaTranslator.translate(flexDirection)
        // Then
        XCTAssertEqual(expectedYogaFlexDirection, flexDirectionTranslated, "Expected columnReverse type of YGFlexDirection, but got \(String(describing: type(of: flexDirectionTranslated.self)))")
    }
    
    // MARK: - Direction Tests
    func test_translateWhenLeftToRightDirection_shouldReturnLeftToRightYGDirection() {
        // Given
        let expectedYogaDirection: YGDirection = .LTR
        let direction: Flex.Direction = .ltr
        // When
        let directionTranslated = yogaTranslator.translate(direction)
        // Then
        XCTAssertEqual(expectedYogaDirection, directionTranslated, "Expected ltr type of YGDirection, but got \(String(describing: type(of: directionTranslated.self)))")
    }
    
    func test_translateWhenRightToLeftlDirection_shouldReturnRightTLeftYGDirection() {
        // Given
        let expectedYogaDirection: YGDirection = .RTL
        let direction: Flex.Direction = .rtl
        // When
        let directionTranslated = yogaTranslator.translate(direction)
        // Then
        XCTAssertEqual(expectedYogaDirection, directionTranslated, "Expected rtl type of YGDirection, but got \(String(describing: type(of: directionTranslated.self)))")
    }
    
    func test_translateWhenInheritDirection_shouldReturnInheritYGDirection() {
        // Given
        let expectedYogaDirection: YGDirection = .inherit
        let direction: Flex.Direction = .inherit
        // When
        let directionTranslated = yogaTranslator.translate(direction)
        // Then
        XCTAssertEqual(expectedYogaDirection, directionTranslated, "Expected inherit type of YGDirection, but got \(String(describing: type(of: directionTranslated.self)))")
    }
    
    // MARK: - Display Tests
    func test_translateWhenFlexDisplay_shouldReturnFlexYGDisplay() {
        // Given
        let expectedYogaDisplay: YGDisplay = .flex
        let display: Flex.Display = .flex
        // When
        let displayTranslated = yogaTranslator.translate(display)
        // Then
        XCTAssertEqual(expectedYogaDisplay, displayTranslated, "Expected flex type of YGDisplay, but got \(String(describing: type(of: displayTranslated.self)))")
    }
    
    func test_translateWhenNoneDisplay_shouldReturnNoneYGDisplay() {
        // Given
        let expectedYogaDisplay: YGDisplay = .none
        let display: Flex.Display = .none
        // When
        let displayTranslated = yogaTranslator.translate(display)
        // Then
        XCTAssertEqual(expectedYogaDisplay, displayTranslated, "Expected none type of YGDisplay, but got \(String(describing: type(of: displayTranslated.self)))")
    }
    
    func test_translatePercentUnitValue_shouldReturnPercentYGValue() {
        // Given
        let expectedYGValue = YGValue(value: 1.0, unit: .percent)
        let unitValue = UnitValue(value: 1.0, type: .percent)
        // When
        let translated = yogaTranslator.translate(unitValue)
        // Then
        XCTAssertEqual(expectedYGValue, translated, "Expected \(expectedYGValue), but got \(String(describing: translated))")
    }
    
    func test_translateRealUnitValue_shouldReturnPointYGValue() {
        // Given
        let expectedYGValue = YGValue(value: 1.0, unit: .point)
        let unitValue = UnitValue(value: 1.0, type: .real)
        // When
        let translated = yogaTranslator.translate(unitValue)
        // Then
        XCTAssertEqual(expectedYGValue, translated, "Expected \(expectedYGValue), but got \(String(describing: translated))")
    }
    
    func test_translateAutoUnitValue_shouldReturnAutoYGValue() {
        // Given
        let expectedYGValue = YGValue(value: 1.0, unit: .auto)
        let unitValue = UnitValue(value: 1.0, type: .auto)
        // When
        let translated = yogaTranslator.translate(unitValue)
        // Then
        XCTAssertEqual(expectedYGValue, translated, "Expected \(expectedYGValue), but got \(String(describing: translated))")
    }
    
    func test_translateAbsolutePositionType_shouldReturnAbsoluteYGPositionType() {
        // Given
        let expectedYGPositionType: YGPositionType = .absolute
        let positionType: Flex.PositionType = .absolute
        // When
        let translated = yogaTranslator.translate(positionType)
        // Then
        XCTAssertEqual(expectedYGPositionType, translated, "Expected \(expectedYGPositionType), but got \(String(describing: translated))")
    }
    
    func test_translateRelativePositionType_shouldReturnRelativeYGPositionType() {
        // Given
        let expectedYGPositionType: YGPositionType = .relative
        let positionType: Flex.PositionType = .relative
        // When
        let translated = yogaTranslator.translate(positionType)
        // Then
        XCTAssertEqual(expectedYGPositionType, translated, "Expected \(expectedYGPositionType), but got \(String(describing: translated))")
    }
}

// MARK: - Testing Helpers

extension YGValue: Equatable {
    public static func == (lhs: YGValue, rhs: YGValue) -> Bool {
        return lhs.value == rhs.value && lhs.unit == rhs.unit
    }
}
