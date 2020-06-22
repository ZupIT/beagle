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
    
    func get(
        with view: UIView,
        controller: BeagleController,
        updateFunction: @escaping (T) -> Void
    ) -> T? {
        switch self {
        case let .expression(expression):
            controller.addBinding {
                view.configBinding(for: expression, completion: updateFunction)
            }
            return nil
        case let .value(value):
            return value
        }
    }
    
    func get(
        with view: UIView,
        controller: BeagleController,
        updateFunction: @escaping (T?) -> Void
    ) -> T? {
        switch self {
        case let .expression(expression):
            controller.addBinding {
                view.configBinding(for: expression, completion: updateFunction)
            }
            return nil
        case let .value(value):
            return value
        }
    }

    func get(with view: UIView) -> T? {
        switch self {
        case let .expression(expression):
            return view.evaluate(for: expression) as? T
        case let .value(value):
            return value
        }
    }
}

// MARK: ExpressibleByLiteral
extension Expression: ExpressibleByStringLiteral where T == String {
    public init(stringLiteral value: String) {
        if let expression = SingleExpression(rawValue: value) {
            self = .expression(.single(expression))
        } else if let multiple = MultipleExpression(rawValue: value) {
            self = .expression(.multiple(multiple))
        } else {
            self = .value(value)
        }
    }
}

extension Expression: ExpressibleByExtendedGraphemeClusterLiteral where T == String {
    public typealias ExtendedGraphemeClusterLiteralType = String
}
extension Expression: ExpressibleByUnicodeScalarLiteral where T == String {
    public typealias UnicodeScalarLiteralType = String
}

extension Expression: ExpressibleByStringInterpolation where T == String {}

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

// MARK: Evaluate

extension SingleExpression {

    func evaluate(model: DynamicObject) -> Any? {
        let model = model.asAny()
        var nodes = self.path.nodes[...]
        return SingleExpression.evaluate(&nodes, model)
    }
    
    private static func evaluate(_ expression: inout ArraySlice<Path.Node>, _ model: Any?) -> Any? {
        guard let first = expression.first else {
            return model
        }
        switch first {
        case let .key(key):
            guard let dictionary = model as? [String: Any], let value = dictionary[key] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)

        case let .index(index):
            guard let array = model as? [Any], let value = array[safe: index] else {
                return nil
            }
            expression.removeFirst()
            return evaluate(&expression, value)
        }
    }
}
