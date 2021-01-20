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

/// Action that represents confirm
public struct Confirm: Action, AutoInitiableAndDecodable {
    
    public let title: Expression<String>?
    public let message: Expression<String>
    public let onPressOk: Action?
    public let onPressCancel: Action?
    public let labelOk: String?
    public let labelCancel: String?
    public let analytics: ActionAnalyticsConfig?

// sourcery:inline:auto:Confirm.Init
    public init(
        title: Expression<String>? = nil,
        message: Expression<String>,
        onPressOk: Action? = nil,
        onPressCancel: Action? = nil,
        labelOk: String? = nil,
        labelCancel: String? = nil,
        analytics: ActionAnalyticsConfig? = nil
    ) {
        self.title = title
        self.message = message
        self.onPressOk = onPressOk
        self.onPressCancel = onPressCancel
        self.labelOk = labelOk
        self.labelCancel = labelCancel
        self.analytics = analytics
    }
// sourcery:end
}
