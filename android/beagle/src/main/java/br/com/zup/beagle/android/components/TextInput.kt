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

package br.com.zup.beagle.android.components

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.form.InputWidget
import br.com.zup.beagle.android.components.utils.styleManagerFactory
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOrValueOfNullable
import br.com.zup.beagle.android.context.valueOfNullable
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.widget.core.TextInputType
import br.com.zup.beagle.widget.core.TextInputType.DATE
import br.com.zup.beagle.widget.core.TextInputType.EMAIL
import br.com.zup.beagle.widget.core.TextInputType.NUMBER
import br.com.zup.beagle.widget.core.TextInputType.PASSWORD

private const val VALUE_KEY = "value"

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
 * @param styleId This attribute receives a key that is registered in the Design System of each platform that
 * customizes the component.
 * @param onChange Actions array that this field can trigger when its value is altered.
 * @param onFocus Actions array that this field can trigger when this field is on focus.
 * @param onBlur Action array that this field can trigger when its focus is removed
 */
@RegisterWidget
data class TextInput(
    val value: Bind<String>? = null,
    val placeholder: Bind<String>? = null,
    val disabled: Bind<Boolean>? = null,
    val readOnly: Bind<Boolean>? = null,
    val type: Bind<TextInputType>? = null,
    val hidden: Bind<Boolean>? = null,
    val styleId: String? = null,
    val onChange: List<Action>? = null,
    val onFocus: List<Action>? = null,
    val onBlur: List<Action>? = null
) : InputWidget() {

    constructor(
        value: String? = null,
        placeholder: String? = null,
        disabled: Boolean? = null,
        readOnly: Boolean? = null,
        type: TextInputType? = null,
        hidden: Boolean? = null,
        styleId: String? = null,
        onChange: List<Action>? = null,
        onFocus: List<Action>? = null,
        onBlur: List<Action>? = null
    ) : this(
        expressionOrValueOfNullable(value),
        expressionOrValueOfNullable(placeholder),
        valueOfNullable(disabled),
        valueOfNullable(readOnly),
        valueOfNullable(type),
        valueOfNullable(hidden),
        styleId,
        onChange,
        onFocus,
        onBlur
    )

    @Transient
    private val viewFactory = ViewFactory()

    @Transient
    private lateinit var textInputView: EditText

    @Transient
    private var textWatcher: TextWatcher? = null

    override fun buildView(rootView: RootView): View = viewFactory.makeInputText(
        rootView.getContext(),
        styleManagerFactory.getInputTextStyle(styleId)
    ).apply {
        textInputView = this
        setData(this@TextInput, rootView)
        setUpOnTextChange(rootView)
        if (onFocus != null || onBlur != null) setUpOnFocusChange(rootView)
    }

    override fun getValue(): Any = textInputView.text.toString()

    override fun onErrorMessage(message: String) {
        textInputView.error = message
    }

    private fun EditText.setUpOnTextChange(rootView: RootView) {
        textWatcher = object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(newText: CharSequence?, start: Int, before: Int, count: Int) {
                notifyChanges()
                onChange?.let {
                    this@TextInput.handleEvent(
                        rootView,
                        textInputView,
                        onChange,
                        ContextData(
                            id = "onChange",
                            value = mapOf(VALUE_KEY to newText.toString())
                        )
                    )
                }
            }

        }
    }

    private fun EditText.removeOnTextChange() {
        removeTextChangedListener(textWatcher)
    }

    private fun EditText.setUpOnFocusChange(rootView: RootView) {
        this.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                onFocus?.let {
                    this@TextInput.handleEvent(
                        rootView,
                        view,
                        onFocus,
                        ContextData(
                            id = "onFocus",
                            value = mapOf(VALUE_KEY to text.toString())
                        )
                    )
                }
            } else {
                onBlur?.let {
                    this@TextInput.handleEvent(
                        rootView,
                        view,
                        onBlur,
                        ContextData(
                            id = "onBlur",
                            value = mapOf(VALUE_KEY to text.toString())
                        )
                    )
                }
            }
        }
    }

    private fun EditText.setData(textInput: TextInput, rootView: RootView) {
        isFocusable = true
        isFocusableInTouchMode = true
        textInput.placeholder?.let { bind -> observeBindChanges(rootView, this, bind) { it?.let { hint = it } } }
        textInput.value?.let { bind ->
            observeBindChanges(rootView, this, bind) { it?.let { setValue(it, rootView) } }
        }
        textInput.readOnly?.let { bind -> observeBindChanges(rootView, this, bind) { setEnabledConfig(it) } }
        textInput.disabled?.let { bind -> observeBindChanges(rootView, this, bind) { setEnabledConfig(it) } }
        textInput.hidden?.let { bind ->
            observeBindChanges(rootView, this, bind) {
                it?.let { visibility = if (it) View.INVISIBLE else View.VISIBLE }
            }
        }
        textInput.type?.let { bind -> observeBindChanges(rootView, this, bind) { it?.let { setInputType(it) } } }
    }

    private fun EditText.setEnabledConfig(isEnabled: Boolean?) {
        isEnabled?.let { this.isEnabled = !it }
    }

    private fun EditText.setValue(text: String, rootView: RootView) {
        if (text == this.text.toString()) return
        removeOnTextChange()
        setText(text)
        setSelection(text.length)
        setUpOnTextChange(rootView)
    }

    private fun EditText.setInputType(textInputType: TextInputType) {
        this.inputType = when (textInputType) {
            DATE -> InputType.TYPE_CLASS_DATETIME
            EMAIL -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            PASSWORD -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            NUMBER -> InputType.TYPE_CLASS_NUMBER
            else -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        }
    }
}
