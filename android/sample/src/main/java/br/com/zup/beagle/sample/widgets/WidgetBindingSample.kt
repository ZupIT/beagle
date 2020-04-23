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
import android.view.View
import android.widget.TextView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.expression.BindingExpr
import br.com.zup.beagle.widget.core.WidgetView

@RegisterWidget
data class WidgetBindingSample(
    val intValue: BindingExpr<Int>,
    val stringValue: BindingExpr<String>
) : WidgetView(listOf(intValue, stringValue)) {

    override fun toView(context: Context): View =
        TextView(context).apply {

            intValue.observes { value ->
                text = formatString(text, valorInt = value)
            }

            stringValue.observes { value ->
                text = formatString(text, nome = value)
            }

            setBackgroundColor(Color.GREEN)
            setTextColor(Color.BLACK)
        }

    private fun formatString(
        currentValue: CharSequence, valorInt: Int = -1,
        nome: String = ""
    ): String {
        var valorIntOld = ""
        var nomeOld = ""
        if (currentValue.isEmpty()) {
            return "$valorInt - $nome"
        }
        if (valorInt != -1 || nome.isNotEmpty()) {
            val values = currentValue.split(" - ")
            valorIntOld = values.first()
            nomeOld = values.last()

            if (valorInt != -1) {
                valorIntOld = valorInt.toString()
            }

            if (nome.isNotEmpty()) {
                nomeOld = nome
            }

        }


        return "$valorIntOld - $nomeOld"
    }


}


