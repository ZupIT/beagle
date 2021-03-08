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

package br.com.zup.beagle.widget.ui

import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.valueOfNullable
import br.com.zup.beagle.widget.core.TextInputType
import br.com.zup.beagle.widget.form.InputWidget

/**
 * Input is a component that displays an editable text area for the user. These text fields are used to collect
 * inputs that the user insert using the keyboard.
 *
 * @param value Required. Item referring to the input value that will be entered in the editable text area of the
 * Text Input component.
 * @param placeholder The Placeholder is a text that is displayed when nothing has been entered in the editable
 * text field.
 * @param disabled Enables or disables the field.
 * @param readOnly Check if the Input will be editable or read only.
 * @param type This attribute identifies the type of text that we will receive in the editable text area.
 * On Android and iOS, this field also assigns the type of keyboard that will be displayed to the us.
 * @param hidden Enables the component to be visible or not.
 * @param error is a text that should be rendered, below the text input. It tells the user about the error.
 * This text is visible only if showError is true
 * @param showError controls weather to make the error of the input visible or not.
 * The error will be visible only if showError is true.

 * @param styleId This attribute receives a key that is registered in the Design System of each platform that
 * customizes the component.
 * @param onChange Actions array that this field can trigger when its value is altered.
 * @param onFocus Actions array that this field can trigger when this field is on focus.
 * @param onBlur Action array that this field can trigger when its focus is removed
 * @param enabled Enables or disables the field.
 */
data class TextInput(
    val value: Bind<String>? = null,
    val placeholder: Bind<String>? = null,
    @Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version." +
        " Use field enabled to control is enabled or not in this layout.")
    val disabled: Bind<Boolean>? = null,
    val readOnly: Bind<Boolean>? = null,
    val type: Bind<TextInputType>? = null,
    @Deprecated("It was deprecated in version 1.6.0 and will be removed in a future version." +
        " Use field display to control visibility.")
    val hidden: Bind<Boolean>? = null,
    val error: Bind<String>? = null,
    val showError: Bind<Boolean>? = null,
    val styleId: String? = null,
    val onChange: List<Action>? = null,
    val onFocus: List<Action>? = null,
    val onBlur: List<Action>? = null,
    val enabled: Bind<Boolean>? = null,
) : InputWidget() {

    constructor(
        value: String? = null,
        placeholder: String? = null,
        readOnly: Boolean? = null,
        type: TextInputType? = null,
        error: String? = null,
        showError: Boolean? = null,
        styleId: String? = null,
        onChange: List<Action>? = null,
        onFocus: List<Action>? = null,
        onBlur: List<Action>? = null,
        enabled: Boolean? = null
    ) : this(
        value = valueOfNullable(value),
        placeholder = valueOfNullable(placeholder),
        readOnly = valueOfNullable(readOnly),
        type = valueOfNullable(type),
        error = valueOfNullable(error),
        showError = valueOfNullable(showError),
        styleId = styleId,
        onChange = onChange,
        onFocus = onFocus,
        onBlur = onBlur,
        enabled = valueOfNullable(enabled)
    )

    @Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version. " +
        "Use field enabled to control layout.")
    constructor(
        value: String? = null,
        placeholder: String? = null,
        disabled: Boolean,
        readOnly: Boolean? = null,
        type: TextInputType? = null,
        error: String? = null,
        showError: Boolean? = null,
        styleId: String? = null,
        onChange: List<Action>? = null,
        onFocus: List<Action>? = null,
        onBlur: List<Action>? = null
    ) : this(
        value = valueOfNullable(value),
        placeholder = valueOfNullable(placeholder),
        disabled = valueOfNullable(disabled),
        readOnly = valueOfNullable(readOnly),
        hidden = null,
        type = valueOfNullable(type),
        error = valueOfNullable(error),
        showError = valueOfNullable(showError),
        styleId = styleId,
        onChange = onChange,
        onFocus = onFocus,
        onBlur = onBlur
    )

    @Deprecated("It was deprecated in version 1.6.0 and will be removed in a future version. " +
        "Use field display to control visibility.")
    constructor(
        value: String? = null,
        placeholder: String? = null,
        disabled: Boolean? = null,
        readOnly: Boolean? = null,
        type: TextInputType? = null,
        hidden: Boolean,
        error: String? = null,
        showError: Boolean? = null,
        styleId: String? = null,
        onChange: List<Action>? = null,
        onFocus: List<Action>? = null,
        onBlur: List<Action>? = null
    ) : this(
        value = valueOfNullable(value),
        placeholder = valueOfNullable(placeholder),
        disabled = valueOfNullable(disabled),
        readOnly = valueOfNullable(readOnly),
        type = valueOfNullable(type),
        hidden = valueOfNullable(hidden),
        error = valueOfNullable(error),
        showError = valueOfNullable(showError),
        styleId = styleId,
        onChange = onChange,
        onFocus = onFocus,
        onBlur = onBlur
    )
}