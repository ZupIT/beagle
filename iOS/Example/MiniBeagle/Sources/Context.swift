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
            return contextMap
        }
        set {
            objc_setAssociatedObject(self, &UIView.contextMapKey, ObjectWrapper(newValue), .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }

    func findContext(for expression: Expression) -> Observable<Context>? { // traversal
        // change to config binding
        guard let contextMap = self.contextMap else {
            return self.superview?.findContext(for: expression)
        }
        guard let contextId = expression.context(), let context = contextMap[contextId] else {
            return self.superview?.findContext(for: expression)
        }
        return context
    }

    func configBinding<T>(for expression: Expression, completion: @escaping (T) -> Void) {
        guard let context = findContext(for: expression) else { return }

        let newExp = Expression(nodes: .init(expression.nodes.dropFirst()))
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
