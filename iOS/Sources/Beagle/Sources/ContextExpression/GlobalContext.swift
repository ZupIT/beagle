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

public protocol GlobalContext {
    var globalId: String { get }
    var context: Observable<Context> { get }
    
    func isGlobal(id: String?) -> Bool
    func set(value: DynamicObject, path: String?)
    func get(path: String?) -> DynamicObject
    func clear(path: String?)
}

public extension GlobalContext {
    func set(_ value: DynamicObject) {
        set(value: value, path: nil)
    }
    
    func get() -> DynamicObject {
        get(path: nil)
    }
    
    func clear() {
        clear(path: nil)
    }
}

public protocol DependencyGlobalContext {
    var globalContext: GlobalContext { get }
}

public class DefaultGlobalContext: GlobalContext {
    
    public let globalId = "global"
    
    private(set) public
    lazy var context = Observable(value:
        Context(id: globalId, value: .empty)
    )
    
    public func isGlobal(id: String?) -> Bool {
        globalId == id
    }
    
    public func set(value: DynamicObject, path: String?) {
        guard let pathObject = path.toPath() else {
            return
        }
        let contextValue = context.value.value
        context.value = Context(id: globalId, value: contextValue.set(value, with: pathObject))
    }
    
    public func get(path: String? = nil) -> DynamicObject {
        guard let pathObject = path.toPath() else {
            return .empty
        }
        return context.value.value[pathObject]
    }
    
    public func clear(path: String?) {
        set(value: nil, path: path)
    }
    
}

private extension Optional where Wrapped == String {
    func toPath() -> Path? {
        switch self {
        case let .some(string):
            return Path(rawValue: string)
        case .none:
            return Path(nodes: [])
        }
    }
}
