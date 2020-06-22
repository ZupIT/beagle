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
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.utils.get
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.annotation.RegisterWidget

data class Person(
    val name: String,
    val age: Int
)

@RegisterWidget
data class Input(
    val hint: Bind<Person>,
    val onTextChange: List<Action>? = null
) : WidgetView() {

    override fun buildView(rootView: RootView) = EditText(rootView.getContext()).apply {
        setTextColor(Color.BLACK)
        setHintTextColor(Color.BLACK)
        doOnTextChanged { newText, _, _, _ ->
            val actions = onTextChange ?: emptyList()
            this@Input.handleEvent(rootView, actions, "onTextChange", newText.toString())
        }
        this@Input.hint.get(rootView) {
            this@apply.hint = "name: ${it.name}, age: ${it.age}"
        }
    }
}
