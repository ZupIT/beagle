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

/// The `Touchable` component defines a click listener.
public struct Touchable: ServerDrivenComponent, ClickedOnComponent, AutoDecodable {
    
    /// Defines an `Action` to be executed when the child component is clicked.
    public let onPress: [Action]
    
    @available(*, deprecated, message: "Since version 1.6, a new infrastructure for analytics (Analytics 2.0) was provided, for more info check https://docs.usebeagle.io/v1.9/resources/analytics/")
    /// Defines the event that will be triggered when clicked.
    public let clickAnalyticsEvent: AnalyticsClick?
    
    /// Defines the widget that will trigger the `Action`.
    public let child: ServerDrivenComponent

    @available(*, deprecated, message: "Since version 1.6, a new infrastructure for analytics (Analytics 2.0) was provided, for more info check https://docs.usebeagle.io/v1.9/resources/analytics/")
    public init(
        onPress: [Action],
        clickAnalyticsEvent: AnalyticsClick,
        child: ServerDrivenComponent
    ) {
        self.onPress = onPress
        self.clickAnalyticsEvent = clickAnalyticsEvent
        self.child = child
    }
    
    public init(
        onPress: [Action],
        child: ServerDrivenComponent
    ) {
        self.onPress = onPress
        self.child = child
        self.clickAnalyticsEvent = nil
    }
    
    public init(
        onPress: [Action],
        clickAnalyticsEvent: AnalyticsClick? = nil,
        @ChildBuilder
        _ child: () -> ServerDrivenComponent
    ) {
        if let analytics = clickAnalyticsEvent {
            self.init(onPress: onPress, clickAnalyticsEvent: analytics, child: child())
        } else {
            self.init(onPress: onPress, child: child())
        }
    }
}
