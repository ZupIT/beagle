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
    
final class TabBarUIComponent: UIView {
    
    // MARK: - Model
    
    struct Model {
        var tabIndex: Int
        var tabBarItems: [TabBarItem]
        var selectedTextColor: UIColor?
        var unselectedTextColor: UIColor?
        var selectedIconColor: UIColor?
        var unselectedIconColor: UIColor?
    }

    // MARK: - Properties
    
    private var shouldScrollToCurrentTab = true
    private var shouldAnimateOnCellDisplay = false
    private var containerWidthConstraint: NSLayoutConstraint?
    private let tabBarPreferedHeight: CGFloat = 65
    private let collectionViewCellHeight: CGFloat = 55
    private let tabItemMinimumHorizontalMargin: CGFloat = 40
    private let tabItemIconMinimunWidth: CGFloat = 75
    
    var model: Model
    
    var onTabSelection: ((_ tab: Int) -> Void)?

    // MARK: - UI
    
    lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        layout.minimumInteritemSpacing = 0
        layout.minimumLineSpacing = 0
        let collection = UICollectionView(
            frame: CGRect(),
            collectionViewLayout: layout
        )
        collection.backgroundColor = .clear
        collection.register(TabBarCollectionViewCell.self, forCellWithReuseIdentifier: TabBarCollectionViewCell.className)
        collection.translatesAutoresizingMaskIntoConstraints = false
        collection.showsHorizontalScrollIndicator = false
        collection.dataSource = self
        collection.delegate = self
        return collection
    }()
    
    lazy var containerIndicator: ContainerIndicatorView = {
        let view = ContainerIndicatorView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Initialization
    
    init(
        model: Model
    ) {
        self.model = model
        super.init(frame: .zero)
        setupLayout()
        scrollTo(page: 0)
    }
    
    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Layout
    
    override var frame: CGRect {
        didSet {
            if collectionView.visibleCells.count != 0, shouldScrollToCurrentTab {
                scrollTo(page: model.tabIndex)
                shouldScrollToCurrentTab.toggle()
            }
        }
    }
    
    override func sizeThatFits(_ size: CGSize) -> CGSize {
        .init(width: size.width, height: tabBarPreferedHeight)
    }
    
    private lazy var tabBarItensFreeHorizontalSpace: CGFloat = {
        let tabBarItems = model.tabBarItems
        let tabBarItemsAvailableSpace = frame.width
        let tabItensRequiredSpace = tabBarItems.reduce(0) { result, item -> CGFloat in
            if let title = item.title {
                return result + getCellMinimumWidth(for: title)
            }
            return result + tabItemIconMinimunWidth
        }
        return tabItensRequiredSpace <= tabBarItemsAvailableSpace ? (tabBarItemsAvailableSpace - tabItensRequiredSpace) / CGFloat(tabBarItems.count) : 0
    }()
    
    private func getCellMinimumWidth(for text: String) -> CGFloat {
        let label = UILabel()
        label.numberOfLines = 1
        label.text = text
        return label.intrinsicContentSize.width + tabItemMinimumHorizontalMargin
    }
    
    private func setupLayout() {
        addSubview(collectionView)
        collectionView.anchor(top: topAnchor, left: leftAnchor, right: rightAnchor)
        collectionView.heightAnchor.constraint(lessThanOrEqualToConstant: tabBarPreferedHeight).isActive = true
        collectionView.addSubview(containerIndicator)
        collectionView.bringSubviewToFront(containerIndicator.indicatorView)
        
        containerIndicator.anchor(bottom: collectionView.bottomAnchor, bottomConstant: -tabBarPreferedHeight, heightConstant: 3)
        containerWidthConstraint = NSLayoutConstraint(item: containerIndicator.indicatorView, attribute: .width, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 100)
        containerWidthConstraint?.isActive = true
    }
}

// MARK: - Animation

private extension TabBarUIComponent {
    private func moveIndicatorView(to cell: UICollectionViewCell?) {
        guard let cell = cell else { return }
        UIView.animate(
            withDuration: 0.2,
            delay: 0,
            options: .curveLinear,
            animations: {
                self.containerIndicator.indicatorView.frame.origin.x = cell.frame.origin.x
                self.containerWidthConstraint?.constant = cell.frame.width
                self.layoutIfNeeded()
            }
        )
    }
}

// MARK: - Page Changing

extension TabBarUIComponent {
    func scrollTo(page: Int) {
        model.tabIndex = page
        let indexPath = IndexPath(item: page, section: 0)
        collectionView.selectItem(at: indexPath, animated: true, scrollPosition: .centeredVertically)
        collectionView.scrollToItem(at: indexPath, at: .centeredHorizontally, animated: true)
        guard let cell = collectionView.cellForItem(at: indexPath) else {
            shouldAnimateOnCellDisplay = true
            return
        }
        moveIndicatorView(to: cell)
    }
}

// MARK: - UICollection View Delegate and DataSource Extension

extension TabBarUIComponent: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if indexPath.row == model.tabIndex, shouldAnimateOnCellDisplay {
            moveIndicatorView(to: cell)
            shouldAnimateOnCellDisplay.toggle()
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return model.tabBarItems.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let item = model.tabBarItems[indexPath.row]
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: TabBarCollectionViewCell.className, for: indexPath)
        guard let tabBarCell = cell as? TabBarCollectionViewCell else { return cell }
        
        tabBarCell.model = TabBarCollectionViewCell.Model(
            selectedTextColor: model.selectedTextColor,
            unselectedTextColor: model.unselectedTextColor,
            selectedIconColor: model.selectedIconColor,
            unselectedIconColor: model.unselectedIconColor
        )
        tabBarCell.setupTab(with: item)
        
        return tabBarCell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return getCellSize(forContent: model.tabBarItems[indexPath.row].itemContentType)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let index = Int(indexPath.row)
        scrollTo(page: index)
        onTabSelection?(index)
    }
    
    private func getCellSize(forContent content: TabBarItem.ItemContentType) -> CGSize {
        switch content {
        case .both(_, let title):
            let minimumCellWidth = getCellMinimumWidth(for: title)
            let width = minimumCellWidth <= tabItemIconMinimunWidth ? tabItemIconMinimunWidth : minimumCellWidth
            return getCellSize(forWidth: width)
        case .title(let title):
            return getCellSize(forWidth: getCellMinimumWidth(for: title))
        default:
            return getCellSize(forWidth: tabItemIconMinimunWidth)
        }
    }
    
    private func getCellSize(forWidth width: CGFloat) -> CGSize {
        CGSize(width: width + tabBarItensFreeHorizontalSpace, height: collectionViewCellHeight)
    }
}
