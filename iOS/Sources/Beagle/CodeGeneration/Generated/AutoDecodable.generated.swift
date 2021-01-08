// Generated using Sourcery 1.0.0 — https://github.com/krzysztofzablocki/Sourcery
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

// MARK: AddChildren Decodable
extension AddChildren {

    enum CodingKeys: String, CodingKey {
        case componentId
        case value
        case mode
        case analytics
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        componentId = try container.decode(String.self, forKey: .componentId)
        value = try container.decode(forKey: .value)
        mode = try container.decodeIfPresent(Mode.self, forKey: .mode) ?? .append
        analytics = try container.decodeIfPresent(ActionAnalyticsConfig.self, forKey: .analytics)
    }
}

// MARK: Alert Decodable
extension Alert {

    enum CodingKeys: String, CodingKey {
        case title
        case message
        case onPressOk
        case labelOk
        case analytics
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        title = try container.decodeIfPresent(Expression<String>.self, forKey: .title)
        message = try container.decode(Expression<String>.self, forKey: .message)
        onPressOk = try container.decodeIfPresent(forKey: .onPressOk)
        labelOk = try container.decodeIfPresent(String.self, forKey: .labelOk)
        analytics = try container.decodeIfPresent(ActionAnalyticsConfig.self, forKey: .analytics)
    }
}

// MARK: Button Decodable
extension Button {

    enum CodingKeys: String, CodingKey {
        case text
        case styleId
        case onPress
        case disabled
        case clickAnalyticsEvent
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        text = try container.decode(Expression<String>.self, forKey: .text)
        styleId = try container.decodeIfPresent(String.self, forKey: .styleId)
        onPress = try container.decodeIfPresent(forKey: .onPress)
        disabled = try container.decodeIfPresent(Expression<Bool>.self, forKey: .disabled)
        clickAnalyticsEvent = try container.decodeIfPresent(AnalyticsClick.self, forKey: .clickAnalyticsEvent)
        widgetProperties = try WidgetProperties(from: decoder)
    }
}

// MARK: Condition Decodable
extension Condition {

    enum CodingKeys: String, CodingKey {
        case condition
        case onTrue
        case onFalse
        case analytics
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        condition = try container.decode(Expression<Bool>.self, forKey: .condition)
        onTrue = try container.decodeIfPresent(forKey: .onTrue)
        onFalse = try container.decodeIfPresent(forKey: .onFalse)
        analytics = try container.decodeIfPresent(ActionAnalyticsConfig.self, forKey: .analytics)
    }
}

// MARK: Confirm Decodable
extension Confirm {

    enum CodingKeys: String, CodingKey {
        case title
        case message
        case onPressOk
        case onPressCancel
        case labelOk
        case labelCancel
        case analytics
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        title = try container.decodeIfPresent(Expression<String>.self, forKey: .title)
        message = try container.decode(Expression<String>.self, forKey: .message)
        onPressOk = try container.decodeIfPresent(forKey: .onPressOk)
        onPressCancel = try container.decodeIfPresent(forKey: .onPressCancel)
        labelOk = try container.decodeIfPresent(String.self, forKey: .labelOk)
        labelCancel = try container.decodeIfPresent(String.self, forKey: .labelCancel)
        analytics = try container.decodeIfPresent(ActionAnalyticsConfig.self, forKey: .analytics)
    }
}

// MARK: Container Decodable
extension Container {

    enum CodingKeys: String, CodingKey {
        case children
        case onInit
        case context
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        children = try container.decode(forKey: .children)
        widgetProperties = try WidgetProperties(from: decoder)
        onInit = try container.decodeIfPresent(forKey: .onInit)
        context = try container.decodeIfPresent(Context.self, forKey: .context)
    }
}

// MARK: Context Decodable
extension Context {

    enum CodingKeys: String, CodingKey {
        case id
        case value
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        id = try container.decode(String.self, forKey: .id)
        value = try container.decode(DynamicObject.self, forKey: .value)
    }
}

// MARK: Form Decodable
extension Form {

    enum CodingKeys: String, CodingKey {
        case onSubmit
        case child
        case group
        case additionalData
        case shouldStoreFields
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        onSubmit = try container.decodeIfPresent(forKey: .onSubmit)
        child = try container.decode(forKey: .child)
        group = try container.decodeIfPresent(String.self, forKey: .group)
        additionalData = try container.decodeIfPresent([String: String].self, forKey: .additionalData)
        shouldStoreFields = try container.decodeIfPresent(Bool.self, forKey: .shouldStoreFields) ?? false
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
        child = try container.decode(forKey: .child)
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

        child = try container.decode(forKey: .child)
        enabled = try container.decodeIfPresent(Bool.self, forKey: .enabled)
    }
}

// MARK: Image Decodable
extension Image {

    enum CodingKeys: String, CodingKey {
        case path
        case mode
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        path = try container.decode(Expression<ImagePath>.self, forKey: .path)
        mode = try container.decodeIfPresent(ImageContentMode.self, forKey: .mode)
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
        initialState = try container.decode(forKey: .initialState)
    }
}

// MARK: PageView Decodable
extension PageView {

    enum CodingKeys: String, CodingKey {
        case children
        case pageIndicator
        case context
        case onPageChange
        case currentPage
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        children = try container.decode(forKey: .children)
        let rawPageIndicator: ServerDrivenComponent? = try container.decodeIfPresent(forKey: .pageIndicator)
        pageIndicator = rawPageIndicator as? PageIndicatorComponent
        context = try container.decodeIfPresent(Context.self, forKey: .context)
        onPageChange = try container.decodeIfPresent(forKey: .onPageChange)
        currentPage = try container.decodeIfPresent(Expression<Int>.self, forKey: .currentPage)
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
        case context
    }

    internal init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        identifier = try container.decodeIfPresent(String.self, forKey: .identifier)
        style = try container.decodeIfPresent(Style.self, forKey: .style)
        safeArea = try container.decodeIfPresent(SafeArea.self, forKey: .safeArea)
        navigationBar = try container.decodeIfPresent(NavigationBar.self, forKey: .navigationBar)
        screenAnalyticsEvent = try container.decodeIfPresent(AnalyticsScreen.self, forKey: .screenAnalyticsEvent)
        child = try container.decode(forKey: .child)
        context = try container.decodeIfPresent(Context.self, forKey: .context)
    }
}

// MARK: ScrollView Decodable
extension ScrollView {

    enum CodingKeys: String, CodingKey {
        case children
        case scrollDirection
        case scrollBarEnabled
        case context
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        children = try container.decode(forKey: .children)
        scrollDirection = try container.decodeIfPresent(ScrollAxis.self, forKey: .scrollDirection)
        scrollBarEnabled = try container.decodeIfPresent(Bool.self, forKey: .scrollBarEnabled)
        context = try container.decodeIfPresent(Context.self, forKey: .context)
    }
}

// MARK: SendRequest Decodable
extension SendRequest {

    enum CodingKeys: String, CodingKey {
        case url
        case method
        case data
        case headers
        case onSuccess
        case onError
        case onFinish
        case analytics
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        url = try container.decode(Expression<String>.self, forKey: .url)
        method = try container.decodeIfPresent(Expression<SendRequest.HTTPMethod>.self, forKey: .method)
        data = try container.decodeIfPresent(DynamicObject.self, forKey: .data)
        headers = try container.decodeIfPresent(Expression<[String: String]>.self, forKey: .headers)
        onSuccess = try container.decodeIfPresent(forKey: .onSuccess)
        onError = try container.decodeIfPresent(forKey: .onError)
        onFinish = try container.decodeIfPresent(forKey: .onFinish)
        analytics = try container.decodeIfPresent(ActionAnalyticsConfig.self, forKey: .analytics)
    }
}

// MARK: SimpleForm Decodable
extension SimpleForm {

    enum CodingKeys: String, CodingKey {
        case context
        case onSubmit
        case children
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        context = try container.decodeIfPresent(Context.self, forKey: .context)
        onSubmit = try container.decodeIfPresent(forKey: .onSubmit)
        children = try container.decode(forKey: .children)
        widgetProperties = try WidgetProperties(from: decoder)
    }
}

// MARK: TabBar Decodable
extension TabBar {

    enum CodingKeys: String, CodingKey {
        case items
        case styleId
        case currentTab
        case onTabSelection
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        items = try container.decode([TabBarItem].self, forKey: .items)
        styleId = try container.decodeIfPresent(String.self, forKey: .styleId)
        currentTab = try container.decodeIfPresent(Expression<Int>.self, forKey: .currentTab)
        onTabSelection = try container.decodeIfPresent(forKey: .onTabSelection)
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
        alignment = try container.decodeIfPresent(Expression<Alignment>.self, forKey: .alignment)
        textColor = try container.decodeIfPresent(Expression<String>.self, forKey: .textColor)
        widgetProperties = try WidgetProperties(from: decoder)
    }
}

// MARK: TextInput Decodable
extension TextInput {

    enum CodingKeys: String, CodingKey {
        case value
        case placeholder
        case disabled
        case readOnly
        case type
        case hidden
        case styleId
        case onChange
        case onBlur
        case onFocus
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        value = try container.decodeIfPresent(Expression<String>.self, forKey: .value)
        placeholder = try container.decodeIfPresent(Expression<String>.self, forKey: .placeholder)
        disabled = try container.decodeIfPresent(Expression<Bool>.self, forKey: .disabled)
        readOnly = try container.decodeIfPresent(Expression<Bool>.self, forKey: .readOnly)
        type = try container.decodeIfPresent(Expression<TextInputType>.self, forKey: .type)
        hidden = try container.decodeIfPresent(Expression<Bool>.self, forKey: .hidden)
        styleId = try container.decodeIfPresent(String.self, forKey: .styleId)
        onChange = try container.decodeIfPresent(forKey: .onChange)
        onBlur = try container.decodeIfPresent(forKey: .onBlur)
        onFocus = try container.decodeIfPresent(forKey: .onFocus)
        widgetProperties = try WidgetProperties(from: decoder)
    }
}

// MARK: Touchable Decodable
extension Touchable {

    enum CodingKeys: String, CodingKey {
        case onPress
        case clickAnalyticsEvent
        case child
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        onPress = try container.decode(forKey: .onPress)
        clickAnalyticsEvent = try container.decodeIfPresent(AnalyticsClick.self, forKey: .clickAnalyticsEvent)
        child = try container.decode(forKey: .child)
    }
}

// MARK: WebView Decodable
extension WebView {

    enum CodingKeys: String, CodingKey {
        case url
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        url = try container.decode(Expression<String>.self, forKey: .url)
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
