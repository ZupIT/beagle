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

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import br.com.zup.beagle.android.components.form.InputWidget
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.annotation.RegisterWidget

@RegisterWidget
data class SampleTextField(val placeholder: String = "") : InputWidget() {

    @Transient
    private lateinit var textFieldView: EditText

    override fun getValue() = textFieldView.text.toString()

    override fun onErrorMessage(message: String) {
        textFieldView.error = message
    }

    override fun buildView(rootView: RootView) = EditText(rootView.getContext()).apply {
        textFieldView = this

        textFieldView.isSingleLine = true
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
}
