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

/// Use this Wrapper when your component is based on AutoLayout and does not provide the requirements for working with Yoga
public class AutoLayoutWrapper: UIView {
    public let view: UIView
    
    public init(view: UIView) {
        self.view = view
        super.init(frame: .zero)
        clipsToBounds = true
        
        addSubview(view)
        
        let trailing = view.trailingAnchor.constraint(equalTo: trailingAnchor)
        trailing.priority = .init(999)
        trailing.isActive = true
        view.topAnchor.constraint(equalTo: topAnchor).isActive = true
        let botton = view.bottomAnchor.constraint(equalTo: bottomAnchor)
        botton.priority = .init(rawValue: 999)
        botton.isActive = true
        view.leadingAnchor.constraint(equalTo: leadingAnchor).isActive = true
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    public override func sizeThatFits(_ size: CGSize) -> CGSize {
        systemLayoutSizeFitting(size)
    }
    
}
