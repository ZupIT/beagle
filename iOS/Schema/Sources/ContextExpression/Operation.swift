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

public typealias OperationHandler = (_ parameters: [DynamicObject]) -> DynamicObject

public struct Operation {
    public let name: String
    public let parameters: [Parameter]

    public enum Parameter: Equatable {
        case operation(Operation)
        case value(Value)
    }

    public init(name: String, parameters: [Parameter] = []) {
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
