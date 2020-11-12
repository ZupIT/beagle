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
import SnapshotTesting
import BeagleSchema
@testable import Beagle

final class SetContextTests: XCTestCase {
    
    let size = ImageSize.custom(CGSize(width: 200, height: 100))
    
    func testSetContext() {
        // Given
        let component = Container(context: Context(id: "context", value: "value")) {
            Text("SetContext:")
            Text("@{context}")
        }
        let controller = BeagleScreenViewController(component)
        let navigation = UINavigationController(rootViewController: controller)
        let action = SetContext(contextId: "context", value: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
        
        // When/Then
        assertSnapshotImage(navigation, size: size)
        controller.execute(actions: [action], origin: getOriginView(controller))
        assertSnapshotImage(navigation, size: size)
    }
    
    func testSetContextWithMultipleExpression() {
        // Given
        let component = Container(context: Context(id: "context", value: "John")) {
            Text("SetContext:")
            Text("@{context}")
        }
        let controller = BeagleScreenViewController(component)
        let navigation = UINavigationController(rootViewController: controller)
        let action = SetContext(contextId: "context", value: "@{context} Doe")
        let smallSize = ImageSize.custom(CGSize(width: 150, height: 70))
        
        // When/Then
        assertSnapshotImage(navigation, size: smallSize)
        controller.execute(actions: [action], origin: getOriginView(controller))
        assertSnapshotImage(navigation, size: smallSize)
    }
    
    private func getOriginView(_ controller: BeagleScreenViewController) -> UIView {
        guard case let .view(screen) = controller.content, let container = screen.subviews[safe: 0] else {
            return UIView()
        }
        return container
    }
    
}
