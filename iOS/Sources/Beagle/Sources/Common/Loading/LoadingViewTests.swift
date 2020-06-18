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
@testable import Beagle

final class LoadingViewTests: XCTestCase {
    
    func test_activityIndicatorStyle_shouldUpdate_activityIndicator() {
        // Given
        let sut = LoadingView()
        XCTAssertEqual(sut.activityIndicatorStyle, .whiteLarge, "Expected `whiteLarge`, but got \(String(describing: sut.activityIndicatorStyle)).")
        
        // When
        sut.activityIndicatorStyle = .gray
        
        // Then
        XCTAssertEqual(sut.activityIndicatorStyle, .gray, "Expected `whiteLarge`, but got \(String(describing: sut.activityIndicatorStyle)).")
    }
    
    func test_awakeFromNib_shouldCallSetup() {
        // Given
        let sut = LoadingView()
        
        // When
        sut.awakeFromNib()
        let containsBlurView = sut.subviews.contains {
            $0.alpha == 0.25 && $0.backgroundColor == .black
        }
        let containsActivityIndicator = sut.subviews.contains { $0 is UIActivityIndicatorView }
        
        // Then
        XCTAssertEqual(LoadingView.tag, sut.tag, "Expected \(LoadingView.tag), but got \(sut.tag).")
        XCTAssertEqual(2, sut.subviews.count, "Expected 2 subviews, but got \(sut.subviews.count).")
        XCTAssertTrue(containsBlurView, "The `LoadingView` should contain the `blurView`.")
        XCTAssertTrue(containsActivityIndicator, "The `LoadingView` should contain the `activityIndicator`.")
    }
    
    func test_startAnimating() {
        // Given
        let sut = LoadingView()
        let activityIndicator = sut.subviews.first { $0 is UIActivityIndicatorView } as? UIActivityIndicatorView
        
        // When
        sut.startAnimating()
        
        // Then
        XCTAssertEqual(activityIndicator?.isAnimating, true, "`activityIndicator` should be animating.")
    }
    
    func test_stopAnimating() {
        // Given
        let sut = LoadingView()
        sut.startAnimating()
        let activityIndicator = sut.subviews.first { $0 is UIActivityIndicatorView } as? UIActivityIndicatorView
        
        // When
        sut.stopAnimating()
        
        // Then
        XCTAssertEqual(activityIndicator?.isAnimating, false, "`activityIndicator` should not be animating.")
    }
    
}
