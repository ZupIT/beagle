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
import BeagleSchema
import BeagleUI

final class ComposeComponentTests: XCTestCase { 

    private let imageSize = ImageSize.custom(CGSize(width: 300, height: 200))

    func testComposeComponent() throws {
        let component = ComposeText(title: "TITLE", subtitle: "subtitle")

        let vc = Beagle.screen(.declarative(component.toScreen()))

        assertSnapshotImage(vc, size: imageSize)
    }
}

struct ComposeText: BeagleUI.ComposeComponent {
    var title: String = ""
    var subtitle: String = ""
    
    func build() -> BeagleSchema.RawComponent {
        return Container(children: [
            Text(title),
            Text(subtitle)
        ], widgetProperties: .init(flex: Flex()))
    }
}
