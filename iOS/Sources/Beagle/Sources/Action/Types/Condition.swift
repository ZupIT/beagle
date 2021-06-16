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

/// Action to resolve condition and call onTrue if return true and onFalse if return is false.
public struct Condition: AnalyticsAction, AutoInitiableAndDecodable {
    
    /// Condition should represents a boolean.
    public let condition: Expression<Bool>
    
    /// Defines the actions triggered if the condition returns true.
    public let onTrue: [Action]?
    
    /// Defines the actions triggered if the condition returns false.
    public let onFalse: [Action]?
    
    /// Defines an analytics configuration for this action.
    public let analytics: ActionAnalyticsConfig?

// sourcery:inline:auto:Condition.Init
    public init(
        condition: Expression<Bool>,
        onTrue: [Action]? = nil,
        onFalse: [Action]? = nil,
        analytics: ActionAnalyticsConfig? = nil
    ) {
        self.condition = condition
        self.onTrue = onTrue
        self.onFalse = onFalse
        self.analytics = analytics
    }
// sourcery:end

}
