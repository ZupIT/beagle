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

package br.com.zup.beagle.android.context.operations.parameter

import br.com.zup.beagle.android.context.operations.grammar.isOperationTypeOrArrayMatch
import br.com.zup.beagle.android.context.operations.grammar.MatchTypes
import br.com.zup.beagle.android.context.operations.grammar.Constants
import br.com.zup.beagle.android.context.operations.grammar.GrammarChars
import br.com.zup.beagle.android.context.operations.grammar.RegularExpressions
import br.com.zup.beagle.android.context.operations.grammar.getMatchResults

internal fun String.toType() : Argument {
    val parameterType = removeWhiteSpaces().checkParameter()
    var value: Any = ""

    when (parameterType) {
        ParameterTypes.NUMBER -> value = removeWhiteSpaces().checkNumber()
        ParameterTypes.EMPTY, ParameterTypes.STRING -> value = this
        ParameterTypes.BOOLEAN -> value = removeWhiteSpaces().toBoolean()
        ParameterTypes.ARRAY -> {
            val list: MutableList<Any> = ArrayList()

            this.getMatchResults(MatchTypes.ARRAY).forEachIndexed { index, match ->
                if (index.isOperationTypeOrArrayMatch()) {
                    match.split(GrammarChars.COMMA).forEach { item ->
                        list.add(
                            item.toType()
                        )
                    }
                }
            }

            value = list
        }
        else -> ParameterTypes.BIND
    }

    return Argument(
        parameterType = parameterType,
        value = value
    )
}

private fun String.checkParameter() : ParameterTypes =
    when {
        contains(GrammarChars.OPEN_BRACKET) -> ParameterTypes.ARRAY
        contains(Constants.APOSTROPHE_MARK) -> ParameterTypes.STRING
        contains(RegularExpressions.NUMBER_REGEX.toRegex()) &&
            isNotEmpty() -> ParameterTypes.NUMBER
        contains(Constants.BOOLEAN_VALUE_TRUE) ||
            contains(Constants.BOOLEAN_VALUE_FALSE) -> ParameterTypes.BOOLEAN
        removeWhiteSpaces().isEmpty() -> ParameterTypes.EMPTY
        else -> ParameterTypes.BIND
    }

private const val DOUBLE_SEPARATOR = "."

private fun String.checkNumber() : Any =
    if (isNotEmpty()) toNumber() else Constants.EMPTY_VALUE

private fun String.toNumber() : Any =
    if (contains(DOUBLE_SEPARATOR)) toDouble() else toInt()

internal fun Any?.isDouble() =
    this != null && toString().contains(DOUBLE_SEPARATOR)

internal fun String.removeWhiteSpaces() : String =
    replace(RegularExpressions.REMOVE_WHITESPACE_REGEX.toRegex(), "")
