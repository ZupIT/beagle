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

public protocol DependencyCustomOperationProvider {
    var customOperationsProvider: CustomOperationProvider { get }
}

public protocol CustomOperationProvider {
    func register(operation: Operation, handler: @escaping OperationHandler)
    func getCustomOperationHandler(with operation: Operation, in view: UIView) -> DynamicObject
    func checkCustomOperationExistence(_ operation: Operation) -> Bool
}

public class CustomOperationProviding: CustomOperationProvider {
    private(set) public var operations: [Operation.Name: OperationHandler] = [:]
    
    public func register(operation: Operation, handler: @escaping OperationHandler) {
        if operation.name != .custom("") {
            dependencies.logger.log(Log.customOperations(.alreadyExists))
        }
        operations[operation.name] = handler
    }
    
    public func getCustomOperationHandler(with operation: Operation, in view: UIView) -> DynamicObject {
        guard let operationHandler = operations[operation.name] else { return nil }
        let anyParameters = operation.evaluatedParameters(in: view).map { $0.asAny() }
        return operationHandler(anyParameters)
    }
    
    public func checkCustomOperationExistence(_ operation: Operation) -> Bool {
        return operations.contains(where: { $0.key == operation.name })
    }
}
