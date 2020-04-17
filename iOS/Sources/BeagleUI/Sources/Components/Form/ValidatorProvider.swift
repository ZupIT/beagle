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

public protocol ValidatorProvider {
    func getValidator(name: String) -> Validator?
}

public protocol DependencyValidatorProvider {
    var validatorProvider: ValidatorProvider? { get }
}

public final class ValidatorProviding: ValidatorProvider {
    
    private var handlers: [String: ClosureValidator] = [:]
    
    public init() {
    }
    
    public func getValidator(name: String) -> Validator? {
        return handlers[name]
    }
    
    public subscript(name: String) -> ((Any) -> Bool)? {
        get {
            return handlers[name]?.closure
        }
        set {
            if let closure = newValue {
                handlers[name] = ClosureValidator(closure: closure)
            } else {
                handlers.removeValue(forKey: name)
            }
        }
    }
}

private class ClosureValidator: Validator {
    let closure: (Any) -> Bool
    
    init(closure: @escaping (Any) -> Bool) {
        self.closure = closure
    }
    
    func isValid(input: Any) -> Bool {
        return closure(input)
    }
}
