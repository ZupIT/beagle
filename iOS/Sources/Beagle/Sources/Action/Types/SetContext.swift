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

/// The `SetContext` action is responsible for changing the value of a context.
public struct SetContext: AnalyticsAction {
    
    /// Id of the context to be changed.
    public let contextId: String
    
    /// Specific path to be changed inside of an context.
    public let path: Path?
    
    /// New value to be applied in the context.
    public let value: DynamicObject
    
    /// Defines an analytics configuration for this action.
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
