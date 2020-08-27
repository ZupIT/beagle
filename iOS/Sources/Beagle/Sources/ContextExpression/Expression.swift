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
            controller.addBinding { [weak view] in
               view?.configBinding(for: expression, completion: updateFunction)
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
