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
import Components

//TODO: avoid the casting to SErver driven component
extension FormSubmit: Renderable {
    
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        guard let childView = (child as? ServerDrivenComponent)?.toView(context: context, dependencies: dependencies) else {
            return UIView()
        }
        childView.flex.isEnabled = true
        childView.beagleFormElement = self
        
        let view = FormSubmitView(childView: childView, enabled: enabled, dependencies: dependencies)
        return view
    }
    
    final class FormSubmitView: UIView, Observer, WidgetStateObservable {
        
        let childView: UIView
        let observable: Observable<WidgetState>
        private var dependencies: RenderableDependencies?
        
        init(
            childView: UIView,
            enabled: Bool?,
            dependencies: RenderableDependencies?
        ) {
            self.childView = childView
            self.observable = Observable(value: WidgetState(value: enabled))
            self.dependencies = dependencies
            super.init(frame: .zero)
            addSubview(childView)
        }
        
        required init?(coder aDecoder: NSCoder) {
            fatalError("init(coder:) has not been implemented")
        }
        
        func didChangeValue(_ value: Any?) {
            childView.gestureRecognizers?
                .compactMap { $0 as? SubmitFormGestureRecognizer }
                .forEach { $0.updateSubmitView() }
        }
    }
}
