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

private let defaultLivePreviewHost = "ws://localhost:9721"
private let defaultReconnectionInterval: TimeInterval = 3.0

public struct BeaglePreviewConfig {

    public var host: String
    public var reconnectionInterval: TimeInterval

    public static func load(from fileName: String) throws -> BeaglePreviewConfig {
        return buildDefaultConfig()
    }

    public static func buildDefaultConfig() -> BeaglePreviewConfig {
        return BeaglePreviewConfig(
            host: defaultLivePreviewHost,
            reconnectionInterval: defaultReconnectionInterval
        )
    }

    public init(host: String, reconnectionInterval: TimeInterval) {
        self.host = host
        self.reconnectionInterval = reconnectionInterval
    }
}
