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
import Foundation
import UIKit

extension DynamicObject {
    
    public func get(with view: UIView) -> DynamicObject {
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

// MARK: ExpressibleByLiteral

extension DynamicObject: ExpressibleByNilLiteral {
    public init(nilLiteral: ()) {
        self = .empty
    }
}

extension DynamicObject: ExpressibleByBooleanLiteral {
    public init(booleanLiteral value: Bool) {
        self = .bool(value)
    }
}

extension DynamicObject: ExpressibleByIntegerLiteral {
    public init(integerLiteral value: Int) {
        self = .int(value)
    }
}

extension DynamicObject: ExpressibleByFloatLiteral {
    public init(floatLiteral value: Double) {
        self = .double(value)
    }
}

extension DynamicObject: ExpressibleByStringLiteral {
    public init(stringLiteral value: String) {
        if let expression = SingleExpression(rawValue: value) {
            self = .expression(expression)
        } else {
            self = .string(value)
        }
    }
}

extension DynamicObject: ExpressibleByStringInterpolation {}

extension DynamicObject: ExpressibleByArrayLiteral {
    public init(arrayLiteral elements: DynamicObject...) {
        self = .array(elements)
    }
}

extension DynamicObject: ExpressibleByDictionaryLiteral {
    public init(dictionaryLiteral elements: (String, DynamicObject)...) {
        var dictionary: [String: DynamicObject] = [:]
        elements.forEach { dictionary[$0.0] = $0.1 }
        self = .dictionary(dictionary)
    }
}
