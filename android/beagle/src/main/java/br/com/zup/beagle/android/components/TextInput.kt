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

import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doOnTextChanged
import br.com.zup.beagle.R
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.form.InputWidget
import br.com.zup.beagle.android.context.Bind
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
        valueOfNullable(value),
        valueOfNullable(placeholder),
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

    override fun buildView(rootView: RootView): View = viewFactory.makeInputText(rootView.getContext()).apply {
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
        textWatcher = doOnTextChanged { newText, _, _, _ ->
            notifyChanges()
            onChange?.let {
                this@TextInput.handleEvent(
                    rootView,
                    this,
                    onChange,
                    "onChange",
                    mapOf(VALUE_KEY to newText.toString())
                )
            }
        }
    }

    private fun EditText.removeOnTextChange() {
        this.removeTextChangedListener(textWatcher)
    }

    private fun EditText.setUpOnFocusChange(rootView: RootView) {
        this.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                onFocus?.let {
                    this@TextInput.handleEvent(
                        rootView,
                        view,
                        onFocus,
                        "onFocus",
                        mapOf(VALUE_KEY to this.text.toString())
                    )
                }
            } else {
                onBlur?.let {
                    this@TextInput.handleEvent(
                        rootView,
                        view,
                        onBlur,
                        "onBlur",
                        mapOf(VALUE_KEY to this.text.toString())
                    )
                }
            }
        }
    }

    private fun EditText.setData(textInput: TextInput, rootView: RootView) {
        textInput.placeholder?.let { bind -> observeBindChanges(rootView, bind) { this.hint = it } }
        textInput.value?.let { bind ->
            observeBindChanges(rootView, bind) {
                if (it != this.text.toString()) {
                    this.removeOnTextChange()
                    this.setText(it)
                    this.setSelection(it.length)
                    setUpOnTextChange(rootView)
                }
            }
        }
        textInput.readOnly?.let { bind -> observeBindChanges(rootView, bind) { this.isEnabled = !it } }
        textInput.disabled?.let { bind -> observeBindChanges(rootView, bind) { this.isEnabled = !it } }
        textInput.hidden?.let { bind ->
            observeBindChanges(rootView, bind) {
                this.visibility = if (it) View.INVISIBLE else View.VISIBLE
            }
        }
        textInput.styleId?.let { style -> setStyle(style) }
        textInput.type?.let { bind -> observeBindChanges(rootView, bind) { this.setInputType(it) } }
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

    private fun EditText.setStyle(styleId: String) {
        val typedArray = styleManagerFactory.getInputTextTypedArray(context, styleId)
        typedArray?.let { typedItems ->
            typedItems.getDrawable(R.styleable.BeagleInputTextStyle_background)?.let { background = it }
            typedItems.recycle()
        }

        styleManagerFactory.getInputTextStyle(styleId)?.let {
            TextViewCompat.setTextAppearance(this, it)
        }
    }
}
