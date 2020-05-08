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

/// Your application can define CustomActions, and Beagle will ask to the CustomActionHandler defined by your application
/// to handle what the custom actions will do.
public protocol CustomActionHandler {

    /// here you should see which custom action you are receiving, and execute its logic.
    func handle(context: BeagleContext, action: CustomAction, listener: @escaping Listener)

    /// use it to inform Beagle about the on going execution
    typealias Listener = (CustomActionState) -> Void
}

public protocol DependencyCustomActionHandler {
    var customActionHandler: CustomActionHandler? { get }
}

public final class CustomActionHandling: CustomActionHandler {
    
    public typealias Handler = (BeagleContext, CustomAction, @escaping Listener) -> Void
    
    private var handlers: [String: Handler]
    
    public init(handlers: [String: Handler] = [:]) {
        self.handlers = handlers
    }
    
    public func handle(context: BeagleContext, action: CustomAction, listener: @escaping Listener) {
        self[action.name]?(context, action, listener)
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

public enum CustomActionState {
    case start
    case error(Error)
    case success(action: Action)
}
