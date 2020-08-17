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

package br.com.zup.beagle.android.context.operations.operation

import br.com.zup.beagle.android.context.operations.common.ExtractValueFromExpressionPDA
import br.com.zup.beagle.android.context.operations.common.ExtractValueTypes
import br.com.zup.beagle.android.context.operations.grammar.GrammarChars
import br.com.zup.beagle.android.context.operations.grammar.RegularExpressions
import br.com.zup.beagle.android.context.operations.parameter.Argument
import br.com.zup.beagle.android.context.operations.parameter.Parameter
import br.com.zup.beagle.android.context.operations.parameter.toType

internal fun Operation.toParameterType() : Parameter  {
    val argumentList: MutableList<Argument> = ArrayList()
    var operation = this

    operation.toListParameters().map {
        if (hasArray(argumentList)) {
            operation = updateOperationWithoutArray()
        }

        argumentList.add(
            it.toType()
        )
    }

    return Parameter(
        operation = operation,
        arguments = argumentList
    )
}

private fun Operation.updateOperationWithoutArray() : Operation {
    val operationWithoutArray : MutableList<String> = operationValue
        .replace(
            RegularExpressions.BETWEEN_BRACKET.toRegex(), ""
        ).split(GrammarChars.COMMA).filter { item ->
            item.isNotEmpty()
        }.map { item ->
            item
        }.toMutableList()

    return copy(
        operationValue = operationWithoutArray.toString()
    )
}

private fun Operation.toListParameters() : List<String> {
    return if (hasArray()) {
        createArrayParameter()
    } else {
        operationValue.split(GrammarChars.COMMA)
    }
}

private fun Operation.createArrayParameter(): List<String> {
    val list : MutableList<String> = ArrayList()
    val arrayInOperation = ExtractValueFromExpressionPDA(
        inputValue = operationValue,
        extractValueTypes = ExtractValueTypes.ARRAY_PARAMETER
    ).getValue()

    list.add(arrayInOperation)

    operationValue.replace(arrayInOperation, "")
        .split(GrammarChars.COMMA).forEach {
            if (it.isNotEmpty()) {
                list.add(it)
            }
        }

    return list
}

private fun Operation.hasArray(list: MutableList<Argument>) =
    hasArray() && list.isNotEmpty()

private fun Operation.hasArray () =
    operationValue.contains(GrammarChars.OPEN_BRACKET)
