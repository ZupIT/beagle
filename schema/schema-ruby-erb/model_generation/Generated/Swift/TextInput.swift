// THIS IS A GENERATED FILE. DO NOT EDIT THIS
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

public struct TextInput: RawWidget, AutoDecodable {

    public var value: Expression<String>?
    public var placeholder: Expression<String>?
    public var disabled: Expression<Bool>?
    public var readOnly: Expression<Bool>?
    public var type: Expression<TextInputType>?
    public var hidden: Expression<Bool>?
    public var styleId: String?
    public var onChange: [RawAction]?
    public var onBlur: [RawAction]?
    public var onFocus: [RawAction]?
    public var widgetProperties: WidgetProperties

    public init(
        value: Expression<String>? = nil,
        placeholder: Expression<String>? = nil,
        disabled: Expression<Bool>? = nil,
        readOnly: Expression<Bool>? = nil,
        type: Expression<TextInputType>? = nil,
        hidden: Expression<Bool>? = nil,
        styleId: String? = nil,
        onChange: [RawAction]? = nil,
        onBlur: [RawAction]? = nil,
        onFocus: [RawAction]? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.value = value
        self.placeholder = placeholder
        self.disabled = disabled
        self.readOnly = readOnly
        self.type = type
        self.hidden = hidden
        self.styleId = styleId
        self.onChange = onChange
        self.onBlur = onBlur
        self.onFocus = onFocus
        self.widgetProperties = widgetProperties
    }

}
