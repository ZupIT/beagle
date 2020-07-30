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

extension TabBarCollectionViewCell {
    struct Model {
        var selectedTextColor: UIColor?
        var unselectedTextColor: UIColor?
        var selectedIconColor: UIColor?
        var unselectedIconColor: UIColor?
    }
}

final class TabBarCollectionViewCell: UICollectionViewCell {
    
    // MARK: - UIComponents
    lazy var stackView: UIStackView = {
        let stack = UIStackView()
        stack.axis = .vertical
        stack.distribution = .fill
        stack.alignment = .center
        stack.spacing = 5
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    lazy var icon: UIImageView = {
        let icon = UIImageView()
        icon.invalidateIntrinsicContentSize()
        icon.contentMode = .scaleAspectFit
        icon.translatesAutoresizingMaskIntoConstraints = false
        return icon
    }()
    
    lazy var title: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    var model: Model?
    
    override var isSelected: Bool {
        didSet {
            guard let model = model else { return }
            switch styleVerification(model: model) {
            case .both:
                title.textColor = isSelected ? model.selectedTextColor : model.unselectedTextColor
                icon.tintColor = isSelected ? model.selectedIconColor : model.unselectedIconColor
            case .icon:
                icon.tintColor = isSelected ? model.selectedIconColor : model.unselectedIconColor
                title.textColor = isSelected ? .black : .gray
            case .text:
                title.textColor = isSelected ? model.selectedTextColor : model.unselectedTextColor
                icon.tintColor = isSelected ? .black : .gray
            default:
                title.textColor = isSelected ? .black : .gray
                icon.tintColor = isSelected ? .black : .gray
            }
        }
    }
    
    // MARK: - Initialization
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        contentView.addSubview(stackView)
        stackView.anchorTo(superview: contentView)
        stackView.addArrangedSubview(icon)
        stackView.addArrangedSubview(title)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
        
    // MARK: - Setup
    
    func setupTab(with tab: TabBarItem) {
        switch contentVerification(tabItem: tab) {
        case let .both(iconName, text):
            icon.heightAnchor.constraint(lessThanOrEqualToConstant: 30).isActive = true
            title.text = text
            icon.image = model?.selectedIconColor == nil ? UIImage(named: iconName): UIImage(named: iconName)?.withRenderingMode(.alwaysTemplate)
            title.font = UIFont.systemFont(ofSize: 13)
            icon.isHidden = false
            title.isHidden = false

        case .icon(let iconName):
            icon.widthAnchor.constraint(lessThanOrEqualToConstant: 35).isActive = true
            icon.image = model?.selectedIconColor == nil ? UIImage(named: iconName): UIImage(named: iconName)?.withRenderingMode(.alwaysTemplate)
            icon.isHidden = false
            title.isHidden = true

        case .title(let text):
            title.isHidden = false
            icon.isHidden = true
            title.sizeToFit()
            title.text = text

        case .none:
            title.isHidden = true
            icon.isHidden = true
        }
    }

    private func contentVerification(tabItem: TabBarItem) -> ContentEnabler {
        switch (tabItem.icon, tabItem.title) {
        case let (icon?, title?):
            return .both(icon: icon, title: title)
        case let (_, title?):
            return .title(title)
        case let (icon?, _):
            return .icon(icon)
        default:
            return .none
        }
    }
    
    private func styleVerification(model: Model) -> StyleEnabler {
        switch (model.selectedIconColor, model.unselectedIconColor, model.selectedTextColor, model.unselectedTextColor) {
        case let (selectedIconColor?, unselectedIconColor?, selectedTextColor?, unselectedTextColor?):
            return .both(iconSelectedColor: selectedIconColor,
                         iconUnselectedColor: unselectedIconColor,
                         textSelectedColor: selectedTextColor,
                         textUnselectedColor: unselectedTextColor)
        case let (selectedIconColor?, unselectedIconColor?, _, _):
            return .icon(selectedIconColor, unselectedIconColor)
        case let (_, _, selectedTextColor?, unselectedTextColor?):
            return .text(selectedTextColor, unselectedTextColor)
        default:
            return .none
        }
    }
    
    private enum ContentEnabler {
        case icon(String)
        case title(String)
        case both(icon: String, title: String)
        case none
    }
    
    private enum StyleEnabler {
        case icon(UIColor, UIColor)
        case text(UIColor, UIColor)
        case both(iconSelectedColor: UIColor, iconUnselectedColor: UIColor, textSelectedColor: UIColor, textUnselectedColor: UIColor)
        case none
    }
}
