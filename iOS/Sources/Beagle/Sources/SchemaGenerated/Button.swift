// THIS IS A GENERATED FILE. DO NOT EDIT THIS
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

/// Define a button natively using the server driven information received through Beagle
public struct Button: Widget, TouchableAnalytics, AutoDecodable {

    /// define the button text content.
    public var text: Expression<String>
    /// reference a native style in your local styles file to be applied on this button.
    public var styleId: String?
    /// attribute to define action when onPress
    public var onPress: [Action]?
    /// attribute to set disabled
    public var disabled: Expression<Bool>?
    public var widgetProperties: WidgetProperties
    /// attribute to define click event name
    public var clickAnalyticsEvent: ClickEvent?

    public init(
        text: Expression<String>,
        styleId: String? = nil,
        onPress: [Action]? = nil,
        disabled: Expression<Bool>? = nil,
        widgetProperties: WidgetProperties = WidgetProperties(),
        clickAnalyticsEvent: ClickEvent? = nil
    ) {
        self.text = text
        self.styleId = styleId
        self.onPress = onPress
        self.disabled = disabled
        self.widgetProperties = widgetProperties
        self.clickAnalyticsEvent = clickAnalyticsEvent
    }

}
