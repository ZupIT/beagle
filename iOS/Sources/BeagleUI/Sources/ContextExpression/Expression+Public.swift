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
    
    // TODO: revisar API action
    
//    func get(with view: UIView) -> T? {
//        switch self {
//        case let .expression(expression):
//            return view.evaluate(for: expression) as? T
//        case let .value(value):
//            return value
//        }
//    }
}

// MARK: ExpressibleByLiteral
// TODO: revisar tratativa de erro (criar Expression do tipo unknown???)
extension Expression: ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        if let expression = SingleExpression(rawValue: value) {
            self = .expression(expression)
        } else if let value = value as? T {
            self = .value(value)
        } else {
            preconditionFailure("Expected to set \(T.self) but found String instead")
        }
    }
}

extension Expression: ExpressibleByStringInterpolation {}

extension Expression: ExpressibleByIntegerLiteral {
    public init(integerLiteral value: Int) {
        if let value = value as? T {
            self = .value(value)
        } else {
            preconditionFailure("Expected to set \(T.self) but found Int instead")
        }
    }
}

extension Expression: ExpressibleByFloatLiteral {
    public init(floatLiteral value: Float) {
        if let value = value as? T {
            self = .value(value)
        } else {
            preconditionFailure("Expected to set \(T.self) but found Float instead")
        }
    }
}
