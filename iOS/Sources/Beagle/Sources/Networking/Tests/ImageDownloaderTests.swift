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
@testable import Beagle
import BeagleSchema

class ImageDownloaderTests: XCTestCase {

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
