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

private struct CellsContextManager {
    
    private var orphanCells = [Int: ListViewCell]()
    
    private var itemContexts = [Int: [String: DynamicObject]]()
    
    mutating func track(orphanCell cell: ListViewCell) {
        if let itemHash = cell.itemHash {
            orphanCells[itemHash] = cell
        }
    }
    
    mutating func reuse(cell: ListViewCell) {
        guard let itemHash = cell.itemHash else { return }
        itemContexts[itemHash] = cell.viewContexts.reduce(into: [:]) { result, entry in
            let (view, contexts) = entry
            for context in contexts {
                let value = view.getContextValue(context.id)
                result?[context.id] = value
            }
        }
        orphanCells.removeValue(forKey: itemHash)
    }
    
    mutating func contexts(for itemHash: Int) -> [String: DynamicObject]? {
        if let orphan = orphanCells[itemHash] {
            reuse(cell: orphan)
        }
        return itemContexts[itemHash]
    }
    
}

final class ListViewUIComponent: UIView {
    
    // MARK: - Properties
    
    private var cellsReadyToDisplay = [Int: ListViewCell]()
    private var cellsContextManager = CellsContextManager()
    private var itemsSize = [Int: CGSize]()
    
    let model: Model
    
    var items: [DynamicObject]? {
        didSet {
            listController.collectionView.reloadData()
            listController.collectionView.collectionViewLayout.invalidateLayout()
            onScrollEndExecuted = false
            executeOnScrollEndIfNeededAfterLayout()
        }
    }
    
    let listController: ListViewController
    
    private(set) var onScrollEndExecuted = false
    
    lazy var renderer = listController.dependencies.renderer(listController)
    
    // MARK: - Initialization
    
    init(model: Model, renderer: BeagleRenderer) {
        self.model = model
        self.listController = ListViewController(renderer: renderer)
        super.init(frame: .zero)
        setupViews()
    }
    
    @available (*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupViews() {
        let layout = listController.collectionViewFlowLayout
        layout.scrollDirection = model.direction.scrollDirection
        
        let collection = listController.collectionView
        collection.register(ListViewCell.self, forCellWithReuseIdentifier: "ListViewCell")
        collection.dataSource = self
        collection.delegate = self
        
        let parentController = listController.renderer.controller
        parentController.addChild(listController)
        addSubview(listController.view)
        listController.view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        listController.view.frame = bounds
        listController.didMove(toParent: parentController)
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        executeOnScrollEndIfNeededAfterLayout()
    }
    
    // MARK: - Sizing
    
    /// Asks the view to calculate and return the size that best fits the specified size.
    ///
    /// This method will calculate each item size until the informed `size` is filled,
    /// the remaining items size will be estimated as the average of the others.
    ///
    /// - Parameter size: The size for which the view should calculate its best-fitting size.
    /// - Returns: A new size that fits the receiver’s subviews.
    override func sizeThatFits(_ size: CGSize) -> CGSize {
        let collection = listController.collectionView
        let numberOfItems = collectionView(collection, numberOfItemsInSection: 0)
        guard numberOfItems > 0 else { return .zero }
        
        let directionValue: WritableKeyPath<CGSize, CGFloat>
        let otherValue: WritableKeyPath<CGSize, CGFloat>
        switch model.direction {
        case .vertical:
            (directionValue, otherValue) = (\.height, \.width)
        case .horizontal:
            (directionValue, otherValue) = (\.width, \.height)
        }
        
        var cellSizeAvailable = CGSize(width: CGFloat.nan, height: CGFloat.nan)
        if size[keyPath: otherValue] != CGFloat.greatestFiniteMagnitude {
            cellSizeAvailable[keyPath: otherValue] = size[keyPath: otherValue]
        }
        
        var item = 0
        var listSize = CGSize.zero
        while item < numberOfItems && listSize[keyPath: directionValue] < size[keyPath: directionValue] {
            let indexPath = IndexPath(item: item, section: 0)
            let cell = collection.cellForItem(at: indexPath) ?? collectionView(collection, cellForItemAt: indexPath)
            if let listViewCell = cell as? ListViewCell {
                let cellSize = listViewCell.templateSizeThatFits(cellSizeAvailable)
                listSize[keyPath: directionValue] += cellSize[keyPath: directionValue]
                let otherMeasure = cellSize[keyPath: otherValue]
                if listSize[keyPath: otherValue] < otherMeasure {
                    listSize[keyPath: otherValue] = otherMeasure
                }
            }
            item += 1
        }
        
        if item > 0 && item < numberOfItems {
            let average = listSize[keyPath: directionValue] / CGFloat(item)
            listSize[keyPath: directionValue] += CGFloat(numberOfItems - item) * average
        }
        return listSize
    }
    
    func saveSize(_ size: CGSize, forItem itemHash: Int) {
        itemsSize[itemHash] = size
    }
    
    func invalidateSize(cell: ListViewCell) {
        if listController.collectionView.indexPath(for: cell) != nil {
            listController.collectionViewFlowLayout.invalidateLayout()
        }
    }
    
    // MARK: - Handle Scroll
    
    private func executeOnScrollEndIfNeededAfterLayout() {
        let displayLink = CADisplayLink(
            target: self,
            selector: #selector(executeOnScrollEndIfNeeded(displayLink:))
        )
        displayLink.preferredFramesPerSecond = 60
        displayLink.add(to: .main, forMode: .default)
    }
    
    @objc private func executeOnScrollEndIfNeeded(displayLink: CADisplayLink) {
        displayLink.invalidate()
        executeOnScrollEndIfNeeded()
    }
    
    private func executeOnScrollEndIfNeeded() {
        guard !onScrollEndExecuted else { return }
        
        if items?.count == 0 || didReachScrollThreshol {
            onScrollEndExecuted = true
            renderer.controller.execute(actions: model.onScrollEnd, origin: self)
        }
    }
    
    private var didReachScrollThreshol: Bool {
        let collection = listController.collectionView
        let contentSize = collection.contentSize[keyPath: model.direction.sizeKeyPath]
        let contentOffset = collection.contentOffset[keyPath: model.direction.pointKeyPath]
        let offset = contentOffset + frame.size[keyPath: model.direction.sizeKeyPath]
        
        return (contentSize > 0) && (offset / contentSize * 100 >= model.scrollEndThreshold)
    }
}

// MARK: - Model

extension ListViewUIComponent {
    struct Model {
        var key: Path?
        var direction: ListView.Direction
        var template: RawComponent
        var iteratorName: String
        var onScrollEnd: [RawAction]?
        var scrollEndThreshold: CGFloat
    }
}

// MARK: - CollectionView Data Source

extension ListViewUIComponent: UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return items?.count ?? 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let item = items?[indexPath.item] else {
            return UICollectionViewCell()
        }
        let itemKey = keyFor(item)
        let hash = hashFor(item: item, withKey: itemKey)
        
        if let cell = cellsReadyToDisplay[hash] {
            return cell
        }
        
        let collectionCell = dequeueCellWithoutPendingActions(collectionView, indexPath: indexPath, itemHash: hash)
        guard let cell = collectionCell as? ListViewCell else {
            return collectionCell
        }
        cellsContextManager.reuse(cell: cell)
        listController.delegate = cell
        
        let key = itemKey ?? String(indexPath.item)
        let contexts = cellsContextManager.contexts(for: hash)
        
        cell.configure(hash: hash, key: key, item: item, contexts: contexts, listView: self)
        cellsReadyToDisplay[hash] = cell
        return cell
    }
    
    private func dequeueCellWithoutPendingActions(_ collection: UICollectionView, indexPath: IndexPath, itemHash: Int) -> UICollectionViewCell {
        let dequeuedCell = collection.dequeueReusableCell(withReuseIdentifier: "ListViewCell", for: indexPath)
        if let cell = dequeuedCell as? ListViewCell, cell.hasPendingActions {
            if cell.itemHash == itemHash {
                return cell
            } else if let cellItemHash = cell.itemHash {
                cellsReadyToDisplay[cellItemHash] = cell
            }
            return dequeueCellWithoutPendingActions(collection, indexPath: indexPath, itemHash: itemHash)
        }
        return dequeuedCell
    }
    
    private func keyFor(_ item: DynamicObject) -> String? {
        if let path = model.key {
            switch item[path] {
            case .int(let value):
                return String(value)
            case .double(let value):
                return String(value)
            case .string(let value):
                return value
            case .empty, .bool, .array, .dictionary, .expression:
                break
            }
        }
        return nil
    }
    
    private func hashFor(item: DynamicObject, withKey key: String?) -> Int {
        return key?.hashValue ?? item.hashValue
    }
}

// MARK: - CollectionView Delegate

extension ListViewUIComponent: UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        var size = collectionView.frame.size
        guard let items = items, indexPath.item < items.count else {
            return size
        }
        
        let item = items[indexPath.item]
        let itemKey = keyFor(item)
        let itemHash = hashFor(item: item, withKey: itemKey)
        
        let keyPath = model.direction.sizeKeyPath
        if let calculatedSize = itemsSize[itemHash] {
            size[keyPath: keyPath] = calculatedSize[keyPath: keyPath]
        } else if #available(iOS 12.0, *) {
            // Intentionally empty.
            // Bellow iOS 12, if the size is 0, the size changes made at
            // cell method `preferredLayoutAttributesFitting` won't apply.
            // To fix it the size is set to 1.
        } else if size[keyPath: keyPath] == 0 {
            size[keyPath: keyPath] = 1
        }
        return size
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if let itemHash = (cell as? ListViewCell)?.itemHash {
            cellsReadyToDisplay.removeValue(forKey: itemHash)
        }
    }
        
    func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if let cell = cell as? ListViewCell {
            cellsContextManager.track(orphanCell: cell)
        }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        executeOnScrollEndIfNeeded()
    }
}
