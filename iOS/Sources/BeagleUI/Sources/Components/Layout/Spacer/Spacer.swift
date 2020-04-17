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

public struct Spacer: ServerDrivenComponent {
    
    // MARK: - Public Properties
    
    public let size: Double
    
    // MARK: - Initialization
    
    public init(_ size: Double) {
        self.size = size
    }
    
}

extension Spacer: Renderable {
    
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let flex = Flex(
            size: Size(
                width: UnitValue(value: size, type: .real),
                height: UnitValue(value: size, type: .real)
            )
        )
        
        let view = UIView()
        view.isUserInteractionEnabled = false
        view.isAccessibilityElement = false
        view.backgroundColor = .clear

        view.flex.setup(flex)
        return view
    }
}
