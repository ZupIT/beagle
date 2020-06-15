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
import BeagleSchema

final class ImageContentModeTests: XCTestCase {
    
    func test_imageContentModeRepresentationToUIKitWhenFitXY_shouldReturnScaleToFill() {
        // Given
        let fitXYContentMode = ImageContentMode.toUIKit(.fitXY)
        let expectedContentMode = UIImageView.ContentMode.scaleToFill
        // When /Then
        XCTAssertEqual(fitXYContentMode(), expectedContentMode, "Expected fitXY to represent ScaleToFill content mode, but got \(String(describing: fitXYContentMode))")
    }
    
    func test_imageContentModeRepresentationToUIKitWhenFitCenter_shouldReturnScaleAspectFit() {
        // Given
        let fitCenterContentMode = ImageContentMode.toUIKit(.fitCenter)
        let expectedContentMode = UIImageView.ContentMode.scaleAspectFit
        // When /Then
        XCTAssertEqual(fitCenterContentMode(), expectedContentMode, "Expected fitCenter to represent ScaleAspectFit content mode, but got \(String(describing: fitCenterContentMode))")
    }
    
    func test_imageContentModeRepresentationToUIKitWhenCenter_shouldReturnCenter() {
        // Given
        let centerContentMode = ImageContentMode.toUIKit(.center)
        let expectedContentMode = UIImageView.ContentMode.center
        // When /Then
        XCTAssertEqual(centerContentMode(), expectedContentMode, "Expected center to represent Center content mode, but got \(String(describing: centerContentMode))")
    }
    
    func test_imageContentModeRepresentationToUIKitWhenFitCenterCrop_shouldReturnScaleAspectFill() {
        // Given
        let centerCropContentMode = ImageContentMode.toUIKit(.centerCrop)
        let expectedContentMode = UIImageView.ContentMode.scaleAspectFill
        // When /Then
        XCTAssertEqual(centerCropContentMode(), expectedContentMode, "Expected fitXY to represent ScaleAspectFill content mode, but got \(String(describing: centerCropContentMode))")
    }
}
