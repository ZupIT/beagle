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

extension UIView {
    static var contextMapKey = "contextMapKey"
    static var observers = "contextObservers"

    private class ObjectWrapper<T> {
        let object: T?
        
        init(_ object: T?) {
            self.object = object
        }
    }

    var contextMap: [String: Observable<Context>]? {
        get {
            return (objc_getAssociatedObject(self, &UIView.contextMapKey) as? ObjectWrapper)?.object
        }
        set {
            objc_setAssociatedObject(self, &UIView.contextMapKey, ObjectWrapper(newValue), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
    
    // TODO: remove this
    var observers: [ContextObserver]? {
        get {
            return (objc_getAssociatedObject(self, &UIView.observers) as? ObjectWrapper)?.object
        }
        set {
            objc_setAssociatedObject(self, &UIView.observers, ObjectWrapper(newValue), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }

    func findContext(by id: String?) -> Observable<Context>? {
        guard let contextMap = self.contextMap else {
            return superview?.findContext(by: id)
        }
        guard let context = contextMap[id] else {
            return superview?.findContext(by: id)
        }
        return context
    }

    func configBinding<T>(for expression: Expression, completion: @escaping (T) -> Void) {
        guard let context = findContext(by: expression.context()) else { return }

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
    
    func evaluate(for expression: Expression) -> Any {
        guard let contextMap = self.contextMap, let context = contextMap[expression.context()] else {
            return ()
        }
        let newExp = Expression(nodes: .init(expression.nodes.dropFirst()))
        return newExp.evaluate(model: context.value.value) ?? ()
    }
}

private extension Dictionary where Key == String, Value == Observable<Context> {
    subscript(context: String?) -> Observable<Context>? {
        guard let id = context else {
            return self.first?.value
        }
        return self[id]
    }
}
