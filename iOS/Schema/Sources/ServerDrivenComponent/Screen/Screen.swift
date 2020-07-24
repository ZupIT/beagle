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

public struct Screen: AutoInitiable, HasContext {
    
    // MARK: - Public Properties
    
    public let id: String?
    public let style: Style?
    public let safeArea: SafeArea?
    public let navigationBar: NavigationBar?
    public let screenAnalyticsEvent: AnalyticsScreen?
    public let child: RawComponent
    public let context: Context?

// sourcery:inline:auto:Screen.Init
    public init(
        id: String? = nil,
        style: Style? = nil,
        safeArea: SafeArea? = nil,
        navigationBar: NavigationBar? = nil,
        screenAnalyticsEvent: AnalyticsScreen? = nil,
        child: RawComponent,
        context: Context? = nil
    ) {
        self.id = id
        self.style = style
        self.safeArea = safeArea
        self.navigationBar = navigationBar
        self.screenAnalyticsEvent = screenAnalyticsEvent
        self.child = child
        self.context = context
    }
// sourcery:end
    
    public init(
        id: String? = nil,
        style: Style? = nil,
        safeArea: SafeArea? = nil,
        navigationBar: NavigationBar? = nil,
        screenAnalyticsEvent: AnalyticsScreen? = nil,
        context: Context? = nil,
        @ChildBuilder
        _ child: () -> RawComponent
    ) {
        self.init(id: id, style: style, safeArea: safeArea, navigationBar: navigationBar, screenAnalyticsEvent: screenAnalyticsEvent, child: child(), context: context)
    }

}
