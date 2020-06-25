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

extension ListView.Direction {
    
    func toUIKit() -> UICollectionView.ScrollDirection {
        switch self {
        case .horizontal:
            return .horizontal
        case .vertical:
            return .vertical
        }
    }
    
}

extension ListView: ServerDrivenComponent {

    public func toView(renderer: BeagleRenderer) -> UIView {
        let componentViews: [(view: UIView, size: CGSize)] = children.compactMap {
            let container = Container(children: [$0], widgetProperties: .init(style: .init(positionType: .absolute)))
            let containerView = renderer.render(container)
            let view = UIView()
            view.addSubview(containerView)
            view.style.applyLayout()
            if let view = containerView.subviews.first {
                view.removeFromSuperview()
                return (view: view, size: view.bounds.size)
            }
            return nil
        }
        
        let model = ListViewUIComponent.Model(
            component: self,
            componentViews: componentViews
        )
        
        return ListViewUIComponent(model: model)
    }
}
