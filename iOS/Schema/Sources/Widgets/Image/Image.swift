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

public struct Image: RawWidget, AutoDecodable {

    // MARK: - Public Properties
    public let path: PathType
    public let contentMode: ImageContentMode?
    public var widgetProperties: WidgetProperties
    
    public init(
        _ path: PathType,
        contentMode: ImageContentMode? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.path = path
        self.contentMode = contentMode
        self.widgetProperties = widgetProperties
    }
    
    indirect public enum PathType: Decodable {
        case remote(Remote)
        case local(Local)

        enum CodingKeys: String, CodingKey {
            case type = "_beagleImagePath_"
        }
        
        public init(from decoder: Decoder) throws {
            let container = try decoder.container(keyedBy: CodingKeys.self)

            let type = try container.decode(String.self, forKey: .type)
            switch type {
            case "local":
                self = .local(try Local(from: decoder))
            case "remote":
                self = .remote(try Remote(from: decoder))
            default:
                throw DecodingError.dataCorruptedError(forKey: .type, in: container, debugDescription: "Invalid image type")
            }
        }
    }
}

public extension Image {

    struct Local: Decodable, ExpressibleByStringLiteral {

        public let mobileId: String

        public init(mobileId: String) {
            self.mobileId = mobileId
        }

        public init(stringLiteral value: StringLiteralType) {
            self.mobileId = value
        }

        enum CodingKeys: String, CodingKey {
            case mobileId
        }

        public init(from decoder: Decoder) throws {
            let container = try decoder.container(keyedBy: CodingKeys.self)
            mobileId = try container.decode(String.self, forKey: .mobileId)
        }
    }

    struct Remote: Decodable {
        public let url: String
        public let placeholder: Local?

        public init(url: String, placeholder: Local? = nil) {
            self.url = url
            self.placeholder = placeholder
        }

        enum CodingKeys: String, CodingKey {
            case url
            case placeholder
        }

        public init(from decoder: Decoder) throws {
            let container = try decoder.container(keyedBy: CodingKeys.self)

            url = try container.decode(String.self, forKey: .url)
            placeholder = try container.decodeIfPresent(Local.self, forKey: .placeholder)
        }
    }
}
