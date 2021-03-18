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

class BindingConfiguratorTests: XCTestCase {
    
    func testBindingParticipatsShouldNotBeRetained() throws {
        // Given
        let sut = Bindings()
        weak var view: UIView?
        weak var controller: UIViewController?
        
        // When
        (view, controller) = addBinding(to: sut, config: false)
        // Then
        XCTAssertNil(view)
        XCTAssertNil(controller)
        
        // When
        (view, controller) = addBinding(to: sut, config: true)
        // Then
        XCTAssertNil(view)
        XCTAssertNil(controller)
    }
    
    private func addBinding(to bindings: Bindings, config: Bool) -> (UIView, UIViewController) {
        let expression = ContextExpression.single(.value(.literal(.string("text"))))
        let controller = UIViewController()
        let view = UILabel()
        bindings.add(controller, expression, view) { [weak view] in
            view?.text = $0
        }
        if config {
            bindings.config()
        }
        return (view, controller)
    }

}
