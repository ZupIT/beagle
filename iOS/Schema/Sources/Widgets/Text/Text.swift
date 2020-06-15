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

import UIKit

public struct Text: RawWidget, AutoDecodable {
    
    // MARK: - Public Properties
    public let text: String
    public let styleId: String?
    public let alignment: Alignment?
    public let textColor: String?
    public var widgetProperties: WidgetProperties

    public init(
        _ text: String,
        styleId: String? = nil,
        alignment: Alignment? = nil,
        textColor: String? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.text = text
        self.styleId = styleId
        self.alignment = alignment
        self.textColor = textColor
        self.widgetProperties = widgetProperties
    }
}

extension Text {
    public enum Alignment: String, Decodable {
        case left = "LEFT"
        case right = "RIGHT"
        case center = "CENTER"
    }
}
