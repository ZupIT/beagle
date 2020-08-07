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

import XCTest
import SnapshotTesting
import WebKit
@testable import Beagle
import BeagleSchema

final class WebViewTests: XCTestCase {
    
    private lazy var controller = BeagleControllerStub()
    
    func testRenderWebViewComponent() {
        // Given
        let webView = WebView(url: "https://maps.google.com/")
        let renderer = BeagleRenderer(controller: controller)

        // When
        let view = renderer.render(webView)
        
        // Then
        XCTAssertTrue(view is WebViewUIComponent)
    }
    
    func testIdleState() {
        // Given // When
        let webViewUIComponent = WebViewUIComponent(controller: controller)

        // Then
        XCTAssertEqual(webViewUIComponent.state, .idle)
    }
    
    func testLoadingState() {
        // Given // When
        let webViewUIComponent = WebViewUIComponent(url: "https://maps.google.com/", controller: controller)

        // Then
        XCTAssertEqual(webViewUIComponent.state, .loading)
    }
    
    func testLoadedState() {
        // Given
        let webViewUIComponent = WebViewUIComponent(url: "https://maps.google.com/", controller: controller)
        
        // When
        webViewUIComponent.webView(webViewUIComponent.webView, didFinish: nil)

        // Then
        XCTAssertEqual(webViewUIComponent.state, .loaded)
    }

    func testErrorState() {
        // Given
        let webViewUIComponent = WebViewUIComponent(url: "https://sdfsdjfi.com", controller: controller)
        let webView = webViewUIComponent.webView
        // When
        webViewUIComponent.webView(webView, didFail: nil, withError: NSError(domain: "", code: 0, description: ""))

        // Then
        XCTAssertEqual(webViewUIComponent.state, .error)
    }
}
