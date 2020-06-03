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

import Foundation
import YogaKit

public protocol FlexViewConfiguratorProtocol: AnyObject {
    var view: UIView? { get set }

    func setup(_ flex: Flex?)
    
    func applyLayout()
    func markDirty()
    
    var isEnabled: Bool { get set }
}

public protocol DependencyFlexConfigurator {
    var flex: (UIView) -> FlexViewConfiguratorProtocol { get }
}

extension UIView {
    public var flex: FlexViewConfiguratorProtocol {
        return Beagle.dependencies.flex(self)
    }
}

// MARK: - Implementation

final class FlexViewConfigurator: FlexViewConfiguratorProtocol {
    
    // MARK: - Dependencies

    weak var view: UIView?

    private let yogaTranslator: YogaTranslator
    
    // MARK: - Initialization
    
    init(
        view: UIView,
        yogaTranslator: YogaTranslator = YogaTranslating()
    ) {
        self.view = view
        self.yogaTranslator = yogaTranslator
    }
    
    // MARK: - Public Methods

    func setup(_ flex: Flex?) {
        guard let yoga = view?.yoga else { return }

        isEnabled = true
        applyYogaProperties(from: flex ?? Flex(), to: yoga)
    }
    
    func applyLayout() {
        isEnabled = true
        view?.yoga.applyLayout(preservingOrigin: true)
    }
    
    var isEnabled: Bool {
        get { return view?.yoga.isEnabled ?? false }
        set { view?.yoga.isEnabled = newValue }
    }
    
    func markDirty() {
        view?.yoga.markDirty()
        setNeedsLayout()
    }
    
    // MARK: - Private Methods
    
    private func setNeedsLayout() {
        var view = self.view
        while view != nil {
            (view as? ScreenView)?.setNeedsLayout()
            view = view?.superview
        }
    }
    
    private func applyYogaProperties(from flex: Flex, to layout: YGLayout) {
        layout.direction = yogaTranslator.translate(flex.direction ?? .ltr)
        layout.flexDirection = yogaTranslator.translate(flex.flexDirection ?? .column)
        layout.flexWrap = yogaTranslator.translate(flex.flexWrap ?? .noWrap)
        layout.justifyContent = yogaTranslator.translate(flex.justifyContent ?? .flexStart)
        layout.alignItems = yogaTranslator.translate(flex.alignItems ?? .stretch)
        layout.alignSelf = yogaTranslator.translate(flex.alignSelf ?? .auto)
        layout.alignContent = yogaTranslator.translate(flex.alignContent ?? .flexStart)
        layout.position = yogaTranslator.translate(flex.positionType ?? .relative)
        layout.display = yogaTranslator.translate(flex.display ?? .flex)
        setSize(flex.size, to: layout)
        setMargin(flex.margin, to: layout)
        setPadding(flex.padding, to: layout)
        setPosition(flex.position, to: layout)
        if let basis = flex.basis {
            layout.flexBasis = yogaTranslator.translate(basis)
        }
        if let flex = flex.flex {
            layout.flex = CGFloat(flex)
        }
        if let grow = flex.grow {
            layout.flexGrow = CGFloat(grow)
        }
        if let shrink = flex.shrink {
            layout.flexShrink = CGFloat(shrink)
        }
    }
    
    // MARK: - Flex Layout Methods
    
    private func setSize(_ size: Size?, to layout: YGLayout) {
        guard let size = size else {
            return
        }
        if let width = size.width {
            layout.width = yogaTranslator.translate(width)
        }
        if let height = size.height {
            layout.height = yogaTranslator.translate(height)
        }
        if let maxWidth = size.maxWidth {
            layout.maxWidth = yogaTranslator.translate(maxWidth)
        }
        if let maxHeight = size.maxHeight {
            layout.maxHeight = yogaTranslator.translate(maxHeight)
        }
        if let minWidth = size.minWidth {
            layout.minWidth = yogaTranslator.translate(minWidth)
        }
        if let minHeight = size.minHeight {
            layout.minHeight = yogaTranslator.translate(minHeight)
        }
        if let aspectRatio = size.aspectRatio {
            layout.aspectRatio = CGFloat(aspectRatio)
        }
    }
    
    private func setMargin(_ margin: EdgeValue?, to layout: YGLayout) {
        guard let margin = margin else {
            return
        }
        if let all = margin.all {
            layout.margin = yogaTranslator.translate(all)
        }
        if let left = margin.left {
            layout.marginLeft = yogaTranslator.translate(left)
        }
        if let top = margin.top {
            layout.marginTop = yogaTranslator.translate(top)
        }
        if let right = margin.right {
            layout.marginRight = yogaTranslator.translate(right)
        }
        if let bottom = margin.bottom {
            layout.marginBottom = yogaTranslator.translate(bottom)
        }
        if let start = margin.start {
            layout.marginStart = yogaTranslator.translate(start)
        }
        if let end = margin.end {
            layout.marginEnd = yogaTranslator.translate(end)
        }
        if let horizontal = margin.horizontal {
            layout.marginHorizontal = yogaTranslator.translate(horizontal)
        }
        if let vertical = margin.vertical {
            layout.marginVertical = yogaTranslator.translate(vertical)
        }
    }
    
    private func setPadding(_ padding: EdgeValue?, to layout: YGLayout) {
        guard let padding = padding else {
            return
        }
        if let all = padding.all {
            layout.padding = yogaTranslator.translate(all)
        }
        if let left = padding.left {
            layout.paddingLeft = yogaTranslator.translate(left)
        }
        if let top = padding.top {
            layout.paddingTop = yogaTranslator.translate(top)
        }
        if let right = padding.right {
            layout.paddingRight = yogaTranslator.translate(right)
        }
        if let bottom = padding.bottom {
            layout.paddingBottom = yogaTranslator.translate(bottom)
        }
        if let start = padding.start {
            layout.paddingStart = yogaTranslator.translate(start)
        }
        if let end = padding.end {
            layout.paddingEnd = yogaTranslator.translate(end)
        }
        if let horizontal = padding.horizontal {
            layout.paddingHorizontal = yogaTranslator.translate(horizontal)
        }
        if let vertical = padding.vertical {
            layout.paddingVertical = yogaTranslator.translate(vertical)
        }
    }
    
    private func setPosition(_ position: EdgeValue?, to layout: YGLayout) {
        guard let position = position else {
            return
        }
        if let all = position.all {
            layout.left = yogaTranslator.translate(all)
            layout.right = yogaTranslator.translate(all)
            layout.top = yogaTranslator.translate(all)
            layout.bottom = yogaTranslator.translate(all)
        }
        if let left = position.left {
            layout.left = yogaTranslator.translate(left)
        }
        if let top = position.top {
            layout.top = yogaTranslator.translate(top)
        }
        if let right = position.right {
            layout.right = yogaTranslator.translate(right)
        }
        if let bottom = position.bottom {
            layout.bottom = yogaTranslator.translate(bottom)
        }
        if let start = position.start {
            layout.start = yogaTranslator.translate(start)
        }
        if let end = position.end {
            layout.end = yogaTranslator.translate(end)
        }
        if let vertical = position.vertical {
            layout.top = yogaTranslator.translate(vertical)
            layout.bottom = yogaTranslator.translate(vertical)
        }
        if let horizontal = position.horizontal {
            layout.left = yogaTranslator.translate(horizontal)
            layout.right = yogaTranslator.translate(horizontal)
        }
    }
}
