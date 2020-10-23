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
import android.text.TextWatcher
import android.widget.EditText
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.annotation.RegisterWidget

@RegisterWidget
data class Input(
    val hint: Bind<String>,
    val onTextChange: List<Action>? = null
) : WidgetView() {

    override fun buildView(rootView: RootView) = EditText(rootView.getContext()).apply {
        setTextColor(Color.BLACK)
        setHintTextColor(Color.BLACK)
        val input = this
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val actions = onTextChange ?: emptyList()
                this@Input.handleEvent(
                    rootView,
                    input,
                    actions,
                    ContextData(
                        id = "onTextChange",
                        value = newText.toString()
                    )
                )
            }
        })
        observeBindChanges(rootView, this, this@Input.hint) {
            this@apply.hint = it
        }
    }
}
