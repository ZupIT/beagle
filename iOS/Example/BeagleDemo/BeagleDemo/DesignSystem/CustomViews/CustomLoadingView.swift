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

final class CustomLoadingView: UIView {

    private lazy var blurEffectView: UIVisualEffectView = {
        let blurEffect = UIBlurEffect(style: .extraLight)
        let blurEffectView = UIVisualEffectView(effect: blurEffect)
        blurEffectView.alpha = 1.0
        blurEffectView.autoresizingMask = [
            .flexibleWidth, .flexibleHeight
        ]
        return blurEffectView
    }()
    
    private lazy var activityIndicator: UIActivityIndicatorView = {
        let view = UIActivityIndicatorView(style: .whiteLarge)
        view.color = .demoGray
        view.autoresizingMask = [
            .flexibleLeftMargin, .flexibleRightMargin,
            .flexibleTopMargin, .flexibleBottomMargin
        ]
        return view
    }()
    
    // MARK: - Life Cycle
    override func awakeFromNib() {
        super.awakeFromNib()
        setupActivityIndicator()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupActivityIndicator()
    }
    
    @available(*, unavailable)
    public required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    // MARK: - Layout

    private func setupActivityIndicator() {
        backgroundColor = UIColor.black.withAlphaComponent(0.5)
        blurEffectView.frame = bounds
        insertSubview(blurEffectView, at: 0)
        activityIndicator.center = CGPoint(
            x: bounds.midX,
            y: bounds.midY
        )
        addSubview(activityIndicator)
    }
    
    // MARK: - Public Functions

    func present(in view: UIView) {
        removeFromSuperview()
        alpha = 0
        view.addSubview(self)
        anchorTo(superview: view)
        activityIndicator.startAnimating()
        UIView.animate(withDuration: 0.2) {
            self.alpha = 1
        }
    }

    public func dismiss() {
        activityIndicator.stopAnimating()
        UIView.animate(withDuration: 0.25,
                       animations: {
            self.alpha = 0
        }, completion: { _ in
            self.activityIndicator.stopAnimating()
            self.removeFromSuperview()
        })
    }
}
