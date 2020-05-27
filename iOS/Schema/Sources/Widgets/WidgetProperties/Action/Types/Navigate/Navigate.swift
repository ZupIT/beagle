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

/// Action to represent a screen transition
public enum Navigate: Action {
    
    case openDeepLink(DeepLinkNavigation)
    
    case swapScreen(Screen)
    case swapView(NewPath)
    
    case addScreen(Screen)
    case addView(NewPath)
    
    case presentScreen(Screen)
    case presentView(NewPath)

    case finishView
    case popView
    case popToView(Path)

    public typealias Path = String
    public typealias Data = [String: String]

    public struct NewPath {
        public let path: Path
        public let shouldPrefetch: Bool
        public let fallback: Screen?
        
        public init(path: Path, shouldPrefetch: Bool = false, fallback: Screen? = nil) {
            self.path = path
            self.shouldPrefetch = shouldPrefetch
            self.fallback = fallback
        }
    }
    
    public struct DeepLinkNavigation {
        public let path: Path
        public let data: Data?
        public let component: ServerDrivenComponent?

        public init(path: Path, data: Data? = nil, component: ServerDrivenComponent? = nil) {
            self.path = path
            self.data = data
            self.component = component
        }
    }

    public var newPath: NewPath? {
        switch self {
        case .addView(let newPath), .swapView(let newPath), .presentView(let newPath):
            return newPath

        case .swapScreen, .addScreen, .presentScreen, .finishView, .popView, .popToView, .openDeepLink:
            return nil
        }
    }
}

extension Navigate: Decodable {
    enum CodingKeys: String, CodingKey {
        case type
        case path
        case shouldPrefetch
        case screen
        case data
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let type = try container.decode(NavigateEntity.NavigationType.self, forKey: .type)
        let path = try container.decodeIfPresent(String.self, forKey: .path)
        let shouldPrefetch = try container.decodeIfPresent(Bool.self, forKey: .shouldPrefetch)
        let screen = try container.decodeIfPresent(ScreenComponent.self, forKey: .screen)
        let data = try container.decodeIfPresent([String: String].self, forKey: .data)
        self = try NavigateEntity(type: type, path: path, shouldPrefetch: shouldPrefetch, screen: screen, data: data).mapToUIModel()
    }
}

extension Navigate: CustomStringConvertible {

    public var description: String {
        let name: String
        switch self {
        case .openDeepLink(let link): name = "openDeepLink(\(link))"
        case .swapScreen(let screen): name = "swapScreen(\(screen))"
        case .swapView(let path): name = "swapView(\(path))"
        case .addScreen(let screen): name = "addScreen(\(screen))"
        case .addView(let path): name = "addView(\(path))"
        case .presentScreen(let screen): name = "presentScreen(\(screen))"
        case .presentView(let path): name = "presentView(\(path))"
        case .finishView: name = "finishView"
        case .popView: name = "popView"
        case .popToView(let path): name = "popToView(\(path))"
        }

        return "Navigate.\(name)"
    }
}
