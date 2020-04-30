// Generated using Sourcery 0.18.0 — https://github.com/krzysztofzablocki/Sourcery
// DO NOT EDIT

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
// MARK: Button Decodable
extension Button {

    enum CodingKeys: String, CodingKey {
        case text
        case style
        case action
        case clickAnalyticsEvent
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        text = try container.decode( String.self, forKey: .text)
        style = try container.decodeIfPresent( String.self, forKey: .style)
        action = try container.decodeIfPresent( forKey: .action)
        clickAnalyticsEvent = try container.decodeIfPresent( AnalyticsClick.self, forKey: .clickAnalyticsEvent)
        widgetProperties = try WidgetProperties(from: decoder)
    }
    
}
// MARK: Container Decodable
extension Container {

    enum CodingKeys: String, CodingKey {
        case children
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        children = try container.decode( forKey: .children)
        widgetProperties = try WidgetProperties(from: decoder)
    }
    
}
// MARK: Image Decodable
extension Image {

    enum CodingKeys: String, CodingKey {
        case name
        case contentMode
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        name = try container.decode( String.self, forKey: .name)
        contentMode = try container.decodeIfPresent( ImageContentMode.self, forKey: .contentMode)
        widgetProperties = try WidgetProperties(from: decoder)
    }
    
}
// MARK: NetworkImage Decodable
extension NetworkImage {

    enum CodingKeys: String, CodingKey {
        case path
        case contentMode
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        path = try container.decode( String.self, forKey: .path)
        contentMode = try container.decodeIfPresent( ImageContentMode.self, forKey: .contentMode)
        widgetProperties = try WidgetProperties(from: decoder)
    }
    
}
// MARK: Text Decodable
extension Text {

    enum CodingKeys: String, CodingKey {
        case text
        case style
        case alignment
        case textColor
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        text = try container.decode( String.self, forKey: .text)
        style = try container.decodeIfPresent( String.self, forKey: .style)
        alignment = try container.decodeIfPresent( Alignment.self, forKey: .alignment)
        textColor = try container.decodeIfPresent( String.self, forKey: .textColor)
        widgetProperties = try WidgetProperties(from: decoder)
    }
    
}
