//
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

// MARK: - WidgetWithBind
protocol WidgetWithBind {
    init()
    func updateView(view: UIView)
}

// MARK: - BindedWidget
public class BindedWidget {
    var widget: Widget?
    var view: UIView?
    var observers: [ContextObserver]?
    
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        bind(context: context)
        guard let view = widget?.toView(context: context, dependencies: dependencies) else {
            return UIView()
        }
        self.view = view
        return view
    }
    
    func bind(context: BeagleContext) { }
    
    func configBinding<T>(for expression: Expression, completion: @escaping (T) -> Void) {
        guard let contextId = expression.context(),
            let context = view?.findContext(by: contextId) else { return }

        let newExp = Expression(nodes: .init(expression.nodes.dropFirst()))
        let closure: (Context) -> Void = { context in
            if let value = newExp.evaluate(model: context.value) as? T {
                completion(value)
            }
        }

        let contextObserver = ContextObserver(onContextChange: closure)

        if observers == nil {
            observers = []
        }
        observers?.append(contextObserver)
        context.addObserver(contextObserver)
        closure(context.value)
    }
}
