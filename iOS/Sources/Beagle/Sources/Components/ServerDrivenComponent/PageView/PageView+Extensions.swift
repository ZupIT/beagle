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

extension PageView {

    public func toView(renderer: BeagleRenderer) -> UIView {
        let view = UIView()
        let indicatorView = pageIndicator.ifSome {
            renderer.render($0) as? PageIndicatorUIView & UIView
        }
        let pagesView = PageViewUIComponent(
            model: .init(pages: (children ?? []).map {
                ComponentHostController($0, renderer: renderer)
            }),
            indicatorView: indicatorView,
            controller: renderer.controller
        )
        pagesView.onPageChange = { [weak view] page in
            guard let view = view else { return }
            renderer.controller?.execute(actions: self.onPageChange, with: "onPageChange", and: .int(page), origin: view)
        }
        renderer.observe(currentPage, andUpdateManyIn: view) { [weak pagesView] page in
            if let page = page {
                pagesView?.swipeToPage(at: page)
            }
        }

        view.backgroundColor = .clear
        renderer.dependencies.style(view).setup(Style(flex: Flex().flexDirection(.column)))

        view.addSubview(pagesView)
        renderer.dependencies.style(pagesView).setup(Style(flex: Flex(grow: 1)))

        if let indicatorView = indicatorView {
            view.addSubview(indicatorView)
            let style = Style()
                .size(Size().height(40))
                .margin(EdgeValue().top(10))
                .flex(Flex().shrink(0))
            renderer.dependencies.style(indicatorView).setup(style)
        }

        return view
    }
}
