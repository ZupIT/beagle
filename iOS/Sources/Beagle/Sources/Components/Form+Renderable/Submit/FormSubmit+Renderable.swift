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

extension Deprecated.FormSubmit: ServerDrivenComponent {
    
    public func toView(renderer: BeagleRenderer) -> UIView {
        let childView = renderer.render(child)
        childView.beagleFormElement = self
        
        let view = FormSubmitView(childView: childView, enabled: enabled)
        return view
    }
    
    final class FormSubmitView: UIView, Observer, WidgetStateObservable {
        
        weak var childView: UIView?
        let observable: Observable<WidgetState>
        
        init(
            childView: UIView,
            enabled: Bool?
        ) {
            self.childView = childView
            self.observable = Observable(value: WidgetState(value: enabled))
            super.init(frame: .zero)
            addSubview(childView)
        }
        
        required init?(coder aDecoder: NSCoder) {
            fatalError("init(coder:) has not been implemented")
        }
        
        func didChangeValue(_ value: Any?) {
            childView?.gestureRecognizers?
                .compactMap { $0 as? SubmitFormGestureRecognizer }
                .forEach { $0.updateSubmitView() }
        }
    }
}
