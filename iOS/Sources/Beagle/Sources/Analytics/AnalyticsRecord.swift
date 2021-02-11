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

public struct AnalyticsRecord {

    public let platform = "ios"
    public let screen: String?
    public var timestamp: Double

    public var type: RecordType

    public enum RecordType {
        case screen
        case action(Action)
    }
    
    public init(type: RecordType, screen: String?, timestamp: Double) {
        self.type = type
        self.screen = screen
        self.timestamp = timestamp
    }

    public struct Action {
        public var beagleAction: String
        public var event: String?
        public var screen: String?
        public var component: Component
        public var attributes: DynamicDictionary
        public var additionalEntries: DynamicDictionary

        public struct Component {
            public var id: String?
            public var type: String?
            public var position: Position

            public struct Position { let x, y: Double }
        }
    }
}

// MARK: Dictionary

extension AnalyticsRecord {

    public func toDictionary() -> DynamicDictionary {
        var dict = DynamicDictionary()

        switch type {
        case .action(let action):
            dict["type"] = "action"
            let object = transformToDynamicObject(action).asDictionary()
            dict.merge(object, uniquingKeysWith: { $1 })

        case .screen:
            dict["type"] = "screen"
        }

        screen.map { dict["screen"] = .string($0) }
        dict["platform"] = .string(platform)
        dict["timestamp"] = .double(timestamp)
        return dict
    }
}

// MARK: Encodable

extension AnalyticsRecord: Encodable {

    public func encode(to encoder: Encoder) throws {
        let dict = self.toDictionary()
        try dict.encode(to: encoder)
    }
}

// MARK: Equatable

extension AnalyticsRecord: Equatable {}
extension AnalyticsRecord.RecordType: Equatable {}
extension AnalyticsRecord.Action: Equatable {}
extension AnalyticsRecord.Action.Component: Equatable {}
extension AnalyticsRecord.Action.Component.Position: Equatable {}
