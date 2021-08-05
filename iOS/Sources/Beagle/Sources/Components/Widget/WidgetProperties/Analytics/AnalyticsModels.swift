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

@available(*, deprecated, message: "Since version 1.6, a new infrastructure for analytics (Analytics 2.0) was provided, for more info check https://docs.usebeagle.io/v1.9/resources/analytics/")
public protocol AnalyticsEvent: Codable { }

@available(*, deprecated, message: "Since version 1.6, a new infrastructure for analytics (Analytics 2.0) was provided, for more info check https://docs.usebeagle.io/v1.9/resources/analytics/")
public struct AnalyticsClick: AnalyticsEvent, AutoInitiable {
    
    public let category: String
    public let label: String?
    public let value: String?

// sourcery:inline:auto:AnalyticsClick.Init
    public init(
        category: String,
        label: String? = nil,
        value: String? = nil
    ) {
        self.category = category
        self.label = label
        self.value = value
    }
// sourcery:end
}

@available(*, deprecated, message: "Since version 1.6, a new infrastructure for analytics (Analytics 2.0) was provided, for more info check https://docs.usebeagle.io/v1.9/resources/analytics/")
public struct AnalyticsScreen: AnalyticsEvent, AutoInitiable {
    public let screenName: String

// sourcery:inline:auto:AnalyticsScreen.Init
    public init(
        screenName: String
    ) {
        self.screenName = screenName
    }
// sourcery:end
}
