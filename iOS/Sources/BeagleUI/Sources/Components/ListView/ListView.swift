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

public struct ListView: ServerDrivenComponent, AutoInitiableAndDecodable {
    
    // MARK: - Public Properties
    
    public let rows: [ServerDrivenComponent]
    public var direction: Direction = .vertical

// sourcery:inline:auto:ListView.Init
    public init(
        rows: [ServerDrivenComponent],
        direction: Direction = .vertical
    ) {
        self.rows = rows
        self.direction = direction
    }
// sourcery:end
}

extension ListView {
    
    public enum Direction: String, Decodable {
        
        case vertical = "VERTICAL"
        case horizontal = "HORIZONTAL"

        func toUIKit() -> UICollectionView.ScrollDirection {
            switch self {
            case .horizontal:
                return .horizontal
            case .vertical:
                return .vertical
            }
        }
    }
}

extension ListView: Renderable {
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let componentViews: [(view: UIView, size: CGSize)] = rows.compactMap {
            let container = Container(children: [$0], widgetProperties: .init(flex: Flex(positionType: .absolute)))
            let containerView = container.toView(context: context, dependencies: dependencies)
            let view = UIView()
            view.addSubview(containerView)
            view.flex.applyLayout()
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
