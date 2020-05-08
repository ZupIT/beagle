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

public struct PageView: ServerDrivenComponent, AutoInitiable {

    public let pages: [ServerDrivenComponent]
    public let pageIndicator: PageIndicatorComponent?

// sourcery:inline:auto:PageView.Init
    public init(
        pages: [ServerDrivenComponent],
        pageIndicator: PageIndicatorComponent? = nil
    ) {
        self.pages = pages
        self.pageIndicator = pageIndicator
    }
// sourcery:end
}

extension PageView: Renderable {
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let pagesControllers = pages.map {
            BeagleScreenViewController(
                viewModel: .init(screenType: .declarative($0.toScreen()))
            )
        }

        var indicatorView: PageIndicatorUIView?
        if let indicator = pageIndicator {
            indicatorView = indicator.toView(context: context, dependencies: dependencies) as? PageIndicatorUIView
        }

        let view = PageViewUIComponent(
            model: .init(pages: pagesControllers),
            indicatorView: indicatorView
        )
        
        view.flex.setup(Flex(grow: 1.0))
        return view
    }
}

extension PageView: Decodable {
    enum CodingKeys: String, CodingKey {
        case pages
        case pageIndicator
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.pages = try container.decode(forKey: .pages)
        let pageIndicator = try container.decodeIfPresent(AnyDecodableContainer.self, forKey: .pageIndicator)
        self.pageIndicator = (pageIndicator?.content as? PageIndicatorComponent)
    }
}

public protocol PageIndicatorComponent: ServerDrivenComponent {}
