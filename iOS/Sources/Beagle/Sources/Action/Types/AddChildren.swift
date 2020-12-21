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

/// Action that insert children components in a node hierarchy
public struct AddChildren: Action, AutoInitiableAndDecodable {
    
    public let componentId: String
    public let value: [ServerDrivenComponent]
    public var mode: Mode = .append
    public let analytics: ActionAnalyticsConfig?
    
    public enum Mode: String, Codable {
        case append = "APPEND"
        case prepend = "PREPEND"
        case replace = "REPLACE"
    }

// sourcery:inline:auto:AddChildren.Init
    public init(
        componentId: String,
        value: [ServerDrivenComponent],
        mode: Mode = .append,
        analytics: ActionAnalyticsConfig? = nil
    ) {
        self.componentId = componentId
        self.value = value
        self.mode = mode
        self.analytics = analytics
    }
// sourcery:end
}
