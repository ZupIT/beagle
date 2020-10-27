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

import Foundation

public typealias OperationHandler = (_ evaluatedParameters: [Any?]) -> DynamicObject

public struct Operation {
    public let name: Name
    public let parameters: [Parameter]

    public enum Parameter: Equatable {
        case operation(Operation)
        case value(Value)
    }

    public init(name: Name, parameters: [Parameter]) {
        self.name = name
        self.parameters = parameters
    }
}

extension Operation: RepresentableByParsableString {
    public static let parser = operation

    public var rawValue: String {
        var result = "\(name)("
        
        for (index, parameter) in parameters.enumerated() {
            switch parameter {
            case let .operation(operation):
                result += operation.rawValue
            case let .value(value):
                result += value.rawValue
            }
            
            if index != parameters.count - 1 {
                result += ", "
            }
        }
        result += ")"
        
        return result
    }
}

extension Operation {
    public enum Name: RawRepresentable, CaseIterable, Hashable {
        public static var allCases: [Name] = [.sum, .subtract, .multiply, .divide, .condition, .not, .and, .or, .gt, .gte, .lt, .lte, eq, .concat, .capitalize, .uppercase, .lowercase, .substr, .insert, .remove, .removeIndex, .contains, .isNull, .isEmpty, .length, .custom("")]
        
        // number
        case sum, subtract, multiply, divide

        // logic
        case condition, not, and, or

        // comparison
        case gt, gte, lt, lte, eq

        // string
        case concat, capitalize, uppercase, lowercase, substr

        // array
        case insert, remove, removeIndex, contains

        // other
        case isNull, isEmpty, length
        
        // new
        case custom(String)
        
        public init?(rawValue: String) {
            for value in Operation.Name.allCases where "\(value)" == rawValue {
                self = value
                return
            }
            return nil
        }

        public var rawValue: String {
            switch self {
            case .sum: return "sum"
            case .subtract: return "subtract"
            case .multiply: return "multiply"
            case .divide: return "divide"
            case .condition: return "condition"
            case .not: return "not"
            case .and: return "and"
            case .or: return "or"
            case .gt: return "gt"
            case .gte: return "gte"
            case .lt: return "lt"
            case .lte: return "lte"
            case .eq: return "eq"
            case .concat: return "concat"
            case .capitalize: return "capitalize"
            case .uppercase: return "uppercase"
            case .lowercase: return "lowercase"
            case .substr: return "substr"
            case .insert: return "insert"
            case .remove: return "remove"
            case .removeIndex: return "removeIndex"
            case .contains: return "contains"
            case .isNull: return "isNull"
            case .isEmpty: return "isEmpty"
            case .length: return "length"
            case .custom(let name): return name
            }
        }
    }
}
