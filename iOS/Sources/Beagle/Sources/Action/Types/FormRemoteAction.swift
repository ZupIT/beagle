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

public struct FormRemoteAction: Action, AutoInitiable {
    
    public let path: String
    public let method: Method
    public let analytics: ActionAnalyticsConfig?

    public enum Method: String, Codable, CaseIterable {
        case get = "GET"
        case post = "POST"
        case put = "PUT"
        case delete = "DELETE"
    }

// sourcery:inline:auto:FormRemoteAction.Init
    public init(
        path: String,
        method: Method,
        analytics: ActionAnalyticsConfig? = nil
    ) {
        self.path = path
        self.method = method
        self.analytics = analytics
    }
// sourcery:end
    
    public func execute(controller: BeagleController, origin: UIView) {
        // Intentionally unimplemented...
        // This Action is converted to another
    }
}
