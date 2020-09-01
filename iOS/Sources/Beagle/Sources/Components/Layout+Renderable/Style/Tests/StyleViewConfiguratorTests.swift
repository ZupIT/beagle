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
@testable import Beagle
import SnapshotTesting
import BeagleSchema

final class StyleViewConfiguratorTests: XCTestCase {
    
    func test_init_shouldReturnInstanceWithYogaTranslatorDependencySetProperly() {
        // Given
        let sut = StyleViewConfigurator(view: UIView())
        let mirror = Mirror(reflecting: sut)
        // When
        let yogaTranslator = mirror.firstChild(of: YogaTranslating.self)
        // Then
        XCTAssert(yogaTranslator != nil)
    }
    
    func test_setupFlex_shouldApplyDefaultYogaPropertiesProperly() {
        // Given
        let view = UIView()
        let sut = StyleViewConfigurator(view: view)

        // When
        sut.setup(nil)

        // Then
        XCTAssertEqual(view.yoga.flexDirection, .column)
        XCTAssertEqual(view.yoga.flexWrap, .noWrap)
        XCTAssertEqual(view.yoga.justifyContent, .flexStart)
        XCTAssertEqual(view.yoga.alignItems, .stretch)
        XCTAssertEqual(view.yoga.alignSelf, .auto)
        XCTAssertEqual(view.yoga.alignContent, .flexStart)
        XCTAssertEqual(view.yoga.position, .relative)
        XCTAssertEqual(view.yoga.display, .flex)
    }
    
    func test_setupFlex_shouldApplyValueProperties() {
        // Given
        let view = UIView()
        let sut = StyleViewConfigurator(view: view)
        
        let flex = Flex(basis: UnitValue(value: 50, type: .real), flex: 1, grow: 0, shrink: 1)
        let style = Style(flex: flex)
        
        //When
        sut.setup(style)
        
        //Then
        XCTAssertEqual(view.yoga.flexBasis.value, 50)
        XCTAssertEqual(view.yoga.flex, 1)
        XCTAssertEqual(view.yoga.flexGrow, 0)
        XCTAssertEqual(view.yoga.flexShrink, 1)
        XCTAssertEqual(view.yoga.display, .flex)
    }
    
    func test_setupFlex_shouldApplyAllYogaPropertiesProperly() {
        // Given
        let value = UnitValue(value: 1, type: .real)
        let size = Size(
            width: value,
            height: value,
            maxWidth: value,
            maxHeight: value,
            minWidth: value,
            minHeight: value,
            aspectRatio: 1
        )
        let edgeValue = EdgeValue(
            left: value,
            top: value,
            right: value,
            bottom: value,
            horizontal: value,
            vertical: value,
            all: value
        )
        let style = Style(
            size: size,
            margin: edgeValue,
            padding: edgeValue,
            position: edgeValue
        )
        let expectedYGValue = YGValue(value: 1, unit: .point)
        let view = UIView()
        let sut = StyleViewConfigurator(view: view)

        // When
        sut.setup(style)

        // Then
        XCTAssertEqual(view.yoga.width, expectedYGValue)
        XCTAssertEqual(view.yoga.height, expectedYGValue)
        XCTAssertEqual(view.yoga.maxWidth, expectedYGValue)
        XCTAssertEqual(view.yoga.maxHeight, expectedYGValue)
        XCTAssertEqual(view.yoga.minWidth, expectedYGValue)
        XCTAssertEqual(view.yoga.minHeight, expectedYGValue)
        XCTAssertEqual(view.yoga.aspectRatio, 1)
        
        XCTAssertEqual(view.yoga.marginLeft, expectedYGValue)
        XCTAssertEqual(view.yoga.marginTop, expectedYGValue)
        XCTAssertEqual(view.yoga.marginRight, expectedYGValue)
        XCTAssertEqual(view.yoga.marginBottom, expectedYGValue)
        XCTAssertEqual(view.yoga.marginHorizontal, expectedYGValue)
        XCTAssertEqual(view.yoga.marginVertical, expectedYGValue)
        XCTAssertEqual(view.yoga.margin, expectedYGValue)
        
        XCTAssertEqual(view.yoga.paddingLeft, expectedYGValue)
        XCTAssertEqual(view.yoga.paddingTop, expectedYGValue)
        XCTAssertEqual(view.yoga.paddingRight, expectedYGValue)
        XCTAssertEqual(view.yoga.paddingBottom, expectedYGValue)
        XCTAssertEqual(view.yoga.paddingHorizontal, expectedYGValue)
        XCTAssertEqual(view.yoga.paddingVertical, expectedYGValue)
        XCTAssertEqual(view.yoga.padding, expectedYGValue)
        
        XCTAssertEqual(view.yoga.left, expectedYGValue)
        XCTAssertEqual(view.yoga.top, expectedYGValue)
        XCTAssertEqual(view.yoga.right, expectedYGValue)
        XCTAssertEqual(view.yoga.bottom, expectedYGValue)
    }
    
    func test_setupFlex_positionAll() {
        // Given
        let view = UIView()
        let sut = StyleViewConfigurator(view: view)
        let style = Style(position: .init(.init(all: .init(value: 50, type: .real))))
        
        //When
        sut.setup(style)
        
        //Then
        XCTAssertEqual(view.yoga.left, 50)
        XCTAssertEqual(view.yoga.top, 50)
        XCTAssertEqual(view.yoga.right, 50)
        XCTAssertEqual(view.yoga.bottom, 50)
    }
    
    func test_setupFlex_positionVertical() {
        // Given
        let view = UIView()
        let sut = StyleViewConfigurator(view: view)
        let style = Style(position: .init(.init(vertical: .init(value: 50, type: .real))))
        
        //When
        sut.setup(style)
        
        //Then
        XCTAssertEqual(view.yoga.top, 50)
        XCTAssertEqual(view.yoga.bottom, 50)
    }
    
    func test_setupFlex_positionHorizontal() {
        // Given
        let view = UIView()
        let sut = StyleViewConfigurator(view: view)
        let style = Style(position: .init(.init(horizontal: .init(value: 50, type: .real))))
        
        //When
        sut.setup(style)
        
        //Then
        XCTAssertEqual(view.yoga.left, 50)
        XCTAssertEqual(view.yoga.right, 50)
    }
    
    func test_applyYogaLayout_shouldEnableYoga_and_applyLayout() {
        // Given
        let expectedOrigin = CGPoint(x: 1, y: 1)
        let view = UIView(frame: .init(x: expectedOrigin.x, y: expectedOrigin.y, width: 1, height: 1))
        let sut = StyleViewConfigurator(view: view)
        
        // When
        sut.applyLayout()
        
        // Then
        XCTAssert(view.yoga.isEnabled)
        XCTAssert(expectedOrigin == view.frame.origin)
    }
    
    func test_enableYoga_shouldEnableIt() {
        // Given
        let view = UIView()
        let sut = StyleViewConfigurator(view: view)
        
        // When
        sut.isFlexEnabled = true
        
        // Then
        XCTAssert(sut.isFlexEnabled == true)
    }

    func test_disableYoga_shouldDisableIt() {
        // Given
        let view = UIView()
        let sut = StyleViewConfigurator(view: view)

        // When
        sut.isFlexEnabled = false

        // Then
        XCTAssert(sut.isFlexEnabled == false)
    }

    func testMarkDirty() {
        // Given
        let view = ViewDummy()
        assertSnapshotImage(view, size: ImageSize.custom(CGSize(width: 50, height: 50)))

        // When
        view.addRedSubview()
        view.style.markDirty()
        assertSnapshotImage(view, size: ImageSize.custom(CGSize(width: 50, height: 50)))
        
        // Then
        XCTAssertTrue(view.didCallLayoutSubviews)
    }
}

final class StyleViewConfiguratorDummy: StyleViewConfiguratorProtocol {
    var view: UIView? = UIView()
    var isFlexEnabled = false

    func setup(_ style: Style?) {}
    func applyLayout() {}
    func markDirty() {}
}

final class ViewDummy: UIView {
    private(set) var didCallLayoutSubviews = false
 
    override init(frame: CGRect) {
        super.init(frame: CGRect(x: 0, y: 0, width: 50, height: 50))
        backgroundColor = .green
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        didCallLayoutSubviews = true
    }
    
    func addRedSubview() {
        let view = UIView(frame: .init(x: 1, y: 1, width: 20, height: 20))
        view.backgroundColor = .red
        addSubview(view)
    }
}
