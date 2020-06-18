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
        let rawVerySpecificAction: Action? = try container.decode( forKey: .verySpecificAction)
        if let aux = rawVerySpecificAction as? SpecificActionFromContainer { 
             verySpecificAction = aux
        } else { 
            throw ComponentDecodingError.couldNotCastToType("SpecificActionFromContainer")
        }
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
        case firstTextContainer
        case secondTextContainer
        case rawChild
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        let rawFirstTextContainer: ServerDrivenComponent? = try container.decode( forKey: .firstTextContainer)
        if let aux = rawFirstTextContainer as? TextComponents { 
             firstTextContainer = aux
        } else { 
            throw ComponentDecodingError.couldNotCastToType("TextComponents")
        }
        let rawSecondTextContainer: ServerDrivenComponent? = try container.decode( forKey: .secondTextContainer)
        if let aux = rawSecondTextContainer as? TextComponents { 
             secondTextContainer = aux
        } else { 
            throw ComponentDecodingError.couldNotCastToType("TextComponents")
        }
        rawChild = try container.decode( forKey: .rawChild)
    }
}

// MARK: TextContainer Decodable
extension TextContainer {

    enum CodingKeys: String, CodingKey {
        case childrenOfTextContainer
        case headerOfTextContainer
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        let rawChildrenOfTextContainer: ServerDrivenComponent? = try container.decode( forKey: .childrenOfTextContainer)
        if let aux = rawChildrenOfTextContainer as? [TextComponents] { 
             childrenOfTextContainer = aux
        } else { 
            throw ComponentDecodingError.couldNotCastToType("[TextComponents]")
        }
        let rawHeaderOfTextContainer: ServerDrivenComponent? = try container.decode( forKey: .headerOfTextContainer)
        if let aux = rawHeaderOfTextContainer as? TextComponentHeader { 
             headerOfTextContainer = aux
        } else { 
            throw ComponentDecodingError.couldNotCastToType("TextComponentHeader")
        }
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

        let rawChidrenOfTextContainer: ServerDrivenComponent? = try container.decodeIfPresent( forKey: .chidrenOfTextContainer)
        chidrenOfTextContainer = rawChidrenOfTextContainer as? TextComponents
        action = try container.decode( forKey: .action)
    }
}
