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

/// Defines form local actions, that is, that do not make http requests, such as an action that creates a customized Dialog.
public struct FormLocalAction: AnalyticsAction, AutoInitiable {
    
    /// Defines the name of the action.
    public let name: String
    
    /// Sending data for the action.
    public let data: [String: String]
    
    /// Defines an analytics configuration for this action.
    public let analytics: ActionAnalyticsConfig?

// sourcery:inline:auto:FormLocalAction.Init
    public init(
        name: String,
        data: [String: String],
        analytics: ActionAnalyticsConfig? = nil
    ) {
        self.name = name
        self.data = data
        self.analytics = analytics
    }
// sourcery:end
}
