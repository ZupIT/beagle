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

/// The screen element will help you define the screen view structure. By using this component you can define configurations like whether or not you want to use safe areas or display a tool bar/navigation bar.
public struct Screen: HasContext {

    /// identifies your screen globally inside your application so that it could have actions set on itself.
    public var id: String?
    /// enable a few visual options to be changed.
    public var style: Style?
    /// enable Safe areas to help you place your views within the visible portion of the overall interface. By default it is not enabled and it wont constrain considering any safe area.
    public var safeArea: SafeArea?
    /// enable a action bar/navigation bar into your view. By default it is set as null.
    public var navigationBar: NavigationBar?
    /// send event when screen appear/disappear
    public var screenAnalyticsEvent: AnalyticsScreen?
    /// define the child elements on this screen. It could be any visual component that extends ServerDrivenComponent
    public var child: RawComponent?
    public var context: Context?

    public init(
        id: String? = nil,
        style: Style? = nil,
        safeArea: SafeArea? = nil,
        navigationBar: NavigationBar? = nil,
        screenAnalyticsEvent: AnalyticsScreen? = nil,
        child: RawComponent? = nil,
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

}
