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
import android.graphics.Color
import android.widget.TextView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.expression.BindingExpr
import br.com.zup.beagle.widget.core.WidgetView

@RegisterWidget
class NestedBindingWidget(
    private val abc: BindingExpr<Int>,
    private val abd: BindingExpr<String>,
    private val abe0f: BindingExpr<String>,
    private val abg0: BindingExpr<String>,
    private val abg1: BindingExpr<Double>,
    private val abg2: BindingExpr<Boolean>
) : WidgetView(listOf(abc, abd, abe0f, abg0, abg1, abg2)) {
    private val evaluatedFields = this.bindingProperties.associate { it.expression to "" }.toMutableMap()

    override fun toView(context: Context) = TextView(context).also { view ->
        this.bindingProperties.forEach { field ->
            field.observes { value ->
                this.evaluatedFields[field.expression] = value.toString()
                view.text = this.evaluatedFields.entries.joinToString("\r\n") { "${it.key} = ${it.value}" }
            }
        }

        view.textSize = 25.0f
        view.setTextColor(Color.BLACK)
    }
}