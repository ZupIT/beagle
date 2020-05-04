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

public protocol ServerDrivenComponent: Renderable, Decodable {}

public protocol ComposeComponent: ServerDrivenComponent {
    func build() -> ServerDrivenComponent
}

extension ComposeComponent {
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        return build().toView(context: context, dependencies: dependencies)
    }
}

public protocol Renderable {
    func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView
}

public protocol RenderableDependencies: DependencyTheme,
    DependencyValidatorProvider,
    DependencyPreFetching,
    DependencyAppBundle,
    DependencyRepository,
    DependencyLogger {
}

extension ServerDrivenComponent {
    public func toScreen() -> Screen {
        let screen = self as? ScreenComponent
        let safeArea = screen?.safeArea
            ?? SafeArea(top: true, leading: true, bottom: true, trailing: true)

        return Screen(
            identifier: screen?.identifier,
            appearance: screen?.appearance,
            safeArea: safeArea,
            navigationBar: screen?.navigationBar,
            screenAnalyticsEvent: screen?.screenAnalyticsEvent,
            child: screen?.child ?? self
        )
    }
}
