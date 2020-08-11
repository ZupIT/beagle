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
    private static var contextMapKey = "contextMapKey"
    private static var expressionLastValueMapKey = "expressionLastValueMapKey"
    
    private class ObjectWrapper<T> {
        let object: T?
        
        init(_ object: T?) {
            self.object = object
        }
    }

    var contextMap: [String: Observable<Context>] {
        get {
            return (objc_getAssociatedObject(self, &UIView.contextMapKey) as? ObjectWrapper)?.object ?? [String: Observable<Context>]()
        }
        set {
            objc_setAssociatedObject(self, &UIView.contextMapKey, ObjectWrapper(newValue), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
        
    var expressionLastValueMap: [String: DynamicObject] {
        get {
            return (objc_getAssociatedObject(self, &UIView.expressionLastValueMapKey) as? ObjectWrapper)?.object ?? [String: DynamicObject]()
        }
        set {
            objc_setAssociatedObject(self, &UIView.expressionLastValueMapKey, ObjectWrapper(newValue), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
    
    // MARK: Context Expression
    
    func configBinding<T: Decodable>(for expression: ContextExpression, completion: @escaping (T?) -> Void) {
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
    
    private func configBinding<T: Decodable>(for expression: SingleExpression, completion: @escaping (T?) -> Void) {
        guard let context = getContext(with: expression.context) else { return }
        let closure: (Context) -> Void = { context in
            let dynamicObject = expression.evaluate(model: context.value)
            let value: T? = self.transform(dynamicObject)
            completion(value)
        }
        let contextObserver = ContextObserver(onContextChange: closure)
        context.addObserver(contextObserver)
        closure(context.value)
    }
    
    private func evaluate<T: Decodable>(for expression: SingleExpression) -> T? {
        guard let context = getContext(with: expression.context) else { return nil }
        let dynamicObject = expression.evaluate(model: context.value.value)
        expressionLastValueMap[expression.rawValue] = dynamicObject
        return transform(dynamicObject)
    }
    
    // MARK: Multiple Expression
    
    private func configBinding<T: Decodable>(for expression: MultipleExpression, completion: @escaping (T?) -> Void) {
        expression.nodes.forEach {
            if case let .expression(single) = $0 {
                guard let context = getContext(with: single.context) else { return }
                let closure: (Context) -> Void = { _ in
                    let value: T? = self.evaluate(for: expression, contextId: single.context)
                    completion(value)
                }
                let contextObserver = ContextObserver(onContextChange: closure)
                context.addObserver(contextObserver)
            }
        }
        let value: T? = self.evaluate(for: expression)
        completion(value)
    }
    
    private func evaluate<T: Decodable>(for expression: MultipleExpression, contextId: String? = nil) -> T? {
        var result: String = ""
        
        expression.nodes.forEach {
            switch $0 {
            case let .expression(expression):
                let evaluated: String? = evaluateWithCache(for: expression, contextId: contextId)
                result += evaluated ?? ""
            case let .string(string):
                result += string
            }
        }
        return result as? T
    }
    
    // MARK: Get/Set Context
    
    func getContext(with id: String) -> Observable<Context>? {
        let global = dependencies.globalContext
        if global.isGlobal(id: id) {
            return global.context
        }
        guard let context = contextMap[id] else {
            let observable = superview?.getContext(with: id)
            if let contextObservable = observable {
                contextMap[id] = contextObservable
            }
            return observable
        }
        return context
    }
    
    func setContext(_ context: Context) {
        let global = dependencies.globalContext
        guard !global.isGlobal(id: context.id) else {
            global.setValue(context.value)
            return
        }
        if let contextObservable = contextMap[context.id] {
            contextObservable.value = context
        } else {
            contextMap[context.id] = Observable(value: context)
        }
    }
    
    // MARK: Private
    
    private func transform<T: Decodable>(_ dynamicObject: DynamicObject) -> T? {
        let encoder = JSONEncoder()
        let decoder = JSONDecoder()
        if #available(iOS 13.0, *) {
            guard let data = try? encoder.encode(dynamicObject) else { return nil }
            return try? decoder.decode(T.self, from: data)
        } else {
            // here we use array as a wrapper because iOS 12 (or prior) JSONEncoder/Decoder bug
            // https://bugs.swift.org/browse/SR-6163
            guard let data = try? encoder.encode([dynamicObject]) else { return nil }
            return try? decoder.decode([T].self, from: data).first
        }
    }
        
    // expression last value cache is used only for multiple expressions binding
    private func evaluateWithCache<T: Decodable>(for expression: SingleExpression, contextId: String? = nil) -> T? {
        if contextId == nil || contextId == expression.context {
            return evaluate(for: expression)
        } else {
            return transform(expressionLastValueMap[expression.rawValue] ?? .empty)
        }
    }
    
}
