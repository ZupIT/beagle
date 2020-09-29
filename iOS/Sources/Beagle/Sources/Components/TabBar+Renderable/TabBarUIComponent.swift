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
        var tabBarItems: [TabBarItem]
        var renderer: BeagleRenderer
    }
    
    // MARK: - Properties
    
    private var shouldScrollToCurrentTab = true
    private var shouldAnimateOnCellDisplay = false
    private let tabItemMinimumHorizontalMargin: CGFloat = 40
    private let tabItemIconMinimunWidth: CGFloat = 75
    
    var model: Model
    var contentViewSize = CGRect(x: 0, y: 0, width: 0, height: 0)
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
        setupTabBarItems()
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
        }
    }
    
    // MARK: - Layout

    private func setupContentView() {
        contentView.style.setup(
            Style(flex: Flex(flexDirection: .row, grow: 0, shrink: 0))
        )
        contentView.addSubview(indicatorView)
        addSubview(contentView)
        style.applyLayout()
    }
    
    private func setupTabBarItems() {
        var index = 0
        model.tabBarItems.forEach { item in
            let size = getContentSize(forItem: item.itemContentType)
            let itemView = setupTabBarItemView(with: item, index: index)
            itemView.style.setup(
                Style(
                    size: Size().height(62).width(.init(value: Double(size.width), type: .real)),
                    position: EdgeValue().left(5),
                    flex: Flex()
                        .alignItems(.center)
                        .justifyContent(.spaceEvenly))
            )
            tabItemViews[index] = itemView
            index += 1
            contentView.addSubview(itemView)
        }
    }
    
    private func setupTabBarItemView(with item: TabBarItem, index: Int) -> TabBarItemUIComponent {
        let itemView = TabBarItemUIComponent(index: index, renderer: model.renderer)
        itemView.setupTab(with: item)
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(selectTabItem(sender:)))
        itemView.addGestureRecognizer(tap)
        itemView.isUserInteractionEnabled = true
        return itemView
    }
    
    @objc func selectTabItem(sender: UITapGestureRecognizer) {
        guard let tabItem = sender.view as? TabBarItemUIComponent, let index = tabItem.index else { return }
        
        setupTabBarItemsStyle(with: index)
        onTabSelection?(index)
        moveIndicatorView(to: tabItem)
    }
    
    private func setupTabBarItemsStyle(with currentIndex: Int) {
        tabItemViews.forEach { _, item in
            item.isSelected = currentIndex == item.index ? true : false
        }
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
        CGSize(width: width + getTabBarItensFreeHorizontalSpace(), height: 65)
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
                self.indicatorView.frame.origin.x = tabItem.frame.origin.x
                self.indicatorView.style.setup(
                    Style(
                        size: Size().height(3).width(.init(value: Double(tabItem.bounds.width), type: .real)),
                        position: EdgeValue().bottom(0),
                        positionType: .absolute)
                )
                self.indicatorView.style.applyLayout()
            }
        )
    }
}

// MARK: - Page Changing

extension TabBarUIComponent {
    func scrollTo(page: Int) {
        guard let view = tabItemViews[page] else { return }
        let newRect = view.convert(view.bounds, to: self)
        scrollRectToVisible(newRect, animated: true)
        setupTabBarItemsStyle(with: page)
        moveIndicatorView(to: view)
    }
}
