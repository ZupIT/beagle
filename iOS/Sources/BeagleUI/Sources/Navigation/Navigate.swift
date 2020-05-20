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

    case openExternalURL(Path)
    case openNativeRoute(DeepLinkNavigation)
    case resetApplication(ScreenType)
    case resetStack(ScreenType)
    case pushStack(ScreenType)
    case popStack
    case pushView(ScreenType)
    case popView
    case popToView(Path)

    public typealias Path = String
    public typealias Data = [String: String]

    public enum ScreenType {

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
        public let shouldResetApplication: Bool

        public init(
            path: Path,
            data: Data? = nil,
            component: ServerDrivenComponent? = nil,
            shouldResetApplication reset: Bool? = false
        ) {
            self.path = path
            self.data = data
            self.component = component
            self.shouldResetApplication = reset ?? false
        }
    }

    var newPath: NewPath? {
        switch self {
        case .resetApplication(let type), .resetStack(let type), .pushStack(let type), .pushView(let type):
            return type.path
        case .openExternalURL, .openNativeRoute, .popStack, .popView, .popToView:
            return nil
        }
    }
}

extension Navigate: CustomStringConvertible {

    public var description: String {
        let name: String
        switch self {
        case .openExternalURL(let path):            name = "openExternalURL(\(path))"
        case .openNativeRoute(let link):            name = "openNativeRoute(\(link))"
        case .resetApplication(let path):           name = "resetApplication(\(path)"
        case .resetStack(let path):                 name = "resetStack(\(path))"
        case .pushStack(let path):                  name = "pushStack(\(path))"
        case .popStack:                             name = "popStack"
        case .pushView(let path):                   name = "pushView(\(path))"
        case .popView:                              name = "popView"
        case .popToView(let path):                  name = "popToView(\(path))"
        }
        return "\(String(describing: type(of: self))).\(name)"
    }
}
