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

class DSCollectionViewCell: UICollectionViewCell {
    
    // MARK: - Views
    
    private lazy var avatarImage: UIImageView = {
        let image = UIImageView()
        image.image = #imageLiteral(resourceName: "imageBeagle")
        image.layer.masksToBounds = false
        image.translatesAutoresizingMaskIntoConstraints = false
        return image
    }()
    
    private lazy var nameLabel = buildLabel()
    private lazy var ageLabel = buildLabel()
    
    // MARK: - Initialization
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupCellLayout()
        setupCellLayer()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Properties
    
    static let reuseId = String(describing: DSCollectionViewCell.self)
    
    private let spacing: CGFloat = 20.0
    
    // MARK: - Public functions
    
    func setupCell(for card: DSCollectionDataSource.Card) {
        nameLabel.text = "\(card.name)"
        ageLabel.text = "\(card.age) years old"
    }
    
    // MARK: - Private functions
    
    private func setupCellLayout() {
        addSubview(avatarImage)
        addSubview(nameLabel)
        addSubview(ageLabel)
        
        [
            avatarImage.heightAnchor.constraint(equalToConstant: bounds.width - spacing * 2),
            avatarImage.widthAnchor.constraint(equalToConstant: bounds.width - spacing * 2),
            avatarImage.centerXAnchor.constraint(equalTo: centerXAnchor),
            avatarImage.bottomAnchor.constraint(equalTo: centerYAnchor)
        ].forEach { $0.isActive = true }
        
        [
            nameLabel.topAnchor.constraint(equalTo: avatarImage.bottomAnchor, constant: spacing),
            nameLabel.leadingAnchor.constraint(equalTo: leadingAnchor),
            nameLabel.trailingAnchor.constraint(equalTo: trailingAnchor)
        ].forEach { $0.isActive = true }
        
        [
            ageLabel.topAnchor.constraint(equalTo: nameLabel.bottomAnchor, constant: spacing),
            ageLabel.leadingAnchor.constraint(equalTo: leadingAnchor),
            ageLabel.trailingAnchor.constraint(equalTo: trailingAnchor)
        ].forEach { $0.isActive = true }
    }
    
    private func setupCellLayer() {
        clipsToBounds = true
        backgroundColor = .lightGray
        layer.cornerRadius = 20
        avatarImage.clipsToBounds = true
        avatarImage.layer.cornerRadius = (bounds.width - spacing * 2) / 2
        avatarImage.layer.borderWidth = 2
        avatarImage.layer.borderColor = UIColor.white.cgColor
    }
    
    private func buildLabel() -> UILabel {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.textAlignment = .center
        label.numberOfLines = 0
        return label
    }
}
