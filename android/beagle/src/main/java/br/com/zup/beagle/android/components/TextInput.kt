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
import android.view.View
import android.widget.EditText
import androidx.core.widget.TextViewCompat
import br.com.zup.beagle.R
import br.com.zup.beagle.action.Action
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.widget.core.TextInputType
import br.com.zup.beagle.widget.core.TextInputType.DATE
import br.com.zup.beagle.widget.core.TextInputType.EMAIL
import br.com.zup.beagle.widget.core.TextInputType.NUMBER
import br.com.zup.beagle.widget.core.TextInputType.PASSWORD

data class TextInput(
    val value: String? = null,
    val placeholder: String? = null,
    val disabled: Boolean? = null,
    val readOnly: Boolean? = null,
    val type: TextInputType? = null,
    val hidden: Boolean? = null,
    val styleId: String? = null,
    val onChange: List<Action>? = null,
    val onBlur: List<Action>? = null,
    val onFocus: List<Action>? = null
) : WidgetView() {

    @Transient
    private val viewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val editText = viewFactory.makeInputText(rootView.getContext())
        editText.setData(this)
        return editText
    }

    private fun EditText.setData(textInput: TextInput) {
        textInput.placeholder?.let { this.hint = it }
        textInput.value?.let { this.setText(it) }
        textInput.readOnly?.let { this.isEnabled = !it }
        textInput.disabled?.let { this.isEnabled = !it }
        textInput.type?.let { this.setInputType(it) }
        textInput.hidden?.let { this.visibility = if (it) View.INVISIBLE else View.VISIBLE }
        textInput.styleId?.let { this.setStyle(it) }
    }
}

private fun EditText.setInputType(textInputType: TextInputType) {
    this.inputType = when (textInputType) {
        DATE -> InputType.TYPE_CLASS_DATETIME
        EMAIL -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        PASSWORD -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        NUMBER -> InputType.TYPE_CLASS_NUMBER
        else -> InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
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
