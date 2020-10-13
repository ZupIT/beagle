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

/// Handles screens navigations actions of the application.
public enum Navigate: RawAction {
    
    /// Opens up an available browser on the device and navigates to a specified URL as String.
    case openExternalURL(String)
    
  /// Opens a screen that is defined completely local in your app (does not depend on Beagle) which will be retrieved using `DeeplinkScreenManager`.
    case openNativeRoute(OpenNativeRoute)

    /// Resets the application's root navigation stack with a new navigation stack that has `Route` as the first view
    case resetApplication(Route)
    
    /// Resets the views stack to create a new flow with the passed route.
    case resetStack(Route)
    
    /// Presents a new screen that comes from a specified route starting a new flow.
    /// You can specify a controllerId, describing the id of navigation controller used for the new flow.
    case pushStack(Route, controllerId: String? = nil)
    
    /// Unstacks the current view stack.
    case popStack

    /// Opens a new screen for the given route and stacks that at the top of the hierarchy.
    case pushView(Route)
    
    /// Dismisses the current view.
    case popView
    
    /// Returns the stack of screens in the application flow for a given screen in a route specified as String.
    case popToView(String)
    
    public struct OpenNativeRoute {
        
        /// Deeplink identifier.
        public let route: String
        
        /// Data that could be passed between screens.
        public let data: [String: String]?
        
        /// Allows customization of the behavior of restarting the application view stack.
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

/// Defines a navigation route type which can be `remote` or `declarative`.
public enum Route {
    
    /// Navigates to a remote content on a specified path.
    case remote(NewPath)
    
    /// Navigates to a specified screen.
    case declarative(Screen)
}

extension Route {
    
    /// Constructs a new path to a remote screen.
    public struct NewPath {
        
        /// Contains the navigation endpoint.
        public let url: Expression<String>
        
        /// Changes _when_ this screen is requested.
        ///
        /// - If __false__, Beagle will only request this screen when the Navigate action gets triggered (e.g: user taps a button).
        /// - If __true__, Beagle will trigger the request as soon as it renders the component that have
        /// this action. (e.g: when a button appears on the screen it will trigger)
        public let shouldPrefetch: Bool
        
        /// A screen that should be rendered in case of request fail.
        public let fallback: Screen?

        /// Constructs a new path to a remote screen.
        ///
        /// - Parameters:
        ///   - url: Contains the navigation endpoint. Since its a _ExpressibleString_ type you can pass a Expression<String> or a regular String.
        ///   - shouldPrefetch: Changes _when_ this screen is requested.
        ///   - fallback: A screen that should be rendered in case of request fail.
        public init(url: StringOrExpression, shouldPrefetch: Bool = false, fallback: Screen? = nil) {
            self.url = "\(url)"
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
        case controllerId
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
            self = .pushStack(try container.decode(Route.self, forKey: .route),
                              controllerId: try container.decodeIfPresent(String.self, forKey: .controllerId))
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
        self.url = try container.decode(Expression<String>.self, forKey: .url)
        self.shouldPrefetch = try container.decodeIfPresent(Bool.self, forKey: .shouldPrefetch) ?? false
        self.fallback = try container.decodeIfPresent(ScreenComponent.self, forKey: .fallback)?.toScreen()
    }
}
