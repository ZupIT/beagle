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

extension ScreenComponent: ServerDrivenComponent {

    public func toView(renderer: BeagleRenderer) -> UIView {

        prefetch(dependencies: renderer.dependencies)
        
        return buildChildView(renderer: renderer)
    }

    // MARK: - Private Functions
    
    private func prefetch(dependencies: RenderableDependencies) {
        navigationBar?.navigationBarItems?
            .compactMap { $0.action as? Navigate }
            .compactMap { $0.newPath }
            .forEach { dependencies.preFetchHelper.prefetchComponent(newPath: $0) }
    }

    private func buildChildView(renderer: BeagleRenderer) -> UIView {
        let view = renderer.render(child)
        let holder = UIView()
        holder.addSubview(view)
        holder.flex.setup(Flex(grow: 1))

        return holder
    }
}
