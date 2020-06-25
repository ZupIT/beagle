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

extension ScrollView: ServerDrivenComponent {

    public func toView(renderer: BeagleRenderer) -> UIView {
        let scrollBarEnabled = self.scrollBarEnabled ?? true
        let flexDirection = (scrollDirection ?? .vertical).flexDirection
        let scrollView = BeagleContainerScrollView()
        let contentView = UIView()
        
        children.forEach {
            let childView = renderer.render($0)
            contentView.addSubview(childView)
        }
        
        scrollView.addSubview(contentView)
        scrollView.style.setup(Style(flex: Flex(flexDirection: flexDirection, grow: 1)))
        scrollView.showsVerticalScrollIndicator = scrollBarEnabled
        scrollView.showsHorizontalScrollIndicator = scrollBarEnabled
        scrollView.yoga.overflow = .scroll

        contentView.style.setup(
            Style(flex: Flex(flexDirection: flexDirection, grow: 0, shrink: 0))
        )
        
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
