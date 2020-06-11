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

import Foundation
import UIKit
import BeagleSchema

extension PageIndicator: ServerDrivenComponent {

    public func toView(renderer: BeagleRenderer) -> UIView {
        let view = PageIndicatorUIComponent(selectedColor: selectedColor, unselectedColor: unselectedColor)
        return view
    }
}

class PageIndicatorUIComponent: UIView, PageIndicatorUIView {
    
    weak var outputReceiver: PageIndicatorOutput?
    
    typealias Model = PageIndicatorUIViewModel
    
    private let selectedColor: UIColor
    private let unselectedColor: UIColor
    
    var model: Model? { didSet {
        guard let model = model else { return }
        updateView(model: model)
        }}
    
    private lazy var pageControl: UIPageControl = {
        let indicator = UIPageControl()
        indicator.currentPageIndicatorTintColor = selectedColor
        indicator.pageIndicatorTintColor = unselectedColor
        indicator.currentPage = 0
        return indicator
    }()
    
    // MARK: - Init
    
    required init(selectedColor: String? = nil, unselectedColor: String? = nil) {
        if let selected = selectedColor, !selected.isEmpty {
            self.selectedColor = UIColor(hex: selected)
        } else {
            self.selectedColor = UIColor(hex: "#808080")
        }
        
        if let unselected = unselectedColor, !unselected.isEmpty {
            self.unselectedColor = UIColor(hex: unselected)
        } else {
            self.unselectedColor = UIColor(hex: "#d3d3d3")
        }
        
        super.init(frame: .zero)
        
        addSubview(pageControl)
        pageControl.translatesAutoresizingMaskIntoConstraints = false
        pageControl.anchor(
            top: topAnchor, left: leftAnchor, bottom: bottomAnchor, right: rightAnchor
        )
    }
    
    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Update
    
    private func updateView(model: Model) {
        pageControl.currentPage = model.currentPage
        pageControl.numberOfPages = model.numberOfPages
    }
}
