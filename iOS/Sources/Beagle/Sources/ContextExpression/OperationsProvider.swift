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

import BeagleSchema
import UIKit

public typealias Operation = BeagleSchema.Operation

public protocol DependencyOperationsProvider {
    var operationsProvider: OperationsProvider { get }
}

public protocol OperationsProvider {
    
    /// Use this function to register your custom operation.
    /// - Warning:
    ///     - Be careful when replacing a default operation in Beagle, consider creating it using `custom()`
    ///     - Custom Operations names must have at least 1 letter. It can also contain numbers and the character _
    /// - Parameters:
    ///   - operation: The custom operation you wish to register.
    ///   - handler: A closure where you tell us what your custom operation should do.
    func register(operationId: String, handler: @escaping OperationHandler)
    
    func evaluate(with operation: Operation, in view: UIView) -> DynamicObject
}

public class OperationsDefault: OperationsProvider {
    
    public typealias Dependencies =
        DependencyLogger

    let dependencies: Dependencies
    
    private(set) var operations: [String: OperationHandler] = [:]
    
    // MARK: Init

    public init(dependencies: Dependencies) {
        self.dependencies = dependencies
        registerDefault()
    }
    
    public func register(operationId: String, handler: @escaping OperationHandler) {
        guard
            operationId.range(of: #"^\w*[a-zA-Z_]+\w*$"#, options: .regularExpression) != nil else {
            dependencies.logger.log(Log.customOperations(.invalid(name: operationId)))
            return
        }
        
        if operations[operationId] != nil {
            dependencies.logger.log(Log.customOperations(.alreadyExists))
        }
        
        operations[operationId] = handler
    }
    
    public func evaluate(with operation: Operation, in view: UIView) -> DynamicObject {
        guard let operationHandler = operations[operation.name] else {
            dependencies.logger.log(Log.customOperations(.notFound))
            return nil
        }
        
        let parameters = evaluatedParameters(in: view, with: operation)
        return operationHandler(parameters)
    }
    
    func evaluatedParameters(in view: UIView, with operation: Operation) -> [DynamicObject] {
        return operation.parameters.map { parameter in
            switch parameter {
            case .operation(let operation):
                return operation.evaluate(in: view)
            case .value(.binding(let binding)):
                return binding.evaluate(in: view)
            case .value(.literal(let literal)):
                return literal.evaluate()
            }
        }
    }
}

// MARK: - Registering Default Operations

extension OperationsProvider {
    
    func registerDefault() {
        register(operationId: "sum", handler: sum())
        register(operationId: "subtract", handler: subtract())
        register(operationId: "multiply", handler: multiply())
        register(operationId: "divide", handler: divide())
        register(operationId: "condition", handler: condition())
        register(operationId: "not", handler: not())
        register(operationId: "and", handler: and())
        register(operationId: "or", handler: or())
        register(operationId: "gt", handler: gt())
        register(operationId: "gte", handler: gte())
        register(operationId: "lt", handler: lt())
        register(operationId: "lte", handler: lte())
        register(operationId: "eq", handler: eq())
        register(operationId: "concat", handler: concat())
        register(operationId: "capitalize", handler: capitalize())
        register(operationId: "uppercase", handler: uppercase())
        register(operationId: "lowercase", handler: lowercase())
        register(operationId: "substr", handler: substr())
        register(operationId: "insert", handler: insert())
        register(operationId: "remove", handler: remove())
        register(operationId: "removeIndex", handler: removeIndex())
        register(operationId: "contains", handler: contains())
        register(operationId: "isNull", handler: isNull())
        register(operationId: "isEmpty", handler: isEmpty())
        register(operationId: "length", handler: length())
    }
    
    // MARK: Number
    
    func sum() -> OperationHandler {
        return { parameters in
            guard !parameters.isEmpty else { return nil }
            
            let anyParameters = parameters.map { $0.asAny() }
            if let integerParameters = anyParameters as? [Int] {
                return .int(integerParameters.reduce(0, +))
            } else if let doubleParameters = anyParameters as? [Double] {
                return .double(doubleParameters.reduce(0.0, +))
            }
            
            return nil
        }
    }
    
    func subtract() -> OperationHandler {
        return { parameters in
            guard !parameters.isEmpty else { return nil }

            let anyParameters = parameters.map { $0.asAny() }
            if let integerParameters = anyParameters as? [Int] {
                return .int(integerParameters.reduce(integerParameters[0] * 2, -))
            } else if let doubleParameters = anyParameters as? [Double] {
                return .double(doubleParameters.reduce(doubleParameters[0] * 2, -))
            }
            return nil
        }
    }
    
    func multiply() -> OperationHandler {
        return { parameters in
            guard !parameters.isEmpty else { return nil }

            let anyParameters = parameters.map { $0.asAny() }
            if let integerParameters = anyParameters as? [Int] {
                return .int(integerParameters.reduce(1, *))
            } else if let doubleParameters = anyParameters as? [Double] {
                return .double(doubleParameters.reduce(1.0, *))
            }
            
            return nil
        }
    }
    
    func divide() -> OperationHandler {
        return { parameters in
            guard !parameters.isEmpty else { return nil }

            let anyParameters = parameters.map { $0.asAny() }
            if let integerParameters = anyParameters as? [Int] {
                return .int(integerParameters.reduce(integerParameters[0] * integerParameters[0], /))
            } else if let doubleParameters = anyParameters as? [Double] {
                return .double(doubleParameters.reduce(doubleParameters[0] * doubleParameters[0], /))
            }
            
            return nil
        }
    }
    
    // MARK: Logic
    
    func condition() -> OperationHandler {
        return { parameters in
            guard parameters.count == 3 else { return nil }

            guard case let .bool(firstParameter) = parameters[0],
                parameters[1].isEqualIgnoringAssociatedValues(parameters[2]) else { return nil }
            
            return firstParameter ? parameters[1] : parameters[2]
        }
    }
    
    func not() -> OperationHandler {
        return { parameters in
            guard parameters.count == 1 else { return nil }
            
            guard case let .bool(firstParameter) = parameters.first else { return nil }
            return .bool(!firstParameter)
        }
    }
    
    func and() -> OperationHandler {
        return { parameters in
            guard !parameters.isEmpty else { return nil }
            
            let anyParameters = parameters.map { $0.asAny() }
            if let boolParameters = anyParameters as? [Bool] {
                return .bool(!boolParameters.contains(false))
            }
            return nil
        }
    }
    
    func or() -> OperationHandler {
        return { parameters in
            guard !parameters.isEmpty else { return nil }
            
            let anyParameters = parameters.map { $0.asAny() }
            if let boolParameters = anyParameters as? [Bool] {
                return .bool(boolParameters.contains(true))
            }
            
            return nil
        }
    }
    
    // MARK: Comparison
    
    func gt() -> OperationHandler {
        return { parameters in
            guard parameters.count == 2 else { return nil }
            
            let anyParameters = parameters.map { $0.asAny() }
            if let firstInt = anyParameters.first as? Int,
                let lastInt = anyParameters.last as? Int {
                return .bool(firstInt > lastInt)
            } else if let firstDouble = anyParameters.first as? Double,
                let lastDouble = anyParameters.last as? Double {
                return .bool(firstDouble > lastDouble)
            }
            
            return nil
        }
    }
    
    func gte() -> OperationHandler {
        return { parameters in

            guard parameters.count == 2 else { return nil }
            
            let anyParameters = parameters.map { $0.asAny() }
            if let firstInt = anyParameters.first as? Int,
                let lastInt = anyParameters.last as? Int {
                return .bool(firstInt >= lastInt)
            } else if let firstDouble = anyParameters.first as? Double,
                let lastDouble = anyParameters.last as? Double {
                return .bool(firstDouble >= lastDouble)
            }
            
            return nil
        }
    }
    
    func lt() -> OperationHandler {
        return { parameters in

            guard parameters.count == 2 else { return nil }
            
            let anyParameters = parameters.map { $0.asAny() }
            if let firstInt = anyParameters.first as? Int,
                let lastInt = anyParameters.last as? Int {
                return .bool(firstInt < lastInt)
            } else if let firstDouble = anyParameters.first as? Double,
                let lastDouble = anyParameters.last as? Double {
                return .bool(firstDouble < lastDouble)
            }
            
            return nil
        }
    }
    
    func lte() -> OperationHandler {
        return { parameters in
            guard parameters.count == 2 else { return nil }
            let anyParameters = parameters.map { $0.asAny() }
            
            if let firstInt = anyParameters.first as? Int,
                let lastInt = anyParameters.last as? Int {
                return .bool(firstInt <= lastInt)
            } else if let firstDouble = anyParameters.first as? Double,
                let lastDouble = anyParameters.last as? Double {
                return .bool(firstDouble <= lastDouble)
            }
            
            return nil
        }
    }
    
    func eq() -> OperationHandler {
        return { parameters in
            guard parameters.count == 2 else { return nil }
            return .bool(parameters.first == parameters.last)
        }
    }
    
    // MARK: String
    
    func concat() -> OperationHandler {
        return { parameters in
            guard !parameters.isEmpty else { return nil }
            let anyParameters = parameters.map { $0.asAny() }
            
            if let stringParameters = anyParameters as? [String] {
                return .string(stringParameters.reduce("", +))
            }
            
            return nil
        }
    }
    
    func capitalize() -> OperationHandler {
        return { parameters in
            guard
                parameters.count == 1,
                case let .string(firstParameter) = parameters.first else {
                return nil
            }
            
            return .string(firstParameter.capitalized)
        }
    }
    
    func uppercase() -> OperationHandler {
        return { parameters in
            guard
                parameters.count == 1,
                case let .string(firstParameter) = parameters.first else {
                return nil
            }
            
            return .string(firstParameter.uppercased())
        }
    }
    
    func lowercase() -> OperationHandler {
        return { parameters in
            guard
                parameters.count == 1,
                case let .string(firstParameter) = parameters.first else {
                return nil
            }

            return .string(firstParameter.lowercased())
        }
    }
    
    func substr() -> OperationHandler {
        return { parameters in
            guard parameters.count >= 2 else { return nil }
            
            let anyParameters = parameters.map { $0.asAny() }
            guard let text = anyParameters[0] as? String,
                let from = anyParameters[1] as? Int,
                from >= 0, from <= text.count - 1 else { return nil }
            
            let fromIndex = text.index(text.startIndex, offsetBy: from)
            
            let fromToLengthIndex: String.Index
            if parameters.count == 3 {
                guard let length = anyParameters[2] as? Int,
                    from + length >= 0, from + length <= text.count else { return nil }
                fromToLengthIndex = text.index(fromIndex, offsetBy: length)
            } else {
                fromToLengthIndex = text.endIndex
            }
            
            let subString = text[fromIndex..<fromToLengthIndex]
            
            return .string(String(subString))
        }
    }
    
    // MARK: Array
    
    func insert() -> OperationHandler {
        return { parameters in
            guard parameters.count >= 2, case var .array(array) = parameters[0] else {
                return nil
            }
                    
            if parameters.count == 3 {
                guard case let .int(index) = parameters[2],
                    index >= 0, index <= array.count - 1  else { return nil }
                array.insert(parameters[1], at: index)
            } else {
                array.append(parameters[1])
            }
            
            return .array(array)
        }
    }
    
    func remove() -> OperationHandler {
        return { parameters in
            guard parameters.count == 2, case var .array(array) = parameters[0] else { return nil }
            
            array.removeAll { $0 == parameters[1] }
            return .array(array)
        }
    }
    
    func removeIndex() -> OperationHandler {
        return { parameters in
            guard parameters.count >= 1, case var .array(array) = parameters[0] else { return nil }
            
            if parameters.count == 2 {
                guard case let .int(index) = parameters[1],
                    index >= 0, index <= array.count - 1 else { return nil }
                array.remove(at: index)
            } else {
                array.removeLast()
            }
            
            return .array(array)
        }
    }
    
    func contains() -> OperationHandler {
        return { parameters in
            guard parameters.count == 2, case let .array(array) = parameters[0] else { return nil }
            
            return .bool(array.contains(parameters[1]))
        }
    }
    
    // MARK: Other
    
    func isNull() -> OperationHandler {
        return { parameters in
            guard parameters.count == 1 else { return nil }
            
            return .bool(parameters[0] == .empty)
        }
    }
    
    func isEmpty() -> OperationHandler {
        return { parameters in
            guard parameters.count == 1 else { return nil }

            if case let .string(string) = parameters.first {
                return .bool(string.isEmpty)
            } else if case let .array(array) = parameters.first {
                return .bool(array.isEmpty)
            } else if case let .dictionary(dictionary) = parameters.first {
                return .bool(dictionary.isEmpty)
            }
            
            return nil
        }
    }
    
    func length() -> OperationHandler {
        return { parameters in
            guard parameters.count == 1 else { return nil }
        
            if case let .string(string) = parameters.first {
                return .int(string.count)
            } else if case let .array(array) = parameters.first {
                return .int(array.count)
            } else if case let .dictionary(dictionary) = parameters.first {
                return .int(dictionary.count)
            }
            
            return nil
        }
    }
}
