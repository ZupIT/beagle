// Generated using Sourcery 0.17.0 — https://github.com/krzysztofzablocki/Sourcery
// DO NOT EDIT

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
