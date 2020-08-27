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
@testable import Beagle
import XCTest

typealias Operation = BeagleSchema.Operation

class OperationEvaluationTests: XCTestCase {
    func evaluateOperations(_ operations: [Operation], contexts: [Context], completion: ([DynamicObject]) -> Void) {
        // Given
        let view = UIView()
        
        // When
        contexts.forEach { view.setContext($0) }
        let evaluatedResults = operations.map {
            $0.evaluate(in: view)
        }
        
        // Then
        completion(evaluatedResults)
    }
}

extension String {
    func toOperation(name: Operation.Name) -> Operation? {
        Operation(rawValue: name.rawValue + "(\(self))")
    }
}

extension Array where Element == String {
    func toOperations(name: Operation.Name) -> [Operation] {
        self.compactMap { $0.toOperation(name: name) }
    }
}
