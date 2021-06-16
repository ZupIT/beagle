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

/// The `AddChildren` action is responsible for adding a component to a component hierarchy.
public struct AddChildren: AnalyticsAction {
    
    /// Defines the widget's id, in which you want to add the views.
    public let componentId: String
    
    /// Defines the list of children you want to add.
    public let value: DynamicObject
    
    /// Defines the placement of where the children will be inserted in the list or if the contents of the list will be replaced.
    public var mode: Mode = .append
    
    /// Defines an analytics configuration for this action.
    public let analytics: ActionAnalyticsConfig?
    
    internal var staticValue: [ServerDrivenComponent]?
    
    public enum Mode: String, Codable {
        case append = "APPEND"
        case prepend = "PREPEND"
        case replace = "REPLACE"
    }

    public init(
        componentId: String,
        value: [ServerDrivenComponent],
        mode: Mode = .append,
        analytics: ActionAnalyticsConfig? = nil
    ) {
        self.componentId = componentId
        self.value = .empty
        self.staticValue = value
        self.mode = mode
        self.analytics = analytics
    }
    
    // MARK: Decodable
    
    enum CodingKeys: String, CodingKey {
        case componentId
        case value
        case mode
        case analytics
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        componentId = try container.decode(String.self, forKey: .componentId)
        value = try container.decode(DynamicObject.self, forKey: .value)
        mode = try container.decodeIfPresent(Mode.self, forKey: .mode) ?? .append
        analytics = try container.decodeIfPresent(ActionAnalyticsConfig.self, forKey: .analytics)
    }
}
