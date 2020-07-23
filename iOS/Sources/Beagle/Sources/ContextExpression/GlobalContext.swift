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

public protocol GlobalContext {
    var globalId: String { get }
    
    func isGlobal(id: String?) -> Bool
    func getContext() -> Observable<Context>?
    func setContextValue(_ value: DynamicObject)
}

public protocol DependencyGlobalContext {
    var globalContext: GlobalContext { get }
}

final public class DefaultGlobalContext: GlobalContext {
    
    public let globalId = "global"
    
    private(set) var contextObservable: Observable<Context>?
    
    public func isGlobal(id: String?) -> Bool {
        globalId == id
    }
    
    public func getContext() -> Observable<Context>? {
        guard let contextObservable = contextObservable else {
            setContextValue(.empty)
            return getContext()
        }
        return contextObservable
    }
    
    public func setContextValue(_ value: DynamicObject) {
        let context = Context(id: globalId, value: value)
        
        if let contextObservable = contextObservable {
            contextObservable.value = context
        } else {
            contextObservable = Observable(value: context)
        }
    }
}
