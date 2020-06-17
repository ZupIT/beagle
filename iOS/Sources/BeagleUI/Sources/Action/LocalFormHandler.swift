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
import BeagleSchema

/// Your application can handle the form submit by its own using a `FormLocalAction`.
/// The `LocalFormHandler` defined by your application will be used to achieve it.
public protocol LocalFormHandler {

    /// here you should see which custom action you are receiving, and execute its logic.
    func handle(action: FormLocalAction, controller: BeagleController, listener: @escaping Listener)

    /// use it to inform Beagle about the on going execution
    typealias Listener = (FormActionState) -> Void
}

public protocol DependencyLocalFormHandler {
    var localFormHandler: LocalFormHandler? { get }
}

public final class LocalFormHandling: LocalFormHandler {
    
    public typealias Handler = (BeagleController, FormLocalAction, @escaping Listener) -> Void
    
    private var handlers: [String: Handler]
    
    public init(handlers: [String: Handler] = [:]) {
        self.handlers = handlers
    }
    
    public func handle(action: FormLocalAction, controller: BeagleController, listener: @escaping Listener) {
        self[action.name]?(controller, action, listener)
    }
    
    public subscript(name: String) -> Handler? {
        get {
            return handlers[name]
        }
        set {
            handlers[name] = newValue
        }
    }
}

public enum FormActionState {
    case start
    case error(Error)
    case success(action: Action)
}
