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

// MARK: CustomActionableContainer Decodable
extension CustomActionableContainer {

    enum CodingKeys: String, CodingKey {
        case child
        case verySpecificAction
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        child = try container.decode( forKey: .child)
        verySpecificAction = try container.decode( forKey: .verySpecificAction)
    }
}

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

// MARK: SingleCustomActionableContainer Decodable
extension SingleCustomActionableContainer {

    enum CodingKeys: String, CodingKey {
        case child
        case action
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        child = try container.decode( forKey: .child)
        action = try container.decode( forKey: .action)
    }
}

// MARK: SingleTextContainer Decodable
extension SingleTextContainer {

    enum CodingKeys: String, CodingKey {
        case child
        case rawChild
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        child = try container.decode( forKey: .child)
        rawChild = try container.decode( forKey: .rawChild)
    }
}

// MARK: TextContainer Decodable
extension TextContainer {

    enum CodingKeys: String, CodingKey {
        case chidren
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        chidren = try container.decode([TextComponents].self, forKey: .chidren)
    }
}

// MARK: TextContainerWithAction Decodable
extension TextContainerWithAction {

    enum CodingKeys: String, CodingKey {
        case chidrenOfTextContainer
        case action
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        chidrenOfTextContainer = try container.decode([TextComponents].self, forKey: .chidrenOfTextContainer)
        action = try container.decode( forKey: .action)
    }
}
