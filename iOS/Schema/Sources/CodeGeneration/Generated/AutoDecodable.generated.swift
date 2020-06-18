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
        case styleId
        case action
        case clickAnalyticsEvent
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        text = try container.decode(String.self, forKey: .text)
        styleId = try container.decodeIfPresent(String.self, forKey: .styleId)
        action = try container.decodeIfPresent( forKey: .action)
        clickAnalyticsEvent = try container.decodeIfPresent(AnalyticsClick.self, forKey: .clickAnalyticsEvent)
        widgetProperties = try WidgetProperties(from: decoder)
    }
}

// MARK: Container Decodable
extension Container {

    enum CodingKeys: String, CodingKey {
        case children
        case _context_
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        children = try container.decode( forKey: .children)
        widgetProperties = try WidgetProperties(from: decoder)
        _context_ = try container.decodeIfPresent(Context.self, forKey: ._context_)
    }
}

// MARK: Form Decodable
extension Form {

    enum CodingKeys: String, CodingKey {
        case action
        case child
        case group
        case additionalData
        case shouldStoreFields
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        action = try container.decode( forKey: .action)
        child = try container.decode( forKey: .child)
        group = try container.decodeIfPresent(String.self, forKey: .group)
        additionalData = try container.decodeIfPresent([String: String].self, forKey: .additionalData)
        shouldStoreFields = try container.decode(Bool.self, forKey: .shouldStoreFields)
    }
}

// MARK: FormInput Decodable
extension FormInput {

    enum CodingKeys: String, CodingKey {
        case name
        case required
        case validator
        case errorMessage
        case child
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        name = try container.decode(String.self, forKey: .name)
        required = try container.decodeIfPresent(Bool.self, forKey: .required)
        validator = try container.decodeIfPresent(String.self, forKey: .validator)
        errorMessage = try container.decodeIfPresent(String.self, forKey: .errorMessage)
        child = try container.decode( forKey: .child)
    }
}

// MARK: FormSubmit Decodable
extension FormSubmit {

    enum CodingKeys: String, CodingKey {
        case child
        case enabled
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        child = try container.decode( forKey: .child)
        enabled = try container.decodeIfPresent(Bool.self, forKey: .enabled)
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

        name = try container.decode(String.self, forKey: .name)
        contentMode = try container.decodeIfPresent(ImageContentMode.self, forKey: .contentMode)
        widgetProperties = try WidgetProperties(from: decoder)
    }
}

// MARK: LazyComponent Decodable
extension LazyComponent {

    enum CodingKeys: String, CodingKey {
        case path
        case initialState
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        path = try container.decode(String.self, forKey: .path)
        initialState = try container.decode( forKey: .initialState)
    }
}

// MARK: ListView Decodable
extension ListView {

    enum CodingKeys: String, CodingKey {
        case children
        case direction
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        children = try container.decode( forKey: .children)
        direction = try container.decode(Direction.self, forKey: .direction)
    }
}

// MARK: NavigationBarItem Decodable
extension NavigationBarItem {

    enum CodingKeys: String, CodingKey {
        case id
        case image
        case text
        case action
        case accessibility
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        id = try container.decodeIfPresent(String.self, forKey: .id)
        image = try container.decodeIfPresent(String.self, forKey: .image)
        text = try container.decode(String.self, forKey: .text)
        action = try container.decode( forKey: .action)
        accessibility = try container.decodeIfPresent(Accessibility.self, forKey: .accessibility)
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

        path = try container.decode(String.self, forKey: .path)
        contentMode = try container.decodeIfPresent(ImageContentMode.self, forKey: .contentMode)
        widgetProperties = try WidgetProperties(from: decoder)
    }
}

// MARK: ScreenComponent Decodable
extension ScreenComponent {

    enum CodingKeys: String, CodingKey {
        case identifier
        case style
        case safeArea
        case navigationBar
        case screenAnalyticsEvent
        case child
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        identifier = try container.decodeIfPresent(String.self, forKey: .identifier)
        style = try container.decodeIfPresent(Style.self, forKey: .style)
        safeArea = try container.decodeIfPresent(SafeArea.self, forKey: .safeArea)
        navigationBar = try container.decodeIfPresent(NavigationBar.self, forKey: .navigationBar)
        screenAnalyticsEvent = try container.decodeIfPresent(AnalyticsScreen.self, forKey: .screenAnalyticsEvent)
        child = try container.decode( forKey: .child)
    }
}

// MARK: ScrollView Decodable
extension ScrollView {

    enum CodingKeys: String, CodingKey {
        case children
        case scrollDirection
        case scrollBarEnabled
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        children = try container.decode( forKey: .children)
        scrollDirection = try container.decodeIfPresent(ScrollAxis.self, forKey: .scrollDirection)
        scrollBarEnabled = try container.decodeIfPresent(Bool.self, forKey: .scrollBarEnabled)
    }
}

// MARK: TabItem Decodable
extension TabItem {

    enum CodingKeys: String, CodingKey {
        case icon
        case title
        case child
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        icon = try container.decodeIfPresent(String.self, forKey: .icon)
        title = try container.decodeIfPresent(String.self, forKey: .title)
        child = try container.decode( forKey: .child)
    }
}

// MARK: Text Decodable
extension Text {

    enum CodingKeys: String, CodingKey {
        case text
        case styleId
        case alignment
        case textColor
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        text = try container.decode(Expression<String>.self, forKey: .text)
        styleId = try container.decodeIfPresent(String.self, forKey: .styleId)
        alignment = try container.decodeIfPresent(Alignment.self, forKey: .alignment)
        textColor = try container.decodeIfPresent(String.self, forKey: .textColor)
        widgetProperties = try WidgetProperties(from: decoder)
    }
}

// MARK: Touchable Decodable
extension Touchable {

    enum CodingKeys: String, CodingKey {
        case action
        case clickAnalyticsEvent
        case child
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        action = try container.decode( forKey: .action)
        clickAnalyticsEvent = try container.decodeIfPresent(AnalyticsClick.self, forKey: .clickAnalyticsEvent)
        child = try container.decode( forKey: .child)
    }
}

// MARK: WebView Decodable
extension WebView {

    enum CodingKeys: String, CodingKey {
        case url
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        url = try container.decode(String.self, forKey: .url)
        widgetProperties = try WidgetProperties(from: decoder)
    }
}

// MARK: WidgetProperties Decodable
extension WidgetProperties {

    enum CodingKeys: String, CodingKey {
        case id
        case style
        case accessibility
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        id = try container.decodeIfPresent(String.self, forKey: .id)
        style = try container.decodeIfPresent(Style.self, forKey: .style)
        accessibility = try container.decodeIfPresent(Accessibility.self, forKey: .accessibility)
    }
}
