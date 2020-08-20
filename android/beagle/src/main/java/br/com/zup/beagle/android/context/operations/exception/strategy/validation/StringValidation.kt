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

package br.com.zup.beagle.android.context.operations.exception.strategy.validation

import br.com.zup.beagle.android.context.operations.exception.ExceptionFactory
import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionParameterTypes
import br.com.zup.beagle.android.context.operations.parameter.Argument
import br.com.zup.beagle.android.context.operations.parameter.Parameter
import br.com.zup.beagle.android.context.operations.parameter.ParameterTypes
import br.com.zup.beagle.android.context.operations.parameter.removeWhiteSpaces
import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.string.StringOperationTypes

internal class StringValidation : Validation {

    override fun validate(operationType: Operations?, parameter: Parameter) {
        if (isSubstringOperation(operationType)) {
            verifyParametersFromSubstringOperation(parameter)
        } else {
            parameter.arguments.forEach {
                if (isNotStringParameter(it)) {
                    ExceptionFactory.create(
                        ExceptionParameterTypes.STRING,
                        parameter.operation,
                        it.toString()
                    )
                }
            }
        }
    }

    private fun isSubstringOperation(operationType: Operations?) =
        operationType == StringOperationTypes.SUBSTRING

    private fun verifyParametersFromSubstringOperation(parameter: Parameter) {
        if (notHasRequireArgs(parameter)) {
            ExceptionFactory.create(
                ExceptionParameterTypes.REQUIRED_ARGS,
                parameter.operation,
                parameter.arguments.size.toString()
            )
        } else {
            parameter.arguments.forEachIndexed { index, item ->
                if (isNotFirstParameterString(index, item)) {
                    ExceptionFactory.create(
                        ExceptionParameterTypes.STRING,
                        parameter.operation,
                        item.toString()
                    )
                } else if (isNotNumberOthersParameters(index, item)) {
                    val parameterType = parameter.arguments[parameter.arguments.lastIndex].parameterType

                    ExceptionFactory.create(
                        if (parameterType == ParameterTypes.EMPTY)
                            ExceptionParameterTypes.EMPTY
                        else ExceptionParameterTypes.INDEX,
                        parameter.operation,
                        item.toString()
                    )
                }
            }
        }
    }

    private fun isNotFirstParameterString(index: Int, item: Argument) =
        index == 0 && isNotStringParameter(item)

    private fun isNotStringParameter(item: Argument) =
        item.parameterType != ParameterTypes.STRING

    private fun isNotNumberOthersParameters(index: Int, item: Argument) =
        index > 0 && (item.parameterType != ParameterTypes.NUMBER ||
            item.value.toString().removeWhiteSpaces().isEmpty())

    private fun notHasRequireArgs(parameter: Parameter) =
        parameter.arguments.size < 3

}
