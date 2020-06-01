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

//TODO: implement snapshot testing. We should test this as image
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
        stack.spacing = 5
        return stack
    }()
    
    struct Model {
        let url: String
    }
    
    private var model: Model?
    
    init(model: Model) {
        self.model = model
        super.init(frame: .zero)
        setupViews()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupViews() {
        addSubview(stackView)
        stackView.addArrangedSubview(loadingView)
        stackView.addArrangedSubview(webView)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.anchor(top: topAnchor, left: leftAnchor, bottom: bottomAnchor, right: rightAnchor)
        
        guard let model = model else { return }
        if let url = URL(string: model.url) {
            let request = URLRequest(url: url)
            webView.load(request)
        }
        
        loadingView.hidesWhenStopped = true
        webView.navigationDelegate = self
        webView.isHidden = loadingView.isHidden ? true : false
        webView.isLoading ? loadingView.startAnimating() : loadingView.stopAnimating()
    }
}
extension WebViewUIComponent: WKNavigationDelegate {
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation) {
        loadingView.stopAnimating()
        webView.isHidden = false
    }
}
