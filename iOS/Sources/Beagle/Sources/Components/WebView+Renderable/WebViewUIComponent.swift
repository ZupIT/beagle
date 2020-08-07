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
import WebKit

// test loading, idle and loaded state if possible.
final class WebViewUIComponent: UIView {
    
    private var webView = WKWebView()
    
    private lazy var loadingView: UIActivityIndicatorView = {
        let loadingView = UIActivityIndicatorView()
        loadingView.color = .gray
        return loadingView
    }()
    
    private lazy var stackView: UIStackView = {
        let stack = UIStackView()
        stack.axis = .vertical
        stack.distribution = .fill
        stack.alignment = .fill
        return stack
    }()

    public var url: String {
        didSet { updateView() }
    }
    
    init(url: String) {
        self.url = url

        super.init(frame: .zero)

        setupViews()
        updateView()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    private func updateView() {
        guard let url = URL(string: url) else { return }

        let request = URLRequest(url: url)
        loadingView.startAnimating()
        webView.load(request)
    }
    
    private func setupViews() {
        addSubview(stackView)
        stackView.addArrangedSubview(loadingView)
        stackView.addArrangedSubview(webView)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.anchorTo(superview: self)
        loadingView.anchorCenterSuperview()
        loadingView.hidesWhenStopped = true
        webView.navigationDelegate = self
    }
}

extension WebViewUIComponent: WKNavigationDelegate {
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation) {
        loadingView.stopAnimating()
        webView.isHidden = false
    }
}
