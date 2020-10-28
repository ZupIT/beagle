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
    
    /// Use this function to register your custom operation.
    /// - Warning:
    ///     - Be careful when replacing a default operation in Beagle, consider creating it using `custom()`
    ///     - Custom Operations names must have at least 1 letter. It can also contain numbers and the character _
    /// - Parameters:
    ///   - operation: The custom operation you wish to register.
    ///   - handler: A closure where you tell us what your custom operation should do.
    func register(operation: Operation.Name, handler: @escaping OperationHandler)
    
    func getOperationHandler(with operation: Operation, in view: UIView) -> DynamicObject
    
    func checkCustomOperationExistence(_ operation: Operation.Name) -> Bool
}

public class CustomOperationsDefault: CustomOperationProvider {
    
    public typealias Dependencies =
        DependencyLogger

    let dependencies: Dependencies
    
    private(set) var operations: [Operation.Name: OperationHandler] = [:]
    
    // MARK: Init

    public init(dependencies: Dependencies) {
        self.dependencies = dependencies
    }
    
    public func register(operation: Operation.Name, handler: @escaping OperationHandler) {
        guard operation.rawValue.range(of: "^[a-zA-Z0-9_]*$", options: .regularExpression) != nil else {
            dependencies.logger.log(Log.customOperations(.invalid(name: operation.rawValue)))
            return
        }
        
        operations[operation] = handler
        
        guard case Operation.Name.custom = operation else {
            dependencies.logger.log(Log.customOperations(.alreadyExists))
            return
        }
    }
    
    public func getOperationHandler(with operation: Operation, in view: UIView) -> DynamicObject {
        guard let operationHandler = operations[operation.name] else {
            dependencies.logger.log(Log.customOperations(.notFound))
            return nil
        }
        
        let anyParameters = operation.evaluatedParameters(in: view).map { $0.asAny() }
        return operationHandler(anyParameters)
    }
    
    public func checkCustomOperationExistence(_ operation: Operation.Name) -> Bool {
        return operations.contains(where: { $0.key == operation })
    }
}
