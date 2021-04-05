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

final class RepositoryTests: XCTestCase {

    private var dependencies = BeagleDependencies()

    private lazy var sut = RepositoryDefault(dependencies: dependencies)

    private let url = "www.something.com"

    // swiftlint:disable force_unwrapping
    func test_requestWithInvalidURL_itShouldFail() {
        // Given
        let invalidURL = "🥶"
        let beagleDependencies = BeagleDependencies()
        beagleDependencies.urlBuilder = UrlBuilderStub(baseUrl: nil, resultURL: nil)
        let repository = RepositoryDefault(dependencies: beagleDependencies)
        
        let fetchComponentExpectation = expectation(description: "fetchComponent")
        var fetchError: Request.Error?
        let expectedError = Request.Error.urlBuilderError
        
        // When
        repository.fetchComponent(url: invalidURL, additionalData: nil) {
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

        repository.submitForm(url: invalidURL, additionalData: nil, data: formData) {
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
        let jsonData = """
        {
            "_beagleComponent_": "beagle:text",
            "text": "some text"
        }
        """.data(using: .utf8)!

        dependencies.networkClient = NetworkClientStub(result:
            .success(.init(data: jsonData, response: URLResponse()))
        )

        let expec = expectation(description: "fetchComponentExpectation")

        // When
        var componentReturned: ServerDrivenComponent?
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
        dependencies.networkClient = NetworkClientStub(result:
            .success(.init(data: Data(), response: URLResponse()))
        )

        let decoderStub = ComponentDecodingStub()
        decoderStub.errorToThrowOnDecode = NSError(domain: "Mock", code: 1, description: "Mock")
        dependencies.decoder = decoderStub

        let expec = expectation(description: "fetch")

        // When
        var errorThrown: Request.Error?

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
    
    func test_shouldAddAdditionalDataToRequest() {
        // Given
        let body = "{}".data(using: .utf8)!

        let clientStub = NetworkClientStub(result: .success(.init(data: body, response: URLResponse())))
        dependencies.networkClient = clientStub

        let additionalData = HttpAdditionalData(
            httpData: .init(method: .POST, body: body),
            headers: ["headerKey": "headerValue"]
        )

        let expec = expectation(description: "fetch")

        // When
        sut.fetchComponent(url: url, additionalData: additionalData) { _ in
            expec.fulfill()
        }
        wait(for: [expec], timeout: 1.0)

        // Then
        let expectedData = clientStub.executedRequest?.additionalData as? HttpAdditionalData

        XCTAssertEqual(expectedData, additionalData)
        XCTAssertEqual(clientStub.executedRequest?.url.absoluteString, url)
    }
}

// MARK: - Testing Helpers

final class ComponentDecodingStub: ComponentDecoding {
    func register<T>(component type: T.Type) where T: ServerDrivenComponent {}
    func register<A>(action type: A.Type) where A: Action {}
    func register<T>(component type: T.Type, named typeName: String) where T: ServerDrivenComponent {}
    func register<A>(action type: A.Type, named typeName: String) where A: Action {}
    func componentType(forType type: String) -> Decodable.Type? { return nil }
    func actionType(forType type: String) -> Decodable.Type? { return nil }
    func nameForComponent(ofType type: ServerDrivenComponent.Type) -> String? { return nil }
    func nameForAction(ofType type: Action.Type) -> String? { return nil }
    
    var componentToReturnOnDecode: ServerDrivenComponent?
    var errorToThrowOnDecode: Error?
    
    func decodeComponent(from data: Data) throws -> ServerDrivenComponent {
        if let error = errorToThrowOnDecode {
            throw error
        }
        return ComponentDummy()
    }

    func decodeAction(from data: Data) throws -> Action {
        if let error = errorToThrowOnDecode {
            throw error
        }
        return ActionDummy()
    }
}

final class RepositoryStub: Repository {

    var componentResult: Result<ServerDrivenComponent, Request.Error>?
    var formResult: Result<Action, Request.Error>?

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
        formResult: Result<Action, Request.Error>? = nil
    ) {
        self.componentResult = componentResult
        self.formResult = formResult
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
        completion: @escaping (Result<Action, Request.Error>) -> Void
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
        return nil
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

private struct UrlBuilderStub: UrlBuilderProtocol {
    var baseUrl: URL?
    var resultURL: URL?
    
    func build(path: String) -> URL? {
        return resultURL
    }
}
