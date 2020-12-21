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

class BeagleScreenViewModel {
        
    var screenType: ScreenType {
        didSet {
            screenAppearEventIsPending = true
            screen = nil
        }
    }
    var screen: Screen?
    var state: State {
        didSet { stateObserver?.didChangeState(state) }
    }
    
    public enum State {
        case initialized
        case loading
        case success
        case failure(ServerDrivenState.Error)
    }

    var dependencies: BeagleDependenciesProtocol
    
    private var screenAppearEventIsPending = true

    // MARK: Observer

    public weak var stateObserver: BeagleScreenStateObserver? {
        didSet { stateObserver?.didChangeState(state) }
    }

    // MARK: Init
    
    static func remote(
        _ remote: ScreenType.Remote,
        dependencies: BeagleDependenciesProtocol,
        completion: @escaping (Result<BeagleScreenViewModel, Request.Error>) -> Void
    ) -> RequestToken? {
        
        return fetchScreen(remote: remote, dependencies: dependencies) { result in
            let viewModel = self.init(screenType: .remote(remote), dependencies: dependencies)
            switch result {
            case .success(let screen):
                viewModel.handleRemoteScreenSuccess(screen)
                completion(.success(viewModel))
            case .failure(let error):
                viewModel.handleRemoteScreenFailure(error)
                if viewModel.screen == nil {
                    completion(.failure(error))
                } else {
                    completion(.success(viewModel))
                }
            }
        }
    }

    public required init(
        screenType: ScreenType,
        dependencies: BeagleDependenciesProtocol = Beagle.dependencies
    ) {
        self.screenType = screenType
        self.dependencies = dependencies
        self.state = .initialized
    }
    
    public func loadScreen() {
        guard screen == nil else { return }
        switch screenType {
        case .remote(let remote):
            loadRemoteScreen(remote)
        case .declarative(let screen):
            self.screen = screen
            state = .success
        case .declarativeText(let text):
            tryToLoadScreenFromText(text)
        }
    }
    
    public func trackEventOnScreenAppeared() {
        if let event = screen?.screenAnalyticsEvent {
            screenAppearEventIsPending = false
            dependencies.analytics?.trackEventOnScreenAppeared(event)
        }
        AnalyticsService.shared?.createRecord(screen: screenType)
    }
    
    public func trackEventOnScreenDisappeared() {
        if let event = screen?.screenAnalyticsEvent {
            dependencies.analytics?.trackEventOnScreenDisappeared(event)
        }
    }

    // MARK: Core
    
    func tryToLoadScreenFromText(_ text: String) {
        guard let loadedScreen = loadScreenFromText(text) else {
            state = .failure(.declarativeText)
            return
        }

        screen = loadedScreen
        state = .success
    }

    func loadScreenFromText(_ text: String) -> Screen? {
        guard let data = text.data(using: .utf8) else { return nil }
        let component = try? self.dependencies.decoder.decodeComponent(from: data)
        return component?.toScreen()
    }

    func loadRemoteScreen(_ remote: ScreenType.Remote) {
        state = .loading

        Self.fetchScreen(remote: remote, dependencies: dependencies) {
            [weak self] result in guard let self = self else { return }
            
            switch result {
            case .success(let screen):
                self.handleRemoteScreenSuccess(screen)
                if self.screenAppearEventIsPending {
                    self.trackEventOnScreenAppeared()
                }
            case .failure(let error):
                self.handleRemoteScreenFailure(error)
            }
        }
    }
    
    @discardableResult
    private static func fetchScreen(
        remote: ScreenType.Remote,
        dependencies: BeagleDependenciesProtocol,
        completion: @escaping (Result<Screen, Request.Error>) -> Void
    ) -> RequestToken? {
        return dependencies.repository.fetchComponent(
            url: remote.url,
            additionalData: remote.additionalData,
            useCache: true
        ) {
            completion($0.map { $0.toScreen() })
        }
    }
    
    private func handleRemoteScreenSuccess(_ screen: Screen) {
        self.screen = screen
        state = .success
    }
    
    private func handleRemoteScreenFailure(_ error: Request.Error) {
        if case let .remote(remote) = screenType, let fallback = remote.fallback {
            screen = fallback
            state = .success
        } else {
            state = .failure(.remoteScreen(error))
        }
    }
}

// MARK: - Observer

protocol BeagleScreenStateObserver: AnyObject {
    typealias ViewModel = BeagleScreenViewModel

    func didChangeState(_ state: ViewModel.State)
}
