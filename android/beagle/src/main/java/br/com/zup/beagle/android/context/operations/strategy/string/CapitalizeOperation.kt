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

package br.com.zup.beagle.android.context.operations.strategy.string

import br.com.zup.beagle.android.context.operations.grammar.GrammarChars
import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Parameter
import java.util.*
import kotlin.collections.ArrayList

internal class CapitalizeOperation(override val operationType: Operations) : BaseOperation<Operations>() {
    override fun solve(parameter: Parameter): Any {
        var value = parameter.arguments[0].withoutApostrophe()
        val listWhiteSpacePositions: MutableList<Int> = ArrayList()

        value.forEachIndexed { index, char ->
            if (char == GrammarChars.WHITE_SPACE) {
                listWhiteSpacePositions.add(index)
            }
        }

        value = value.trim()

        var result = (value[0].toUpperCase() + value.substring(1).toLowerCase(Locale.ROOT))

        listWhiteSpacePositions.forEach {
            result = result.restoreWhiteSpaces(it)
        }

        return result.withApostropheMark()
    }
}

private fun String.restoreWhiteSpaces(position: Int) : String =
     this.substring(0, position) + GrammarChars.WHITE_SPACE + this.substring(position);
