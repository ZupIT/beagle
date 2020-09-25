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
    
    private func setValue(_ value: DynamicObject) {
        context.value = Context(id: globalId, value: value)
    }
    
    public func set(value: DynamicObject, path: String? = nil) {
        var contextValue = context.value.value
        guard let path = path else {
            context.value = Context(id: globalId, value: value)
            return
        }
        contextValue.set(value, forPath: Path(nodes: [.key(path)]))
        context.value = Context(id: globalId, value: contextValue)
        
    }
    
    public func get(path: String? = nil) -> DynamicObject {
        
        guard let path = path else {
            return context.value.value
        }
        
        return compilePath(Path(nodes: [.key(path)]), in: context.value.value)
    }
    
    public func clear(path: String?) {
        guard let path = path else {
            context.value = Context(id: globalId, value: nil)
            return
        }
        
        let referencePath = Path(nodes: [.key(path)])
        var nodes = referencePath.nodes[...]
        context.value = Context(id: globalId, value: context.value.value.clear(nodes: &nodes))
        
    }
}
