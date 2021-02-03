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

class ErrorView: UIVisualEffectView {
    
    private var retry: [(() -> Void)?] = []
    
    private lazy var stackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [
            titleLabel,
            subtitleLabel,
            retryButton,
            cancelButton
        ])
        stack.axis = .vertical
        stack.alignment = .center
        stack.distribution = .equalCentering
        stack.spacing = 20
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .center
        label.numberOfLines = 0
        label.textColor = .darkText
        label.font = .preferredFont(forTextStyle: .title1)
        label.text = "Error"
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var subtitleLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .center
        label.numberOfLines = 0
        label.textColor = .darkGray
        label.font = .preferredFont(forTextStyle: .body)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var retryButton: UIButton = {
        let button = UIButton()
        button.setTitleColor(.darkGray, for: .normal)
        button.setTitle("Retry", for: .normal)
        button.titleLabel?.font = .preferredFont(forTextStyle: .callout)
        button.addTarget(self, action: #selector(retryAction), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var cancelButton: UIButton = {
        let button = UIButton()
        button.setTitleColor(.darkGray, for: .normal)
        button.setTitle("Cancel", for: .normal)
        button.titleLabel?.font = .preferredFont(forTextStyle: .callout)
        button.addTarget(self, action: #selector(cancelAction), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    init(message: String?, retry: @escaping () -> Void) {
        self.retry.append(retry)
        super.init(effect: UIBlurEffect(style: .light))
        subtitleLabel.text = message
        setupView()
    }
    
    required init?(coder: NSCoder) {
        self.retry.removeAll()
        super.init(coder: coder)
        setupView()
    }
    
    func present(in view: UIView) {
        removeFromSuperview()
        alpha = 0
        view.addSubview(self)
        anchorTo(superview: view)
        UIView.animate(withDuration: 0.2) {
            self.alpha = 1
        }
    }
    
    func addRetry(_ retry: @escaping () -> Void) {
        self.retry.append(retry)
    }
    
    private func setupView() {
        translatesAutoresizingMaskIntoConstraints = false
        contentView.addSubview(stackView)
        stackView.anchorCenterSuperview()
        NSLayoutConstraint.activate([
            stackView.leadingAnchor.constraint(greaterThanOrEqualTo: contentView.leadingAnchor)
        ])
    }
    
    @objc private func retryAction() {
        dismiss()
        retry.forEach { $0?() }
        retry.removeAll()
    }
    
    @objc private func cancelAction() {
        dismiss()
    }
    
    private func dismiss() {
        UIView.animate(
            withDuration: 0.2,
            animations: {
                self.alpha = 0
            },
            completion: { _ in
                self.removeFromSuperview()
            }
        )
    }
}
