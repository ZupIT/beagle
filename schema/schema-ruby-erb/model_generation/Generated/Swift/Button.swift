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

public struct Button: Widget, ClickedOnComponent {

    public let text: Expression<String>
    public let styleId: String?
    public let onPress: [Action]?
    public let clickAnalyticsEvent: AnalyticsClick?

    public init(
        text: Expression<String>,
        styleId: String? = nil,
        onPress: [Action]? = nil,
        clickAnalyticsEvent: AnalyticsClick? = nil
    ) {
        self.text = text
        self.styleId = styleId
        self.onPress = onPress
        self.clickAnalyticsEvent = clickAnalyticsEvent
    }

}
