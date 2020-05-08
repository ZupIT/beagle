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

public protocol LazyLoadManaging {
    func lazyLoad(url: String, initialState: UIView)
}

protocol LazyLoadManagerDelegate: AnyObject {
    func replaceView(_ oldView: UIView, with component: ServerDrivenComponent)
    func applyLayout()
}

class LazyLoadManager: LazyLoadManaging {
    
    // MARK: - Dependencies
    
    typealias Dependencies = DependencyRepository
    
    var dependencies: Dependencies
    
    // MARK: - Delegate
    
    typealias delegates =
    LazyLoadManagerDelegate
    & ContextErrorHandlingDelegate
    
    public weak var delegate: delegates?
    
    // MARK: - Init
    
    init(
        dependencies: Dependencies,
        delegate: delegates? = nil
    ) {
        self.dependencies = dependencies
        self.delegate = delegate
    }
    
    // MARK: - Functions
    
    public func lazyLoad(url: String, initialState: UIView) {
        dependencies.repository.fetchComponent(url: url, additionalData: nil) {
            [weak self] result in guard let self = self else { return }

            switch result {
            case .success(let component):
                self.update(initialView: initialState, lazyLoaded: component)

            case .failure(let error):
                self.delegate?.handleContextError(.lazyLoad(error))
            }
        }
    }

    private func update(initialView: UIView, lazyLoaded: ServerDrivenComponent) {
        let updatable = initialView as? OnStateUpdatable
        let updated = updatable?.onUpdateState(component: lazyLoaded) ?? false

        if updated && initialView.flex.isEnabled {
            initialView.flex.markDirty()
        } else if !updated {
            delegate?.replaceView(initialView, with: lazyLoaded)
        }
        delegate?.applyLayout()
    }
}
