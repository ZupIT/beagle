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

public struct WebView: FlexComponent, ServerDrivenComponent {
    public let url: String
    public let flex: Flex?
    
    public init
    (
        url: String,
        flex: Flex? = nil
    ) {
        self.url = url
        self.flex = flex
    }
}

extension WebView: Renderable {
    public func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let webView = WebViewUIComponent(model: WebViewUIComponent.Model(url: url))
        webView.flex.setup(flex)
        return webView
    }
}

extension WebView: Decodable {
    enum CodingKeys: String, CodingKey {
        case url
        case flex
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.url = try container.decode(String.self, forKey: .url)
        self.flex = try container.decodeIfPresent(Flex.self, forKey: .flex)
    }
}
