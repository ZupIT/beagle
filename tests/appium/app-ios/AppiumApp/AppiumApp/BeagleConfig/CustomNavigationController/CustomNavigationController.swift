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
import Beagle

/// Appium App Navigation Controller.
/// When some server driven request fails it shows an error view with a 'Try again' button.
class CustomBeagleNavigationController: BeagleNavigationController {
    
    let errorView = CustomErrorView()
        
    override func serverDrivenStateDidChange(
        to state: ServerDrivenState,
        at screenController: BeagleController
    ) {
        guard case .error(_, let retry) = state else { return }
        errorView.retry = { [weak self] in
            guard let self = self else { return }
            self.errorView.removeFromSuperview()
            retry?()
        }
        errorView.removeFromSuperview()
        view.addSubview(errorView)
        errorView.anchorTo(superview: view)
    }
}

/// This Navigation changes the 'Try again' button title to 'Retry'.
class OtherBeagleNavigationController: CustomBeagleNavigationController {
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    init() {
        super.init(nibName: nil, bundle: nil)
        errorView.button.setTitle("Retry", for: .normal)
    }
    
}

class CustomErrorView: UIView {
    
    var retry: (() -> Void)?
    
    lazy var label: UILabel = {
        let label = UILabel()
        label.text = "Oops! Something went wrong!"
        return label
    }()
    
    lazy var button: UIButton = {
        let button = UIButton(type: .system)
        button.setTitle("Try again", for: .normal)
        button.addTarget(self, action: #selector(retryAction), for: .touchUpInside)
        return button
    }()
    
    private lazy var stack: UIStackView = {
        label.translatesAutoresizingMaskIntoConstraints = false
        button.translatesAutoresizingMaskIntoConstraints = false
        let stack = UIStackView(arrangedSubviews: [label, button])
        stack.alignment = .center
        stack.axis = .vertical
        stack.distribution = .equalSpacing
        stack.spacing = 20
        return stack
    }()
    
    private lazy var close: UIButton = {
        let button = UIButton(type: .system)
        button.setTitle("Close", for: .normal)
        button.addTarget(self, action: #selector(closeAction), for: .touchUpInside)
        return button
    }()
    
    init() {
        super.init(frame: .zero)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    private func setupView() {
        if #available(iOS 13.0, *) {
            backgroundColor = .systemBackground
        } else {
            backgroundColor = .white
        }
        addSubview(stack)
        stack.anchorCenterSuperview()
        
        addSubview(close)
        let (top, right): (NSLayoutYAxisAnchor, NSLayoutXAxisAnchor)
        if #available(iOS 11.0, *) {
            (top, right) = (safeAreaLayoutGuide.topAnchor, safeAreaLayoutGuide.rightAnchor)
        } else {
            (top, right) = (topAnchor, rightAnchor)
        }
        close.anchor(top: top, right: right, topConstant: 20, rightConstant: 20)
    }
    
    @objc private func retryAction() {
        retry?()
    }
    
    @objc private func closeAction() {
        removeFromSuperview()
    }
}
