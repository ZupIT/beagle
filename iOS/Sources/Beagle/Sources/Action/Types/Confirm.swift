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

/// This action will show alert natively, such as an error alert indicating alternative flows, business system errors and others.
public struct Confirm: AnalyticsAction, AutoInitiableAndDecodable {
    
    /// Defines the title on the alert.
    public let title: Expression<String>?
    
    /// Defines the alert message.
    public let message: Expression<String>
    
    /// Defines the action of the button positive in the alert.
    public let onPressOk: Action?
    
    /// Defines the action of the button negative in the alert.
    public let onPressCancel: Action?
    
    /// Defines the text of the button positive in the alert.
    public let labelOk: String?
    
    /// Defines the text of the button negative in the alert.
    public let labelCancel: String?
    
    /// Defines an analytics configuration for this action.
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
