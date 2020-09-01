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

extension Text: Widget {

    public func toView(renderer: BeagleRenderer) -> UIView {
        let textView = UITextView()
        textView.isEditable = false
        textView.isSelectable = false //The flag `isSelectable` cause retain cycle between the textview and the inside scrollview, some bug inside UIKIt component.
        textView.isScrollEnabled = false
        textView.textContainerInset = .zero
        textView.textContainer.lineFragmentPadding = 0
        textView.textContainer.lineBreakMode = .byTruncatingTail
        textView.font = .systemFont(ofSize: 16)
        textView.backgroundColor = .clear

        // the order of `observe` here is important (`textColor`should be set before `text`) to avoid a weird UIKit behavior when setting `textColor` to nil (issue: #766)
        renderer.observe(textColor, andUpdate: \.textColor, in: textView) {
            $0.flatMap { UIColor(hex: $0) }
        }
        
        renderer.observe(text, andUpdate: \.text, in: textView)

        renderer.observe(alignment, andUpdate: \.textAlignment, in: textView) { alignment in
            alignment?.toUIKit() ?? .natural
        }

        if let styleId = styleId {
            renderer.controller.dependencies.theme.applyStyle(for: textView, withId: styleId)
        }
        
        return textView
    }
}

extension Text.Alignment {
    public func toUIKit() -> NSTextAlignment {
        switch self {
        case .left:
            return .left
        case .right:
            return .right
        case .center:
            return .center
        }
    }
}
