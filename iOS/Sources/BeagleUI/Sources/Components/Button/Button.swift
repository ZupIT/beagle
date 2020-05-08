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

public struct Button: Widget, ClickedOnComponent {
    
    // MARK: - Public Properties
    public let text: String
    public let style: String?
    public let action: Action?
    public var id: String?
    public let appearance: Appearance?
    public var accessibility: Accessibility?
    public let flex: Flex?
    public var clickAnalyticsEvent: AnalyticsClick?
    
    public init(
        text: String,
        style: String? = nil,
        action: Action? = nil,
        id: String? = nil,
        appearance: Appearance? = nil,
        flex: Flex? = nil,
        accessibility: Accessibility? = nil,
        clickAnalyticsEvent: AnalyticsClick? = nil
    ) {
        self.text = text
        self.style = style
        self.action = action
        self.id = id
        self.appearance = appearance
        self.flex = flex
        self.accessibility = accessibility
        self.clickAnalyticsEvent = clickAnalyticsEvent
    }
}

extension Button: Renderable {
    
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        
        let button = BeagleUIButton.button(
            context: context,
            action: action,
            clickAnalyticsEvent: clickAnalyticsEvent,
            dependencies: dependencies
        )
        button.setTitle(text, for: .normal)
        
        if let newPath = (action as? Navigate)?.newPath {
            dependencies.preFetchHelper.prefetchComponent(newPath: newPath)
        }
        
        button.style = style
        button.beagle.setup(self)
        
        return button
    }
    
    final class BeagleUIButton: UIButton {
        
        var style: String? {
            didSet { applyStyle() }
        }

        override var isEnabled: Bool {
            get { return super.isEnabled }
            set {
                super.isEnabled = newValue
                applyStyle()
            }
        }
        
        override var isSelected: Bool {
            get { return super.isSelected }
            set {
                super.isSelected = newValue
                applyStyle()
            }
        }
        
        override var isHighlighted: Bool {
            get { return super.isHighlighted }
            set {
                super.isHighlighted = newValue
                applyStyle()
            }
        }
        
        private var action: Action?
        private var clickAnalyticsEvent: AnalyticsClick?
        private weak var context: BeagleContext?
        private var dependencies: DependencyTheme?
        
        static func button(
            context: BeagleContext,
            action: Action?,
            clickAnalyticsEvent: AnalyticsClick? = nil,
            dependencies: DependencyTheme
        ) -> BeagleUIButton {
            let button = BeagleUIButton(type: .system)
            button.action = action
            button.context = context
            button.dependencies = dependencies
            button.clickAnalyticsEvent = clickAnalyticsEvent
            button.addTarget(button, action: #selector(triggerTouchUpInsideActions), for: .touchUpInside)
            return button
        }
        
        @objc func triggerTouchUpInsideActions() {
            if let action = action {
                context?.doAction(action, sender: self)
            }
            
            if let click = clickAnalyticsEvent {
                context?.doAnalyticsAction(click, sender: self)
            }
        }
        
        private func applyStyle() {
            guard let style = style else { return }
            dependencies?.theme.applyStyle(for: self as UIButton, withId: style)
        }
    }
    
}

extension Button: Decodable {
    enum CodingKeys: String, CodingKey {
        case text
        case style
        case action
        case id
        case appearance
        case accessibility
        case flex
        case clickAnalyticsEvent
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.text = try container.decode(String.self, forKey: .text)
        self.style = try container.decodeIfPresent(String.self, forKey: .style)
        self.action = try container.decodeIfPresent(forKey: .action)
        self.id = try container.decodeIfPresent(String.self, forKey: .id)
        self.appearance = try container.decodeIfPresent(Appearance.self, forKey: .appearance)
        self.accessibility = try container.decodeIfPresent(Accessibility.self, forKey: .accessibility)
        self.flex = try container.decodeIfPresent(Flex.self, forKey: .flex)
        self.clickAnalyticsEvent = try container.decodeIfPresent(AnalyticsClick.self, forKey: .clickAnalyticsEvent)
    }
}
