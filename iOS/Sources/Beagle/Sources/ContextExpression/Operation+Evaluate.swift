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

import UIKit
import BeagleSchema

extension BeagleSchema.Operation {
    func evaluate(in view: UIView) -> DynamicObject {
        switch self.name {
        case .sum:
            return sum(in: view)
        case .subtract:
            return subtract(in: view)
        case .multiply:
            return multiply(in: view)
        case .divide:
            return divide(in: view)
        case .condition:
            return condition(in: view)
        case .not:
            return not(in: view)
        case .and:
            return and(in: view)
        case .or:
            return or(in: view)
        case .gt:
            return gt(in: view)
        case .gte:
            return gte(in: view)
        case .lt:
            return lt(in: view)
        case .lte:
            return lte(in: view)
        case .eq:
            return eq(in: view)
        case .concat:
            return concat(in: view)
        case .capitalize:
            return capitalize(in: view)
        case .uppercase:
            return uppercase(in: view)
        case .lowercase:
            return lowercase(in: view)
        case .substr:
            return substr(in: view)
        case .insert:
            return insert(in: view)
        case .remove:
            return remove(in: view)
        case .removeIndex:
            return removeIndex(in: view)
        case .contains:
            return contains(in: view)
        case .isNull:
            return isNull(in: view)
        case .isEmpty:
            return isEmpty(in: view)
        case .length:
            return length(in: view)
        }
    }
    
    private func evaluatedParameters(in view: UIView) -> [DynamicObject] {
        return parameters.map { parameter in
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
    
    // MARK: Number
    
    private func sum(in view: UIView) -> DynamicObject {
        guard !parameters.isEmpty else { return nil }
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
        if let integerParameters = anyParameters as? [Int] {
            return .int(integerParameters.reduce(0, +))
        } else if let doubleParameters = anyParameters as? [Double] {
            return .double(doubleParameters.reduce(0.0, +))
        }
        
        return nil
    }
    
    private func subtract(in view: UIView) -> DynamicObject {
        guard !parameters.isEmpty else { return nil }
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
        if let integerParameters = anyParameters as? [Int] {
            return .int(integerParameters.reduce(integerParameters[0] * 2, -))
        } else if let doubleParameters = anyParameters as? [Double] {
            return .double(doubleParameters.reduce(doubleParameters[0] * 2, -))
        }
        
        return nil
    }
    
    private func multiply(in view: UIView) -> DynamicObject {
        guard !parameters.isEmpty else { return nil }
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
        if let integerParameters = anyParameters as? [Int] {
            return .int(integerParameters.reduce(1, *))
        } else if let doubleParameters = anyParameters as? [Double] {
            return .double(doubleParameters.reduce(1.0, *))
        }
        
        return nil
    }
    
    private func divide(in view: UIView) -> DynamicObject {
        guard !parameters.isEmpty else { return nil }
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
        if let integerParameters = anyParameters as? [Int] {
            return .int(integerParameters.reduce(integerParameters[0] * integerParameters[0], /))
        } else if let doubleParameters = anyParameters as? [Double] {
            return .double(doubleParameters.reduce(doubleParameters[0] * doubleParameters[0], /))
        }
        
        return nil
    }
    
    // MARK: Logic
    
    private func condition(in view: UIView) -> DynamicObject {
        guard parameters.count == 3 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
        guard case let .bool(firstParameter) = parameters[0],
            parameters[1].isEqualIgnoringAssociatedValues(parameters[2]) else { return nil }
        
        return firstParameter ? parameters[1] : parameters[2]
    }
    
    private func not(in view: UIView) -> DynamicObject {
        guard parameters.count == 1 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
        guard case let .bool(firstParameter) = parameters.first else { return nil }
        
        return .bool(!firstParameter)
    }
    
    private func and(in view: UIView) -> DynamicObject {
        guard !parameters.isEmpty else { return nil }
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
        if let boolParameters = anyParameters as? [Bool] {
            return .bool(!boolParameters.contains(false))
        }
        
        return nil
    }
    
    private func or(in view: UIView) -> DynamicObject {
        guard !parameters.isEmpty else { return nil }
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
        if let boolParameters = anyParameters as? [Bool] {
            return .bool(boolParameters.contains(true))
        }
        
        return nil
    }
    
    // MARK: Comparison
    
    private func gt(in view: UIView) -> DynamicObject {
        guard parameters.count == 2 else { return nil }
        
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
        if let firstInt = anyParameters.first as? Int,
            let lastInt = anyParameters.last as? Int {
            return .bool(firstInt > lastInt)
        } else if let firstDouble = anyParameters.first as? Double,
            let lastDouble = anyParameters.last as? Double {
            return .bool(firstDouble > lastDouble)
        }
        
        return nil
    }
    
    private func gte(in view: UIView) -> DynamicObject {
        guard parameters.count == 2 else { return nil }
        
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
        if let firstInt = anyParameters.first as? Int,
            let lastInt = anyParameters.last as? Int {
            return .bool(firstInt >= lastInt)
        } else if let firstDouble = anyParameters.first as? Double,
            let lastDouble = anyParameters.last as? Double {
            return .bool(firstDouble >= lastDouble)
        }
        
        return nil
    }
    
    private func lt(in view: UIView) -> DynamicObject {
        guard parameters.count == 2 else { return nil }
        
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
        if let firstInt = anyParameters.first as? Int,
            let lastInt = anyParameters.last as? Int {
            return .bool(firstInt < lastInt)
        } else if let firstDouble = anyParameters.first as? Double,
            let lastDouble = anyParameters.last as? Double {
            return .bool(firstDouble < lastDouble)
        }
        
        return nil
    }
    
    private func lte(in view: UIView) -> DynamicObject {
        guard parameters.count == 2 else { return nil }
        
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
        if let firstInt = anyParameters.first as? Int,
            let lastInt = anyParameters.last as? Int {
            return .bool(firstInt <= lastInt)
        } else if let firstDouble = anyParameters.first as? Double,
            let lastDouble = anyParameters.last as? Double {
            return .bool(firstDouble <= lastDouble)
        }
        
        return nil
    }
    
    private func eq(in view: UIView) -> DynamicObject {
        guard parameters.count == 2 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
        return .bool(parameters.first == parameters.last)
    }
    
    // MARK: String
    
    private func concat(in view: UIView) -> DynamicObject {
        guard !parameters.isEmpty else { return nil }
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
        if let stringParameters = anyParameters as? [String] {
            return .string(stringParameters.reduce("", +))
        }
        
        return nil
    }
    
    private func capitalize(in view: UIView) -> DynamicObject {
        guard parameters.count == 1 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
        guard case let .string(firstParameter) = parameters.first else { return nil }
        
        return .string(firstParameter.capitalized)
    }
    
    private func uppercase(in view: UIView) -> DynamicObject {
        guard parameters.count == 1 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
        guard case let .string(firstParameter) = parameters.first else { return nil }
        
        return .string(firstParameter.uppercased())
    }
    
    private func lowercase(in view: UIView) -> DynamicObject {
        guard parameters.count == 1 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
        guard case let .string(firstParameter) = parameters.first else { return nil }
        
        return .string(firstParameter.lowercased())
    }
    
    private func substr(in view: UIView) -> DynamicObject {
        guard parameters.count >= 2 else { return nil }
        
        let anyParameters = evaluatedParameters(in: view).map { $0.asAny() }
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
    
    // MARK: Array
    
    private func insert(in view: UIView) -> DynamicObject {
        guard parameters.count >= 2 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
        guard case var .array(array) = parameters[0] else { return nil }
        
        if parameters.count == 3 {
            guard case let .int(index) = parameters[2],
                index >= 0, index <= array.count - 1  else { return nil }
            array.insert(parameters[1], at: index)
        } else {
            array.append(parameters[1])
        }
        
        return .array(array)
    }
    
    private func remove(in view: UIView) -> DynamicObject {
        guard parameters.count == 2 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
        guard case var .array(array) = parameters[0] else { return nil }
        
        array.removeAll { $0 == parameters[1] }
        
        return .array(array)
    }
    
    private func removeIndex(in view: UIView) -> DynamicObject {
        guard parameters.count >= 1 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
        guard case var .array(array) = parameters[0] else { return nil }
        
        if parameters.count == 2 {
            guard case let .int(index) = parameters[1],
                index >= 0, index <= array.count - 1 else { return nil }
            array.remove(at: index)
        } else {
            array.removeLast()
        }
        
        return .array(array)
    }
    
    private func contains(in view: UIView) -> DynamicObject {
        guard parameters.count == 2 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
        guard case let .array(array) = parameters[0] else { return nil }
        
        return .bool(array.contains(parameters[1]))
    }
    
    // MARK: Other
    
    private func isNull(in view: UIView) -> DynamicObject {
        guard parameters.count == 1 else { return nil }
        
        return .bool(evaluatedParameters(in: view)[0] == .empty)
    }
    
    private func isEmpty(in view: UIView) -> DynamicObject {
        guard parameters.count == 1 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
        if case let .string(string) = parameters.first {
            return .bool(string.isEmpty)
        } else if case let .array(array) = parameters.first {
            return .bool(array.isEmpty)
        } else if case let .dictionary(dictionary) = parameters.first {
            return .bool(dictionary.isEmpty)
        }
        
        return nil
    }
    
    private func length(in view: UIView) -> DynamicObject {
        guard parameters.count == 1 else { return nil }
        
        let parameters = evaluatedParameters(in: view)
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
