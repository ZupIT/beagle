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

import BeagleSchema
import UIKit

public extension Expression {

    func observe(
        view: UIView,
        controller: BeagleController,
        updateFunction: @escaping (T?) -> Void
    ) {
        switch self {
        case let .expression(expression):
            controller.addBinding {
                view.configBinding(for: expression, completion: updateFunction)
            }
        case let .value(value):
            updateFunction(value)
        }
    }

    func evaluate(with view: UIView?) -> T? {
        switch self {
        case let .expression(expression):
            return view?.evaluate(for: expression)
        case let .value(value):
            return value
        }
    }
}

// MARK: ExpressibleByLiteral
extension Expression: ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        let escaped = value.escapeExpressions()
        if let expression = SingleExpression(rawValue: value) {
            self = .expression(.single(expression))
        } else if let multiple = MultipleExpression(rawValue: value) {
            self = .expression(.multiple(multiple))
        } else if let value = escaped as? T {
            self = .value(value)
        } else {
            assertionFailure("Error: invalid Expression syntax \(value)")
            Beagle.dependencies.logger.log(Log.expression(.invalidSyntax))
            self = .expression(.multiple(MultipleExpression(nodes: [])))
        }
    }
}

extension Expression: ExpressibleByStringInterpolation {}

extension Expression: ExpressibleByIntegerLiteral where T == Int {
    public init(integerLiteral value: Int) {
        self = .value(value)
    }
}

extension Expression: ExpressibleByFloatLiteral where T == Float {
    public init(floatLiteral value: Float) {
        self = .value(value)
    }
}

// MARK: - Evaluate

extension SingleExpression {

    func evaluate(model: DynamicObject) -> DynamicObject {
        var nodes = self.path.nodes[...]
        return SingleExpression.evaluate(&nodes, model)
    }
    
    private static func evaluate(_ expression: inout ArraySlice<Path.Node>, _ model: DynamicObject) -> DynamicObject {
        guard let first = expression.first else {
            return model
        }
        switch first {
        case let .key(key):
            guard case let .dictionary(dictionary) = model, let value = dictionary[key] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)

        case let .index(index):
            guard case let .array(array) = model, let value = array[safe: index] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)
        }
    }
    
}
