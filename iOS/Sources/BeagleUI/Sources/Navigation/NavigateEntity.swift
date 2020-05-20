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

extension Navigate: Decodable {
    enum CodingKeys: String, CodingKey {
        case type
        case path
        case shouldPrefetch
        case screen
        case data
        case shouldResetApplication
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let type = try container.decode(NavigateEntity.NavigationType.self, forKey: .type)
        let path = try container.decodeIfPresent(String.self, forKey: .path)
        let shouldPrefetch = try container.decodeIfPresent(Bool.self, forKey: .shouldPrefetch)
        let screen = try container.decodeIfPresent(ScreenComponent.self, forKey: .screen)
        let data = try container.decodeIfPresent([String: String].self, forKey: .data)
        let shouldResetApplication = try container.decodeIfPresent(Bool.self, forKey: .shouldResetApplication)
        self = try NavigateEntity(
            type: type,
            path: path,
            shouldPrefetch: shouldPrefetch,
            screen: screen,
            data: data,
            shouldResetApplication: shouldResetApplication
        ).mapToUIModel()
    }
}

extension NavigateEntity.Destination {
    var screenType: Navigate.ScreenType {
        switch self {
        case .declarative(let screen):
            return Navigate.ScreenType.declarative(screen)
        case .remote(let path):
            return Navigate.ScreenType.remote(path)
        }
    }
}

struct NavigateEntity {
    let type: NavigationType
    let path: String?
    let shouldPrefetch: Bool?
    let screen: ScreenComponent?
    let data: [String: String]?
    let shouldResetApplication: Bool?

    enum NavigationType: String, Decodable, CaseIterable {
        case openExternalURL    = "OPEN_EXTERNAL_URL"
        case openNativeRoute    = "OPEN_NATIVE_ROUTE"
        case resetApplication   = "RESET_APPLICATION"
        case resetStack         = "RESET_STACK"
        case pushStack          = "PUSH_STACK"
        case popStack           = "POP_STACK"
        case pushView           = "PUSH_VIEW"
        case popView            = "POP_VIEW"
        case popToView          = "POP_TO_VIEW"
    }

    enum Destination {
        case declarative(Screen)
        case remote(Navigate.NewPath)
    }

    struct Error: Swift.Error {
        let reason: String
    }

    func mapToUIModel() throws -> Navigate {
        switch type {
        case .popToView:
            let path = try usePath()
            return .popToView(path)

        case .openExternalURL:
            let path = try usePath()
            return .openExternalURL(path)

        case .openNativeRoute:
            let path = try usePath()
            return .openNativeRoute(.init(path: path, data: data, shouldResetApplication: shouldResetApplication))

        case .resetApplication:
            let screenType = try destination().screenType
            return .resetApplication(screenType)

        case .resetStack:
            let screenType = try destination().screenType
            return .resetStack(screenType)

        case .pushView:
            let screenType = try destination().screenType
            return .pushView(screenType)

        case .pushStack:
            let screenType = try destination().screenType
            return .pushStack(screenType)

        case .popStack:
            return .popStack

        case .popView:
            return .popView
        }
    }

    func usePath() throws -> String {
        guard let path = self.path else {
            throw Error(reason: "Error: Navigate of `type` \(type), should have property `path`")
        }
        return path
    }

    func destination() throws -> Destination {
        let screen = self.screen?.toScreen()
        if let screen = screen, path == nil {
            return .declarative(screen)
        }
        if let path = self.path {
            return .remote(.init(
                path: path,
                shouldPrefetch: shouldPrefetch ?? false,
                fallback: screen))
        }
        throw Error(reason: "Error: Navigate of `type` \(type), should have property `path` or `screen`")
    }
}
