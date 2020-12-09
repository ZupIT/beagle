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

package br.com.zup.beagle.sample.widgets

import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.EditText
import br.com.zup.beagle.android.components.form.InputWidget
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.sample.utils.MaskApplier

enum class TextFieldInputType {
    NUMBER,
    PASSWORD,
    TEXT
}

@RegisterWidget
data class TextField(
    val description: String = "",
    val hint: String = "",
    val color: String = "#000000",
    val mask: String? = null,
    val inputType: TextFieldInputType? = null
) : InputWidget() {

    @Transient
    private lateinit var textFieldView: EditText

    override fun buildView(rootView: RootView) = EditText(rootView.getContext()).apply {
        textFieldView = this
        bind(this@TextField)

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                notifyChanges()
            }

        })
    }

    override fun onErrorMessage(message: String) {
        textFieldView.error = message
    }

    override fun getValue() = textFieldView.text.toString()

    private fun bind(textField: TextField) {
        textField.apply {
            val color = Color.parseColor(getColorWithHashTag(color))
            textFieldView.setText(description)
            textFieldView.hint = hint
            textFieldView.setTextColor(color)
            textFieldView.setHintTextColor(color)

            inputType?.let {
                if (it == TextFieldInputType.NUMBER) {
                    textFieldView.inputType = InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_SIGNED
                } else if (it == TextFieldInputType.PASSWORD) {
                    textFieldView.inputType = InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }

            mask?.let {
                MaskApplier(textFieldView, it)
            }
        }
    }

    private fun getColorWithHashTag(value: String): String = if (value.startsWith("#")) value else "#$value"
}
