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
import Schema

extension ScreenComponent: ServerDrivenComponent {
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {

        prefetch(dependencies: dependencies)
        
        let contentView = buildChildView(context: context, dependencies: dependencies)
        contentView.beagle.setup(appearance: appearance)
        return contentView
    }

    // MARK: - Private Functions
    
    private func prefetch(dependencies: RenderableDependencies) {
        navigationBar?.navigationBarItems?
            .compactMap { $0.action as? Navigate }
            .compactMap { $0.newPath }
            .forEach { dependencies.preFetchHelper.prefetchComponent(newPath: $0) }
    }
    
    //TODO: avoid casting to ServerDrivenComponent
    private func buildChildView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let childHolder = UIView()
        guard let childView = (child as? ServerDrivenComponent)?.toView(context: context, dependencies: dependencies) else {
            return childHolder
        }
        
        childHolder.addSubview(childView)
        childHolder.flex.setup(Flex(grow: 1))
        childView.flex.isEnabled = true
        
        return childHolder
    }
}
