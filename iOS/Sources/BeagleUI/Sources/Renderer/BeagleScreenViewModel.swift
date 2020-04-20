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

public protocol RemoteScreenAdditionalData {
    typealias Http = HttpAdditionalData
    
    var headers: [String: String] { get set }
}

public class BeagleScreenViewModel: ScreenEvent {
    
    // MARK: Analytics Events
    internal enum ScreenAnalyticsEvents: String {
        case screenAppeared = "Screen Appeared"
        case screenDisapeared = "Screen Disappeared"
    }
    
    public var screenAnalyticsEvent: AnalyticsScreen? {
        return screen?.screenAnalyticsEvent
    }
    
    // MARK: ScreenType

    var screenType: ScreenType
    
    public enum ScreenType {
        case remote(Remote)
        case declarative(Screen)
        case declarativeText(String)

        public struct Remote {
            let url: String
            let fallback: Screen?
            let additionalData: RemoteScreenAdditionalData?

            public init(
                url: String,
                fallback: Screen? = nil,
                additionalData: RemoteScreenAdditionalData? = nil
            ) {
                self.url = url
                self.fallback = fallback
                self.additionalData = additionalData
            }
        }
    }

    // MARK: State

    public var state: State {
        didSet { didChangeState() }
    }
    
    private(set) var screen: Screen?

    public enum State {
        case loading
        case success
        case failure(Error)
        case rendered

        public enum Error {
            case remoteScreen(Request.Error)
            case action(Swift.Error)
            case lazyLoad(Request.Error)
            case submitForm(Request.Error)
            case declarativeText
        }
    }

    // MARK: Dependencies

    var dependencies: Dependencies

    public typealias Dependencies =
        DependencyActionExecutor
        & DependencyRepository
        & DependencyAnalyticsExecutor
        & RenderableDependencies
        & DependencyComponentDecoding

    // MARK: Delegate and Observer

    public weak var delegate: BeagleScreenDelegate?

    public weak var stateObserver: BeagleScreenStateObserver? {
        didSet { stateObserver?.didChangeState(state) }
    }

    // MARK: Init

    public init(
        screenType: ScreenType,
        dependencies: Dependencies = Beagle.dependencies,
        delegate: BeagleScreenDelegate? = nil
    ) {
        self.screenType = screenType
        self.dependencies = dependencies
        self.delegate = delegate

        switch screenType {
        case .declarative(let screen):
            self.screen = screen
            state = .success
        case .declarativeText(let text):
            state = .loading
            self.tryToLoadScreenFromText(text)
        case .remote(let remote):
            state = .loading
            loadRemoteScreen(remote)
        }
    }

    public func reloadScreen(with screenType: ScreenType) {
        state = .loading
        self.screenType = screenType
        switch screenType {
        case .declarative(let screen):
            self.screen = screen
            state = .success
        case .declarativeText(let text):
            state = .loading
            self.tryToLoadScreenFromText(text)
        case .remote(let remote):
            state = .loading
            loadRemoteScreen(remote)
        }
    }

    // MARK: Core

    func sendScreenAnalyticsEvent(_ eventType: ScreenAnalyticsEvents) {
        guard let event = screenAnalyticsEvent else { return }
        switch eventType {
        case .screenAppeared:
            dependencies.analytics?.trackEventOnScreenAppeared(event)
        case .screenDisapeared:
            dependencies.analytics?.trackEventOnScreenDisappeared(event)
        }
    }
    
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

        guard let screen = component as? ScreenComponent else {
            return nil
        }

        return Screen(child: screen)
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
    
    func didRenderComponent() {
        state = .rendered
    }

    func handleError(_ error: State.Error) {
        delegate?.beagleScreen(viewModel: self, didFailToLoadWithError: error)
    }
    
    // MARK: - Private

    private func didChangeState() {
        stateObserver?.didChangeState(state)

        if case .failure(let error) = state {
            handleError(error)
        }
    }
    
    private func handleRemoteScreenSuccess(_ component: ServerDrivenComponent) {
        screen = component.toScreen()
        state = .success
    }
    
    private func handleRemoteScreenFailure(_ error: Request.Error) {
        if case let .remote(remote) = self.screenType, let screen = remote.fallback {
            self.screen = screen
        }
        self.state = .failure(.remoteScreen(error))
    }
}

// MARK: - Delegate and Observer

public protocol BeagleScreenDelegate: AnyObject {
    typealias ViewModel = BeagleScreenViewModel

    func beagleScreen(
        viewModel: ViewModel,
        didFailToLoadWithError error: ViewModel.State.Error
    )
}

public protocol BeagleScreenStateObserver: AnyObject {
    typealias ViewModel = BeagleScreenViewModel

    func didChangeState(_ state: ViewModel.State)
}
