//
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

final class BeagleNavigatorAnimationTests: XCTestCase {

    func test_mapPushTranstionToCATransition() {
        let animation = BeagleNavigatorAnimation(pushTransition: .init(type: .fade, subtype: .fromRight, duration: 1.0))
        let caTransition = animation.getTransition(.push)
        
        verifyMappingToCATransition(caTransition: caTransition, transition: animation.pushTransition)
    }
    
    func test_mapPopTranstionToCATransition() {
        let animation = BeagleNavigatorAnimation(popTransition: .init(type: .reveal, subtype: .fromLeft, duration: 2.0))
        let caTransition = animation.getTransition(.pop)
        
        verifyMappingToCATransition(caTransition: caTransition, transition: animation.popTransition)
    }
    
    private func verifyMappingToCATransition(caTransition: CATransition?, transition: BeagleNavigatorAnimation.Transition?) {
        XCTAssertEqual(caTransition?.type, transition?.type)
        XCTAssertEqual(caTransition?.subtype, transition?.subtype)
        XCTAssertEqual(caTransition?.duration, transition?.duration)
    }
}
