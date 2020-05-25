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

//TODO: avoid casting to ServerDrivenComponent
extension LazyComponent: Renderable {
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        guard let view = (initialState as? ServerDrivenComponent)?.toView(context: context, dependencies: dependencies) else {
            return UIView()
        }
        context.lazyLoad(url: path, initialState: view)
        return view
    }
}
