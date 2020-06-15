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

    func configBinding<T>(for expression: SingleExpression, completion: @escaping (T) -> Void) {
        guard let context = findContext(by: expression.context()) else { return }

        let newExp = SingleExpression(nodes: .init(expression.nodes.dropFirst()))
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
    
    func evaluate(for expression: SingleExpression) -> Any? {
        guard let contextMap = self.contextMap, let context = contextMap[expression.context()] else {
            return nil
        }
        let newExp = SingleExpression(nodes: .init(expression.nodes.dropFirst()))
        return newExp.evaluate(model: context.value.value)
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

// TODO: revisar onde vai ficar (no Expression ????)
public extension ServerDrivenComponent {
    func get<T>(
        _ expression: Expression<T>,
        with view: UIView,
        controller: BeagleContext,
        updateFunction: @escaping (T) -> Void
    ) -> T? {
        
        switch expression {
        case let .expression(expression):
            controller.bindingToConfig.append {
                view.configBinding(for: expression, completion: updateFunction)
            }
            return nil
        case let .value(value):
            return value
        }
    }
    
    func get<T>(
        _ expression: Expression<T?>,
        with view: UIView,
        controller: BeagleContext,
        updateFunction: @escaping (T?) -> Void
    ) -> T? {
        
        switch expression {
        case let .expression(expression):
            controller.bindingToConfig.append {
                view.configBinding(for: expression, completion: updateFunction)
            }
            return nil
        case let .value(value):
            return value
        }
    }
}

public extension Action {
    func get<T>(_ expression: Expression<T>, with view: UIView) -> T? {
        switch expression {
        case let .expression(expression):
            return view.evaluate(for: expression) as? T
        case let .value(value):
            return value
        }
    }
    
    func get<T>(_ expression: Expression<T?>, with view: UIView) -> T? {
        switch expression {
        case let .expression(expression):
            return view.evaluate(for: expression) as? T
        case let .value(value):
            return value
        }
    }
}

// TODO: Organizar
extension DynamicObject {
// TODO: usar mutating?
    func get(with view: UIView) -> DynamicObject {
        switch self {
        case .empty:
            return .empty
        case let .bool(bool):
            return .bool(bool)
        case let .int(int):
            return .int(int)
        case let .double(double):
            return .double(double)
        case let .string(string):
            return .string(string)
        case let .array(array):
            return .array(array.map { $0.get(with: view) })
        case let .dictionary(dictionary):
            return .dictionary(dictionary.mapValues { $0.get(with: view) })
        case let .expression(expression):
            return DynamicObject(from: view.evaluate(for: expression))
        }
    }
}

extension SingleExpression {

    func evaluate(model: DynamicObject) -> Any? {
        let model = model.asAny()
        var nodes = self.nodes[...]
        return SingleExpression.evaluate(&nodes, model)
    }
    
    private static func evaluate(_ expression: inout ArraySlice<Node>, _ model: Any?) -> Any? {
        guard let first = expression.first else {
            return model
        }
        switch first {
        case let .property(key):
            guard let dictionary = model as? [String: Any], let value = dictionary[key] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)

        case let .arrayItem(index):
            guard let array = model as? [Any], let value = array[safe: index] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)
        }
    }

    func context() -> String? {
        if let node = nodes.first {
            switch node {
            case let .property(context):
                return context
            default:
                return nil
            }
        }
        return  nil
    }
}
