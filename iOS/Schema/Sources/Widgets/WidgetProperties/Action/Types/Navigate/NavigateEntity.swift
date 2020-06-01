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

import Foundation

//TODO: Check map to ui model here
//public struct NavigateEntity: AutoInitiable {
//    let type: NavigationType
//    let path: String?
//    let shouldPrefetch: Bool?
//    let screen: ScreenComponent?
//    let data: [String: String]?
//
//// sourcery:inline:auto:NavigateEntity.Init
//    public init(
//        type: NavigationType,
//        path: String? = nil,
//        shouldPrefetch: Bool? = nil,
//        screen: ScreenComponent? = nil,
//        data: [String: String]? = nil
//    ) {
//        self.type = type
//        self.path = path
//        self.shouldPrefetch = shouldPrefetch
//        self.screen = screen
//        self.data = data
//    }
//// sourcery:end
//}
//
//extension NavigateEntity {
//    public enum NavigationType: String, Decodable, CaseIterable {
//        case openDeepLink = "OPEN_DEEP_LINK"
//        case swapView = "SWAP_VIEW"
//        case addView = "ADD_VIEW"
//        case finishView = "FINISH_VIEW"
//        case popView = "POP_VIEW"
//        case popToView = "POP_TO_VIEW"
//        case presentView = "PRESENT_VIEW"
//    }
//
//    public enum Destination {
//        case declarative(Screen)
//        case remote(Navigate.NewPath)
//    }
//
//    public struct Error: Swift.Error {
//        let reason: String
//    }
//
//    public func mapToUIModel() throws -> Navigate {
//        switch type {
//        case .popToView:
//            let path = try usePath()
//            return .popToView(path)
//
//        case .openDeepLink:
//            let path = try usePath()
//            return .openDeepLink(.init(path: path, data: data))
//
//        case .swapView:
//            switch try destination() {
//            case .declarative(let screen):
//                return .swapScreen(screen)
//            case .remote(let newPath):
//                return .swapView(newPath)
//            }
//
//        case .addView:
//            switch try destination() {
//            case .declarative(let screen):
//                return .addScreen(screen)
//            case .remote(let newPath):
//                return .addView(newPath)
//            }
//
//        case .presentView:
//            switch try destination() {
//            case .declarative(let screen):
//                return .presentScreen(screen)
//            case .remote(let newPath):
//                return .presentView(newPath)
//            }
//
//        case .finishView:
//            return .finishView
//
//        case .popView:
//            return .popView
//        }
//    }
//
//    public func usePath() throws -> String {
//        guard let path = self.path else {
//            throw Error(reason: "Error: Navigate of `type` \(type), should have property `path`")
//        }
//        return path
//    }
//
//    public func destination() throws -> Destination {
//        let screen = self.screen?.toScreen()
//        if let screen = screen, path == nil {
//            return .declarative(screen)
//        }
//        if let path = self.path {
//            return .remote(.init(
//                path: path,
//                shouldPrefetch: shouldPrefetch ?? false,
//                fallback: screen))
//        }
//        throw Error(reason: "Error: Navigate of `type` \(type), should have property `path` or `screen`")
//    }
//}
