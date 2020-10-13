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
import BeagleSchema

public protocol StyleViewConfiguratorProtocol: AnyObject {
    var view: UIView? { get set }

    func setup(_ style: Style?)
    
    func applyLayout()
    func markDirty()
    
    var isFlexEnabled: Bool { get set }
}

public protocol DependencyStyleViewConfigurator {
    var style: (UIView) -> StyleViewConfiguratorProtocol { get }
}

extension UIView {
    public var style: StyleViewConfiguratorProtocol {
        return Beagle.dependencies.style(self)
    }
}

// MARK: - Implementation

final class StyleViewConfigurator: StyleViewConfiguratorProtocol {
    
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

    func setup(_ style: Style?) {
        guard let yoga = view?.yoga else { return }

        isFlexEnabled = true
        applyYogaProperties(from: style ?? Style(), to: yoga)
    }
    
    func applyLayout() {
        isFlexEnabled = true
        view?.yoga.applyLayout(preservingOrigin: true)
    }
    
    var isFlexEnabled: Bool {
        get { return view?.yoga.isEnabled ?? false }
        set { view?.yoga.isEnabled = newValue }
    }
        
    func markDirty() {
        view?.yoga.markDirty()
        var view = self.view
        while let currentView = view {
            if !(currentView.superview?.yoga.isEnabled ?? false) {
                view?.setNeedsLayout()
            }
            view = view?.superview
        }
    }
    
    // MARK: - Private Methods
    
    private func applyYogaProperties(from style: Style, to layout: YGLayout) {
        applyYogaProperties(from: style.flex ?? Flex(), to: layout)
        layout.position = yogaTranslator.translate(style.positionType ?? .relative)
        layout.display = yogaTranslator.translate(style.display ?? .flex)
        setSize(style.size, to: layout)
        setMargin(style.margin, to: layout)
        setPadding(style.padding, to: layout)
        setPosition(style.position, to: layout)
    }

    private func applyYogaProperties(from flex: Flex, to layout: YGLayout) {
        layout.flexDirection = yogaTranslator.translate(flex.flexDirection ?? .column)
        layout.flexWrap = yogaTranslator.translate(flex.flexWrap ?? .noWrap)
        layout.justifyContent = yogaTranslator.translate(flex.justifyContent ?? .flexStart)
        layout.alignItems = yogaTranslator.translate(flex.alignItems ?? .stretch)
        layout.alignSelf = yogaTranslator.translate(flex.alignSelf ?? .auto)
        layout.alignContent = yogaTranslator.translate(flex.alignContent ?? .flexStart)
        layout.flexBasis = yogaTranslator.translate(flex.basis ?? .auto)
        if let flexValue = flex.flex {
            layout.flex = CGFloat(flexValue)
            if let grow = flex.grow {
                layout.flexGrow = CGFloat(grow)
            }
            if let shrink = flex.shrink {
                layout.flexShrink = CGFloat(shrink)
            }
        } else {
            layout.flex = .nan
            layout.flexGrow = CGFloat(flex.grow ?? 0)
            layout.flexShrink = CGFloat(flex.shrink ?? 1)
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
