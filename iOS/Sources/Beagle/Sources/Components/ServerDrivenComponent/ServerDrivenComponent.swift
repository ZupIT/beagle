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

public protocol ServerDrivenComponent: Decodable, Renderable {}

public protocol ComposeComponent: ServerDrivenComponent {
    func build() -> ServerDrivenComponent
}

extension ComposeComponent {
    public func toView(renderer: BeagleRenderer) -> UIView {
        return renderer.render(build())
    }
}

extension ServerDrivenComponent {
    public func toScreen() -> Screen {
        let screen = self as? ScreenComponent
        let safeArea = screen?.safeArea
            ?? SafeArea(top: true, leading: true, bottom: true, trailing: true)

        return Screen(
            identifier: screen?.identifier,
            style: screen?.style,
            safeArea: safeArea,
            navigationBar: screen?.navigationBar,
            screenAnalyticsEvent: screen?.screenAnalyticsEvent,
            child: screen?.child ?? self,
            context: screen?.context
        )
    }
}

public protocol Renderable {

    /// here is where your component should turn into a UIView. If your component has child components,
    /// let *renderer* do the job to render those children into UIViews; don't call this method directly
    /// in your children.
    func toView(renderer: BeagleRenderer) -> UIView
}

extension UnknownComponent {

    public func toView(renderer: BeagleRenderer) -> UIView {
        #if DEBUG
        let label = UILabel(frame: .zero)
        label.numberOfLines = 2
        label.text = "Unknown Component of type:\n \(String(describing: type))"
        label.textColor = .red
        label.backgroundColor = .yellow
        return label
        #else
        let view = UIView()
        return view
        #endif
    }
    
}
