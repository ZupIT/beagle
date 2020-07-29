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

extension PageView: ServerDrivenComponent {

    public func toView(renderer: BeagleRenderer) -> UIView {
        let pagesControllers = children.map {
            ComponentHostController($0, renderer: renderer)
        }

        var indicatorView: PageIndicatorUIView?
        if let indicator = pageIndicator {
            indicatorView = renderer.render(indicator) as? PageIndicatorUIView
        }

        let view = PageViewUIComponent(
            model: .init(pages: pagesControllers),
            indicatorView: indicatorView,
            controller: renderer.controller
        )
        
        if let actions = onPageChange {
            view.onPageChange = { page in
                renderer.controller.execute(actions: actions, with: "onPageChange", and: .int(page), origin: view)
            }
        }

        renderer.observe(currentPage, andUpdateManyIn: view) { page in
            if let page = page {
                view.swipeToPage(at: page)
            }
        }
        
        view.style.setup(Style(flex: Flex(grow: 1.0)))
        return view
    }
}
