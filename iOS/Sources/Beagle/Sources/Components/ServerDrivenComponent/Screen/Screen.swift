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

/// The screen element will help you define the screen view structure.
/// By using this component you can define configurations like whether or
/// not you want to use safe areas or display a tool bar/navigation bar.
public struct Screen: AutoInitiable, HasContext {
    
    /// identifies your screen globally inside your application so that it could have actions set on itself.
    public let identifier: String?
    
    /// Enables a few visual options to be changed.
    public let style: Style?
    
    /// Enables safe area to help you place your views within the visible portion of the overall interface.
    public let safeArea: SafeArea?
    
    /// Enables a action bar/navigation bar into your view. By default it is set as null.
    public let navigationBar: NavigationBar?
    
    /// Event send event when screen appear/disappear.
    public let screenAnalyticsEvent: AnalyticsScreen?
    
    /// Defines the child elements on this screen.
    public let child: ServerDrivenComponent
    
    /// Defines the context that be set to screen.
    public let context: Context?

// sourcery:inline:auto:Screen.Init
    public init(
        identifier: String? = nil,
        style: Style? = nil,
        safeArea: SafeArea? = nil,
        navigationBar: NavigationBar? = nil,
        screenAnalyticsEvent: AnalyticsScreen? = nil,
        child: ServerDrivenComponent,
        context: Context? = nil
    ) {
        self.identifier = identifier
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
        _ child: () -> ServerDrivenComponent
    ) {
        self.init(identifier: id, style: style, safeArea: safeArea, navigationBar: navigationBar, screenAnalyticsEvent: screenAnalyticsEvent, child: child(), context: context)
    }

}
