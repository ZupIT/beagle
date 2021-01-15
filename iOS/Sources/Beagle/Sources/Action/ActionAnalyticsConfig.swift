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

/// Can be used in an `Action` to override or extend the global `AnalyticsConfig`
public enum ActionAnalyticsConfig {

    /// this action will never be recorded (overrides global)
    case disabled

    /// action that will always be recorded (overrides global)
    case enabled(Attributes? = nil)

    public struct Attributes: AutoInitiable {

        /// all action's properties that you want to be recorded, plus those already configured globally
        public var attributes: [String]?

        /// any additional entry you want recorded
        public var additionalEntries: [String: DynamicObject]?

        // sourcery:inline:auto:ActionAnalyticsConfig.Attributes.Init
            public init(
                attributes: [String]? = nil,
                additionalEntries: [String: DynamicObject]? = nil
            ) {
                self.attributes = attributes
                self.additionalEntries = additionalEntries
            }
        // sourcery:end
    }
}

// MARK: - Codable

extension ActionAnalyticsConfig: Codable, Equatable {

    enum CodingKeys: CodingKey {
        case attributes, additionalEntries
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.singleValueContainer()
        do {
            let isEnabled = try container.decode(Bool.self)
            self = isEnabled ? .enabled() : .disabled
        } catch {
            self = .enabled(try container.decode(Attributes.self))
        }
    }

    public func encode(to encoder: Encoder) throws {
        var container = encoder.singleValueContainer()
        switch self {
        case .disabled: try container.encode(false)
        case .enabled(nil): try container.encode(true)
        case .enabled(let attributes?): try container.encode(attributes)
        }
    }
}

extension ActionAnalyticsConfig.Attributes: Codable, Equatable {}
