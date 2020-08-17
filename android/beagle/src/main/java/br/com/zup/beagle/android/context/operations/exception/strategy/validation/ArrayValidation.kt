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
import br.com.zup.beagle.android.context.operations.parameter.Parameter
import br.com.zup.beagle.android.context.operations.parameter.ParameterTypes
import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.array.ArrayOperationTypes

internal class ArrayValidation : Validation {

    override fun validate(operationType: Operations?, parameter: Parameter) {
        when {
            notHasRequireArgs(operationType, parameter) -> {
                ExceptionFactory.create(
                    ExceptionParameterTypes.REQUIRED_ARGS,
                    parameter.operation,
                    parameter.arguments.size.toString())
            }
            isNotFirstParameterArray(parameter) -> {
                ExceptionFactory.create(
                    ExceptionParameterTypes.ARRAY,
                    parameter.operation,
                    parameter.arguments.size.toString()
                )
            }
            isNotNumberIndexParameter(operationType, parameter) -> {
                val parameterType = parameter.arguments[parameter.arguments.lastIndex].parameterType

                ExceptionFactory.create(
                    if (parameterType == ParameterTypes.EMPTY)
                        ExceptionParameterTypes.EMPTY
                    else ExceptionParameterTypes.INDEX,
                    parameter.operation,
                    parameter.arguments[parameter.arguments.lastIndex].value.toString()
                )
            }
        }
    }

    private fun isNotNumberIndexParameter(operationType: Operations?, parameter: Parameter) =
        (operationType == ArrayOperationTypes.REMOVE_INDEX || operationType == ArrayOperationTypes.INSERT) &&
            parameter.arguments[parameter.arguments.lastIndex].parameterType != ParameterTypes.NUMBER

    private fun isNotFirstParameterArray(parameter: Parameter) =
        parameter.arguments[0].parameterType != ParameterTypes.ARRAY

    private fun notHasRequireArgs(operationType: Operations?, parameter: Parameter) =
        parameter.arguments.isEmpty() ||
            notHasRequireArgsInsertOperation(operationType, parameter) ||
            notHasRequireArgsNonInsertOperation(operationType, parameter)

    private fun notHasRequireArgsInsertOperation(operationType: Operations?, parameter: Parameter) =
        parameter.arguments.size < 3 && operationType == ArrayOperationTypes.INSERT

    private fun notHasRequireArgsNonInsertOperation(operationType: Operations?, parameter: Parameter) =
        parameter.arguments.size < 2 && operationType != ArrayOperationTypes.INSERT
}
