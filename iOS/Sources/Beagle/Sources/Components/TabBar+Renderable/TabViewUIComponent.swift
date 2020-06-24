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
    
    // MARK: - Properties
    private var containerWidthConstraint: NSLayoutConstraint?
    var model: Model

    // MARK: - UIComponents
    
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
    
    lazy var contentView: PageViewUIComponent = {
        let pages = model.tabViewItems.map {
            BeagleScreenViewController(
                viewModel: .init(screenType: .declarative($0.child.toScreen()))
            )
        }
        let view = PageViewUIComponent(model: .init(pages: pages), indicatorView: nil)
        view.translatesAutoresizingMaskIntoConstraints = false
        view.pageViewDelegate = self
        return view
    }()
    
    // MARK: - Initialization
    init(
        model: Model
    ) {
        self.model = model
        super.init(frame: .zero)
        setupViews()
    }
    
    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupViews() {
        addSubview(collectionView)
        collectionView.anchor(top: topAnchor, left: leftAnchor, right: rightAnchor)
        collectionView.heightAnchor.constraint(lessThanOrEqualToConstant: 65).isActive = true
        collectionView.addSubview(containerIndicator)
        collectionView.bringSubviewToFront(containerIndicator.indicatorView)
        
        containerIndicator.anchor(bottom: collectionView.bottomAnchor, bottomConstant: -65, heightConstant: 3)
        containerWidthConstraint = NSLayoutConstraint(item: containerIndicator.indicatorView, attribute: .width, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 100)
        containerWidthConstraint?.isActive = true
        
        addSubview(contentView)
        contentView.anchor(top: collectionView.bottomAnchor, left: leftAnchor, bottom: bottomAnchor, right: rightAnchor)
        contentView.setContentHuggingPriority(.defaultLow, for: .vertical)
    }
}

// MARK: - Animation

private extension TabViewUIComponent {
    func animateIndicatorView(from cell: UICollectionViewCell?) {
        guard let cell = cell else { return }
        UIView.animate(withDuration: 0.2,
                       delay: 0,
                       options: .curveLinear,
                       animations: {
            self.containerIndicator.indicatorView.frame.origin.x = cell.frame.origin.x
            self.containerWidthConstraint?.constant = cell.frame.width
            self.layoutIfNeeded()
        }, completion: nil)
    }
}

// MARK: - PageViewUIComponentDelegate

extension TabViewUIComponent: PageViewUIComponentDelegate {
    func changedCurrentPage(_ currentPage: Int) {
        model.tabIndex = currentPage
        collectionView.selectItem(at: IndexPath(item: currentPage, section: 0), animated: true, scrollPosition: .centeredVertically)
        collectionView.scrollToItem(at: IndexPath(item: currentPage, section: 0), at: .centeredHorizontally, animated: true)
        guard let cell = collectionView.cellForItem(at: IndexPath(item: currentPage, section: 0)) else {
            return
        }
        animateIndicatorView(from: cell)
    }
}

// MARK: - UICollection View Delegate and DataSource Extension

extension TabViewUIComponent: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return model.tabViewItems.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let item = model.tabViewItems[indexPath.row]
        guard let cell = collectionView.dequeueReusableCell(
        withReuseIdentifier: TabBarCollectionViewCell.className,
        for: indexPath) as? TabBarCollectionViewCell else { return UICollectionViewCell() }
        cell.model = TabBarCollectionViewCell.Model(selectedTextColor: model.selectedTextColor, unselectedTextColor: model.unselectedTextColor, selectedIconColor: model.selectedIconColor, unselectedIconColor: model.unselectedIconColor)
        
        if indexPath.row == 0 {
            collectionView.selectItem(at: indexPath, animated: true, scrollPosition: .centeredHorizontally)
            cell.isSelected = true
        } else {
            cell.isSelected = false
        }
        cell.setupTab(with: item)
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let item = model.tabViewItems[indexPath.row]
        guard let title = item.title else {
            let size = CGSize(width: frame.width / CGFloat(model.tabViewItems.count), height: 55)
            if indexPath.row == 0 {
                containerWidthConstraint?.constant = size.width
            }
            return size
        }
                
        let newTitle = NSAttributedString(string: title, attributes: [NSAttributedString.Key.font: UIFont.systemFont(ofSize: 30)])
        let stringWidth = newTitle.boundingRect(with: CGSize(width: 300, height: 20), options: [.usesFontLeading, .usesLineFragmentOrigin], context: nil).size.width
        
        let allStringsWidth = model.tabViewItems.compactMap { item in
            return item.title?.boundingRect(with: CGSize(width: 300, height: 20), options: [.usesFontLeading, .usesLineFragmentOrigin], context: nil).size.width
        }
        
        let width = allStringsWidth.reduce(0, +)
        let cellSize = width * 3 < frame.width ? CGSize(width: frame.width / CGFloat(model.tabViewItems.count), height: 55) : CGSize(width: stringWidth, height: 55)
        
        if indexPath.row == 0 {
            containerWidthConstraint?.constant = cellSize.width
        }
        return cellSize
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let index = Int(indexPath.row)
        collectionView.scrollToItem(at: IndexPath(item: index, section: 0), at: .centeredHorizontally, animated: true)
        if index > contentView.model.currentPage {
            contentView.model.currentPage = index
            contentView.pageViewController.setViewControllers(
            [contentView.model.pages[index]],
            direction: .forward,
            animated: true) { _ in
                DispatchQueue.main.async {
                    self.animateIndicatorView(from: collectionView.cellForItem(at: indexPath))
                }
            }
        } else {
            contentView.model.currentPage = index
            contentView.pageViewController.setViewControllers(
                [contentView.model.pages[index]],
                direction: .reverse,
                animated: true) { _ in
                    DispatchQueue.main.async {
                        self.animateIndicatorView(from: collectionView.cellForItem(at: indexPath))
                    }
            }
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsets(top: 0, left: 0, bottom: 10, right: 0)
    }
}
