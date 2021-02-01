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

public struct AnalyticsConfig: AutoInitiableAndDecodable, Codable {

    /// Default is true, when false no analytics will be generated by this system when a screen is loaded.
    public var enableScreenAnalytics: Bool = true
    
    /// A map of actions allowed to create analytics records. By default no action creates records.
    ///
    /// In this map, each key is the name of a `_beagleAction_`, and values are an array of strings. The value
    /// indicates which properties of the action will compose the analytics record. If we want to send for instance, the `url` and
    /// `method` of every `"beagle:sendRequest"`, we must create the entry
    /// `{"beagle:sendRequest": ["url", "method"]}`.
    public var actions = AttributesByActionName()

    public typealias AttributesByActionName = [String: [String]]

// sourcery:inline:auto:AnalyticsConfig.Init
    public init(
        enableScreenAnalytics: Bool = true,
        actions: AttributesByActionName = AttributesByActionName()
    ) {
        self.enableScreenAnalytics = enableScreenAnalytics
        self.actions = actions
    }
// sourcery:end
}
