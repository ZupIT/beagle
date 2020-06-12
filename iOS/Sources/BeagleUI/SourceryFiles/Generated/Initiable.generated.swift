// Generated using Sourcery 0.18.0 — https://github.com/krzysztofzablocki/Sourcery
// DO NOT EDIT



    public init(
        category: String,
        label: String? = nil,
        value: String? = nil
    ) {
        self.category = category
        self.label = label
        self.value = value
    }



    public init(
        screenName: String
    ) {
        self.screenName = screenName
    }



    public init(
        backgroundColor: String? = nil,
        cornerRadius: CornerRadius? = nil
    ) {
        self.backgroundColor = backgroundColor
        self.cornerRadius = cornerRadius
    }



    public init(
        text: String,
        style: String? = nil,
        action: Action? = nil,
        clickAnalyticsEvent: AnalyticsClick? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.text = text
        self.style = style
        self.action = action
        self.clickAnalyticsEvent = clickAnalyticsEvent
        self.widgetProperties = widgetProperties
    }



    public init(
        children: [ServerDrivenComponent],
        widgetProperties: WidgetProperties = WidgetProperties(),
        context: Context? = nil
    ) {
        self.children = children
        self.widgetProperties = widgetProperties
        self.context = context
    }



    public init(
        radius: Double
    ) {
        self.radius = radius
    }



    public init(
        name: String,
        data: [String: String]
    ) {
        self.name = name
        self.data = data
    }



    public init(
        left: UnitValue? = nil,
        top: UnitValue? = nil,
        right: UnitValue? = nil,
        bottom: UnitValue? = nil,
        start: UnitValue? = nil,
        end: UnitValue? = nil,
        horizontal: UnitValue? = nil,
        vertical: UnitValue? = nil,
        all: UnitValue? = nil
    ) {
        self.left = left
        self.top = top
        self.right = right
        self.bottom = bottom
        self.start = start
        self.end = end
        self.horizontal = horizontal
        self.vertical = vertical
        self.all = all
    }



    public init(
        inputName: String,
        message: String
    ) {
        self.inputName = inputName
        self.message = message
    }



    public init(
        direction: Direction? = nil,
        flexDirection: FlexDirection? = nil,
        flexWrap: Wrap? = nil,
        justifyContent: JustifyContent? = nil,
        alignItems: AlignItems? = nil,
        alignSelf: AlignSelf? = nil,
        alignContent: AlignContent? = nil,
        positionType: PositionType? = nil,
        basis: UnitValue? = nil,
        flex: Double? = nil,
        grow: Double? = nil,
        shrink: Double? = nil,
        display: Display? = nil,
        size: Size? = nil,
        margin: EdgeValue? = nil,
        padding: EdgeValue? = nil,
        position: EdgeValue? = nil
    ) {
        self.direction = direction
        self.flexDirection = flexDirection
        self.flexWrap = flexWrap
        self.justifyContent = justifyContent
        self.alignItems = alignItems
        self.alignSelf = alignSelf
        self.alignContent = alignContent
        self.positionType = positionType
        self.basis = basis
        self.flex = flex
        self.grow = grow
        self.shrink = shrink
        self.display = display
        self.size = size
        self.margin = margin
        self.padding = padding
        self.position = position
    }



    public init(
        action: Action,
        child: ServerDrivenComponent,
        group: String? = nil,
        additionalData: [String: String]? = nil,
        shouldStoreFields: Bool = false
    ) {
        self.action = action
        self.child = child
        self.group = group
        self.additionalData = additionalData
        self.shouldStoreFields = shouldStoreFields
    }



    public init(
        name: String,
        required: Bool? = nil,
        validator: String? = nil,
        errorMessage: String? = nil,
        child: ServerDrivenComponent
    ) {
        self.name = name
        self.required = required
        self.validator = validator
        self.errorMessage = errorMessage
        self.child = child
    }



    public init(
        path: String,
        method: Method
    ) {
        self.path = path
        self.method = method
    }



    public init(
        name: String,
        contentMode: ImageContentMode? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.name = name
        self.contentMode = contentMode
        self.widgetProperties = widgetProperties
    }



    public init(
        path: String,
        initialState: ServerDrivenComponent
    ) {
        self.path = path
        self.initialState = initialState
    }



    public init(
        rows: [ServerDrivenComponent],
        direction: Direction = .vertical
    ) {
        self.rows = rows
        self.direction = direction
    }



    public init(
        title: String,
        style: String? = nil,
        showBackButton: Bool? = nil,
        backButtonAccessibility: Accessibility? = nil,
        navigationBarItems: [NavigationBarItem]? = nil
    ) {
        self.title = title
        self.style = style
        self.showBackButton = showBackButton
        self.backButtonAccessibility = backButtonAccessibility
        self.navigationBarItems = navigationBarItems
    }



    public init(
        id: String? = nil,
        image: String? = nil,
        text: String,
        action: Action,
        accessibility: Accessibility? = nil
    ) {
        self.id = id
        self.image = image
        self.text = text
        self.action = action
        self.accessibility = accessibility
    }



    public init(
        path: String,
        contentMode: ImageContentMode? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.path = path
        self.contentMode = contentMode
        self.widgetProperties = widgetProperties
    }



    public init(
        selectedColor: String? = nil,
        unselectedColor: String? = nil
    ) {
        self.selectedColor = selectedColor
        self.unselectedColor = unselectedColor
    }



    public init(
        pages: [ServerDrivenComponent],
        pageIndicator: PageIndicatorComponent? = nil
    ) {
        self.pages = pages
        self.pageIndicator = pageIndicator
    }



    public init(
        top: Bool? = nil,
        leading: Bool? = nil,
        bottom: Bool? = nil,
        trailing: Bool? = nil
    ) {
        self.top = top
        self.leading = leading
        self.bottom = bottom
        self.trailing = trailing
    }



    public init(
        identifier: String? = nil,
        appearance: Appearance? = nil,
        safeArea: SafeArea? = nil,
        navigationBar: NavigationBar? = nil,
        screenAnalyticsEvent: AnalyticsScreen? = nil,
        child: ServerDrivenComponent
    ) {
        self.identifier = identifier
        self.appearance = appearance
        self.safeArea = safeArea
        self.navigationBar = navigationBar
        self.screenAnalyticsEvent = screenAnalyticsEvent
        self.child = child
    }



    internal init(
        identifier: String? = nil,
        appearance: Appearance? = nil,
        safeArea: SafeArea? = nil,
        navigationBar: NavigationBar? = nil,
        screenAnalyticsEvent: AnalyticsScreen? = nil,
        child: ServerDrivenComponent
    ) {
        self.identifier = identifier
        self.appearance = appearance
        self.safeArea = safeArea
        self.navigationBar = navigationBar
        self.screenAnalyticsEvent = screenAnalyticsEvent
        self.child = child
    }



    public init(
        children: [ServerDrivenComponent],
        scrollDirection: ScrollAxis? = nil,
        scrollBarEnabled: Bool? = nil,
        appearance: Appearance? = nil
    ) {
        self.children = children
        self.scrollDirection = scrollDirection
        self.scrollBarEnabled = scrollBarEnabled
        self.appearance = appearance
    }



    public init(
        title: String,
        message: String,
        buttonText: String
    ) {
        self.title = title
        self.message = message
        self.buttonText = buttonText
    }



    public init(
        width: UnitValue? = nil,
        height: UnitValue? = nil,
        maxWidth: UnitValue? = nil,
        maxHeight: UnitValue? = nil,
        minWidth: UnitValue? = nil,
        minHeight: UnitValue? = nil,
        aspectRatio: Double? = nil
    ) {
        self.width = width
        self.height = height
        self.maxWidth = maxWidth
        self.maxHeight = maxHeight
        self.minWidth = minWidth
        self.minHeight = minHeight
        self.aspectRatio = aspectRatio
    }



    public init(
        icon: String? = nil,
        title: String? = nil,
        content: ServerDrivenComponent
    ) {
        self.icon = icon
        self.title = title
        self.content = content
    }



    public init(
        tabItems: [TabItem],
        style: String? = nil
    ) {
        self.tabItems = tabItems
        self.style = style
    }



     init(
        tabIndex: Int,
        tabViewItems: [TabItem],
        selectedTextColor: UIColor? = nil,
        unselectedTextColor: UIColor? = nil,
        selectedIconColor: UIColor? = nil,
        unselectedIconColor: UIColor? = nil
    ) {
        self.tabIndex = tabIndex
        self.tabViewItems = tabViewItems
        self.selectedTextColor = selectedTextColor
        self.unselectedTextColor = unselectedTextColor
        self.selectedIconColor = selectedIconColor
        self.unselectedIconColor = unselectedIconColor
    }



    public init(
        action: Action,
        clickAnalyticsEvent: AnalyticsClick? = nil,
        child: ServerDrivenComponent
    ) {
        self.action = action
        self.clickAnalyticsEvent = clickAnalyticsEvent
        self.child = child
    }



    public init(
        url: String,
        flex: Flex? = nil
    ) {
        self.url = url
        self.flex = flex
    }



    public init(
        id: String? = nil,
        appearance: Appearance? = nil,
        flex: Flex? = nil,
        accessibility: Accessibility? = nil
    ) {
        self.id = id
        self.appearance = appearance
        self.flex = flex
        self.accessibility = accessibility
    }



