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

final class GlobalContext {
    
    public static var global = GlobalContext()
    
    public var contextMap: [String: Observable<Context>]?
    
    func isGlobal(id: String) -> Bool {
        return id == Context.globalId
    }
    
    func getContext(with id: String?) -> Observable<Context>? {
        guard let contextId = id, isGlobal(id: contextId) else { return nil }
        
        guard let context = contextMap?[contextId] else {
            setContext(Context(id: contextId, value: .string("")))
            return getContext(with: id)
        }
        return context
    }
    
    func setContext(_ context: Context) {
        if var contextMap = contextMap {
            if let contextObservable = contextMap[context.id] {
                contextObservable.value = context
            } else {
                contextMap[context.id] = Observable(value: context)
            }
            self.contextMap = contextMap
        } else {
            contextMap = [context.id: Observable(value: context)]
        }
    }
}
