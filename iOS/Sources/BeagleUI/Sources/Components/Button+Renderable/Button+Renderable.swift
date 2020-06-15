//
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
import BeagleSchema

extension Button: Widget {
    
    public func toView(renderer: BeagleRenderer) -> UIView {
        let button = BeagleUIButton.button(
            context: renderer.context,
            action: action,
            clickAnalyticsEvent: clickAnalyticsEvent,
            dependencies: renderer.dependencies
        )
        button.setTitle(text, for: .normal)
        
        if let newPath = (action as? Navigate)?.newPath {
            renderer.dependencies.preFetchHelper.prefetchComponent(newPath: newPath)
        }
        
        button.styleId = styleId
        
        return button
    }
    
    final class BeagleUIButton: UIButton {
        
        var styleId: String? {
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
                context?.actionManager.doAction(action, sender: self)
            }
            
            if let click = clickAnalyticsEvent {
                context?.actionManager.doAnalyticsAction(click, sender: self)
            }
        }
        
        private func applyStyle() {
            guard let styleId = styleId else { return }
            dependencies?.theme.applyStyle(for: self as UIButton, withId: styleId)
        }
    }
}
