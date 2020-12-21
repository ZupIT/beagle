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

class ImageDownloaderTests: XCTestCase {

    private var dependencies = BeagleDependencies()

    private lazy var sut = ImageDownloaderDefault(dependencies: dependencies)

    private let url = "www.something.com"
    
    // swiftlint:disable force_unwrapping
    func testFetchImage() {
        // Given
        let image = UIImage(named: "beagle", in: Bundle(for: type(of: self)), compatibleWith: nil)
        let imageData = image!.pngData()!

        dependencies.networkClient = NetworkClientStub(result:
            .success(.init(data: imageData, response: URLResponse()))
        )

        let expec = expectation(description: "fetchComponentExpectation")

        // When
        var dataReturned: Data?
        sut.fetchImage(url: url, additionalData: nil) { result in
            if case .success(let data) = result {
                dataReturned = data
            }
            expec.fulfill()
        }
        wait(for: [expec], timeout: 1.0)

        // Then
        XCTAssertNotNil(dataReturned)
        XCTAssertEqual(dataReturned, imageData)
    }
    // swiftlint:enable force_unwrapping
}

final class ImageDownloaderStub: ImageDownloader {
    
    var imageResult: Result<Data, Request.Error>?

    private(set) var didCallDispatch = false
    private(set) var token = Token()
    
    class Token: RequestToken {
        var didCallCancel = false

        func cancel() {
            didCallCancel = true
        }
    }
    
    init(
        imageResult: Result<Data, Request.Error>? = nil
    ) {
        self.imageResult = imageResult
    }
    
    func fetchImage(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        completion: @escaping (Result<Data, Request.Error>) -> Void
    ) -> RequestToken? {
        didCallDispatch = true
        if let result = imageResult {
            completion(result)
        }
        return token
    }
}
