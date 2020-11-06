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

/// Defines a container to hold any registered Decodable type
public struct AnyDecodableContainer {
    public let content: Decodable
}

// MARK: - Decodable

extension AnyDecodableContainer: Decodable {
    
    enum CodingKeys: String, CodingKey {
        case componentType = "_beagleComponent_"
        case actionType = "_beagleAction_"
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        
        if let type = try? container.decode(String.self, forKey: .componentType) {
            if let decodable = dependencies.decoder.componentType(forType: type.lowercased()) {
                content = try decodable.init(from: decoder)
            } else {
                dependencies.logger.log(Log.decode(.decodingError(type: type)))
                content = UnknownComponent(type: type)
            }
        } else {
            let type = try container.decode(String.self, forKey: .actionType)
            if let decodable = dependencies.decoder.actionType(forType: type) {
                content = try decodable.init(from: decoder)
            } else {
                dependencies.logger.log(Log.decode(.decodingError(type: type)))
                content = UnknownAction(type: type)
            }
        }
    }
}

extension KeyedDecodingContainer {
    
    // MARK: - Action
    public func decode(forKey key: KeyedDecodingContainer<K>.Key) throws -> Action {
        let content = try decode(AnyDecodableContainer.self, forKey: key)
        return (content.content as? Action) ?? UnknownAction(type: String(describing: content.content))
    }
    
    public func decodeIfPresent(forKey key: KeyedDecodingContainer<K>.Key) throws -> Action? {
        let content = try decodeIfPresent(AnyDecodableContainer.self, forKey: key)
        return content?.content as? Action
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
    
    // MARK: - ServerDrivenComponent
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
    
    public func decode(forKey key: KeyedDecodingContainer<K>.Key) throws -> [ServerDrivenComponent]? {
        let content = try decodeIfPresent([AnyDecodableContainer].self, forKey: key)
        
        if let components = content {
            return components.map {
                ($0.content as? ServerDrivenComponent) ?? UnknownComponent(type: String(describing: $0.content))
            }
        }
        return nil
    }
    
}

public struct UnknownComponent: ServerDrivenComponent, AutoInitiable {
    public var type: String

// sourcery:inline:auto:UnknownComponent.Init
    public init(
        type: String
    ) {
        self.type = type
    }
// sourcery:end
}
