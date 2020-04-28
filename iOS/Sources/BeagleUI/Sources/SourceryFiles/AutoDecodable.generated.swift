// Generated using Sourcery 0.17.0 — https://github.com/krzysztofzablocki/Sourcery
// DO NOT EDIT


extension Button {

    enum CodingKeys: String, CodingKey {
        case text
        case style
        case action
        case clickAnalyticsEvent
        case widgetProperties
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        text = try container.decode(String.self, forKey: .text)
        style = try container.decodeIfPresent(String.self, forKey: .style)
        action = try container.decodeIfPresent(forKey: .action)
        clickAnalyticsEvent = try container.decodeIfPresent(AnalyticsClick.self, forKey: .clickAnalyticsEvent)
        widgetProperties = try WidgetProperties(from: decoder)
    }

}
