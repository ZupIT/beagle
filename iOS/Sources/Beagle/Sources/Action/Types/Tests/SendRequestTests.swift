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
import BeagleSchema
import SnapshotTesting
@testable import Beagle

final class SendRequestTests: XCTestCase {

    func test_whenSendRequestWithSuccess_shouldDoRequestAndTriggerActions() {
        // Given
        let sendRequest = SendRequest(
            url: "http://mock",
            method: .post,
            data: .string("data")
        )
        let view = UIView()
        let controller = BeagleControllerSpy()
        // swiftlint:disable force_unwrapping
        let jsonData = """
        [
            {
                "a": "value a", "b": "value b"
            }
        ]
        """.data(using: .utf8)!
        // swiftlint:enable force_unwrapping
        let networkClient = NetworkClientStub(result: .success(.init(data: jsonData, response: URLResponse())))
        controller.dependencies = BeagleScreenDependencies(networkClient: networkClient)
        let expec = expectation(description: "executeActions")
        expec.expectedFulfillmentCount = 2
        controller.expectation = expec
        
        // When
        sendRequest.execute(controller: controller, origin: view)

        // Then
        wait(for: [expec], timeout: 1.0)
        assertSnapshot(matching: controller.lastImplicitContext, as: .dump)
        
    }
    
    func test_whenSendRequestWithError_shouldDoRequestAndTriggerActions() {
        // Given
        let url = "http://mock"
        let sendRequest = SendRequest(
            url: "\(url)",
            method: .get
        )
        let view = UIView()
        let controller = BeagleControllerSpy()
        // swiftlint:disable force_unwrapping
        let jsonData = """
        {
            "error": "errorString"
        }
        """.data(using: .utf8)!
        let networkError = NetworkError(
            error: NSError(),
            data: jsonData,
            request: URLRequest(url: URL(string: url)!),
            response: URLResponse()
        )
        // swiftlint:enable force_unwrapping
        let networkClient = NetworkClientStub(result: .failure(networkError))
        controller.dependencies = BeagleScreenDependencies(networkClient: networkClient)
        let expec = expectation(description: "executeActions")
        expec.expectedFulfillmentCount = 2
        controller.expectation = expec
        
        // When
        sendRequest.execute(controller: controller, origin: view)

        // Then
        wait(for: [expec], timeout: 1.0)
        assertSnapshot(matching: controller.lastImplicitContext, as: .dump)
        
    }
}
