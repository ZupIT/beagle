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

class ComponentHostController: BeagleController {

    let component: RawComponent
    let renderer: BeagleRenderer

    private var bindings: [() -> Void] = []

    var dependencies: BeagleDependenciesProtocol {
        return renderer.controller.dependencies
    }
    var serverDrivenState: ServerDrivenState {
        get { renderer.controller.serverDrivenState }
        set { renderer.controller.serverDrivenState = newValue }
    }
    var screenType: ScreenType {
        return renderer.controller.screenType
    }
    var screen: Screen? {
        return renderer.controller.screen
    }

    func addOnInit(_ onInit: [RawAction], in view: UIView) {
        renderer.controller.addOnInit(onInit, in: view)
    }

    func addBinding<T: Decodable>(expression: ContextExpression, in view: UIView, update: @escaping (T?) -> Void) {
        bindings.append { [weak self, weak view] in
            guard let self = self else { return }
            view?.configBinding(
                for: expression,
                completion: self.bindBlock(view: view, update: update)
            )
        }
    }
    
    private func bindBlock<T: Decodable>(view: UIView?, update: @escaping (T?) -> Void) -> (T?) -> Void {
        return { [weak self, weak view] value in
            update(value)
            view?.yoga.markDirty()
            self?.viewIfLoaded?.setNeedsLayout()
        }
    }
    
    func execute(actions: [RawAction]?, origin: UIView) {
        renderer.controller.execute(actions: actions, origin: origin)
    }

    func execute(actions: [RawAction]?, with contextId: String, and contextValue: DynamicObject, origin: UIView) {
        renderer.controller.execute(actions: actions, with: contextId, and: contextValue, origin: origin)
    }

    func configBindings() {
        while let bind = bindings.popLast() {
            bind()
        }
    }

    init(_ component: RawComponent, renderer: BeagleRenderer) {
        self.component = component
        self.renderer = renderer
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func loadView() {
        let renderer = BeagleRenderer(controller: self)
        view = renderer.render(component)
    }

    override func viewDidLayoutSubviews() {
        configBindings()
        dependencies.style(view).applyLayout()
        super.viewDidLayoutSubviews()
    }

}
