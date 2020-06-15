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

import BeagleSchema
import BeagleUI

// MARK: DSCollection Decodable
extension DSCollection {

    enum CodingKeys: String, CodingKey {
        case dataSource
    }

    internal init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        dataSource = try container.decode(DSCollectionDataSource.self, forKey: .dataSource)
        widgetProperties = try WidgetProperties(from: decoder)
    }
}

// MARK: DemoTextField Decodable
extension DemoTextField {

    enum CodingKeys: String, CodingKey {
        case placeholder
    }

    internal init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        placeholder = try container.decode(String.self, forKey: .placeholder)
        widgetProperties = try WidgetProperties(from: decoder)
    }
}

// MARK: OtherComponent Decodable
extension OtherComponent {

    enum CodingKeys: String, CodingKey {
        case widgetProperties
    }

    internal init(from decoder: Decoder) throws {
        
        widgetProperties = try WidgetProperties(from: decoder)
    }
}
