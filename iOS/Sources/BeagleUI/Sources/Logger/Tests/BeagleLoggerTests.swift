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

import Foundation
import XCTest
@testable import BeagleUI
import SnapshotTesting
import Schema

class BeagleLoggerTests: XCTestCase {
    // swiftlint:disable force_unwrapping

    func testLogs() {
        // Given
        let form = Form(action: ActionDummy(), child: ComponentDummy())
        let path = "path"

        let logs: [Log] = [
            Log.network(.couldNotBuildUrl(url: "asdfa/asdfa/asdf")),
            Log.network(.httpRequest(request: .init(url: URLRequest(url: URL(string: "test")!)))),
            Log.network(.httpResponse(response: .init(data: nil, reponse: nil))),

            Log.form(.divergentInputViewAndValueCount(form: form)),
            Log.form(.inputsNotFound(form: form)),
            Log.form(.submitNotFound(form: form)),
            Log.form(.submittedValues(values: ["key1": "value1"])),
            Log.form(.validationInputNotValid(inputName: "inputName")),
            Log.form(.validatorNotFound(named: "validatorName")),

            Log.navigation(.cantPopToAlreadyCurrentScreen(identifier: "identifier")),
            Log.navigation(.didReceiveAction(Navigate.addView(.init(path: path)))),
            Log.navigation(.didReceiveAction(Navigate.openDeepLink(.init(path: path)))),
            Log.navigation(.didReceiveAction(Navigate.openDeepLink(.init(path: path, data: ["key": "value"], component: Text("bla"))))),
            Log.navigation(.errorTryingToPopScreenOnNavigatorWithJustOneScreen),
            Log.navigation(.didNotFindDeepLinkScreen(path: path)),

            Log.decode(.decodingError(type: "error"))
        ]

        // When
        let messages = logs.map { $0.message }

        // Then
        let result = messages.joined(separator: "\n\n")
        assertSnapshot(matching: result, as: .description)
    }
}

class BeagleLoggerDumb: BeagleLoggerType {
    func log(_ log: LogType) {
        return
    }
}
