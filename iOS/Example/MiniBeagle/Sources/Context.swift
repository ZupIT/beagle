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

import Foundation
import UIKit

struct Context {
    let id: String
    let value: Any
}

extension Collection {
    subscript (safe index: Index) -> Element? {
        return indices.contains(index) ? self[index] : nil
    }
}

extension String {
    func matches(pattern: String) -> [String] {
        let regex = NSRegularExpression(pattern)
        let results = regex.matches(in: self, range: NSRange(self.startIndex..., in: self))
        return results.map {
            return String(self[Range($0.range(at: 0), in: self)!])
        }
    }

    func match(pattern: String) -> String? {
        let regex = NSRegularExpression(pattern)
        let result = regex.firstMatch(in: self, range: NSRange(self.startIndex..., in: self))
        guard let unwrapped = result else { return nil }
        return String(self[Range(unwrapped.range, in: self)!])
    }
}

// MARK: BeagleExtensions
// Observer pattern
//protocol ObserverProtocol {
//    var id : String { get }
//}

class Observable<T> {
    typealias CompletionHandler = ((T) -> Void)
    var value : T {
        didSet {
            self.notifyObservers()
        }
    }
    var observers : [CompletionHandler] = []
    init(_ value: T) {
        self.value = value
    }
    func addObserver(completion: @escaping CompletionHandler) {
        self.observers.append(completion)
    }

//    func removeObserver(_ observer: ObserverProtocol) {
//        self.observers.removeValue(forKey: observer.id)
//    }

    func notifyObservers() {
        observers.forEach { $0(value) }
    }
    deinit {
        observers.removeAll()
    }
}

//extension UIView: ObserverProtocol {
//    var id: String {
//        get {
//            self.accessibilityIdentifier ?? ""
//        }
//    }
//}

extension UIView {
    static var contextMapKey = "contextMapKey"
    private class ObjectWrapper<T> {
        let object: T?
        init(_ object: T?) {
            self.object = object
        }
    }
    var contextMap: [String: Observable<Context>]? {
        get {
            let contextMap: [String: Observable<Context>]? = (objc_getAssociatedObject(self, &UIView.contextMapKey) as? ObjectWrapper)?.object
//            print("getContextMap: \(contextMap), object: \(self)")
            return contextMap
        }
        set {
//            print("setContextMap: \(newValue), object: \(self)")
            objc_setAssociatedObject(self, &UIView.contextMapKey, ObjectWrapper(newValue), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }

    func findContext(for expression: Expression) -> Observable<Context>? { // traversal
        // change to config binding
        guard let contextMap = self.contextMap else {
//            print("parent: \(self.superview)")
            guard let parent = self.superview else {
                return nil
            }
            return parent.findContext(for: expression)
        }
        guard let contextId = expression.context(), let context = contextMap[contextId] else {
//            print("setContextMap: \(newValue), object: \(self)")
            guard let parent = self.superview else {
                return nil
            }
            return parent.findContext(for: expression)
        }
        return context
    }

    func configBinding<T>(for expression: Expression, completion: @escaping (T) -> Void) -> Void {
        guard let context = findContext(for: expression) else { return }
        let newExp = Expression(nodes: Array<Expression.Node>(expression.nodes.dropFirst()))
        let closure: (Context) -> Void = { context in
            print("value changed")
            if let value = newExp.evaluate(model: context.value) as? T {
                completion(value)
            }
        }
        print("value configured")
        context.addObserver(completion: closure)
        closure(context.value)
    }
}
