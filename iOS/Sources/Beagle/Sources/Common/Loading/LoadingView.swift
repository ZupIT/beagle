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

/// Defines a simple loadingView for the project
final class LoadingView: UIView {
    
    // MARK: - Constants
    
    static let tag = 11111
    
    // MARK: - UI
    
    private lazy var blurView: UIView = {
        let view = UIView(frame: frame)
        view.backgroundColor = .black
        view.alpha = 0.25
        return view
    }()
    
    private lazy var activityIndicator: UIActivityIndicatorView = {
        let view = UIActivityIndicatorView(style: .whiteLarge)
        
        view.color = .white
        return view
    }()
    
    // MARK: - Properties
    
    /// Exposes the `ActivityIndicator.Style`
    var activityIndicatorStyle: UIActivityIndicatorView.Style {
        get { return activityIndicator.style }
        set { activityIndicator.style = newValue }
    }
    
    // MARK: - Life Cycle
    
    override func awakeFromNib() {
        super.awakeFromNib()
        setup()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    @available(*, unavailable)
    public required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Setup
    
    private func setup() {
        tag = LoadingView.tag
        addSubviews()
    }
    
    // MARK: - Layout
    
    private func addSubviews() {
        constrainBlurView()
        constrainActivityIndicator()
    }
    
    private func constrainBlurView() {
        addSubview(blurView)
        blurView.anchor(
            top: topAnchor,
            left: leftAnchor,
            bottom: bottomAnchor,
            right: rightAnchor
        )
    }
    
    private func constrainActivityIndicator() {
        addSubview(activityIndicator)
        activityIndicator.anchorCenterSuperview()
        activityIndicator.heightAnchor.constraint(
            equalTo: widthAnchor,
            multiplier: 0.2
        ).isActive = true
        activityIndicator.widthAnchor.constraint(
            equalTo: activityIndicator.heightAnchor,
            constant: 1.0
        ).isActive = true
    }
    
    // MARK: - Public Functions

    /// Starts the loading animation
    public func startAnimating() {
        activityIndicator.startAnimating()
    }

    /// Stops the loading animation
    public func stopAnimating() {
        activityIndicator.stopAnimating()
    }
    
}
