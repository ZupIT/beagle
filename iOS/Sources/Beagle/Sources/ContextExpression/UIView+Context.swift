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
import BeagleSchema

extension UIView {
    static var contextMapKey = "contextMapKey"
    
    private class ObjectWrapper<T> {
        let object: T?
        
        init(_ object: T?) {
            self.object = object
        }
    }

    public var contextMap: [String: Observable<Context>]? {
        get {
            return (objc_getAssociatedObject(self, &UIView.contextMapKey) as? ObjectWrapper)?.object
        }
        set {
            objc_setAssociatedObject(self, &UIView.contextMapKey, ObjectWrapper(newValue), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
    
    // TODO: fix weak reference
    static var observers = "contextObservers"
    private var observers: [ContextObserver]? {
        get {
            return (objc_getAssociatedObject(self, &UIView.observers) as? ObjectWrapper)?.object
        }
        set {
            objc_setAssociatedObject(self, &UIView.observers, ObjectWrapper(newValue), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
    
    // MARK: Context Expression
    
    func configBinding<T: Decodable>(for expression: ContextExpression, completion: @escaping (T) -> Void) {
        switch expression {
        case let .single(expression):
            configBinding(for: expression, completion: completion)
        case let .multiple(expression):
            configBinding(for: expression, completion: completion)
        }
    }
    
    func evaluate<T: Decodable>(for expression: ContextExpression) -> T? {
        switch expression {
        case let .single(expression):
            return evaluate(for: expression)
        case let .multiple(expression):
            return evaluate(for: expression)
        }
    }

    // MARK: Single Expression
    
    private func configBinding<T: Decodable>(for expression: SingleExpression, completion: @escaping (T) -> Void) {
        guard let context = getContext(with: expression.context) else { return }
        let closure: (Context) -> Void = { context in
            let dynamicObject = expression.evaluate(model: context.value)
            if let value: T = self.transform(dynamicObject) {
                completion(value)
            }
        }
        configBinding(with: context, completion: closure)
        closure(context.value)
    }
    
    private func evaluate<T: Decodable>(for expression: SingleExpression) -> T? {
        guard let context = getContext(with: expression.context) else { return nil }
        let dynamicObject = expression.evaluate(model: context.value.value)
        return transform(dynamicObject)
    }
    
    // MARK: Multiple Expression
    
    private func configBinding<T: Decodable>(for expression: MultipleExpression, completion: @escaping (T) -> Void) {
        expression.nodes.forEach {
            if case let .expression(single) = $0 {
                guard let context = getContext(with: single.context) else { return }
                configBinding(with: context) { _ in
                    if let value: T = self.evaluate(for: expression, contextId: single.context) {
                        completion(value)
                    }
                }
            }
        }
        if let value: T = self.evaluate(for: expression) {
            completion(value)
        }
    }
    
    private func evaluate<T: Decodable>(for expression: MultipleExpression, contextId: String? = nil) -> T? {
        var result: String = ""
        
        expression.nodes.forEach {
            switch $0 {
            case let .expression(expression):
                // TODO: create cache mechanism
                let evaluated: String? = evaluate(for: expression)
                result += evaluated ?? expression.rawValue
            case let .string(string):
                result += string
            }
        }
        return result as? T
    }
    
    // MARK: Get/Set Context
    
    func getContext(with id: String?) -> Observable<Context>? {
        guard let contextMap = self.contextMap, let context = contextMap[id] else {
            // TODO: create cache mechanism
            return superview?.getContext(with: id)
        }
        return context
    }
    
    func setContext(_ context: Context) {
        if var contextMap = contextMap {
            if let contextObservable = contextMap[context.id] {
                contextObservable.value = context
            } else {
                contextMap[context.id] = Observable(value: context)
            }
            self.contextMap = contextMap
        } else {
            contextMap = [context.id: Observable(value: context)]
        }
    }
    
    // MARK: Private
    
    private func configBinding(with context: Observable<Context>, completion: @escaping (Context) -> Void) {
        let contextObserver = ContextObserver(onContextChange: completion)
        if observers == nil {
            observers = []
        }
        observers?.append(contextObserver)
        context.addObserver(contextObserver)
    }
    
    private func transform<T: Decodable>(_ dynamicObject: DynamicObject) -> T? {
        let encoder = JSONEncoder()
        let decoder = JSONDecoder()
        guard let data = try? encoder.encode(dynamicObject) else { return nil }
        return try? decoder.decode(T.self, from: data)
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
