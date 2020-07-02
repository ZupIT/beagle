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

class DSCollectionUIComponent: UIView {
    
    //MARK: Views
    
    private lazy var collectionView: UICollectionView = {
        let collection = UICollectionView(frame: .zero, collectionViewLayout: buildCollectionViewFlowLayout())
        collection.translatesAutoresizingMaskIntoConstraints = false
        collection.delegate = self
        collection.dataSource = self
        collection.backgroundColor = .clear
        return collection
    }()
    
    //MARK: Properties
    
    private let collectionSpacing: CGFloat = 20
    private let dataSource: DSCollectionDataSource
    
    //MARK: Initialization
    
    init(dataSource: DSCollectionDataSource) {
        self.dataSource = dataSource
        super.init(frame: .zero)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    //MARK: Private functions
    
    ///This needs to be overridden by all custom component views
    override func sizeThatFits(_ size: CGSize) -> CGSize {
        return size
    }
    
    private func setupView() {
        registerCollectionViewCells()
        setupViewLayout()
        collectionView.reloadData()
    }
    
    private func registerCollectionViewCells() {
        self.collectionView.register(DSCollectionViewCell.self, forCellWithReuseIdentifier: DSCollectionViewCell.reuseId)
    }
    
    private func setupViewLayout() {
        addSubview(collectionView)
        
        [collectionView.topAnchor.constraint(equalTo: topAnchor),
         collectionView.leadingAnchor.constraint(equalTo: leadingAnchor),
         collectionView.bottomAnchor.constraint(equalTo: bottomAnchor),
         collectionView.trailingAnchor.constraint(equalTo: trailingAnchor)
        ].forEach { $0.isActive = true }
    }
    
    private func buildCollectionViewFlowLayout() -> UICollectionViewFlowLayout{
        let flowLayout = UICollectionViewFlowLayout()
        flowLayout.scrollDirection = .horizontal
        flowLayout.minimumInteritemSpacing = collectionSpacing
        return flowLayout
    }
}

//MARK: UICollectionViewDataSource, UICollectionViewDelegateFlowLayout

extension DSCollectionUIComponent: UICollectionViewDataSource, UICollectionViewDelegateFlowLayout  {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return dataSource.cards.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: DSCollectionViewCell.reuseId, for: indexPath)
        if let dsCollectionViewCell = cell as? DSCollectionViewCell {
            dsCollectionViewCell.setupCell(for: dataSource.cards[indexPath.row])
            return dsCollectionViewCell
        }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: bounds.height / 2, height: bounds.height)
    }
}
