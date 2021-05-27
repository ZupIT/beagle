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

public extension DynamicObject {

    func evaluate(with view: UIView?, implicitContext: Context? = nil) -> DynamicObject {
        switch self {
        case .empty, .bool, .int, .double, .string:
            return self

        case let .array(array):
            return .array(array.map { $0.evaluate(with: view, implicitContext: implicitContext) })
        case let .dictionary(dictionary):
            return .dictionary(dictionary.mapValues { $0.evaluate(with: view, implicitContext: implicitContext) })
        case let .expression(expression):
            let dynamicObject: DynamicObject? = view?.evaluateExpression(expression, implicitContext: implicitContext)
            return dynamicObject ?? .empty
        }
    }

    @available(*, deprecated, message: "use evaluate(with view: UIView) instead")
    func get(with view: UIView, implicitContext: Context? = nil) -> DynamicObject {
        return evaluate(with: view, implicitContext: implicitContext)
    }
}

// MARK: - Internal

extension UIView {

    func evaluateExpression(_ expression: ContextExpression, implicitContext: Context?) -> DynamicObject {
        switch expression {
        case let .single(expression):
            return evaluateSingle(expression, implicitContext: implicitContext)
        case let .multiple(expression):
            return evaluateMultiple(expression, implicitContext: implicitContext)
        }
    }

    func evaluateSingle(_ expression: SingleExpression, implicitContext: Context? = nil) -> DynamicObject {
        switch expression {
        case let .value(.binding(binding)):
            return binding.evaluate(in: self, implicitContext: implicitContext)
        case let .value(.literal(literal)):
            return literal.evaluate()
        case let .operation(operation):
            return operation.evaluate(in: self, implicitContext: implicitContext)
        }
    }

    func evaluateMultiple(_ expression: MultipleExpression, implicitContext: Context? = nil, contextId: String? = nil) -> DynamicObject {
        var result: String = ""
        expression.nodes.forEach {
            switch $0 {
            case let .expression(expression):
                let evaluated: String? = evaluateWithCache(for: expression, implicitContext: implicitContext, contextId: contextId).transform()
                result += evaluated ?? ""
            case let .string(string):
                result += string
            }
        }
        return .string(result)
    }

    /// expression last value cache is used only for multiple expressions binding
    private func evaluateWithCache(for expression: SingleExpression, implicitContext: Context?, contextId: String? = nil) -> DynamicObject {
        switch expression {
        case let .value(.binding(binding)):
            if contextId == nil || contextId == binding.context {
                return evaluateSingle(expression, implicitContext: implicitContext)
            } else {
                return expressionLastValueMap[binding.rawValue, default: .empty]
            }
        case let .value(.literal(literal)):
            return literal.evaluate()
        case let .operation(operation):
            return operation.evaluate(in: self, implicitContext: implicitContext)
        }
    }
}

extension Operation {
    func evaluate(in view: UIView, implicitContext: Context?) -> DynamicObject {
        dependencies.operationsProvider.evaluate(with: self, in: view, implicitContext: implicitContext)
    }
}

extension Binding {
    func evaluate(in view: UIView, implicitContext: Context?) -> DynamicObject {
        guard let context = view.getContext(with: context, implicitContext: implicitContext) else { return nil }
        let dynamicObject = context.value.value[path]
        view.expressionLastValueMap[rawValue] = dynamicObject
        return dynamicObject
    }
}

extension Literal {
    func evaluate() -> DynamicObject {
        switch self {
        case .int(let int):
            return .int(int)
        case .double(let double):
            return .double(double)
        case .bool(let bool):
            return .bool(bool)
        case .string(let string):
            return .string(string)
        case .null:
            return .empty
        }
    }
}
