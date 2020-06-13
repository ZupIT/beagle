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

import android.content.Context
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import br.com.zup.beagle.android.widget.core.Bind
import br.com.zup.beagle.android.widget.core.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.widget.core.Action

@RegisterWidget
data class Input(
    val hint: Bind<String>,
    val onTextChange: List<Action>? = null
) : WidgetView() {

    override fun buildView(context: Context) = EditText(context).apply {
        doOnTextChanged { newText, _, _, _ ->
            val actions = onTextChange ?: emptyList()
//            this@Input.handleEvent(rootView, actions, "onTextChange", newText.toString())
        }
        this@apply.hint = hint
    }
}
