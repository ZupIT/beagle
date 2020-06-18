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

extension UIView {

    /// Shows a loading view on top of some View
    func showLoading(_ activityIndicatorStyle: UIActivityIndicatorView.Style = .gray) {
        viewWithTag(LoadingView.tag)?.removeFromSuperview() // ensure that we have no other loadingView here
        let loadingView = LoadingView(frame: frame)
        loadingView.activityIndicatorStyle = activityIndicatorStyle
        addSubview(loadingView)
        loadingView.anchor(
            top: topAnchor,
            left: leftAnchor,
            bottom: bottomAnchor,
            right: rightAnchor
        )
        loadingView.startAnimating()
    }
    
    /// Tries to hide the loadingView that is visible
    func hideLoading(completion: (() -> Void)? = nil) {
        let loadingView = viewWithTag(LoadingView.tag)
        // swiftlint:disable multiline_arguments
        UIView.animate(withDuration: 0.25, animations: {
            loadingView?.alpha = 0
        }, completion: { _ in
            (loadingView as? LoadingView)?.stopAnimating()
            loadingView?.removeFromSuperview()
            completion?()
        })
    }

}
