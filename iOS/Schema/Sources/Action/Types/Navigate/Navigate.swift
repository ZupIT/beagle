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
    case openNativeRoute(OpenNativeRoute)

    case resetApplication(Route)
    case resetStack(Route)
        
    case pushStack(Route)
    case popStack

    case pushView(Route)
    case popView
    case popToView(String)
    
    public struct OpenNativeRoute {
        public let route: String
        public let data: [String: String]?
        public let shouldResetApplication: Bool

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
    
    case remote(NewPath)
    case declarative(Screen)
    
}

extension Route {
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
}

// MARK: Decodable

extension Navigate.OpenNativeRoute: Decodable {}

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
            self = .openNativeRoute(try .init(from: decoder))
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
            self = .remote(newPath)
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
