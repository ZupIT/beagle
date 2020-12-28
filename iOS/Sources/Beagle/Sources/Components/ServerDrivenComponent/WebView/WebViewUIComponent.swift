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

final class WebViewUIComponent: UIView {
    
    enum State {
        case idle
        case loading
        case loaded
        case error
    }
    
    private(set) var state: State = .idle
    private weak var controller: BeagleController?
    
    lazy var webView: WKWebView = {
        let webView = WKWebView()
        webView.navigationDelegate = self
        return webView
    }()

    public var url: String? {
        didSet { updateView() }
    }
    
    init(url: String? = nil, controller: BeagleController) {
        self.url = url
        self.controller = controller
        super.init(frame: .zero)

        setupViews()
        updateView()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func sizeThatFits(_ size: CGSize) -> CGSize {
        return size
    }

    private func updateView() {
        guard let url = URL(string: url ?? "") else { return }
        
        let request = URLRequest(url: url)
        webView.showLoading()
        webView.load(request)
        state = .loading
    }
    
    private func setupViews() {
        addSubview(webView)
        webView.anchorTo(superview: self)
    }
}

extension WebViewUIComponent: WKNavigationDelegate {
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation?) {
        webView.hideLoading()
        state = .loaded
    }
    
    func webView(_ webView: WKWebView, didFail navigation: WKNavigation?, withError error: Error) {
        webView.hideLoading()
        state = .error
        controller?.serverDrivenState = .error(
            .webView(error),
            retryClosure()
        )
    }
    
    private func retryClosure() -> BeagleRetry {
        return {
            self.updateView()
        }
    }
}
