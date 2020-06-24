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

extension LazyComponent: ServerDrivenComponent {

    public func toView(renderer: BeagleRenderer) -> UIView {
        let view = renderer.render(initialState)
        lazyLoad(initialState: view, renderer: renderer)
        return view
    }
    
    private func lazyLoad(initialState view: UIView, renderer: BeagleRenderer) {
        renderer.controller.dependencies.repository.fetchComponent(url: path, additionalData: nil) {
            [weak view, weak renderer] result in
            guard let view = view, let renderer = renderer else { return }
            switch result {
            case .success(let component):
                view.update(lazyLoaded: component, renderer: renderer)
            case .failure(let error):
                renderer.controller.serverDrivenState = .error(.lazyLoad(error))
            }
        }
    }
}

extension UIView {
    fileprivate func update(
        lazyLoaded: ServerDrivenComponent,
        renderer: BeagleRenderer
    ) {
        let finalView: UIView
        if let updatable = self as? OnStateUpdatable,
            updatable.onUpdateState(component: lazyLoaded) {
            finalView = self
        } else {
            finalView = replace(with: lazyLoaded, renderer: renderer)
        }
        renderer.controller.dependencies.style(finalView).markDirty()
    }
    
    private func replace(
        with component: ServerDrivenComponent,
        renderer: BeagleRenderer
    ) -> UIView {
        guard let superview = superview else { return self }
        
        let newView = renderer.render(component)
        newView.frame = frame
        superview.insertSubview(newView, belowSubview: self)
        removeFromSuperview()
        
        if renderer.controller.dependencies.style(self).isFlexEnabled {
            renderer.controller.dependencies.style(newView).isFlexEnabled = true
        }
        return newView
    }
}
