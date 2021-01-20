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

/// Action to represent a alert
public struct Alert: Action, AutoInitiableAndDecodable {
    
    public let title: Expression<String>?
    public let message: Expression<String>
    public let onPressOk: Action?
    public let labelOk: String?
    public let analytics: ActionAnalyticsConfig?

// sourcery:inline:auto:Alert.Init
    public init(
        title: Expression<String>? = nil,
        message: Expression<String>,
        onPressOk: Action? = nil,
        labelOk: String? = nil,
        analytics: ActionAnalyticsConfig? = nil
    ) {
        self.title = title
        self.message = message
        self.onPressOk = onPressOk
        self.labelOk = labelOk
        self.analytics = analytics
    }
// sourcery:end
}
