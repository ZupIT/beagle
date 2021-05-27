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

extension GridView {
    
    public func toView(renderer: BeagleRenderer) -> UIView {
        let view = ListViewUIComponent(
            model: ListViewUIComponent.Model(
                key: key.ifSome { Path(rawValue: $0) },
                direction: .vertical,
                templates: templates,
                iteratorName: iteratorName ?? "item",
                onScrollEnd: onScrollEnd,
                scrollEndThreshold: CGFloat(scrollEndThreshold ?? 100),
                isScrollIndicatorVisible: isScrollIndicatorVisible ?? false,
                columns: max(1, numColumns)
            ),
            renderer: renderer
        )
        renderer.observe(dataSource, andUpdate: \.items, in: view)
        return view
    }
}
