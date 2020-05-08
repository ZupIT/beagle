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

import UIKit

struct ScreenComponent: AppearanceComponent {

    // MARK: - Public Properties
    
    public let identifier: String?
    public let appearance: Appearance?
    public let safeArea: SafeArea?
    public let navigationBar: NavigationBar?
    public let child: ServerDrivenComponent
    public let screenAnalyticsEvent: AnalyticsScreen?
    
    // MARK: - Initialization
    
    public init(
        identifier: String? = nil,
        appearance: Appearance? = nil,
        safeArea: SafeArea? = nil,
        navigationBar: NavigationBar? = nil,
        screenAnalyticsEvent: AnalyticsScreen? = nil,
        child: ServerDrivenComponent
    ) {
        self.identifier = identifier
        self.appearance = appearance
        self.safeArea = safeArea
        self.navigationBar = navigationBar
        self.child = child
        self.screenAnalyticsEvent = screenAnalyticsEvent
    }
}

extension ScreenComponent: Decodable {
    enum CodingKeys: String, CodingKey {
        case identifier
        case appearance
        case safeArea
        case navigationBar
        case child
        case screenAnalyticsEvent
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.identifier = try container.decodeIfPresent(String.self, forKey: .identifier)
        self.appearance = try container.decodeIfPresent(Appearance.self, forKey: .appearance)
        self.safeArea = try container.decodeIfPresent(SafeArea.self, forKey: .safeArea)
        self.navigationBar = try container.decodeIfPresent(NavigationBar.self, forKey: .navigationBar)
        self.screenAnalyticsEvent = try container.decodeIfPresent(AnalyticsScreen.self, forKey: .screenAnalyticsEvent)
        self.child = try container.decode(forKey: .child)
    }
}

extension ScreenComponent: Renderable {
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {

        prefetch(dependencies: dependencies)
        
        let contentView = buildChildView(context: context, dependencies: dependencies)
        contentView.beagle.setup(appearance: appearance)
        return contentView
    }

    // MARK: - Private Functions
    
    private func prefetch(dependencies: RenderableDependencies) {
        navigationBar?.navigationBarItems?
            .compactMap { $0.action as? Navigate }
            .compactMap { $0.newPath }
            .forEach { dependencies.preFetchHelper.prefetchComponent(newPath: $0) }
    }
    
    private func buildChildView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let childHolder = UIView()
        let childView = child.toView(context: context, dependencies: dependencies)
        
        childHolder.addSubview(childView)
        childHolder.flex.setup(Flex(grow: 1))
        childView.flex.isEnabled = true
        
        return childHolder
    }
}

public struct SafeArea: Equatable, Decodable {

    // MARK: - Public Properties

    public let top: Bool?
    public let leading: Bool?
    public let bottom: Bool?
    public let trailing: Bool?

    // MARK: - Initialization

    public init(
        top: Bool? = nil,
        leading: Bool? = nil,
        bottom: Bool? = nil,
        trailing: Bool? = nil
    ) {
        self.top = top
        self.leading = leading
        self.bottom = bottom
        self.trailing = trailing
    }
    
    public static var all: SafeArea {
        return SafeArea(top: true, leading: true, bottom: true, trailing: true)
    }
    
    public static var none: SafeArea {
        return SafeArea(top: false, leading: false, bottom: false, trailing: false)
    }
}
