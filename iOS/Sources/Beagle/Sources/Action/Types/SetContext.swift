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

public struct SetContext: Action {
    public let contextId: String
    public let path: Path?
    public let value: DynamicObject
    public let analytics: ActionAnalyticsConfig?

    public init(
        contextId: String,
        path: String? = nil,
        value: DynamicObject,
        analytics: ActionAnalyticsConfig? = nil
    ) {
        self.contextId = contextId
        self.path = path.flatMap { Path(rawValue: $0) }
        self.value = value
        self.analytics = analytics
    }
}

extension SetContext: CustomReflectable {
    public var customMirror: Mirror {
        return Mirror(
            self,
            children: [
                "contextId": contextId,
                "path": path?.rawValue as Any,
                "value": value,
                "analytics": analytics as Any
            ],
            displayStyle: .struct
        )
    }
}
