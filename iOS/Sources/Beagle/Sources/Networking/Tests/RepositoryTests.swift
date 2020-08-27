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

final class RepositoryTests: XCTestCase {

    private struct Dependencies: RepositoryDefault.Dependencies {
        var logger: BeagleLoggerType
        var urlBuilder: UrlBuilderProtocol
        
        var cacheManager: CacheManagerProtocol?
        var baseURL: URL?
        var networkClient: NetworkClient
        var decoder: ComponentDecoding

        init(
            logger: BeagleLoggerType = BeagleLoggerDumb(),
            urlBuilder: UrlBuilderProtocol = UrlBuilder(),
            cacheManager: CacheManagerProtocol = CacheManagerDummy(),
            baseURL: URL? = nil,
            networkClient: NetworkClient = NetworkClientDummy(),
            decoder: ComponentDecoding = ComponentDecodingDummy()
        ) {
            self.logger = logger
            self.urlBuilder = urlBuilder
            self.cacheManager = cacheManager
            self.baseURL = baseURL
            self.networkClient = networkClient
            self.decoder = decoder
        }
    }

    // swiftlint:disable force_unwrapping
    func test_requestWithInvalidURL_itShouldFail() {
        let sut = RepositoryDefault(dependencies: BeagleDependencies())
        let invalidURL = "🥶"
        
        // When
        let fetchComponentExpectation = expectation(description: "fetchComponent")
        var fetchError: Request.Error?
        let expectedError = Request.Error.networkError(.init(error:
            NSError(domain: "kCFErrorDomainCFNetwork", code: 1002, description: ""), request: .init(url: URL(string: "url")!)
        ))

        sut.fetchComponent(url: invalidURL, additionalData: nil) {
            if case let .failure(error) = $0 {
                fetchError = error
            }
            fetchComponentExpectation.fulfill()
        }

        let submitFormExpectation = expectation(description: "submitForm")
        var formError: Request.Error?
        let formData = Request.FormData(
            method: .post, values: [:]
        )

        sut.submitForm(url: invalidURL, additionalData: nil, data: formData) {
            if case let .failure(error) = $0 {
                formError = error
            }
            submitFormExpectation.fulfill()
        }
        wait(for: [fetchComponentExpectation, submitFormExpectation], timeout: 1.0)
                
        // Then
        XCTAssertEqual(expectedError.localizedDescription, fetchError?.localizedDescription)
        XCTAssertEqual(expectedError.localizedDescription, formError?.localizedDescription)
    }
    
    func test_whenRequestSucceeds_withValidData_itShouldReturnSomeComponent() {
        // Given
        guard let jsonData = """
        {
            "_beagleComponent_": "beagle:text",
            "text": "some text"
        }
        """.data(using: .utf8) else {
            XCTFail("Could not create test data.")
            return
        }
        let result = Result<NetworkResponse, NetworkError>.success(.init(data: jsonData, response: URLResponse()))
        let clientStub = NetworkClientStub(result: result)
        let sut = RepositoryDefault(dependencies: Dependencies(
            networkClient: clientStub,
            decoder: ComponentDecoder()
        ))
        let url = "www.something.com"

        // When
        var componentReturned: ServerDrivenComponent?
        let expec = expectation(description: "fetchComponentExpectation")
        sut.fetchComponent(url: url, additionalData: nil) { result in
            if case .success(let component) = result {
                componentReturned = component
            }
            expec.fulfill()
        }
        wait(for: [expec], timeout: 1.0)

        // Then
        XCTAssertNotNil(componentReturned)
        XCTAssert(componentReturned is Text)
    }
    
    func test_whenRequestSucceeds_butTheDecodingFailsWithAnError_itShouldThrowDecodingError() {
        // Given
        let result = Result<NetworkResponse, NetworkError>.success(.init(data: Data(), response: URLResponse()))
        let clientStub = NetworkClientStub(result: result)
        let decoderStub = ComponentDecodingStub()
        decoderStub.errorToThrowOnDecode = NSError(domain: "Mock", code: 1, description: "Mock")
        let sut = RepositoryDefault(dependencies: Dependencies(
            networkClient: clientStub,
            decoder: decoderStub
        ))

        let url = "www.something.com"

        // When
        var errorThrown: Request.Error?
        let expec = expectation(description: "fetchComponentExpectation")
        sut.fetchComponent(url: url, additionalData: nil) { result in
            if case let .failure(error) = result {
                errorThrown = error
            }
            expec.fulfill()
        }
        wait(for: [expec], timeout: 1.0)

        // Then
        XCTAssertNotNil(errorThrown)
        guard case .decoding? = errorThrown else {
            XCTFail("Expected a `.decoding` error, but got \(String(describing: errorThrown)).")
            return
        }
    }
}

// MARK: - Testing Helpers

final class ComponentDecodingStub: ComponentDecoding {
    func register<T>(component type: T.Type) where T: RawComponent {}
    func register<A>(action type: A.Type) where A: RawAction {}
    func register<T>(component type: T.Type, named typeName: String) where T: BeagleSchema.RawComponent {}
    func register<A>(action type: A.Type, named typeName: String) where A: BeagleSchema.RawAction {}
    func componentType(forType type: String) -> Decodable.Type? { return nil }
    func actionType(forType type: String) -> Decodable.Type? { return nil }
    
    var componentToReturnOnDecode: BeagleSchema.RawComponent?
    var errorToThrowOnDecode: Error?
    
    func decodeComponent(from data: Data) throws -> BeagleSchema.RawComponent {
        if let error = errorToThrowOnDecode {
            throw error
        }
        return ComponentDummy()
    }

    func decodeAction(from data: Data) throws -> RawAction {
        if let error = errorToThrowOnDecode {
            throw error
        }
        return ActionDummy()
    }
}

final class RepositoryStub: Repository {

    var componentResult: Result<ServerDrivenComponent, Request.Error>?
    var formResult: Result<RawAction, Request.Error>?
    var imageResult: Result<Data, Request.Error>?

    private(set) var didCallDispatch = false
    private(set) var token = Token()
    private(set) var formData = Request.FormData(method: .post, values: [:])

    class Token: RequestToken {
        var didCallCancel = false

        func cancel() {
            didCallCancel = true
        }
    }

    init(
        componentResult: Result<ServerDrivenComponent, Request.Error>? = nil,
        formResult: Result<RawAction, Request.Error>? = nil,
        imageResult: Result<Data, Request.Error>? = nil
    ) {
        self.componentResult = componentResult
        self.formResult = formResult
        self.imageResult = imageResult
    }

    func fetchComponent(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        useCache: Bool,
        completion: @escaping (Result<ServerDrivenComponent, Request.Error>) -> Void
    ) -> RequestToken? {
        didCallDispatch = true
        if let result = componentResult {
            completion(result)
        }
        return token
    }

    func submitForm(
        url: String,
        additionalData: RemoteScreenAdditionalData?,
        data: Request.FormData,
        completion: @escaping (Result<RawAction, Request.Error>) -> Void
    ) -> RequestToken? {
        didCallDispatch = true
        formData = data
        if let result = formResult {
            completion(result)
        }
        return token
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

class NetworkClientStub: NetworkClient {

    let result: NetworkClient.NetworkResult

    private(set) var executedRequest: Request?

    init(result: NetworkClient.NetworkResult) {
        self.result = result
    }

    func executeRequest(_ request: Request, completion: @escaping RequestCompletion) -> RequestToken? {
        executedRequest = request
        completion(result)
        return nil
    }
}
 enum TestErrors: Swift.Error {
     case generic
 }

class CacheManagerSpy: CacheManagerProtocol {
    
    var references = [Reference]()

    class Reference {
        let cache: CacheReference
        var isValid: Bool

        init(cache: CacheReference, isValid: Bool) {
            self.cache = cache
            self.isValid = isValid
        }
    }
    
    func addToCache(_ reference: CacheReference) {
        guard first(reference.identifier) == nil else { return }
        references.append(.init(cache: reference, isValid: false))
    }
    
    func getReference(identifiedBy id: String) -> CacheReference? {
        return first(id)?.cache
    }
    
    func isValid(reference: CacheReference) -> Bool {
        return first(reference.identifier)?.isValid ?? false
    }

    private func first(_ id: String) -> Reference? {
        return references.first { $0.cache.identifier == id }
    }
}
