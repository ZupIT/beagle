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
import Components

extension Text: Widget {

    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let textView = UITextView()
        textView.isEditable = false
        textView.isSelectable = false
        textView.isScrollEnabled = false
        textView.textContainerInset = .zero
        textView.textContainer.lineFragmentPadding = 0
        textView.textContainer.lineBreakMode = .byTruncatingTail
        textView.font = .systemFont(ofSize: 16)
        textView.backgroundColor = .clear
        
        textView.textAlignment = alignment?.toUIKit() ?? .natural
        textView.text = text
        
        if let style = style {
            dependencies.theme.applyStyle(for: textView, withId: style)
        }
        if let color = textColor {
            textView.textColor = UIColor(hex: color)
        }

        textView.beagle.setup(self)
        
        return textView
    }
}
