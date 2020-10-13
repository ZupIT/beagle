//
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

struct TabBarTheme {
    var selectedTextColor: UIColor?
    var unselectedTextColor: UIColor?
    var selectedIconColor: UIColor?
    var unselectedIconColor: UIColor?
}
    
final class TabBarUIComponent: UIScrollView {
    
    // MARK: - Model
    
    struct Model {
        var tabIndex: Int?
        var tabBarItems: [TabBarItem]
        var styleId: String?
        var renderer: BeagleRenderer
    }
    
    // MARK: - Properties
    private var shouldCreateTabItemsView = true
    private let tabItemMinimumHorizontalMargin: CGFloat = 40
    private let tabItemIconMinimunWidth: CGFloat = 75
    
    var model: Model
    var tabItemViews = [Int: TabBarItemUIComponent]()
    var onTabSelection: ((_ tab: Int) -> Void)?
    
    // MARK: - UI
    
    private lazy var contentView: UIView = {
        let view = UIView()
        return view
    }()
    
    lazy var indicatorView: UIView = {
        let view = UIView()
        view.backgroundColor = .black
        return view
    }()
        
    // MARK: - Initialization
    
    init(
        model: Model
    ) {
        self.model = model
        super.init(frame: .zero)
        setupScrollView()
        setupContentView()
    }
    
    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        if let contentView = subviews.first {
            contentSize = contentView.frame.size
            resetTabItemsStyle()
            
            // Creates tabItems only after view it's already in superview hierarchy
            if superview != nil && shouldCreateTabItemsView {
                setupTabBarItems()
                setupIndicatorViewStyle(for: tabItemViews[0])
                style.applyLayout()
                scrollTo(page: model.tabIndex ?? 0)
            }
        }
    }

    // MARK: - Layout
    
    private func resetTabItemsStyle() {
        tabItemViews.forEach { key, item in
            let size = getContentSize(forItem: model.tabBarItems[key].itemContentType)
            setupTabBarItemStyle(for: item, with: size.width)
        }
        guard let selectedTabItem = tabItemViews[model.tabIndex ?? 0] else { return }
        setupIndicatorViewStyle(for: selectedTabItem)
        model.renderer.controller.setNeedsLayout(component: self)
    }
    
    private func setupScrollView() {
        showsHorizontalScrollIndicator = false
        showsVerticalScrollIndicator = false
        style.setup(Style(
            size: Size().height(65), flex: Flex().flexDirection(.row).shrink(0))
        )
    }
    
    private func setupContentView() {
        contentView.style.setup(Style(
            flex: Flex(flexDirection: .row, grow: 0, shrink: 0))
        )
        contentView.addSubview(indicatorView)
        addSubview(contentView)
    }
    
    func setupTabBarItems() {
        var index = 0
        shouldCreateTabItemsView = false
        model.tabBarItems.forEach { item in
            let size = getContentSize(forItem: item.itemContentType)
            let itemView = createTabBarItemsView(with: item, index: index)
            setupTabBarItemStyle(for: itemView, with: size.width)
            tabItemViews[index] = itemView
            index += 1
            contentView.addSubview(itemView)
        }
        
        if let styleId = model.styleId {
            beagle.applyStyle(for: self as UIView, styleId: styleId, with: model.renderer.controller)
        }
    }
    
    private func createTabBarItemsView(with item: TabBarItem, index: Int) -> TabBarItemUIComponent {
        let itemView = TabBarItemUIComponent(index: index, renderer: model.renderer)
        itemView.setupTab(with: item)
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(didSelectTabItem(sender:)))
        itemView.addGestureRecognizer(tap)
        itemView.isUserInteractionEnabled = true
        return itemView
    }
    
    @objc func didSelectTabItem(sender: UITapGestureRecognizer) {
        guard let tabItem = sender.view as? TabBarItemUIComponent, let index = tabItem.index else { return }
        model.tabIndex = index
        setupTabBarItemsTheme(for: index)
        onTabSelection?(index)
    }
    
    private func setupTabBarItemsTheme(for currentIndex: Int) {
        tabItemViews.forEach { _, item in
            item.isSelected = currentIndex == item.index
        }
    }
}

// MARK: - Style
private extension TabBarUIComponent {
    func setupTabBarItemStyle(for item: TabBarItemUIComponent, with width: CGFloat) {
        item.style.setup(
            Style(
                size: Size().height(62).width(.init(value: Double(width), type: .real)),
                position: EdgeValue().left(5),
                flex: Flex()
                    .alignItems(.center)
                    .justifyContent(.spaceEvenly))
        )
    }
    
    func setupIndicatorViewStyle(for selectedItem: TabBarItemUIComponent?) {
        guard let selectedItem = selectedItem else { return }
        indicatorView.style.setup(
            Style(
                size: Size().height(3).width(.init(value: Double(selectedItem.bounds.width), type: .real)),
                position: EdgeValue(left: UnitValue(value: Double(selectedItem.frame.origin.x), type: .real)),
                positionType: .absolute,
                flex: Flex().alignSelf(.flexEnd)
            )
        )
    }

}

// MARK: - TabBarItem Size

private extension TabBarUIComponent {
    
    func getTabBarItensFreeHorizontalSpace() -> CGFloat {
        let tabBarItems = model.tabBarItems
        let tabBarItemsAvailableSpace = frame.width
        let tabItensRequiredSpace = tabBarItems.reduce(0) { result, item -> CGFloat in
            if let title = item.title {
                return result + getItemMinimumWidth(for: title)
            }
            return result + tabItemIconMinimunWidth
        }
        return tabItensRequiredSpace <= tabBarItemsAvailableSpace ? (tabBarItemsAvailableSpace - tabItensRequiredSpace) / CGFloat(tabBarItems.count) : 0
    }
    
    func getItemMinimumWidth(for text: String) -> CGFloat {
        let label = UILabel()
        label.numberOfLines = 1
        label.text = text
        return label.intrinsicContentSize.width + tabItemMinimumHorizontalMargin
    }

    func getContentSize(forItem item: TabBarItem.ItemContentType) -> CGSize {
        switch item {
        case .both(_, let title):
            let minimumCellWidth = getItemMinimumWidth(for: title)
            let width = minimumCellWidth <= tabItemIconMinimunWidth ? tabItemIconMinimunWidth : minimumCellWidth
            return getContentSize(forWidth: width)
        case .title(let title):
            return getContentSize(forWidth: getItemMinimumWidth(for: title))
        default:
            return getContentSize(forWidth: tabItemIconMinimunWidth)
        }
    }
    
    func getContentSize(forWidth width: CGFloat) -> CGSize {
        CGSize(width: width + getTabBarItensFreeHorizontalSpace(), height: 62)
    }
}

// MARK: - Animation

private extension TabBarUIComponent {
    private func moveIndicatorView(to tabItem: TabBarItemUIComponent) {
        UIView.animate(
            withDuration: 0.2,
            delay: 0,
            options: .curveLinear,
            animations: {
                self.setupIndicatorViewStyle(for: tabItem)
                
                // TODO: setNeedLayout should call layoutIfNeeded
                self.model.renderer.controller.setNeedsLayout(component: self)
                self.model.renderer.controller.view.layoutIfNeeded()
            }
        )
    }
}

// MARK: - Page Changing

extension TabBarUIComponent {
    func scrollTo(page: Int) {
        model.tabIndex = page
        guard let view = tabItemViews[page] else { return }
        
        let visibleRect = CGRect(
            origin: CGPoint(x: max(0, view.center.x - (frame.width / 2)), y: 0),
            size: bounds.size
        )
        scrollRectToVisible(visibleRect, animated: true)
        setupTabBarItemsTheme(for: page)
        moveIndicatorView(to: view)
    }
}
