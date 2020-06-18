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

extension KeyedDecodingContainer {
    
    public func decode(forKey key: KeyedDecodingContainer<K>.Key) throws -> ServerDrivenComponent {
        let content = try decode(AnyDecodableContainer.self, forKey: key)
        return (content.content as? ServerDrivenComponent) ?? UnknownComponent(type: String(describing: content.content))
    }
    
    public func decodeIfPresent(forKey key: KeyedDecodingContainer<K>.Key) throws -> ServerDrivenComponent? {
        let content = try decodeIfPresent(AnyDecodableContainer.self, forKey: key)
        return content?.content as? ServerDrivenComponent
    }
    
    public func decode(forKey key: KeyedDecodingContainer<K>.Key) throws -> [ServerDrivenComponent] {
        let content = try decode([AnyDecodableContainer].self, forKey: key)
        return content.map {
            ($0.content as? ServerDrivenComponent) ?? UnknownComponent(type: String(describing: $0.content))
        }
    }
    
    public func decode(forKey key: KeyedDecodingContainer<K>.Key) throws -> [Action] {
        let content = try decode([AnyDecodableContainer].self, forKey: key)
        return content.map {
            ($0.content as? Action) ?? UnknownAction(type: String(describing: $0.content))
        }
    }

    public func decodeIfPresent(forKey key: KeyedDecodingContainer<K>.Key) throws -> [Action]? {
        let content = try decodeIfPresent([AnyDecodableContainer].self, forKey: key)
        if let actions = content {
            return actions.map {
                ($0.content as? Action) ?? UnknownAction(type: String(describing: $0.content))
            }
        }
        return nil
    }

    public func decode(forKey key: KeyedDecodingContainer<K>.Key) throws -> Action {
        let content = try decode(AnyDecodableContainer.self, forKey: key)
        return (content.content as? Action) ?? UnknownAction(type: String(describing: content.content))
    }

    public func decodeIfPresent(forKey key: KeyedDecodingContainer<K>.Key) throws -> Action? {
        let content = try decodeIfPresent(AnyDecodableContainer.self, forKey: key)
        return content?.content as? Action
    }
}
