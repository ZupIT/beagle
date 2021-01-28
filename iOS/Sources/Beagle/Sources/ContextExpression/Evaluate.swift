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

    func evaluate(with view: UIView) -> DynamicObject {
        switch self {
        case .empty, .bool, .int, .double, .string:
            return self

        case let .array(array):
            return .array(array.map { $0.evaluate(with: view) })
        case let .dictionary(dictionary):
            return .dictionary(dictionary.mapValues { $0.evaluate(with: view) })
        case let .expression(expression):
            let dynamicObject: DynamicObject? = view.evaluateExpression(expression)
            return dynamicObject ?? .empty
        }
    }

    @available(*, deprecated, message: "use evaluate(with view: UIView) instead")
    func get(with view: UIView) -> DynamicObject {
        return evaluate(with: view)
    }
}

// MARK: - Internal

extension UIView {

    func evaluateExpression(_ expression: ContextExpression) -> DynamicObject {
        switch expression {
        case let .single(expression):
            return evaluateSingle(expression)
        case let .multiple(expression):
            return evaluateMultiple(expression)
        }
    }

    func evaluateSingle(_ expression: SingleExpression) -> DynamicObject {
        switch expression {
        case let .value(.binding(binding)):
            return binding.evaluate(in: self)
        case let .value(.literal(literal)):
            return literal.evaluate()
        case let .operation(operation):
            return operation.evaluate(in: self)
        }
    }

    func evaluateMultiple(_ expression: MultipleExpression, contextId: String? = nil) -> DynamicObject {
        var result: String = ""
        expression.nodes.forEach {
            switch $0 {
            case let .expression(expression):
                let evaluated: String? = evaluateWithCache(for: expression, contextId: contextId).transform()
                result += evaluated ?? ""
            case let .string(string):
                result += string
            }
        }
        return .string(result)
    }

    /// expression last value cache is used only for multiple expressions binding
    private func evaluateWithCache(for expression: SingleExpression, contextId: String? = nil) -> DynamicObject {
        switch expression {
        case let .value(.binding(binding)):
            if contextId == nil || contextId == binding.context {
                return evaluateSingle(expression)
            } else {
                return expressionLastValueMap[binding.rawValue, default: .empty]
            }
        case let .value(.literal(literal)):
            return literal.evaluate()
        case let .operation(operation):
            return operation.evaluate(in: self)
        }
    }
}

extension Operation {
    func evaluate(in view: UIView) -> DynamicObject {
        dependencies.operationsProvider.evaluate(with: self, in: view)
    }
}

extension Binding {
    func evaluate(in view: UIView) -> DynamicObject {
        guard let context = view.getContext(with: context) else { return nil }
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
