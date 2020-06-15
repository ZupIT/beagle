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
import BeagleSchema

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
    
    // MARK: - AlignItems Tests
       func test_translateWhenBaselineAlignItems_shouldReturnBaselineYGAlign() {
           // Given
           let expectedYogaAlign: YGAlign = .baseline
           let baselineAlignItems: Flex.AlignItems = .baseline
           // When
           let alignItemsTranslated = yogaTranslator.translate(baselineAlignItems)
           // Then
           XCTAssertEqual(expectedYogaAlign, alignItemsTranslated, "Expected baseline type of YGAlign, but got \(String(describing: type(of: alignItemsTranslated.self)))")
       }
       
       func test_translateWhenCenterAlignItems_shouldReturnCenterYGAlign() {
           // Given
           let expectedYogaAlign: YGAlign = .center
           let centerAlignItems: Flex.AlignItems = .center
           // When
           let alignItemsTranslated = yogaTranslator.translate(centerAlignItems)
           // Then
           XCTAssertEqual(expectedYogaAlign, alignItemsTranslated, "Expected center type of YGAlign, but got \(String(describing: type(of: alignItemsTranslated.self)))")
       }
       
       func test_translateWhenFlexStartAlignItems_shouldReturnFlexStartYGAlign() {
           // Given
           let expectedYogaAlign: YGAlign = .flexStart
           let flexStartAlignItems: Flex.AlignItems = .flexStart
           // When
           let alignItemsTranslated = yogaTranslator.translate(flexStartAlignItems)
           // Then
           XCTAssertEqual(expectedYogaAlign, alignItemsTranslated, "Expected flexStart type of YGAlign, but got \(String(describing: type(of: alignItemsTranslated.self)))")
       }
       
       func test_translateWhenFlexEndAlignItems_shouldReturnFlexEndYGAlign() {
           // Given
           let expectedYogaAlign: YGAlign = .flexEnd
           let flexEndAlignItems: Flex.AlignItems = .flexEnd
           // When
           let alignItemsTranslated = yogaTranslator.translate(flexEndAlignItems)
           // Then
           XCTAssertEqual(expectedYogaAlign, alignItemsTranslated, "Expected flexEnd type of YGAlign, but got \(String(describing: type(of: alignItemsTranslated.self)))")
       }
       
       func test_translateWhenStretchAlignItems_shouldReturnStretchYGAlign() {
           // Given
           let expectedYogaAlign: YGAlign = .stretch
           let stretchAlignItems: Flex.AlignItems = .stretch
           // When
           let alignItemsTranslated = yogaTranslator.translate(stretchAlignItems)
           // Then
           XCTAssertEqual(expectedYogaAlign, alignItemsTranslated, "Expected stretch type of YGAlign, but got \(String(describing: type(of: alignItemsTranslated.self)))")
       }
    
    // MARK: - AlignSelf Tests
    func test_translateWhenAutoAlignSelf_shouldReturnAutoYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .auto
        let autoAlignSelf: Flex.AlignSelf = .auto
        // When
        let alignSelfTranslated = yogaTranslator.translate(autoAlignSelf)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignSelfTranslated, "Expected auto type of YGAlign, but got \(String(describing: type(of: alignSelfTranslated.self)))")
    }
    
    func test_translateWhenBaselineAlignSelf_shouldReturnBaselineYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .baseline
        let baselineAlignSelf: Flex.AlignSelf = .baseline
        // When
        let alignSelfTranslated = yogaTranslator.translate(baselineAlignSelf)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignSelfTranslated, "Expected baseline type of YGAlign, but got \(String(describing: type(of: alignSelfTranslated.self)))")
    }
    
    func test_translateWhenCenterAlignSelf_shouldReturnCenterYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .center
        let centerAlignSelf: Flex.AlignSelf = .center
        // When
        let alignSelfTranslated = yogaTranslator.translate(centerAlignSelf)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignSelfTranslated, "Expected center type of YGAlign, but got \(String(describing: type(of: alignSelfTranslated.self)))")
    }
    
    func test_translateWhenFlexStartAlignSelf_shouldReturnFlexStartYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .flexStart
        let flexStartAlignSelf: Flex.AlignSelf = .flexStart
        // When
        let alignSelfTranslated = yogaTranslator.translate(flexStartAlignSelf)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignSelfTranslated, "Expected flexStart type of YGAlign, but got \(String(describing: type(of: alignSelfTranslated.self)))")
    }
    
    func test_translateWhenFlexEndAlignSelf_shouldReturnFlexEndYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .flexEnd
        let flexEndAlignSelf: Flex.AlignSelf = .flexEnd
        // When
        let alignSelfTranslated = yogaTranslator.translate(flexEndAlignSelf)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignSelfTranslated, "Expected flexEnd type of YGAlign, but got \(String(describing: type(of: alignSelfTranslated.self)))")
    }
    
    func test_translateWhenStretchAlignSelf_shouldReturnStretchYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .stretch
        let stretchAlignSelf: Flex.AlignSelf = .stretch
        // When
        let alignSelfTranslated = yogaTranslator.translate(stretchAlignSelf)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignSelfTranslated, "Expected stretch type of YGAlign, but got \(String(describing: type(of: alignSelfTranslated.self)))")
    }
    
    // MARK: - AlignContent Tests
    func test_translateWhenCenterAlignContent_shouldReturnCenterYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .center
        let centerAlignContent: Flex.AlignContent = .center
        // When
        let alignContentTranslated = yogaTranslator.translate(centerAlignContent)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignContentTranslated, "Expected center type of YGAlign, but got \(String(describing: type(of: alignContentTranslated.self)))")
    }
    
    func test_translateWhenFlexStartAlignContent_shouldReturnFlexStartYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .flexStart
        let flexStartAlignContent: Flex.AlignContent = .flexStart
        // When
        let alignContentTranslated = yogaTranslator.translate(flexStartAlignContent)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignContentTranslated, "Expected flexStart type of YGAlign, but got \(String(describing: type(of: alignContentTranslated.self)))")
    }
    
    func test_translateWhenFlexEndAlignContent_shouldReturnFlexEndYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .flexEnd
        let flexEndAlignContent: Flex.AlignContent = .flexEnd
        // When
        let alignContentTranslated = yogaTranslator.translate(flexEndAlignContent)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignContentTranslated, "Expected flexEnd type of YGAlign, but got \(String(describing: type(of: alignContentTranslated.self)))")
    }
    
    func test_translateWhenSpaceAroundAlignContent_shouldReturnSpaceAroundYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .spaceAround
        let spaceAroundAlignContent: Flex.AlignContent = .spaceAround
        // When
        let alignContentTranslated = yogaTranslator.translate(spaceAroundAlignContent)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignContentTranslated, "Expected spaceAround type of YGAlign, but got \(String(describing: type(of: alignContentTranslated.self)))")
    }
    
    func test_translateWhenSpaceBetweenAlignContent_shouldReturnSpaceBetweenYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .spaceBetween
        let spaceBetweenAlignContent: Flex.AlignContent = .spaceBetween
        // When
        let alignContentTranslated = yogaTranslator.translate(spaceBetweenAlignContent)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignContentTranslated, "Expected spaceBetween type of YGAlign, but got \(String(describing: type(of: alignContentTranslated.self)))")
    }
    
    func test_translateWhenStretchAlignContent_shouldReturnStretchYGAlign() {
        // Given
        let expectedYogaAlign: YGAlign = .stretch
        let stretchAlignContent: Flex.AlignContent = .stretch
        // When
        let alignContentTranslated = yogaTranslator.translate(stretchAlignContent)
        // Then
        XCTAssertEqual(expectedYogaAlign, alignContentTranslated, "Expected stretch type of YGAlign, but got \(String(describing: type(of: alignContentTranslated.self)))")
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
