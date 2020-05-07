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

import UIKit

public struct ScrollView: AppearanceComponent, ServerDrivenComponent, AutoInitiableAndDecodable {
    
    // MARK: - Public Properties
    
    public let children: [ServerDrivenComponent]
    public let scrollDirection: ScrollAxis?
    public let scrollBarEnabled: Bool?
    public let appearance: Appearance?

// sourcery:inline:auto:ScrollView.Init
    public init(
        children: [ServerDrivenComponent],
        scrollDirection: ScrollAxis? = nil,
        scrollBarEnabled: Bool? = nil,
        appearance: Appearance? = nil
    ) {
        self.children = children
        self.scrollDirection = scrollDirection
        self.scrollBarEnabled = scrollBarEnabled
        self.appearance = appearance
    }
// sourcery:end
}

public enum ScrollAxis: String, Decodable {
    case vertical = "VERTICAL"
    case horizontal = "HORIZONTAL"
    
    var flexDirection: Flex.FlexDirection {
        switch self {
        case .vertical:
            return .column
        case .horizontal:
            return .row
        }
    }
}

extension ScrollView: Renderable {
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let scrollBarEnabled = self.scrollBarEnabled ?? true
        let flexDirection = (scrollDirection ?? .vertical).flexDirection
        let scrollView = BeagleContainerScrollView()
        let contentView = UIView()
        
        children.forEach {
            let childView = $0.toView(context: context, dependencies: dependencies)
            contentView.addSubview(childView)
            childView.flex.isEnabled = true
        }
        scrollView.addSubview(contentView)
        scrollView.beagle.setup(appearance: appearance)
        scrollView.flex.setup(Flex(flexDirection: flexDirection, grow: 1))
        scrollView.showsVerticalScrollIndicator = scrollBarEnabled
        scrollView.showsHorizontalScrollIndicator = scrollBarEnabled
        scrollView.yoga.overflow = .scroll
        
        let flexContent = Flex(flexDirection: flexDirection, grow: 0, shrink: 0)
        contentView.flex.setup(flexContent)
        
        return scrollView
    }
}

final class BeagleContainerScrollView: UIScrollView {
    override func layoutSubviews() {
        super.layoutSubviews()
        if let contentView = subviews.first {
            contentSize = contentView.frame.size
        }
    }
}
