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

public protocol Navigate: Action {}

extension Navigate {
    func newPath() -> Route.NewPath? {
        switch self {
        case let action as ResetApplication:
            return action.route.path
        case let action as ResetStack:
            return action.route.path
        case let action as PushStack:
            return action.route.path
        case let action as PushView:
            return action.route.path
        default:
            return nil
        }
    }
}

public enum Route {
    public struct NewPath {
        public let route: String
        public let shouldPrefetch: Bool
        public let fallback: Screen?

        public init(route: String, shouldPrefetch: Bool = false, fallback: Screen? = nil) {
            self.route = route
            self.shouldPrefetch = shouldPrefetch
            self.fallback = fallback
        }
    }
    
    case remote(NewPath)
    case declarative(Screen)
    
    var path: NewPath? {
        switch self {
        case .remote(let path):
            return path
        case .declarative:
            return nil
        }
    }
}

public struct OpenExternalURL: Navigate {
    let url: String

    public init(
        _ url: String
    ) {
        self.url = url
    }
}

public struct OpenNativeRoute: Navigate {
    let route: String
    var data: [String: String]?
    var shouldResetApplication: Bool = false

    public init(
        route: String,
        data: [String: String]? = nil,
        shouldResetApplication: Bool = false
    ) {
        self.route = route
        self.data = data
        self.shouldResetApplication = shouldResetApplication
    }
}

public struct ResetApplication: Navigate {
    let route: Route

    public init(
        _ route: Route
    ) {
        self.route = route
    }
}

public struct ResetStack: Navigate {
    let route: Route

    public init(
        _ route: Route
    ) {
        self.route = route
    }
}

public struct PushStack: Navigate {
    let route: Route

    public init(
        _ route: Route
    ) {
        self.route = route
    }
}

public struct PopStack: Navigate {}

public struct PushView: Navigate {
    let route: Route

    public init(
        _ route: Route
    ) {
        self.route = route
    }
}

public struct PopView: Navigate {}

public struct PopToView: Navigate {
    let route: String

    public init(
        _ route: String
    ) {
        self.route = route
    }
}

extension Route: Decodable {
    
    enum CodingKeys: CodingKey {
        case screen
        case route
        case shouldPrefetch
        case fallback
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        if let screen = try? container.decode(ScreenComponent.self, forKey: .screen) {
            self = Route.declarative(screen.toScreen())
        } else {
            let route = try container.decode(String.self, forKey: .route)
            let shouldPrefetch = try container.decodeIfPresent(Bool.self, forKey: .screen)
            let fallback = try container.decodeIfPresent(ScreenComponent.self, forKey: .screen)
            self = Route.remote(Route.NewPath(route: route, shouldPrefetch: shouldPrefetch ?? false, fallback: fallback?.toScreen()))
        }
    }
}
