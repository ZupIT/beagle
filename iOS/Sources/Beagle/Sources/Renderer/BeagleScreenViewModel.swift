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

import UIKit
import BeagleSchema

class BeagleScreenViewModel {
        
    var screenType: ScreenType {
        didSet { screen = nil }
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

    // MARK: Observer

    public weak var stateObserver: BeagleScreenStateObserver? {
        didSet { stateObserver?.didChangeState(state) }
    }

    // MARK: Init

    public init(
        screenType: ScreenType,
        dependencies: BeagleDependenciesProtocol = Beagle.dependencies
    ) {
        self.screenType = screenType
        self.dependencies = dependencies
        self.state = .initialized
    }
    
    public func loadScreen() {
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

        dependencies.repository.fetchComponent(
            url: remote.url,
            additionalData: remote.additionalData
        ) {
            [weak self] result in guard let self = self else { return }

            switch result {
            case .success(let component):
                self.handleRemoteScreenSuccess(component)
            case .failure(let error):
                self.handleRemoteScreenFailure(error)
            }
        }
    }
    
    private func handleRemoteScreenSuccess(_ component: ServerDrivenComponent) {
        screen = component.toScreen()
        state = .success
    }
    
    private func handleRemoteScreenFailure(_ error: Request.Error) {
        if case let .remote(remote) = screenType, let fallback = remote.fallback {
            screen = fallback
        }
        state = .failure(.remoteScreen(error))
    }
}

// MARK: - Observer

protocol BeagleScreenStateObserver: AnyObject {
    typealias ViewModel = BeagleScreenViewModel

    func didChangeState(_ state: ViewModel.State)
}
