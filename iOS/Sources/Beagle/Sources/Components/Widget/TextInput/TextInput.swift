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

/// Input is a component that displays an editable text area for the user. These text fields are used to collect
/// inputs that the user insert using the keyboard.
public struct TextInput: Widget, AutoDecodable {
    
    /// Item referring to the input value that will be entered in the editable text area of the `TextInput`.
    public let value: Expression<String>?
    
    /// Text that is displayed when nothing has been entered in the editable text field.
    public let placeholder: Expression<String>?
    
    /// Enables or disables the field.
    @available(*, deprecated, message: "It was deprecated in version 1.7.0 and will be removed in a future version. Use field enabled to control is enabled or not in this layout.")
    public let disabled: Expression<Bool>?
    
    /// Enables or disables the field.
    public let enabled: Expression<Bool>?
    
    /// Check if the Input will be editable or read only.
    public let readOnly: Expression<Bool>?
    
    /// This attribute identifies the type of text that we will receive in the editable text area.
    /// On Android and iOS, this field also assigns the type of keyboard that will be displayed to the us.
    public let type: Expression<TextInputType>?

    /// Enables the component to be visible or not.
    @available(*, deprecated, message: "It was deprecated in version 1.6.0 and will be removed in a future version. Use field display to control visibility.")
    public let hidden: Expression<Bool>?
    
    /// References a style configured to be applied on this view.
    public let styleId: String?
    
    /// Actions array that this field can trigger when its value is altered.
    public let onChange: [Action]?
    
    /// Action array that this field can trigger when its focus is removed.
    public let onBlur: [Action]?
    
    /// Actions array that this field can trigger when this field is on focus.
    public let onFocus: [Action]?
    
    /// Is a text that should be rendered, below the text input. It tells the user about the error.
    /// This text is visible only if showError is true
    public let error: Expression<String>?
    
    /// Controls weather to make the error of the input visible or not.
    /// The error will be visible only if showError is true.
    public let showError: Expression<Bool>?
    
    /// Properties that all widgets have in common.
    public var widgetProperties: WidgetProperties
    
    @available(*, deprecated, message: "It was deprecated in version 1.6.0 and will be removed in a future version. Use field display to control visibility.")
    public init(
        value: Expression<String>? = nil,
        placeholder: Expression<String>? = nil,
        disabled: Expression<Bool>? = nil,
        readOnly: Expression<Bool>? = nil,
        type: Expression<TextInputType>? = nil,
        hidden: Expression<Bool>?,
        styleId: String? = nil,
        onChange: [Action]? = nil,
        onBlur: [Action]? = nil,
        onFocus: [Action]? = nil,
        error: Expression<String>? = nil,
        showError: Expression<Bool>? = nil,
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
        self.error = error
        self.showError = showError
        self.widgetProperties = widgetProperties
        self.enabled = nil
    }
    
    @available(*, deprecated, message: "It was deprecated in version 1.7.0 and will be removed in a future version. Use field enabled to control is enabled or not in this layout.")
    public init(
        value: Expression<String>? = nil,
        placeholder: Expression<String>? = nil,
        disabled: Expression<Bool>?,
        readOnly: Expression<Bool>? = nil,
        type: Expression<TextInputType>? = nil,
        styleId: String? = nil,
        onChange: [Action]? = nil,
        onBlur: [Action]? = nil,
        onFocus: [Action]? = nil,
        error: Expression<String>? = nil,
        showError: Expression<Bool>? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.value = value
        self.placeholder = placeholder
        self.disabled = disabled
        self.readOnly = readOnly
        self.type = type
        self.hidden = nil
        self.styleId = styleId
        self.onChange = onChange
        self.onBlur = onBlur
        self.onFocus = onFocus
        self.error = error
        self.showError = showError
        self.widgetProperties = widgetProperties
        self.enabled = nil
    }
    
    public init(
        value: Expression<String>? = nil,
        placeholder: Expression<String>? = nil,
        enabled: Expression<Bool>? = nil,
        readOnly: Expression<Bool>? = nil,
        type: Expression<TextInputType>? = nil,
        styleId: String? = nil,
        onChange: [Action]? = nil,
        onBlur: [Action]? = nil,
        onFocus: [Action]? = nil,
        error: Expression<String>? = nil,
        showError: Expression<Bool>? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.value = value
        self.placeholder = placeholder
        self.disabled = nil
        self.readOnly = readOnly
        self.type = type
        self.hidden = nil
        self.styleId = styleId
        self.onChange = onChange
        self.onBlur = onBlur
        self.onFocus = onFocus
        self.error = error
        self.showError = showError
        self.widgetProperties = widgetProperties
        self.enabled = enabled
    }
}

public enum TextInputType: String, Decodable {
    case date = "DATE"
    case email = "EMAIL"
    case password = "PASSWORD"
    case number = "NUMBER"
    case text = "TEXT"
}
