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
@testable import BeagleUI

final class UIViewLoadingTests: XCTestCase {
    
    func test_showLoading() {
        // Given
        let view = UIView()
        
        // When
        view.showLoading()
        
        // Then
        XCTAssertNotNil(view.viewWithTag(LoadingView.tag), "The view should contain a `LoadingView`.")
    }
    
    func test_hideLoading() {
        // Given
        let view = UIView()
        view.showLoading()
        let theViewHadALoadingView = view.viewWithTag(LoadingView.tag) != nil
        
        // When
        let hideLoadingExpectation = expectation(description: "hideLoadingExpectation")
        view.hideLoading {
            hideLoadingExpectation.fulfill()
        }
        wait(for: [hideLoadingExpectation], timeout: 1.0)
        
        // Then
        XCTAssertTrue(theViewHadALoadingView, "The view had a `LoadingView` that needed to be hidden.")
        XCTAssertNil(view.viewWithTag(LoadingView.tag), "The view should not contain a `LoadingView`.")
    }
    
}
