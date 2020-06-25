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

public enum Navigate: RawAction {
    
    case openExternalURL(String)
    case openNativeRoute(String, data: [String: String]? = nil, shouldResetApplication: Bool = false)

    case resetApplication(Route)
    case resetStack(Route)
        
    case pushStack(Route)
    case popStack

    case pushView(Route)
    case popView
    case popToView(String)
    
    public var newPath: Route.NewPath? {
        switch self {
        case let .resetApplication(route),
             let .resetStack(route),
             let .pushStack(route),
             let .pushView(route):
            return route.path
        default:
            return nil
        }
    }
    
    public struct DeepLink {
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
}

public enum Route {
    public struct NewPath {
        public let url: String
        public let shouldPrefetch: Bool
        public let fallback: Screen?

        public init(url: String, shouldPrefetch: Bool = false, fallback: Screen? = nil) {
            self.url = url
            self.shouldPrefetch = shouldPrefetch
            self.fallback = fallback
        }
    }
    
    case remote(String, shouldPrefetch: Bool = false, fallback: Screen? = nil)
    case declarative(Screen)
    
    var path: NewPath? {
        switch self {
        case let .remote(url, shouldPrefetch, fallback):
            return NewPath(url: url, shouldPrefetch: shouldPrefetch, fallback: fallback)
        case .declarative:
            return nil
        }
    }
}

// MARK: Decodable

extension Navigate.DeepLink: Decodable {}

extension Navigate: Decodable {
    
    enum CodingKeys: CodingKey {
        case _beagleAction_
        case route
        case url
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let type = try container.decode(String.self, forKey: ._beagleAction_)
        switch type.lowercased() {
        case "beagle:openexternalurl":
            self = .openExternalURL(try container.decode(String.self, forKey: .url))
        case "beagle:opennativeroute":
            let deepLink: Navigate.DeepLink = try .init(from: decoder)
            self = .openNativeRoute(deepLink.route,
                                    data: deepLink.data,
                                    shouldResetApplication: deepLink.shouldResetApplication)
        case "beagle:resetapplication":
            self = .resetApplication(try container.decode(Route.self, forKey: .route))
        case "beagle:resetstack":
            self = .resetStack(try container.decode(Route.self, forKey: .route))
        case "beagle:pushstack":
            self = .pushStack(try container.decode(Route.self, forKey: .route))
        case "beagle:popstack":
            self = .popStack
        case "beagle:pushview":
            self = .pushView(try container.decode(Route.self, forKey: .route))
        case "beagle:popview":
            self = .popView
        case "beagle:poptoview":
            self = .popToView(try container.decode(String.self, forKey: .route))
        default:
            throw DecodingError.dataCorruptedError(forKey: ._beagleAction_,
                                                   in: container,
                                                   debugDescription: "Can't decode '\(type)'")
        }
    }
}

extension Route: Decodable {
    
    enum CodingKeys: CodingKey {
        case type
        case path
        case shouldPrefetch
        case screen
        case data
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        if let screen = try? container.decode(ScreenComponent.self, forKey: .screen) {
            self = .declarative(screen.toScreen())
        } else {
            let newPath: Route.NewPath = try .init(from: decoder)
            self = .remote(newPath.url, shouldPrefetch: newPath.shouldPrefetch, fallback: newPath.fallback)
        }
    }
}

extension Route.NewPath: Decodable {
    
    enum CodingKeys: CodingKey {
        case url
        case shouldPrefetch
        case fallback
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.url = try container.decode(String.self, forKey: .url)
        self.shouldPrefetch = try container.decodeIfPresent(Bool.self, forKey: .shouldPrefetch) ?? false
        self.fallback = try container.decodeIfPresent(ScreenComponent.self, forKey: .fallback)?.toScreen()
    }
}
