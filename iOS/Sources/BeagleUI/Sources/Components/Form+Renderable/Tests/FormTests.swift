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

final class FormTests: XCTestCase {
    
    func test_buildView_shouldRegisterFormSubmit() {
        // Given
        let submitView = UILabel()
        let sut = Form(
            action: ActionDummy(),
            child: FormSubmit(child: ComponentDummy(resultView: submitView))
        )
        let controller = BeagleControllerStub()
        
        // When
        _ = sut.toView(renderer: BeagleRenderer(controller: controller))
        
        // Then
        XCTAssertEqual(submitView.gestureRecognizers?.count, 1)
        XCTAssert(submitView.gestureRecognizers?[0] is SubmitFormGestureRecognizer)
        XCTAssert(submitView.isUserInteractionEnabled)
    }
}

// MARK: - Stubs

    private struct InputComponent: BeagleUI.ServerDrivenComponent {
        let value: String
        
        func toView(renderer: BeagleRenderer) -> UIView {
            return InputStub(value: value)
        }
    }

    private class InputStub: UIView, InputValue, ValidationErrorListener, WidgetStateObservable {
        var observable = Observable<WidgetState>(value: WidgetState(value: false))
        
        let value: String
        
        init(value: String = "") {
            self.value = value
            super.init(frame: .zero)
        }
        
        required init?(coder: NSCoder) {
            BeagleUI.fatalError("init(coder:) has not been implemented")
        }
        
        func getValue() -> Any {
            return value
        }
        func onValidationError(message: String?) {
        }
    }
