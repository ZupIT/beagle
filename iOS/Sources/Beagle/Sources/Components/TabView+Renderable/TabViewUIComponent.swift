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
import BeagleSchema

extension TabViewUIComponent {
    struct Model: AutoInitiable {
        var tabIndex: Int
        var tabViewItems: [TabItem]
        var selectedTextColor: UIColor?
        var unselectedTextColor: UIColor?
        var selectedIconColor: UIColor?
        var unselectedIconColor: UIColor?

// sourcery:inline:auto:TabViewUIComponent.Model.Init
    init(
        tabIndex: Int,
        tabViewItems: [TabItem],
        selectedTextColor: UIColor? = nil,
        unselectedTextColor: UIColor? = nil,
        selectedIconColor: UIColor? = nil,
        unselectedIconColor: UIColor? = nil
    ) {
        self.tabIndex = tabIndex
        self.tabViewItems = tabViewItems
        self.selectedTextColor = selectedTextColor
        self.unselectedTextColor = unselectedTextColor
        self.selectedIconColor = selectedIconColor
        self.unselectedIconColor = unselectedIconColor
    }
// sourcery:end
    }
}

final class TabViewUIComponent: UIView {

    // MARK: - UIComponents
    
    var tabBar: TabBarUIComponent
    
    static func contentView(items: [TabItem], controller: BeagleController) -> PageViewUIComponent {
        let pages = items.map { BeagleScreenViewController($0.child) }
        let view = PageViewUIComponent(
            model: .init(pages: pages),
            indicatorView: nil,
            controller: controller
        )
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }
    
    var contentView: PageViewUIComponent
    
    // MARK: - Initialization
    
    init(
        model: Model,
        controller: BeagleController
    ) {
        self.tabBar = TabBarUIComponent(
            model: .init(
                tabIndex: model.tabIndex,
                tabBarItems: model.tabViewItems.map { TabBarItem(icon: $0.icon, title: $0.title) },
                selectedTextColor: model.selectedTextColor,
                unselectedTextColor: model.unselectedTextColor,
                selectedIconColor: model.selectedIconColor,
                unselectedIconColor: model.unselectedIconColor
            )
        )
        self.contentView = Self.contentView(items: model.tabViewItems, controller: controller)
        super.init(frame: .zero)
        setupViews()
        setupTabBarSelectionEvent()
    }
    
    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupTabBarSelectionEvent() {
        contentView.swipeToPage(at: 0)
        tabBar.onTabSelection = { [weak self] index in
            guard let self = self else { return }
            self.contentView.swipeToPage(at: index)
        }
    }
    
    private func setupViews() {
        contentView.pageViewDelegate = self
        
        addSubview(tabBar)
        tabBar.anchor(top: topAnchor, left: leftAnchor, right: rightAnchor)
        tabBar.heightAnchor.constraint(lessThanOrEqualToConstant: 65).isActive = true
        
        addSubview(contentView)
        contentView.anchor(top: tabBar.bottomAnchor, left: leftAnchor, bottom: bottomAnchor, right: rightAnchor)
        contentView.setContentHuggingPriority(.defaultLow, for: .vertical)
    }
}

// MARK: - PageViewUIComponentDelegate

extension TabViewUIComponent: PageViewUIComponentDelegate {
    func changedCurrentPage(_ currentPage: Int) {
        tabBar.scrollTo(page: currentPage)
    }
}
