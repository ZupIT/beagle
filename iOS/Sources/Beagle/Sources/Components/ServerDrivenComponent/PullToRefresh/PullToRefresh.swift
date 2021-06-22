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

/// Adds a RefreshControl on the child if it is a ListView, GridView, Text or ScrollView,
/// otherwise it creates a ScrollView and configures an RefreshControl for it
public struct PullToRefresh: ServerDrivenComponent, HasContext, AutoInitiableAndDecodable {
    
    /// Defines the context of the component.
    public var context: Context?
    
    /// List of actions that are performed when the component is pulled
    public var onPull: [Action]?
    
    /// Expression that controls when the loading indicator is showing
    public var isRefreshing: Expression<Bool>?
    
    /// Defines the color of the loading indicator
    public var color: Expression<String>?
    
    /// Defines the widget that will be configured with the RefreshControl.
    public var child: ServerDrivenComponent
    
// sourcery:inline:auto:PullToRefresh.Init
    public init(
        context: Context? = nil,
        onPull: [Action]? = nil,
        isRefreshing: Expression<Bool>? = nil,
        color: Expression<String>? = nil,
        child: ServerDrivenComponent
    ) {
        self.context = context
        self.onPull = onPull
        self.isRefreshing = isRefreshing
        self.color = color
        self.child = child
    }
// sourcery:end
}
