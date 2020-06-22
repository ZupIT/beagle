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
@testable import BeagleUI
import BeagleSchema

class StyleBuildersExtensionTests: XCTestCase {
    
    func test_SizeBuilder() {
        let sut = Size()
            .height(10)
            .width(11)
            .maxWidth(12)
            .minWidth(1)
            .maxHeight(13)
            .minHeight(2)
            .aspectRatio(2)
        
        assertSnapshot(matching: sut, as: .dump)
    }
    
    func test_EdgeValueBuilder() {
        let sut = EdgeValue()
            .all(10)
            .bottom(11)
            .horizontal(13)
            .left(14)
            .right(15)
            .top(17)
            .vertical(18)
        
        assertSnapshot(matching: sut, as: .dump)
    }
    
    func test_FlexBuilder() {
        let sut = Flex()
            .alignContent(.center)
            .alignItems(.center)
            .alignSelf(.center)
            .basis(10)
            .flexDirection(.column)
            .flexWrap(.noWrap)
            .justifyContent(.spaceBetween)
            .flex(2)
            .grow(3)
            .shrink(0)
        
        assertSnapshot(matching: sut, as: .dump)
    }
    
    func test_StyleBuilder() {
        let sut = Style()
            .flex(Flex())
            .display(.flex)
            .positionType(.absolute)
            .size(Size())
            .margin(EdgeValue())
            .padding(EdgeValue())
            .position(EdgeValue())
            .backgroundColor("#FFFFFF")
            .cornerRadius(CornerRadius(radius: 5))
        
        assertSnapshot(matching: sut, as: .dump)
    }
}
