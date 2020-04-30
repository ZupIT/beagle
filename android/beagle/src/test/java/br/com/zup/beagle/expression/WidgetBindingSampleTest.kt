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

package br.com.zup.beagle.expression

import android.content.Context
import android.view.View
import br.com.zup.beagle.widget.core.WidgetView

internal data class WidgetBindingSampleTest(
    val intValue: BindingExpr<Int>,
    val stringValue: BindingExpr<String>
) : WidgetView(listOf(intValue, stringValue)) {

    override fun toView(context: Context): View =
        WidgetBindingSampleTextView(context).apply {

            intValue.observes { value ->
                currentText = formatString(currentText, valorInt = value)
            }

            stringValue.observes { value ->
                currentText = formatString(currentText, nome = value)
            }
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